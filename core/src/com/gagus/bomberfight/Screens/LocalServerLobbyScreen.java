package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Buffers.BufferClientConnected;
import com.gagus.bomberfight.Buffers.BufferClientDisconnected;
import com.gagus.bomberfight.Buffers.BufferServerDisconnected;
import com.gagus.bomberfight.Buffers.BufferStartGame;
import com.gagus.bomberfight.KryoHelper;
import com.gagus.bomberfight.Listeners.ServerLobbyListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gaetan on 03/02/2018.
 */

public class LocalServerLobbyScreen implements Screen {
	Stage stage;
	BomberFight game;
	ServerLobbyListener serverLobbyListener;
	public Server server;
	Label playersCountLabel;
	TextButton startGameButton, backButton;
	BitmapFont font;
	float marginAera;
	public int playersCount;

	public LocalServerLobbyScreen(final BomberFight game) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);
		marginAera = game.GameArea.x;
		playersCount = 1;

		server = new Server();
		Kryo kryo = server.getKryo();
		KryoHelper.registerClasses(kryo);
		server.start();
		try {
			server.bind(54555,54777);
		} catch (Exception e) {
			Gdx.app.log("Server", "Bind server fail");
			e.printStackTrace();
		}

		font = new BitmapFont(Gdx.files.internal("fonts/arialBlack50.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
		playersCountLabel = new Label("players : "+Integer.toString(playersCount),labelStyle);
		playersCountLabel.setPosition(game.WIDTH/2-playersCountLabel.getWidth()/2,game.HEIGHT/2-playersCountLabel.getHeight()/2);
		stage.addActor(playersCountLabel);

		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		startGameButton = new TextButton("Start Game",textButtonStyle);
		startGameButton.setPosition(game.WIDTH/2-startGameButton.getWidth()/2,game.HEIGHT/4);
		startGameButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("Server","Click on start game button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(playersCount > 1){
					startGame();
				}

			}
		});
		stage.addActor(startGameButton);

		backButton = new TextButton("back to menu",textButtonStyle);
		backButton.setPosition(0,0);
		backButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("Server","Click on back to menu button");
				server.stop();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenuScreen(game));
			}
		});
		stage.addActor(backButton);

		serverLobbyListener = new ServerLobbyListener(this);
		server.addListener(serverLobbyListener);
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	public void connect() {
		// when a player connect
		playersCount = server.getConnections().length +1;
		playersCountLabel.setText("players : "+Integer.toString(playersCount));
		playersCountLabel.setPosition(game.WIDTH/2-playersCountLabel.getWidth()/2,game.HEIGHT/2-playersCountLabel.getHeight()/2);
		BufferClientConnected buffer = new BufferClientConnected();
		buffer.clientsCount = server.getConnections().length;
		server.sendToAllTCP(buffer);
	}

	public void disconnect() {
		// when the server disconnect
		playersCount = server.getConnections().length +1;
		playersCountLabel.setText("players : "+Integer.toString(playersCount));
		playersCountLabel.setPosition(game.WIDTH/2-playersCountLabel.getWidth()/2,game.HEIGHT/2-playersCountLabel.getHeight()/2);
		BufferServerDisconnected buffer = new BufferServerDisconnected();
		server.sendToAllTCP(buffer);
	}

	public void startGame(){
		Map<Integer, Integer> hm = new HashMap<Integer, Integer>();
		for(int i = 1; i<=server.getConnections().length;i++){
			BufferStartGame buffer = new BufferStartGame();
			buffer.playerIndex = i;
			buffer.playersCount = playersCount;
			server.getConnections()[i-1].sendTCP(buffer);
			hm.put(server.getConnections()[i-1].getID(),i);
		}
		final Map<Integer, Integer> playersId = hm;
		server.removeListener(serverLobbyListener);
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				game.setScreen(new LocalServerGameScreen(game, server, playersCount, playersId));
			}
		});
		//game.setScreen(new LocalServerGameScreen(game, server, playersCount));
	}

	public Connection[] getConnections(){
		return server.getConnections();
	}
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}

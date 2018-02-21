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
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.KryoHelper;
import com.gagus.bomberfight.Listeners.ClientLobbyListener;

import java.net.InetAddress;

/**
 * Created by Gaetan on 03/02/2018.
 */

public class LocalClientLobbyScreen implements Screen {

	Stage stage;
	BomberFight game;
	ClientLobbyListener clientLobbyListener;
	Client client;
	Label playersCountLabel;
	TextButton backButton;
	BitmapFont font;

	public LocalClientLobbyScreen(final BomberFight game) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);


		font = new BitmapFont(Gdx.files.internal("fonts/arialBlack50.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
		playersCountLabel = new Label("players : 0",labelStyle);
		playersCountLabel.setPosition(game.WIDTH/2-playersCountLabel.getWidth()/2,game.HEIGHT/2-playersCountLabel.getHeight()/2);
		stage.addActor(playersCountLabel);

		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.font = font;
		backButton = new TextButton("back to menu",textButtonStyle);
		backButton.setPosition(0,0);
		backButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("Server","Click on back to menu button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				client.stop();
				game.setScreen(new MainMenuScreen(game));
			}
		});
		stage.addActor(backButton);

		client = new Client();
		Kryo kryo = client.getKryo();
		KryoHelper.registerClasses(kryo);
		client.start();

		//dynamic ip
		InetAddress host = client.discoverHost(54777,5000);
		if(host == null){
			Gdx.app.log("client host discovered","null");
			client.stop();
			playersCountLabel.setText("any server found");
			playersCountLabel.setPosition(game.WIDTH/2-playersCountLabel.getWidth()/2,game.HEIGHT/2-playersCountLabel.getHeight()/2);
		}
		else{
			Gdx.app.log("client host discovered",host.toString());
			try {
				client.connect(1000,host,54555,54777);
			} catch (Exception e) {
				Gdx.app.log("Client", "connection fail");
				e.printStackTrace();
			}
		}

		// static ip
		/*try {
			client.connect(1000,"192.168.1.223",54555,54777);
		} catch (Exception e) {
			Gdx.app.log("Client", "connection fail");
			e.printStackTrace();
		}*/

		clientLobbyListener = new ClientLobbyListener(this);
		client.addListener(clientLobbyListener);
	}

	public void disconnect(){
		client.stop();
		game.setScreen(new MainMenuScreen(game));
	}

	public void onClientConnected(int clientsCount){
		Gdx.app.log("client",String.valueOf(clientsCount));
		playersCountLabel.setText("players : "+String.valueOf(clientsCount));
		playersCountLabel.setPosition(game.WIDTH/2-playersCountLabel.getWidth()/2,game.HEIGHT/2-playersCountLabel.getHeight()/2);
	}

	public void onServerDisconnected(){
		game.setScreen(new MainMenuScreen(game));
	}

	public void startGame(final int playerIndex, final int playersCount){
		client.removeListener(clientLobbyListener);
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				game.setScreen(new LocalClientGameScreen(game, client, playerIndex, playersCount));
			}
		});
		//game.setScreen(new LocalClientGameScreen(game, client, playerIndex, playersCount));
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

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

package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.gagus.bomberfight.Actors.Bomb;
import com.gagus.bomberfight.Actors.BombClient;
import com.gagus.bomberfight.Actors.Bonus;
import com.gagus.bomberfight.Actors.BreakableWall;
import com.gagus.bomberfight.Actors.MapManager;
import com.gagus.bomberfight.Actors.PlayerNetworkClient;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Buffers.BufferPlayerMove;
import com.gagus.bomberfight.Buffers.BufferPlayerPutBomb;
import com.gagus.bomberfight.Listeners.ClientGameListener;

/**
 * Created by Gaetan on 15/02/2018.
 */

public class LocalClientGameScreen implements Screen {
	BomberFight game;
	Client client;
	int playerIndex;
	int playersCount;
	PlayerNetworkClient[] players;
	Stage stage;

	// buttons
	private Touchpad joystick;
	private Vector2 joystickPosition;
	private int deadzoneTouchpad = 10;
	private ImageButton bombButton;
	private Vector2 bombButtonPosition;

	ClientGameListener clientGameListener;

	//actors
	Image backgroundActor;

	public LocalClientGameScreen(final BomberFight game, Client client, int playerIndex, int playersCount) {
		this.game = game;
		this.client = client;
		this.playerIndex = playerIndex;
		this.playersCount = playersCount;
		this.stage = new Stage(game.viewport, game.batch);
		Gdx.input.setInputProcessor(stage);

		//background
		TextureRegion backgroundImage = new TextureRegion(new Texture(Gdx.files.internal("images/backgrounds/herbe.png")));
		backgroundActor = new Image(backgroundImage);
		backgroundActor.setPosition(BomberFight.GameArea.x,0);

		stage.addActor(backgroundActor);
		backgroundActor.setZIndex(0);

		String[] playersNames = {"player 1", "player 2", "player 3", "player 4"};
		players = new PlayerNetworkClient[playersCount];
		for(int i = 0;i<players.length;i++){
			players[i] = new PlayerNetworkClient(playersNames[i],i);
			players[i].setZIndex(3);
			stage.addActor(players[i]);

		}


		MapManager mapManager = new MapManager("map1.txt",stage);

		// Joystick creation
		Touchpad.TouchpadStyle stylepad = new Touchpad.TouchpadStyle(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/joystickBackground.png")))),new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/knob.png")))));
		joystick = new Touchpad(deadzoneTouchpad,stylepad);
		joystick.setSize(100,100);
		joystickPosition = new Vector2(15,15);
		joystick.setPosition(joystickPosition.x,joystickPosition.y);
		joystick.setZIndex(4);
		stage.addActor(joystick);

		Gdx.app.log("client "+playerIndex,"joystick created");

		// Button bombs creation
		bombButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/bombButtonUp.png")))),new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/bombButtonDown.png")))));

		bombButtonPosition = new Vector2(game.WIDTH-joystick.getWidth()/2-15,joystick.getHeight()/2+joystick.getX()-bombButton.getHeight()/2);
		bombButton.setPosition(bombButtonPosition.x,bombButtonPosition.y);
		bombButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("player ","touch down on bomb button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				sendBomb();
			}
		});
		stage.addActor(bombButton);
		bombButton.setZIndex(4);

		clientGameListener = new ClientGameListener(this);
		client.addListener(clientGameListener);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		actJoystick();
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

	public void actJoystick(){
		if(joystick.isTouched()){
			//calculate direction
			Vector2 direction = new Vector2(0,0);
			Vector2 joystickPercent = new Vector2(joystick.getKnobPercentX(),joystick.getKnobPercentY());
			if(joystickPercent.y>0 && Math.abs(joystickPercent.y)>Math.abs(joystickPercent.x)) {
				direction.y = 1;
			}
			else if(joystickPercent.y<0 && Math.abs(joystickPercent.y)>Math.abs(joystickPercent.x)) {
				direction.y = -1;
			}
			else if(joystickPercent.x>0 && Math.abs(joystickPercent.x)>Math.abs(joystickPercent.y)) {
				direction.x = 1;
			}
			else if(joystickPercent.x<0 && Math.abs(joystickPercent.x)>Math.abs(joystickPercent.y)) {
				direction.x = -1;
			}
			//send buffer move
			BufferPlayerMove buffer = new BufferPlayerMove();
			buffer.playerIndex = playerIndex;
			buffer.direction = direction;
			client.sendUDP(buffer);
		}
	}

	public void movePlayer(int playerIndex, Vector2 playerPosition, Vector2 playerDirection){
		players[playerIndex].setPosition(playerPosition, playerDirection);
	}

	public void onPlayerPutBomb(final Vector2 bombSquarePosition){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				BombClient bomb = new BombClient(bombSquarePosition);
				bomb.setZIndex(2);
				stage.addActor(bomb);
			}
		});
	}

	public void sendBomb(){
		if(players[playerIndex].isAlive()) {
			//send buffer to put bomb
			BufferPlayerPutBomb buffer = new BufferPlayerPutBomb();
			buffer.playerIndex = playerIndex;
			client.sendTCP(buffer);
		}
	}

	public void onWallDestroyed(Vector2 wallSquarePosition) {
		for (Actor actor : stage.getActors()) {
			if (actor.getClass() == BreakableWall.class) {
				BreakableWall wall = (BreakableWall) actor;
				if (wall.getSquarePosition().x == wallSquarePosition.x && wall.getSquarePosition().y == wallSquarePosition.y) {
					wall.destroy();
					break;
				}
			}
		}
	}

	public void onBombExploded(Vector2 bombSquarePosition, Array<Vector2> explosionsPositions){
		Gdx.app.log("client", "for bomb position : "+bombSquarePosition.toString());
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == BombClient.class){
				BombClient bomb = (BombClient) actor;
				Gdx.app.log("client", "for bomb position : "+bomb.getSquarePosition().toString());
				if(bomb.getSquarePosition().x == bombSquarePosition.x && bomb.getSquarePosition().y == bombSquarePosition.y){
					Gdx.app.log("client","bomb exploded found");
					bomb.explode(explosionsPositions);
					break;
				}
			}
		}
	}

	public void onBonusAppeared(final int bonus, final Vector2 bonusSquarePosition){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				Bonus b = new Bonus(bonus,bonusSquarePosition);
				b.setZIndex(2);
				stage.addActor(b);
			}
		});
	}

	public void onBonusDisappeared(Vector2 bonusSquarePosition){
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bonus.class){
				Bonus bonus = (Bonus) actor;
				if(bonus.getSquarePosition().x == bonusSquarePosition.x && bonus.getSquarePosition().y == bonusSquarePosition.y){
					bonus.destroy();
				}
			}
		}
	}

	public void onPlayerDie(int playerIndex){
		players[playerIndex].setDie();
	}

	public void endGame(int winnerPlayerIndex){
		if(winnerPlayerIndex != -1){
			Gdx.app.log("client", "player winner : "+players[winnerPlayerIndex].getName());
			final String winnerName = players[winnerPlayerIndex].getName();
			client.removeListener(clientGameListener);
			client.stop();
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					game.setScreen(new EndGameScreen(game,winnerName,false));
				}
			});
		}
		else{
			Gdx.app.log("client", "Game equality");
			client.removeListener(clientGameListener);
			client.stop();
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					game.setScreen(new EndGameScreen(game,null,true));
				}
			});
		}
	}

	public void onDisconnect(){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				client.stop();
				game.setScreen(new EndGameScreen(game,null,true));
			}
		});
	}

	public void onPlayerDisconnected(int playerIndex){
		players[playerIndex].setDie();
	}
}

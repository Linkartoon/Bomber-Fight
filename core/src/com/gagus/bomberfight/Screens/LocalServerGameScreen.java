package com.gagus.bomberfight.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.gagus.bomberfight.Actors.Bomb;
import com.gagus.bomberfight.Actors.Bonus;
import com.gagus.bomberfight.Actors.BreakableWall;
import com.gagus.bomberfight.Actors.MapManager;
import com.gagus.bomberfight.Actors.PlayerNetworkServer;
import com.gagus.bomberfight.Actors.UnbreakableWall;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Buffers.BufferBombExploded;
import com.gagus.bomberfight.Buffers.BufferBombPosition;
import com.gagus.bomberfight.Buffers.BufferBonusAppeared;
import com.gagus.bomberfight.Buffers.BufferBonusDisappeared;
import com.gagus.bomberfight.Buffers.BufferEndGame;
import com.gagus.bomberfight.Buffers.BufferPlayerDie;
import com.gagus.bomberfight.Buffers.BufferPlayerPosition;
import com.gagus.bomberfight.Buffers.BufferWallDestroyed;
import com.gagus.bomberfight.Interfaces.CollisionGetter;
import com.gagus.bomberfight.Interfaces.DataSender;
import com.gagus.bomberfight.Interfaces.PlayerDieListener;
import com.gagus.bomberfight.Listeners.ServerGameListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gaetan on 15/02/2018.
 */

public class LocalServerGameScreen implements Screen, DataSender, CollisionGetter, PlayerDieListener {
	BomberFight game;
	Server server;
	int playersCount;
	Stage stage;
	PlayerNetworkServer[] players;
	ServerGameListener serverGameListener;
	Image backgroundActor;

	//items collide
	Array<Rectangle> bombsRect = new Array<Rectangle>();
	Array<Rectangle> explosionsRect = new Array<Rectangle>();
	Array<Bonus> bonuses = new Array<Bonus>();
	Array<Rectangle> allwallsRect = new Array<Rectangle>();
	Array<Rectangle> unbreakableWallsRect = new Array<Rectangle>();
	Array<Rectangle> breakableWallsRect = new Array<Rectangle>();
	Array<BreakableWall> breakablesWalls = new Array<BreakableWall>();
	Array<UnbreakableWall> unbreakablesWalls = new Array<UnbreakableWall>();

	// buttons
	private Touchpad joystick;
	private Vector2 joystickPosition;
	private int deadzoneTouchpad = 10;
	private ImageButton bombButton;
	private Vector2 bombButtonPosition;

	Map<Integer, Integer> playersId = new HashMap<Integer, Integer>();

	public LocalServerGameScreen(BomberFight game, Server server, int playersCount, Map <Integer, Integer> playersID) {
		this.game = game;
		this.server = server;
		this.playersCount = playersCount;
		this.playersId = playersID;
		stage = new Stage(game.viewport,game.batch);
		Gdx.input.setInputProcessor(stage);

		//background
		TextureRegion backgroundImage = new TextureRegion(new Texture(Gdx.files.internal("images/backgrounds/herbe.png")));
		backgroundActor = new Image(backgroundImage);
		backgroundActor.setPosition(BomberFight.GameArea.x,0);
		stage.addActor(backgroundActor);
		backgroundActor.setZIndex(0);

		MapManager mapManager = new MapManager("map1.txt",stage,this);

		String[] playersNames = {"player 1", "player 2", "player 3", "player 4"};
		players = new PlayerNetworkServer[playersCount];
		for(int i = 0;i<players.length;i++){
			players[i] = new PlayerNetworkServer(playersNames[i],i,this,this, this);
			players[i].setZIndex(3);
			stage.addActor(players[i]);

		}


		// Joystick creation
		Touchpad.TouchpadStyle stylepad = new Touchpad.TouchpadStyle(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/joystickBackground.png")))),new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/knob.png")))));
		joystick = new Touchpad(deadzoneTouchpad,stylepad);
		joystick.setSize(100,100);
		joystickPosition = new Vector2(15,15);
		joystick.setPosition(joystickPosition.x,joystickPosition.y);
		joystick.setZIndex(4);
		stage.addActor(joystick);



		Gdx.app.log("server","joystick created");

		// Button bombs creation
		bombButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/bombButtonUp.png")))),new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/bombButtonDown.png")))));

		bombButtonPosition = new Vector2(game.WIDTH-joystick.getWidth()/2-15,joystick.getHeight()/2+joystick.getX()-bombButton.getHeight()/2);
		bombButton.setPosition(bombButtonPosition.x,bombButtonPosition.y);
		bombButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("server ","touch down on bomb button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(players[0].isAlive()) {
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							players[0].putBomb(stage);
						}
					});
					//players[0].putBomb(stage);
				}
			}
		});
		stage.addActor(bombButton);
		bombButton.setZIndex(4);

		serverGameListener = new ServerGameListener(this);
		server.addListener(serverGameListener);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		actBombsRect();
		actBonuses();
		actExplosionsRect();
		actBreakablesWalls();
		actBreakablesWallsRect();
		actUnbreakablesWalls();
		actUnbreakablesWallsRect();
		if(players[0].isAlive()) actJoystick();
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

			//move player
			players[0].move(direction);
		}
	}

	public void onPlayerMove(int playerIndex, Vector2 direction){
		players[playerIndex].move(direction);
	}

	public void sendWallDestroyed(Vector2 wallSquarePosition){
		BufferWallDestroyed buffer = new BufferWallDestroyed();
		buffer.wallSquarePosition = wallSquarePosition;
		server.sendToAllTCP(buffer);
	}

	public void sendPlayerPosition(int playerIndex, Vector2 position, Vector2 direction){
		BufferPlayerPosition buffer = new BufferPlayerPosition();
		buffer.playerIndex = playerIndex;
		buffer.playerPosition = position;
		buffer.playerDirection = direction;
		server.sendToAllUDP(buffer);
	}

	public void actBombsRect(){
		bombsRect = new Array<Rectangle>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bomb.class){
				bombsRect.add(((Bomb)actor).getRectangle());
			}
		}
	}

	public void actExplosionsRect(){
		explosionsRect = new Array<Rectangle>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bomb.class){
				for(Rectangle rect : ((Bomb)actor).getExplosionRect()){
					explosionsRect.add(rect);
				}
			}
		}
	}


	public void actBonuses() {
		bonuses = new Array<Bonus>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bonus.class){
				bonuses.add((Bonus)actor);
			}
		}
	}

	public Array<Bomb> getBombs(){
		Array<Bomb> bombs = new Array<Bomb>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bomb.class){
				bombs.add((Bomb)actor);
			}
		}
		return bombs;
	}

	public Array<Rectangle> getBombsRect(){
		return bombsRect;
	}

	public Array<Rectangle> getExplosionsRect(){
		return explosionsRect;
	}

	public Array<Bonus> getBonuses(){
		return bonuses;
	}

	public void destroyBlock(Vector2 squarePosition,CollisionGetter collisionGetter)
	{
		int bonusRandom = (int)(Math.random()*4);
		Gdx.app.log("bonusRandom",String.valueOf(bonusRandom));
		if(bonusRandom == 0 || bonusRandom == 1 || bonusRandom == 3) {
			Bonus up = new Bonus(bonusRandom, squarePosition, collisionGetter, this);
			stage.addActor(up);
			up.setZIndex(2);
		}
	}

	public Array<Rectangle> getAllWallsRect() {
		Array<Rectangle> tab = new Array<Rectangle>();
		tab.addAll(unbreakableWallsRect);
		tab.addAll(breakableWallsRect);
		return tab;
	}

	public Array<BreakableWall> getBreakablesWalls() {
		return breakablesWalls;
	}

	public void actBreakablesWalls(){
		breakablesWalls = new Array<BreakableWall>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == BreakableWall.class){
				breakablesWalls.add(((BreakableWall)actor));
			}
		}
	}

	public Array<UnbreakableWall> getUnbreakablesWalls() {
		return unbreakablesWalls;
	}

	public void actUnbreakablesWalls(){
		unbreakablesWalls = new Array<UnbreakableWall>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == UnbreakableWall.class){
				unbreakablesWalls.add(((UnbreakableWall)actor));
			}
		}
	}

	public void actUnbreakablesWallsRect(){
		unbreakableWallsRect = new Array<Rectangle>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == UnbreakableWall.class){
				unbreakableWallsRect.add(((UnbreakableWall)actor).getRect());
			}
		}
	}

	public void actBreakablesWallsRect(){
		breakableWallsRect = new Array<Rectangle>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == BreakableWall.class){
				breakableWallsRect.add(((BreakableWall)actor).getRect());
			}
		}
	}

	public Array<Rectangle> getUnbreakablesWallsRect(){
		return unbreakableWallsRect;
	}

	public Array<Rectangle> getBreakablesWallsRect(){
		return breakableWallsRect;
	}

	public void onPlayerPutBomb(final int playerIndex){
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				players[playerIndex].putBomb(stage);
			}
		});
		//players[playerIndex].putBomb(bombSquarePosition,stage);
	}

	public void sendBombPosition(Vector2 bombSquarePosition){
		BufferBombPosition buffer = new BufferBombPosition();
		buffer.bombSquarePositiion = bombSquarePosition;
		server.sendToAllTCP(buffer);
	}

	public Array<Bomb> getBombsPlayer(int playerIndex){
		Array<Bomb> bombsPlayer = new Array<Bomb>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bomb.class){
				Bomb bomb = (Bomb) actor;
				if(bomb.playerCount == playerIndex)
					bombsPlayer.add(bomb);
			}
		}
		return bombsPlayer;
	}

	public void sendBombExploded(Vector2 bombSquarePosition, Array<Vector2> explosionsSquaresPositions){
		BufferBombExploded buffer = new BufferBombExploded();
		buffer.bombSquarePosition = bombSquarePosition;
		buffer.explosionsSquaresPositions = explosionsSquaresPositions;
		server.sendToAllTCP(buffer);
	}

	public void sendBonusAppeared(int bonus, Vector2 bonusSquarePosition){
		BufferBonusAppeared buffer = new BufferBonusAppeared();
		buffer.bonus = bonus;
		buffer.bonusSquarePosition = bonusSquarePosition;
		server.sendToAllTCP(buffer);
	}

	public void sendBonusDisppeared(Vector2 bonusSquarePosition){
		BufferBonusDisappeared buffer = new BufferBonusDisappeared();
		buffer.bonusSquarePosition = bonusSquarePosition;
		server.sendToAllTCP(buffer);
	}

	public void sendPlayerDie(int playerIndex){
		BufferPlayerDie buffer = new BufferPlayerDie();
		buffer.playerIndex = playerIndex;
		server.sendToAllTCP(buffer);
	}

	public void onPlayerDie(){
		int playersAlive = 0;
		for(int i = 0; i< players.length; i++){
			if(players[i].isAlive()) playersAlive++;
		}

		if(playersAlive == 1){
			PlayerNetworkServer lastPlayerAlive = null;
			for(int i = 0; i< players.length; i++){
				if(players[i].isAlive()) lastPlayerAlive = players[i];
			}
			BufferEndGame buffer = new BufferEndGame();
			buffer.winnerPlayerIndex = lastPlayerAlive.getPlayerIndex();
			server.sendToAllTCP(buffer);
			final String winnerName = lastPlayerAlive.getName();
			server.removeListener(serverGameListener);
			server.stop();
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					game.setScreen(new EndGameScreen(game,winnerName,false));
				}
			});
		}
		else if(playersAlive == 0){
			BufferEndGame buffer = new BufferEndGame();
			buffer.winnerPlayerIndex = -1;
			server.sendToAllTCP(buffer);
			server.removeListener(serverGameListener);
			server.stop();
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					game.setScreen(new EndGameScreen(game,null,true));
				}
			});
		}
	}

	public void onPlayerDisconnected(Connection connection){
		int playerIndex = playersId.get(connection.getID());
		BufferPlayerDie buffer = new BufferPlayerDie();
		buffer.playerIndex = playerIndex;
		server.sendToAllTCP(buffer);
		players[playerIndex].setDie();
		playersId.remove(connection);
	}
}

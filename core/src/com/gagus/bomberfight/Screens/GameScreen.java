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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.gagus.bomberfight.Actors.Bomb;
import com.gagus.bomberfight.Actors.Bonus;
import com.gagus.bomberfight.Actors.BreakableWall;
import com.gagus.bomberfight.Actors.CountdownStartGame;
import com.gagus.bomberfight.Actors.GameTimer;
import com.gagus.bomberfight.Actors.Player;
import com.gagus.bomberfight.Actors.UnbreakableWall;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Actors.MapManager;
import com.gagus.bomberfight.Interfaces.CollisionGetter;
import com.gagus.bomberfight.Interfaces.PlayerDieListener;
import com.gagus.bomberfight.Interfaces.GameTimerListener;


/**
 * Created by Gaetan on 24/12/2017.
 */

public class GameScreen implements Screen, PlayerDieListener, CollisionGetter, GameTimerListener {

	//game states
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_OVER = 3;

	int gameState;

	Stage stage;
	Stage playStage;
	BomberFight game;
	Player playerOne;
	Player playerTwo;
	boolean gameFinished;
	float timerEnd;
	boolean equality;
	Player winner;
	boolean countdownFinished;
	CountdownStartGame countdownStartGame;

	Image backgroundActor;

	// pause/play items
	ImageButton pauseButtonActor;
	ImageButton playButtonActor;

	GameTimer gameTimer;

	Array<Rectangle> bombsRect = new Array<Rectangle>();
	Array<Rectangle> explosionsRect = new Array<Rectangle>();
	Array<Bonus> bonuses = new Array<Bonus>();
	Array<Rectangle> allwallsRect = new Array<Rectangle>();
	Array<Rectangle> unbreakableWallsRect = new Array<Rectangle>();
	Array<Rectangle> breakableWallsRect = new Array<Rectangle>();
	Array<BreakableWall> breakablesWalls = new Array<BreakableWall>();
	Array<UnbreakableWall> unbreakablesWalls = new Array<UnbreakableWall>();



	public GameScreen(final BomberFight game, String fileName, String[] names) {
		this.game = game;
		stage = new Stage(game.viewport,game.batch);
		playStage = new Stage(game.viewport,game.batch);
		Gdx.app.log("stage","charged");
		gameFinished = false;
		timerEnd = 0;
		equality = false;
		countdownFinished = false;
		gameState = GAME_PAUSED;



		// Play/Pause items
		//textures buttons init
		TextureRegion playButtonImage = new TextureRegion(new Texture(Gdx.files.internal("images/buttons/playButton.png")));
		TextureRegion pauseButtonImage = new TextureRegion((new Texture(Gdx.files.internal("images/buttons/pauseButton.png"))));
		// create actors
		pauseButtonActor = new ImageButton(new TextureRegionDrawable(pauseButtonImage));
		playButtonActor = new ImageButton(new TextureRegionDrawable(playButtonImage));
		// set positions
		//pauseButtonActor.setPosition(BomberFight.WIDTH/2-pauseButtonActor.getWidth()/2,BomberFight.HEIGHT-pauseButtonActor.getHeight());
		pauseButtonActor.setPosition(BomberFight.WIDTH/2-pauseButtonActor.getWidth()/2,0);
		playButtonActor.setPosition(BomberFight.WIDTH/2-playButtonActor.getWidth()/2,BomberFight.HEIGHT/2-playButtonActor.getHeight()/2);
		//setlistener to pause button
		pauseButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on pause button");
				pause();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on pause button");
			}
		});
		playButtonActor.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch down on play button");
				play();
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("input","touch up on play button");
			}
		});

		playStage.addActor(playButtonActor);

		pauseButtonActor.setVisible(false);

		//gametimer
		gameTimer = new GameTimer(this);
		stage.addActor(gameTimer);

		//background
		TextureRegion backgroundImage = new TextureRegion(new Texture(Gdx.files.internal("images/backgrounds/herbe.png")));
		backgroundActor = new Image(backgroundImage);
		backgroundActor.setPosition(BomberFight.GameArea.x,0);


		//create actors
		playerOne = new Player(new Vector2(1,9),names[0],1,stage,this,this);
		playerTwo = new Player(new Vector2(13,1),names[1],2,stage,this,this);
		countdownStartGame = new CountdownStartGame();


		// Add actors to stage
		stage.addActor(backgroundActor);
		stage.addActor(pauseButtonActor);
		stage.addActor(playerOne);
		stage.addActor(playerTwo);
		//set z indez to actors
		backgroundActor.setZIndex(1);
		pauseButtonActor.setZIndex(2);
		gameTimer.setZIndex(2);
		playerOne.setZIndex(3);
		playerTwo.setZIndex(3);

		playerOne.setButtons();
		playerTwo.setButtons();

		Gdx.input.setInputProcessor(playStage);

		//mapmanager
		MapManager mapManager = new MapManager(fileName,stage);

		actUnbreakablesWalls();
		actUnbreakablesWallsRect();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		switch (gameState){
			case GAME_READY:
				stage.draw();
				countdownStartGame.act(delta);
				countdownStartGame.draw(stage.getBatch(),stage.getCamera());
				countdownFinished = countdownStartGame.isCountdownFinished();
				if(countdownFinished){
					playerOne.enableButtons();
					playerTwo.enableButtons();
					gameState = GAME_RUNNING;
				}
				break;
			case GAME_RUNNING:
				actBombsRect();
				actBonuses();
				actExplosionsRect();
				actBreakablesWalls();
				actBreakablesWallsRect();
				stage.act(delta);
				stage.draw();
				break;
			case GAME_PAUSED:
				stage.draw();
				playStage.draw();
				break;
			case GAME_OVER:
				stage.draw();
				endGame(delta, getWinner());
				break;
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		gameState = GAME_PAUSED;
		pauseButtonActor.setVisible(false);
		playButtonActor.setVisible(true);
		playerOne.disableButtons();
		playerTwo.disableButtons();
		Gdx.input.setInputProcessor(playStage);
	}

	public void play(){
		if(countdownFinished) {
			gameState = GAME_RUNNING;
			playerOne.enableButtons();
			playerTwo.enableButtons();
		}
		else {
			gameState = GAME_READY;
		}
		pauseButtonActor.setVisible(true);
		playButtonActor.setVisible(false);
		Gdx.input.setInputProcessor(stage);
	}
	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		playStage.dispose();
	}

	@Override
	public void onPlayerDie() {
		gameState = GAME_OVER;
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

	public void endGame(float delta,Player winner){
		timerEnd+=delta;
		if(timerEnd >= 1) {
			results();
			game.setScreen(new EndGameScreen(game,winner.getName(),equality));
		}
	}

	public Player getWinner(){
		Player winner = new Player();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Player.class){
				if(((Player)actor).isAlive()) winner = (Player)actor;
			}
		}
		return winner;
	}

	public Array<Player> getPlayers(){
		Array<Player> players = new Array<Player>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Player.class){
				players.add((Player)actor);
			}
		}
		return players;
	}

	public void results(){
		Array<Player> players = getPlayers();
		int deadPlayerCount = 0;
		for(Player player : players){
			if(!player.isAlive()) deadPlayerCount++;
		}
		if(deadPlayerCount == players.size || deadPlayerCount == 0) equality = true;
		else winner = getWinner();
		Gdx.app.log("result",String.valueOf(equality));
		Gdx.app.log("result","count of dead players : "+String.valueOf(deadPlayerCount));
	}

	@Override
	public void OnGameTimerFinished() {
		gameState = GAME_OVER;
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
			Bonus up = new Bonus(bonusRandom, squarePosition, collisionGetter);
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
}

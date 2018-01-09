package com.gagus.bomberfight.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Interfaces.CollisionGetter;
import com.gagus.bomberfight.Interfaces.PlayerDieListener;

public class Player extends Actor {

	private float marginAera;
	private Touchpad joystick;
	private Vector2 joystickPosition;
	private Stage stage;
	private int deadzoneTouchpad = 10;
	private MapManager mapManager;
	//private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private int marginMove = 20;
	private ImageButton bombButton;
	private Vector2 bombButtonPosition;

	private PlayerDieListener playerDieListener;
	private CollisionGetter collisionGetter;

	// player's caracteristics
	private float deadTime = 0;
	private int bombsCount;
	private boolean isAlive;
	private String name;
	private Rectangle playerRect;
	private int speed;
	private int playerCount;
	private int explosionSizeBomb;
	private int speedBonus;
	private int speedLimit;

	//textures
	private int framesCount = 3;
	private TextureRegion[] downFrames;
	private TextureRegion[] upFrames;
	private TextureRegion[] leftFrames;
	private TextureRegion[] rightFrames;
	private TextureRegion currentFrame;

	// animations
	private Animation walkRightAnimation;
	private Animation walkLeftAnimation;
	private Animation walkUpAnimation;
	private Animation walkDownAnimation;
	private float elapsedTime;
	private float frameDuration;

	// text for player name
	BitmapFont fontName;
	Label nameActor;
	Rectangle nameRect;



	public Player(){}

	public Player(Vector2 positionSquare, String name, final int playerCount, Stage stage, MapManager mapManager, PlayerDieListener playerDieListener,CollisionGetter collisionGetter) {
		// textures init
		downFrames = new TextureRegion[framesCount];
		upFrames = new TextureRegion[framesCount];
		leftFrames = new TextureRegion[framesCount];
		rightFrames = new TextureRegion[framesCount];
		downFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerDown1.png")));
		downFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerDown2.png")));
		downFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerDown3.png")));
		upFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerUp1.png")));
		upFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerUp2.png")));
		upFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerUp3.png")));
		leftFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerLeft1.png")));
		leftFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerLeft2.png")));
		leftFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerLeft3.png")));
		rightFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerRight1.png")));
		rightFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerRight2.png")));
		rightFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/playerImages/playerRight3.png")));
		currentFrame = downFrames[0];

		//Animations
		frameDuration = 0.07f;
		walkDownAnimation = new Animation<TextureRegion>(frameDuration, downFrames);
		walkUpAnimation = new Animation<TextureRegion>(frameDuration, upFrames);
		walkLeftAnimation = new Animation<TextureRegion>(frameDuration, leftFrames);
		walkRightAnimation = new Animation<TextureRegion>(frameDuration, rightFrames);

		// variables init
		this.playerDieListener = playerDieListener;
		this.collisionGetter = collisionGetter;
		this.name = name;
		this.setName(name);
		bombsCount = 1;
		isAlive = true;
		explosionSizeBomb = 3;
		playerRect = new Rectangle(positionSquare.x*BomberFight.SQUARESIZE,positionSquare.y*BomberFight.SQUARESIZE,currentFrame.getRegionWidth(),currentFrame.getRegionHeight());
		speed = 100;
		this.playerCount = playerCount;
		marginAera = BomberFight.GameArea.x;
		this.stage = stage;
		this.mapManager = mapManager;
		elapsedTime = 0;
		speedLimit = 220;
		speedBonus = 30;

		// text name init
		fontName = new BitmapFont(Gdx.files.internal("fonts/comicSansMs15.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle(fontName, Color.BLACK);
		nameActor = new Label(name,labelStyle);
		nameRect = new Rectangle(getCenter().x-nameActor.getWidth()/2,playerRect.y+playerRect.width+nameActor.getHeight(),nameActor.getWidth(),nameActor.getHeight());

		// Joystick creation
		Touchpad.TouchpadStyle stylepad = new Touchpad.TouchpadStyle(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/joystickBackground.png")))),new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/knob.png")))));
		joystick = new Touchpad(deadzoneTouchpad,stylepad);
		joystick.setSize(100,100);
		if(playerCount == 1) joystickPosition = new Vector2(15,BomberFight.HEIGHT-joystick.getHeight()-15);
		else joystickPosition = new Vector2(BomberFight.WIDTH-joystick.getWidth()-15,15);
		joystick.setPosition(joystickPosition.x,joystickPosition.y);
		Gdx.app.log("in player "+playerCount,"joystick created");



		// Button bombs creation
		bombButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/bombButtonUp.png")))),new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/buttons/bombButtonDown.png")))));

		if(playerCount ==1) bombButtonPosition = new Vector2(joystickPosition.x,15);
		else bombButtonPosition = new Vector2(joystickPosition.x+15,BomberFight.HEIGHT-bombButton.getHeight()-15);
		bombButton.setPosition(bombButtonPosition.x+15,bombButtonPosition.y);
		bombButton.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("player "+playerCount,"touch down on bomb button");
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				putBomb();
			}
		});

		disableButtons();



	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(currentFrame,playerRect.x+marginAera,playerRect.y);
		fontName.draw(batch,name,nameRect.x+marginAera,nameRect.y);
		//shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//for(Rectangle wall : wallsCollided) shapeRenderer.rect(wall.x+marginAera,wall.y,wall.width,wall.height);
		//shapeRenderer.end();
	}

	@Override
	public void act(float delta) {
		if(isAlive){
			collideWithExplosions();
			move(delta);
			collideWithBonus();
		}
		else{
			playerDieListener.onPlayerDie();
		}
	}
	public void setButtons(){
		stage.addActor(joystick);
		joystick.setZIndex(4);
		stage.addActor(bombButton);
		bombButton.setZIndex(4);
	}

	public void disableButtons(){
		joystick.setTouchable(Touchable.disabled);
		bombButton.setTouchable(Touchable.disabled);

	}

	public void enableButtons(){
		joystick.setTouchable(Touchable.enabled);
		bombButton.setTouchable(Touchable.enabled);
	}

	private void putBomb(){
		boolean collideWithBomb = false;
		for(Bomb bomb : collisionGetter.getBombs()){
			Gdx.app.log("bomb square",bomb.getSquarePosition().toString());
			Gdx.app.log("player square",this.getCenterSquare().toString());
			if(bomb.getSquarePosition().x == this.getCenterSquare().x && bomb.getSquarePosition().y == this.getCenterSquare().y)
			{
				collideWithBomb = true;
			}
		}

		if(getCountBombsAct() < bombsCount && !collideWithBomb){
			Bomb bomb = new Bomb(playerCount,getCenterSquare(),explosionSizeBomb,mapManager,collisionGetter);
			stage.addActor(bomb);
			bomb.setZIndex(2);
		}
	}



	private Vector2 getCenter(){
		return new Vector2(playerRect.x+playerRect.width/2,playerRect.y+playerRect.height/2);
	}

	private Vector2 getCenterSquare(){
		return mapManager.getSquareByPosition(new Vector2(getCenter().x,getCenter().y));
	}

	public float[][] getSquaresPosition(){
		return new float[][]{ {playerRect.x/BomberFight.SQUARESIZE, (playerRect.y+playerRect.width)/BomberFight.SQUARESIZE} , { (playerRect.x+playerRect.width)/BomberFight.SQUARESIZE, playerRect.y/BomberFight.SQUARESIZE} };
	}

	public Array<Bomb> getBombsPlayer(){
		Array<Bomb> bombsPlayer = new Array<Bomb>();
		for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bomb.class){
				bombsPlayer.add((Bomb)actor);
			}
		}
		return bombsPlayer;
	}

	public int getCountBombsAct(){
		return getBombsPlayer().size;
	}

	private void move(float delta){
		//get the direction of the joystick (4 axes : up, down, left, right)
		Vector2 direction = new Vector2(0,0);
		Array<Rectangle> walls = mapManager.getWalls();
		Array<Rectangle> bombs = collisionGetter.getBombsRect();
		Vector2 newPlayerPosition;
		Rectangle newPlayerRect;
		boolean collideWithBomb = false;
		boolean collideWithWall = false;
		boolean lastCollideWithBomb = false;
		Array<Rectangle> wallsCollided = new Array<Rectangle>();
		Rectangle bombCollided = new Rectangle();
		Rectangle lastWallCollided;

		if(joystick.isTouched())
		{
			elapsedTime += delta;
			Vector2 joystickPercent = new Vector2(joystick.getKnobPercentX(),joystick.getKnobPercentY());
			if(joystickPercent.y>0 && Math.abs(joystickPercent.y)>Math.abs(joystickPercent.x)) {
				direction.y = 1;
				currentFrame = (TextureRegion)walkUpAnimation.getKeyFrame(elapsedTime,true);
			}
			else if(joystickPercent.y<0 && Math.abs(joystickPercent.y)>Math.abs(joystickPercent.x)) {
				direction.y = -1;
				currentFrame = (TextureRegion)walkDownAnimation.getKeyFrame(elapsedTime,true);
			}
			else if(joystickPercent.x>0 && Math.abs(joystickPercent.x)>Math.abs(joystickPercent.y)) {
				direction.x = 1;
				currentFrame = (TextureRegion)walkRightAnimation.getKeyFrame(elapsedTime,true);
			}
			else if(joystickPercent.x<0 && Math.abs(joystickPercent.x)>Math.abs(joystickPercent.y)) {
				direction.x = -1;
				currentFrame = (TextureRegion)walkLeftAnimation.getKeyFrame(elapsedTime,true);
			}
			//Gdx.app.log("direction",direction.toString());
			//Gdx.app.log("joystick position",joystickPosition.toString());
			//Gdx.app.log("joystick percent",joystickPercent.toString());

			newPlayerPosition = new Vector2(playerRect.x+(delta*speed*direction.x),playerRect.y+(delta*speed*direction.y));


			if(newPlayerPosition.x + playerRect.width > BomberFight.GameArea.width-BomberFight.SQUARESIZE) newPlayerPosition.x = BomberFight.GameArea.width-BomberFight.SQUARESIZE - playerRect.width;
			else if(newPlayerPosition.x < BomberFight.SQUARESIZE) newPlayerPosition.x = BomberFight.SQUARESIZE;
			else if(newPlayerPosition.y + playerRect.height > BomberFight.GameArea.height-BomberFight.SQUARESIZE) newPlayerPosition.y = BomberFight.GameArea.height-BomberFight.SQUARESIZE - playerRect.height;
			else if(newPlayerPosition.y < BomberFight.SQUARESIZE) newPlayerPosition.y = BomberFight.SQUARESIZE;

			newPlayerRect = new Rectangle(newPlayerPosition.x,newPlayerPosition.y,playerRect.width,playerRect.height);
			for(Rectangle wall : walls)
				if(wall.overlaps(newPlayerRect))
				{
					collideWithWall = true;
					wallsCollided.add(wall);
				}

			for(Rectangle bomb : bombs)
				if(bomb.overlaps(playerRect))
				{
					lastCollideWithBomb = true;
				}

			for(Rectangle bomb : bombs)
				if(bomb.overlaps(newPlayerRect))
				{
					collideWithBomb = true;
					bombCollided = bomb;
				}

			if(!collideWithWall && !collideWithBomb) playerRect.setPosition(newPlayerPosition);
			else if(!collideWithWall && collideWithBomb && lastCollideWithBomb) playerRect.setPosition(newPlayerPosition);
			else if(collideWithWall && ((!collideWithBomb && !lastCollideWithBomb) || (collideWithBomb && lastCollideWithBomb)))
			{
				lastWallCollided = wallsCollided.get(wallsCollided.size-1);
				Gdx.app.log("last wall collided",lastWallCollided.toString());
				if(direction.x == 1)
				{
					if(newPlayerPosition.y + marginMove > lastWallCollided.y + lastWallCollided.height) newPlayerPosition.y = lastWallCollided.y + lastWallCollided.height;
					else if(newPlayerPosition.y + playerRect.height - marginMove < lastWallCollided.y) newPlayerPosition.y = lastWallCollided.y - playerRect.height;
					else newPlayerPosition.x = lastWallCollided.x - playerRect.width;
				}
				else if(direction.x == -1)
				{
					if(newPlayerPosition.y + marginMove > lastWallCollided.y + lastWallCollided.height) newPlayerPosition.y = lastWallCollided.y + lastWallCollided.height;
					else if(newPlayerPosition.y + playerRect.height - marginMove < lastWallCollided.y) newPlayerPosition.y = lastWallCollided.y - playerRect.height;
					else newPlayerPosition.x = lastWallCollided.x + lastWallCollided.width;
				}
				else if(direction.y == 1)
				{
					if(newPlayerPosition.x + marginMove > lastWallCollided.x + lastWallCollided.width) newPlayerPosition.x = lastWallCollided.x + lastWallCollided.width;
					else if(newPlayerPosition.x + playerRect.width - marginMove < lastWallCollided.x) newPlayerPosition.x = lastWallCollided.x - playerRect.width;
					else newPlayerPosition.y = lastWallCollided.y - playerRect.height;
				}
				else if(direction.y == -1)
				{
					if(newPlayerPosition.x + marginMove > lastWallCollided.x + lastWallCollided.width) newPlayerPosition.x = lastWallCollided.x + lastWallCollided.width;
					else if(newPlayerPosition.x + playerRect.width - marginMove < lastWallCollided.x) newPlayerPosition.x = lastWallCollided.x - playerRect.width;
					else newPlayerPosition.y = lastWallCollided.y + lastWallCollided.height;
				}
				playerRect.setPosition(newPlayerPosition);
			}
			else if(collideWithBomb && !lastCollideWithBomb){
				Gdx.app.log("bomb collided",bombCollided.toString());
				if(direction.x == 1)
				{
					newPlayerPosition.x = bombCollided.x - playerRect.width;
				}
				else if(direction.x == -1)
				{
					newPlayerPosition.x = bombCollided.x + bombCollided.width;
				}
				else if(direction.y == 1)
				{
					newPlayerPosition.y = bombCollided.y - playerRect.height;
				}
				else if(direction.y == -1)
				{
					newPlayerPosition.y = bombCollided.y + bombCollided.height;
				}
				playerRect.setPosition(newPlayerPosition);
			}
			nameRect = new Rectangle(getCenter().x-nameActor.getWidth()/2,playerRect.y+playerRect.width+nameActor.getHeight(),nameActor.getWidth(),nameActor.getHeight());
		}

		/* Part for move square by square
		newPosition = new Vector2((playerSquarePosition.x+direction.x),(playerSquarePosition.y+direction.y));
		char square = mapStructure[(int)newPosition.y][(int)newPosition.x];
		if(square != 'k' && square != 'm') playerRect.setPosition(new Vector2(newPosition.x*BomberFight.SQUARESIZE,newPosition.y*BomberFight.SQUARESIZE));
		*/
	}

	private void collideWithExplosions(){
		boolean collideWithExplosion = false;
		Array<Rectangle> explosions = collisionGetter.getExplosionsRect();
		Array<Rectangle> explosionsCollided = new Array<Rectangle>();
		for(Rectangle explosion : explosions) {
			if (explosion.overlaps(playerRect)) {
				collideWithExplosion = true;
				explosionsCollided.add(explosion);
			}
		}
		if(collideWithExplosion) isAlive = false;
	}

	public boolean isAlive(){
		//return 1 if isAlive or 0 if dead
		return isAlive;
	}

	private void collideWithBonus(){
		boolean collideWithBonus = false;
		Array<Bonus> bonuses = collisionGetter.getBonuses();
		Bonus bonusCollided = new Bonus();
		for(Bonus bonus : bonuses) {
			if (bonus.getRect().overlaps(playerRect)) {
				collideWithBonus = true;
				bonusCollided = bonus;
			}
		}
		if(collideWithBonus){
			switch(bonusCollided.getBonus()){
				case(Bonus.SPEED):
					addSpeedBonus();
					break;
				case(Bonus.BOMBCOUNT):
					addBombCountBonus();
					break;
				case(Bonus.BOMBSIZE):
					addExplosionSizeBombBonus();
					break;
			}
			bonusCollided.destroy();
		}
	}

	private void addSpeedBonus(){
		if(speed < speedLimit) speed+=speedBonus;
	}

	private void addBombCountBonus(){
		bombsCount++;
	}

	private void addExplosionSizeBombBonus(){
		explosionSizeBomb++;
	}


}

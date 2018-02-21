package com.gagus.bomberfight.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Interfaces.CollisionGetter;
import com.gagus.bomberfight.Interfaces.DataSender;
import com.gagus.bomberfight.Interfaces.PlayerDieListener;

/**
 * Created by Gaetan on 15/02/2018.
 */

public class PlayerNetworkServer extends Actor {

	private Vector2 positionSquare;
	private float marginAera;

	private Stage stage;


	//private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private int marginMove = 20;

	private PlayerDieListener playerDieListener;
	private CollisionGetter collisionGetter;
	private DataSender dataSender;

	// player's caracteristics
	private float deadTime = 0;
	private int bombsCount;
	private boolean isAlive;
	private String name;
	private Rectangle playerRect;
	private int speed;
	private int playerIndex;
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

	public PlayerNetworkServer(String name, int playerIndex, DataSender dataSender, CollisionGetter collisionGetter, PlayerDieListener playerDieListener) {
		this.name = name;
		this.setName(name);
		this.playerIndex = playerIndex;
		marginAera = BomberFight.GameArea.x;
		isAlive = true;
		this.dataSender = dataSender;
		this.collisionGetter = collisionGetter;
		this.playerDieListener = playerDieListener;

		this.speed = 100;
		bombsCount = 1;
		explosionSizeBomb = 3;
		speedLimit = 220;
		speedBonus = 30;

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
		elapsedTime = 0;

		//Animations
		frameDuration = 0.07f;
		walkDownAnimation = new Animation<TextureRegion>(frameDuration, downFrames);
		walkUpAnimation = new Animation<TextureRegion>(frameDuration, upFrames);
		walkLeftAnimation = new Animation<TextureRegion>(frameDuration, leftFrames);
		walkRightAnimation = new Animation<TextureRegion>(frameDuration, rightFrames);

		//player position determination
		positionSquare = new Vector2(0,0);
		switch(playerIndex){
			case 0:
				positionSquare.x = 1;
				positionSquare.y = 9;
				break;
			case 1:
				positionSquare.x = 13;
				positionSquare.y = 1;
				break;
			case 2:
				positionSquare.x = 1;
				positionSquare.y = 1;
				break;
			case 3:
				positionSquare.x = 13;
				positionSquare.y = 9;
				break;
		}
		Gdx.app.log("player server "+playerIndex,positionSquare.toString());
		playerRect = new Rectangle(positionSquare.x* BomberFight.SQUARESIZE,positionSquare.y*BomberFight.SQUARESIZE,currentFrame.getRegionWidth(),currentFrame.getRegionHeight());

		// text name init
		fontName = new BitmapFont(Gdx.files.internal("fonts/comicSansMs15.fnt"));
		Label.LabelStyle labelStyle = new Label.LabelStyle(fontName, Color.BLACK);
		nameActor = new Label(name,labelStyle);
		nameRect = new Rectangle(getCenter().x-nameActor.getWidth()/2,playerRect.y+playerRect.width+nameActor.getHeight(),nameActor.getWidth(),nameActor.getHeight());

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(isAlive) {
			batch.draw(currentFrame, playerRect.x + marginAera, playerRect.y);
			fontName.draw(batch, name, nameRect.x + marginAera, nameRect.y);
		}
	}

	@Override
	public void act(float delta) {
		if(isAlive){
			collideWithExplosions();
			collideWithBonus();
		}
		else{
			playerDieListener.onPlayerDie();
		}
	}

	/*public void move(Vector2 direction){
		float delta = Gdx.graphics.getDeltaTime();
		Vector2 newPlayerPosition = new Vector2(playerRect.x+(delta*speed*direction.x),playerRect.y+(delta*speed*direction.y));
		playerRect.setPosition(newPlayerPosition);
		dataSender.sendPlayerPosition(playerIndex, newPlayerPosition);
		nameRect = new Rectangle(getCenter().x-nameActor.getWidth()/2,playerRect.y+playerRect.width+nameActor.getHeight(),nameActor.getWidth(),nameActor.getHeight());
	}*/

	private Vector2 getCenter() {
		return new Vector2(playerRect.x + playerRect.width / 2, playerRect.y + playerRect.height / 2);
	}

	public void move(Vector2 direction){
		float delta = Gdx.graphics.getDeltaTime();
		//get the direction of the joystick (4 axes : up, down, left, right)
		Array<Rectangle> walls = collisionGetter.getAllWallsRect();
		Array<Rectangle> bombs = collisionGetter.getBombsRect();
		Vector2 newPlayerPosition;
		Rectangle newPlayerRect;
		boolean collideWithBomb = false;
		boolean collideWithWall = false;
		boolean lastCollideWithBomb = false;
		Array<Rectangle> wallsCollided = new Array<Rectangle>();
		Rectangle bombCollided = new Rectangle();
		Rectangle lastWallCollided;

		elapsedTime += delta;
		newPlayerPosition = new Vector2(playerRect.x+(delta*speed*direction.x),playerRect.y+(delta*speed*direction.y));
		if(direction.x == 1) currentFrame = (TextureRegion)walkRightAnimation.getKeyFrame(elapsedTime,true);
		else if(direction.x == -1) currentFrame = (TextureRegion)walkLeftAnimation.getKeyFrame(elapsedTime,true);
		else if(direction.y == 1) currentFrame = (TextureRegion)walkUpAnimation.getKeyFrame(elapsedTime,true);
		else if(direction.y == -1) currentFrame = (TextureRegion)walkDownAnimation.getKeyFrame(elapsedTime,true);

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
			//Gdx.app.log("last wall collided",lastWallCollided.toString());
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
			//Gdx.app.log("bomb collided",bombCollided.toString());
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

		dataSender.sendPlayerPosition(playerIndex, newPlayerPosition,direction);
		nameRect = new Rectangle(getCenter().x-nameActor.getWidth()/2,playerRect.y+playerRect.width+nameActor.getHeight(),nameActor.getWidth(),nameActor.getHeight());
	}

	public Array<Bomb> getBombsPlayer(){
		return collisionGetter.getBombsPlayer(playerIndex);
		/*for(Actor actor : stage.getActors()){
			if(actor.getClass() == Bomb.class){
				bombsPlayer.add((Bomb)actor);
			}
		}*/
	}

	public int getCountBombsAct(){
		return getBombsPlayer().size;
	}

	public void putBomb(Vector2 bombSquarePosition,Stage stage){
		boolean collideWithBomb = false;
		for(Bomb bomb : collisionGetter.getBombs()){
			Gdx.app.log("bomb square",bomb.getSquarePosition().toString());
			Gdx.app.log("player square",this.getCenterSquare().toString());
			if(bomb.getSquarePosition().x == bombSquarePosition.x && bomb.getSquarePosition().y == bombSquarePosition.y)
			{
				collideWithBomb = true;
			}
		}

		if(getCountBombsAct() < bombsCount && !collideWithBomb){
			Bomb bomb = new Bomb(playerIndex,bombSquarePosition,explosionSizeBomb,collisionGetter);
			stage.addActor(bomb);
			bomb.setZIndex(2);
			dataSender.sendBombPosition(bombSquarePosition);
		}
	}

	public void putBomb(Stage stage){
		boolean collideWithBomb = false;
		for(Bomb bomb : collisionGetter.getBombs()){
			//Gdx.app.log("bomb square",bomb.getSquarePosition().toString());
			//Gdx.app.log("player square",this.getCenterSquare().toString());
			if(bomb.getSquarePosition().x == getCenterSquare().x && bomb.getSquarePosition().y == getCenterSquare().y)
			{
				collideWithBomb = true;
			}
		}
		Gdx.app.log("server","bomb on bomb : "+collideWithBomb);
		Gdx.app.log("server","player "+playerIndex+" bombs count : "+getCountBombsAct());
		if(getCountBombsAct() < bombsCount && !collideWithBomb){
			Bomb bomb = new Bomb(playerIndex,getCenterSquare(),explosionSizeBomb,collisionGetter, dataSender);
			bomb.setZIndex(2);
			stage.addActor(bomb);
			bomb.setZIndex(2);
			Gdx.app.log("server","bomb putted for player "+playerIndex);
			dataSender.sendBombPosition(getCenterSquare());
		}
	}

	private Vector2 getCenterSquare(){
		return BomberFight.getSquareByPosition(new Vector2(getCenter().x,getCenter().y));
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
		if(collideWithExplosion) {
			isAlive = false;
			dataSender.sendPlayerDie(playerIndex);
		}
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

	public int getPlayerIndex(){return playerIndex;}

	public void setDie(){
		isAlive=false;
	}
}

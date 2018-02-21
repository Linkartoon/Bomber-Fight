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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gagus.bomberfight.BomberFight;

/**
 * Created by Gaetan on 15/02/2018.
 */

public class PlayerNetworkClient extends Actor {
	private float marginAera;
	private String name;
	private int playerIndex;
	private Rectangle playerRect;
	private boolean isAlive;
	private Vector2 positionSquare;

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
	private float frameDuration;
	float elapsedTime;

	BitmapFont fontName;
	Label nameActor;
	Rectangle nameRect;

	public PlayerNetworkClient(String name, int playerIndex) {
		marginAera = BomberFight.GameArea.x;
		isAlive = true;
		elapsedTime = 0;
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
		Gdx.app.log("player client"+playerIndex,positionSquare.toString());

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

		this.name = name;
		this.setName(name);
		this.playerIndex = playerIndex;
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

	private Vector2 getCenter(){
		return new Vector2(playerRect.x+playerRect.width/2,playerRect.y+playerRect.height/2);
	}

	public void setPosition(Vector2 position, Vector2 direction){
		elapsedTime += Gdx.graphics.getDeltaTime();
		if(direction.x == 1) currentFrame = (TextureRegion)walkRightAnimation.getKeyFrame(elapsedTime,true);
		else if(direction.x == -1) currentFrame = (TextureRegion)walkLeftAnimation.getKeyFrame(elapsedTime,true);
		else if(direction.y == 1) currentFrame = (TextureRegion)walkUpAnimation.getKeyFrame(elapsedTime,true);
		else if(direction.y == -1) currentFrame = (TextureRegion)walkDownAnimation.getKeyFrame(elapsedTime,true);
		playerRect.setPosition(position);
		nameRect = new Rectangle(getCenter().x-nameActor.getWidth()/2,playerRect.y+playerRect.width+nameActor.getHeight(),nameActor.getWidth(),nameActor.getHeight());
	}

	public Vector2 getCenterSquare(){
		return BomberFight.getSquareByPosition(new Vector2(getCenter().x,getCenter().y));
	}

	public void setDie(){
		isAlive = false;
	}

	public boolean isAlive(){
		return isAlive;
	}
}

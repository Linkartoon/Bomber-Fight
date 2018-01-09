package com.gagus.bomberfight.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.gagus.bomberfight.BomberFight;
import com.gagus.bomberfight.Interfaces.CollisionGetter;

/**
 * Created by Gaetan on 04/01/2018.
 */

public class Bonus extends Actor {

	// Constants for bonus ID
	public static final int SPEED = 0;
	public static final int BOMBCOUNT = 1;
	public static final int BOMBSIZE = 3;

	// Images bonus
	private Texture currentTexture;

	// variables
	private Rectangle bonusRect;
	private float margin = BomberFight.GameArea.x;
	private int bonus;
	private CollisionGetter collisionGetter;

	public Bonus() {}

	public Bonus(int bonus, Vector2 squarePosition, CollisionGetter collisionGetter) {
		this.bonus = bonus;
		this.collisionGetter = collisionGetter;
		switch(bonus)
		{
			case SPEED:
				currentTexture = new Texture(Gdx.files.internal("images/bonus/speedBonus.png"));
				break;
			case BOMBCOUNT:
				currentTexture = new Texture(Gdx.files.internal("images/bonus/bombCountBonus.png"));
				break;
			case BOMBSIZE:
				currentTexture = new Texture(Gdx.files.internal("images/bonus/bombSizeBonus.png"));
				break;
		}
		bonusRect = new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,currentTexture.getWidth(),currentTexture.getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(currentTexture,bonusRect.x+margin,bonusRect.y);
	}

	@Override
	public void act(float delta) {
		collideWithExplosion();
	}

	public void destroy(){
		this.remove();
	}

	public Rectangle getRect(){
		return bonusRect;
	}

	public int getBonus(){
		return  bonus;
	}

	private void collideWithExplosion(){
		Array<Rectangle> explosionsRect = collisionGetter.getExplosionsRect();
		boolean collideWithExplosion = false;
		for(Rectangle explosion : explosionsRect){
			if(explosion.overlaps(bonusRect)){
				this.destroy();
			}
		}
	}

}

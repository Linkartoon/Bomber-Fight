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
 * Created by Gaetan on 16/02/2018.
 */

public class BombClient extends Actor {

	Vector2 squarePosition;
	int state;
	Array<Vector2> explosionsSquares;
	Texture bombImage;
	Texture explosionImage;
	float marginArea;
	Rectangle bombRect;
	float explodedTime;


	public BombClient(Vector2 squarePosition) {

		explodedTime = 0;
		this.marginArea = BomberFight.GameArea.x;
		this.squarePosition = squarePosition;
		state = 0;
		this.bombImage = new Texture(Gdx.files.internal("images/bomb/bombe.png"));
		this.explosionImage = new Texture(Gdx.files.internal("images/bomb/explosion.png"));
		this.bombRect = new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,bombImage.getWidth(),bombImage.getHeight());
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(state == 0){
			batch.draw(bombImage,bombRect.x+marginArea,bombRect.y);
		}
		else if(state == 1){
			if(explodedTime <= 0.3) {
				explodedTime += Gdx.graphics.getDeltaTime();
				for (Vector2 position : explosionsSquares) {
					batch.draw(explosionImage, position.x*BomberFight.SQUARESIZE + marginArea, position.y*BomberFight.SQUARESIZE);
				}
			}
			else{
				this.remove();
			}
		}
	}

	public void explode(Array<Vector2> explosionsSquares){
		this.state = 1;
		this.explosionsSquares = explosionsSquares;
	}

	public Vector2 getSquarePosition(){
		return squarePosition;
	}

}

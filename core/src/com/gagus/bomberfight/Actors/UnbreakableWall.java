package com.gagus.bomberfight.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gagus.bomberfight.BomberFight;

/**
 * Created by Gaetan on 11/01/2018.
 */

public class UnbreakableWall extends Actor {
	Rectangle wallRect;
	Texture unbreakableBlockImage;
	float marginAera;
	Vector2 squarePosition;

	public UnbreakableWall(Vector2 position) {
		unbreakableBlockImage = new Texture(Gdx.files.internal("images/walls/mur1.png"));
		marginAera = BomberFight.GameArea.x;
		wallRect = new Rectangle(position.x*BomberFight.SQUARESIZE,position.y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE);
		squarePosition = position;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(unbreakableBlockImage,wallRect.x+marginAera,wallRect.y);
	}

	public Rectangle getRect(){
		return wallRect;
	}

	public Vector2 getSquarePosition(){
		return squarePosition;
	}

	public void destroy(){this.remove();}
}

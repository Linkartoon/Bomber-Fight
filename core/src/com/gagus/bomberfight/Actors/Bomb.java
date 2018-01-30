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

import org.w3c.dom.css.Rect;

/**
 * Created by Gaetan on 25/12/2017.
 */

public class Bomb extends Actor {
	int playerCount;
	Vector2 squarePosition;
	double time;
	int state;
	int explosionSize;
	Array<Rectangle> explosionsSquare;
	public Rectangle bombRect;
	Texture bombImage;
	Texture explosionImage;
	float marginArea;
	MapManager mapManager;
	CollisionGetter collisionGetter;

	public Bomb(int playerCount, Vector2 squarePosition, int explosionSize, MapManager mapManager, CollisionGetter collisionGetter) {
		this.marginArea = BomberFight.GameArea.x;
		this.bombImage = new Texture(Gdx.files.internal("images/bomb/bombe.png"));
		this.bombRect = new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,bombImage.getWidth(),bombImage.getHeight());
		this.playerCount = playerCount;
		this.squarePosition = squarePosition;
		this.explosionSize = explosionSize;
		this.state = 0;
		this.time = 0;
		this.explosionsSquare = new Array<Rectangle>();
		this.explosionImage = new Texture(Gdx.files.internal("images/bomb/explosion.png"));
		this.mapManager = mapManager;
		setBounds(bombRect.x,bombRect.y,bombRect.width,bombRect.height);
		this.collisionGetter = collisionGetter;

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(state == 0){
			batch.draw(bombImage,bombRect.x+marginArea,bombRect.y);
		}
		else{
			for(Rectangle rect : explosionsSquare){
				batch.draw(explosionImage,rect.x+marginArea,rect.y);
			}
		}
	}

	@Override
	public void act(float delta) {
		time += delta;
		if(state == 0 && time >= 3){
			explode();
		}
		else if(state == 2) explode();
		else if(state == 1 && time >= 0.3){
			clean();
		}
	}

	public void explode(){
		Array<BreakableWall> breakablesWalls = collisionGetter.getBreakablesWalls();
		Array<UnbreakableWall> unbreakablesWalls = collisionGetter.getUnbreakablesWalls();

		Array<BreakableWall> newBreakablesWalls = new Array<BreakableWall>();
		Array<UnbreakableWall> newUnbreakablesWalls = new Array<UnbreakableWall>();

		for(BreakableWall wall : breakablesWalls)
			if(wall.getSquarePosition().x < squarePosition.x+explosionSize && wall.getSquarePosition().x > squarePosition.x-explosionSize || wall.getSquarePosition().y < squarePosition.y+explosionSize && wall.getSquarePosition().y > squarePosition.y-explosionSize)
				newBreakablesWalls.add(wall);

		for(UnbreakableWall wall : unbreakablesWalls)
			if(wall.getSquarePosition().x < squarePosition.x+explosionSize && wall.getSquarePosition().x > squarePosition.x-explosionSize || wall.getSquarePosition().y < squarePosition.y+explosionSize && wall.getSquarePosition().y > squarePosition.y-explosionSize)
				newUnbreakablesWalls.add(wall);
		//center bomb
		explosionsSquare.add(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));

		// range up
		for(int i=1;i<explosionSize;i++){

			BreakableWall bwall = null;
			boolean isUnbreakable = false;
			boolean isBreakable = false;
			for(UnbreakableWall wall : newUnbreakablesWalls)
				if(wall.getRect().overlaps(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y+i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE)))
					isUnbreakable = true;
			for(BreakableWall wall : newBreakablesWalls)
			if(wall.getRect().overlaps(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y+i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE))){
				isBreakable = true;
				bwall = wall;
			}
			if(isUnbreakable) break;
			else if(isBreakable) {
				collisionGetter.destroyBlock(new Vector2(squarePosition.x,squarePosition.y+i),collisionGetter);
				bwall.destroy();
				Gdx.app.log("destroy","up");
				break;
			}
			else explosionsSquare.add(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y+i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));

			/*if(mapStructure[(int)squarePosition.y+i][(int)squarePosition.x] == 'm') break;
			else if(mapStructure[(int)squarePosition.y+i][(int)squarePosition.x] == 'k'){
				collisionGetter.destroyBlock(new Vector2(squarePosition.x,squarePosition.y+i),collisionGetter);
				break;
			}
			else explosionsSquare.add(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y+i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));*/
		}

		// range down
		for(int i=1;i<explosionSize;i++){

			BreakableWall bwall = null;
			boolean isUnbreakable = false;
			boolean isBreakable = false;
			for(UnbreakableWall wall : newUnbreakablesWalls)
				if(wall.getRect().overlaps(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y-i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE)))
					isUnbreakable = true;
			for(BreakableWall wall : newBreakablesWalls)
				if(wall.getRect().overlaps(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y-i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE))){
					isBreakable = true;
					bwall = wall;
				}
			if(isUnbreakable) break;
			else if(isBreakable) {
				collisionGetter.destroyBlock(new Vector2(squarePosition.x,squarePosition.y-i),collisionGetter);
				bwall.destroy();
				Gdx.app.log("destroy","down");
				break;
			}
			else explosionsSquare.add(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y-i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));

			/*if(mapStructure[(int)squarePosition.y-i][(int)squarePosition.x] == 'm') break;
			else if(mapStructure[(int)squarePosition.y-i][(int)squarePosition.x] == 'k'){
				collisionGetter.destroyBlock(new Vector2(squarePosition.x,squarePosition.y-i),collisionGetter);
				break;
			}
			else explosionsSquare.add(new Rectangle(squarePosition.x*BomberFight.SQUARESIZE,(squarePosition.y-i)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));*/
		}

		// range right
		for(int i=1;i<explosionSize;i++){

			BreakableWall bwall = null;
			boolean isUnbreakable = false;
			boolean isBreakable = false;
			for(UnbreakableWall wall : newUnbreakablesWalls)
				if(wall.getRect().overlaps(new Rectangle((squarePosition.x+i)*BomberFight.SQUARESIZE,(squarePosition.y)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE)))
					isUnbreakable = true;
			for(BreakableWall wall : newBreakablesWalls)
				if(wall.getRect().overlaps(new Rectangle((squarePosition.x+i)*BomberFight.SQUARESIZE,(squarePosition.y)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE))){
					isBreakable = true;
					bwall = wall;
				}
			if(isUnbreakable) break;
			else if(isBreakable) {
				collisionGetter.destroyBlock(new Vector2((squarePosition.x+i),squarePosition.y),collisionGetter);
				bwall.destroy();
				Gdx.app.log("destroy","right");
				break;
			}
			else explosionsSquare.add(new Rectangle((squarePosition.x+i)*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));

			/*if(mapStructure[(int)squarePosition.y][(int)squarePosition.x+i] == 'm') break;
			else if(mapStructure[(int)squarePosition.y][(int)squarePosition.x+i] == 'k'){
				collisionGetter.destroyBlock(new Vector2(squarePosition.x+i,squarePosition.y),collisionGetter);
				break;
			}
			else explosionsSquare.add(new Rectangle((squarePosition.x+i)*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));*/
		}

		// range left
		for(int i=1;i<explosionSize;i++){

			BreakableWall bwall = null;
			boolean isUnbreakable = false;
			boolean isBreakable = false;
			for(UnbreakableWall wall : newUnbreakablesWalls)
				if(wall.getRect().overlaps(new Rectangle((squarePosition.x-i)*BomberFight.SQUARESIZE,(squarePosition.y)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE)))
					isUnbreakable = true;
			for(BreakableWall wall : newBreakablesWalls)
				if(wall.getRect().overlaps(new Rectangle((squarePosition.x-i)*BomberFight.SQUARESIZE,(squarePosition.y)*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE))){
					isBreakable = true;
					bwall = wall;
				}
			if(isUnbreakable) break;
			else if(isBreakable) {
				collisionGetter.destroyBlock(new Vector2((squarePosition.x-i),squarePosition.y),collisionGetter);
				bwall.destroy();
				Gdx.app.log("destroy","left");
				break;
			}
			else explosionsSquare.add(new Rectangle((squarePosition.x-i)*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));

			/*if(mapStructure[(int)squarePosition.y][(int)squarePosition.x-i] == 'm') break;
			else if(mapStructure[(int)squarePosition.y][(int)squarePosition.x-i] == 'k'){
				collisionGetter.destroyBlock(new Vector2(squarePosition.x-i,squarePosition.y),collisionGetter);
				break;
			}
			else explosionsSquare.add(new Rectangle((squarePosition.x-i)*BomberFight.SQUARESIZE,squarePosition.y*BomberFight.SQUARESIZE,BomberFight.SQUARESIZE,BomberFight.SQUARESIZE));*/
		}

		for(Rectangle explosion : getExplosionRect()) {
			for (Bomb bomb : collisionGetter.getBombs()) {
				if(explosion.overlaps(bomb.getRectangle()) && bomb.getState() == 0){
					bomb.explodedByBomb();
					Gdx.app.log("bomb","exploded by bomb");
				}
			}
		}
		time = 0;
		state = 1;
	}

	private void clean(){
		this.remove();
	}

	public Rectangle getRectangle(){
		return bombRect;
	}

	public Array<Rectangle> getExplosionRect(){
		return explosionsSquare;
	}

	public Vector2 getSquarePosition(){
		return squarePosition;
	}

	public void explodedByBomb(){
		state = 2;
	}

	public int getState(){
		return state;
	}
}

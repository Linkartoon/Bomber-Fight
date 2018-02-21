package com.gagus.bomberfight.Interfaces;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gagus.bomberfight.Actors.Bomb;
import com.gagus.bomberfight.Actors.Bonus;
import com.gagus.bomberfight.Actors.BreakableWall;
import com.gagus.bomberfight.Actors.UnbreakableWall;

/**
 * Created by Gaetan on 04/01/2018.
 */

public interface CollisionGetter {
	public Array<Rectangle> getBombsRect();
	public Array<Bomb> getBombs();
	public Array<Rectangle> getExplosionsRect();
	public Array<Bonus> getBonuses();
	public void destroyBlock(Vector2 squarePosition, CollisionGetter collisionGetter);
	public Array<Rectangle> getAllWallsRect();
	public Array<BreakableWall> getBreakablesWalls();
	public Array<UnbreakableWall> getUnbreakablesWalls();
	public Array<Rectangle> getUnbreakablesWallsRect();
	public Array<Rectangle> getBreakablesWallsRect();
	public Array<Bomb> getBombsPlayer(int playerIndex);
}

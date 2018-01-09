package com.gagus.bomberfight.Interfaces;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.gagus.bomberfight.Actors.Bomb;
import com.gagus.bomberfight.Actors.Bonus;

/**
 * Created by Gaetan on 04/01/2018.
 */

public interface CollisionGetter {
	public Array<Rectangle> getBombsRect();
	public Array<Bomb> getBombs();
	public Array<Rectangle> getExplosionsRect();
	public Array<Bonus> getBonuses();
}

package com.gagus.bomberfight.Interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Gaetan on 15/02/2018.
 */

public interface DataSender {
	public void sendPlayerPosition(int playerIndex, Vector2 position, Vector2 direction);
	public void sendBombPosition(Vector2 bombSquarePosition);
	public void sendWallDestroyed(Vector2 wallSquarePosition);
	public void sendBombExploded(Vector2 bombSquarePosition, Array<Vector2> explosionsSquaresPositions);
	public void sendBonusAppeared(int bonus, Vector2 bonusSquarePosition);
	public void sendBonusDisppeared(Vector2 bonusSquarePosition);
	public void sendPlayerDie(int playerIndex);
}

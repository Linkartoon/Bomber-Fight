package com.gagus.bomberfight.Listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gagus.bomberfight.Buffers.BufferBombExploded;
import com.gagus.bomberfight.Buffers.BufferBombPosition;
import com.gagus.bomberfight.Buffers.BufferBonusAppeared;
import com.gagus.bomberfight.Buffers.BufferBonusDisappeared;
import com.gagus.bomberfight.Buffers.BufferEndGame;
import com.gagus.bomberfight.Buffers.BufferPlayerDie;
import com.gagus.bomberfight.Buffers.BufferPlayerPosition;
import com.gagus.bomberfight.Buffers.BufferWallDestroyed;
import com.gagus.bomberfight.Screens.LocalClientGameScreen;

/**
 * Created by Gaetan on 15/02/2018.
 */

public class ClientGameListener extends Listener {
	LocalClientGameScreen client;

	public ClientGameListener(LocalClientGameScreen client) {
		this.client = client;
	}


	@Override
	public void disconnected(Connection connection) {
		// when this player disconnect (the server is disconnected)
		client.onDisconnect();
	}

	@Override
	public void received(Connection connection, Object object) {
		if(object instanceof BufferPlayerPosition){
			BufferPlayerPosition buffer = (BufferPlayerPosition) object;
			client.movePlayer(buffer.playerIndex, buffer.playerPosition, buffer.playerDirection);
		}
		else if(object instanceof BufferWallDestroyed){
			BufferWallDestroyed buffer = (BufferWallDestroyed) object;
			client.onWallDestroyed(buffer.wallSquarePosition);
			Gdx.app.log("client","wall destroyed");
		}
		else if(object instanceof BufferBombPosition){
			BufferBombPosition buffer = (BufferBombPosition) object;
			client.onPlayerPutBomb(buffer.bombSquarePositiion);
			Gdx.app.log("client","bomb received at position "+buffer.bombSquarePositiion.toString());
		}
		else if(object instanceof BufferBombExploded){
			BufferBombExploded buffer = (BufferBombExploded) object;
			client.onBombExploded(buffer.bombSquarePosition,buffer.explosionsSquaresPositions);
			Gdx.app.log("client","bomb exploded at position "+buffer.bombSquarePosition.toString());
		}
		else if(object instanceof BufferBonusAppeared){
			BufferBonusAppeared buffer = (BufferBonusAppeared) object;
			client.onBonusAppeared(buffer.bonus, buffer.bonusSquarePosition);
			Gdx.app.log("client","bonus appear at position "+buffer.bonusSquarePosition.toString());
		}
		else if(object instanceof BufferBonusDisappeared){
			BufferBonusDisappeared buffer = (BufferBonusDisappeared) object;
			client.onBonusDisappeared(buffer.bonusSquarePosition);
			Gdx.app.log("client","bonus disappear at position "+buffer.bonusSquarePosition.toString());
		}
		else if(object instanceof BufferPlayerDie){
			BufferPlayerDie buffer = (BufferPlayerDie) object;
			client.onPlayerDie(buffer.playerIndex);
			Gdx.app.log("client","player "+buffer.playerIndex+" is died");
		}
		else if(object instanceof BufferEndGame){
			BufferEndGame buffer = (BufferEndGame) object;
			client.endGame(buffer.winnerPlayerIndex);
			Gdx.app.log("client","winner : "+buffer.winnerPlayerIndex);
		}
	}
}

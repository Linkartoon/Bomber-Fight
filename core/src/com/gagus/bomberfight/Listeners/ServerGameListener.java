package com.gagus.bomberfight.Listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gagus.bomberfight.Buffers.BufferPlayerMove;
import com.gagus.bomberfight.Buffers.BufferPlayerPutBomb;
import com.gagus.bomberfight.Screens.LocalServerGameScreen;

/**
 * Created by Gaetan on 15/02/2018.
 */

public class ServerGameListener extends Listener {
	LocalServerGameScreen server;

	public ServerGameListener(LocalServerGameScreen server) {
		this.server = server;
	}

	@Override
	public void disconnected(Connection connection) {
		Gdx.app.log("server","a player was disconnected");
		server.onPlayerDisconnected(connection);
		super.disconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		if(object instanceof BufferPlayerMove){
			BufferPlayerMove buffer = (BufferPlayerMove) object;
			server.onPlayerMove(buffer.playerIndex, buffer.direction);
		}
		else if(object instanceof BufferPlayerPutBomb){
			BufferPlayerPutBomb buffer = (BufferPlayerPutBomb) object;
			server.onPlayerPutBomb(buffer.playerIndex);
			Gdx.app.log("server","a player want to put bomb");
		}
	}
}

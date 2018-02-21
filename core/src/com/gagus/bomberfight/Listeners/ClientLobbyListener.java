package com.gagus.bomberfight.Listeners;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gagus.bomberfight.Buffers.BufferClientConnected;
import com.gagus.bomberfight.Buffers.BufferClientDisconnected;
import com.gagus.bomberfight.Buffers.BufferServerDisconnected;
import com.gagus.bomberfight.Buffers.BufferStartGame;
import com.gagus.bomberfight.Screens.LocalClientLobbyScreen;


/**
 * Created by Gaetan on 03/02/2018.
 */

public class ClientLobbyListener extends Listener {

	private LocalClientLobbyScreen client;
	public ClientLobbyListener(LocalClientLobbyScreen client) {
		this.client = client;
	}

	@Override
	public void received(Connection connection, Object object) {
		Gdx.app.log("client","pacqkage received : "+object.getClass());
		if(object instanceof BufferClientConnected)
		{
			Gdx.app.log("client","a client was connected");
			BufferClientConnected buffer = (BufferClientConnected) object;
			client.onClientConnected(buffer.clientsCount);
		}
		else if(object instanceof BufferClientDisconnected)
		{
			Gdx.app.log("client","a client was disconnected");
			BufferClientDisconnected buffer = (BufferClientDisconnected) object;
			client.onClientConnected(buffer.clientsCount);
		}
		else if(object instanceof BufferServerDisconnected)
		{
			Gdx.app.log("client","server disconnected");
			client.onServerDisconnected();
		}
		else if(object instanceof BufferStartGame)
		{
			Gdx.app.log("client","start game");
			BufferStartGame buffer = (BufferStartGame) object;
			Gdx.app.log("client","player count : "+String.valueOf(buffer.playersCount));
			Gdx.app.log("client","player index : "+String.valueOf(buffer.playerIndex));
			client.startGame(buffer.playerIndex,buffer.playersCount);
		}
		super.received(connection, object);
	}

	@Override
	public void connected(Connection connection) {
		// when a player connect
		Gdx.app.log("client","conected");
		super.connected(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		// when this player disconnect
		Gdx.app.log("client","disconected");
		client.disconnect();
		super.disconnected(connection);
	}
}

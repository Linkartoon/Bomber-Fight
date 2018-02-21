package com.gagus.bomberfight.Listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gagus.bomberfight.Buffers.BufferClientConnected;
import com.gagus.bomberfight.Buffers.BufferClientDisconnected;
import com.gagus.bomberfight.Screens.LocalServerLobbyScreen;

import java.util.TimerTask;

/**
 * Created by Gaetan on 03/02/2018.
 */

public class ServerLobbyListener extends Listener {

	LocalServerLobbyScreen server;
	public ServerLobbyListener(LocalServerLobbyScreen server) {
		this.server = server;
	}

	@Override
	public void connected(final Connection connection) {
		if(server.playersCount < 4) {
			Gdx.app.log("server", "a client is conected");
			server.connect();
			super.connected(connection);
			final Timer timer = new Timer();
			timer.schedule(new Timer.Task() {

				@Override
				public void run() {
					BufferClientConnected buffer = new BufferClientConnected();
					buffer.clientsCount = server.playersCount;
					connection.sendTCP(buffer);
					this.cancel();
				}

			}, 0.05f);
		}
		else{
			connection.close();
		}

	}
	@Override
	public void disconnected(Connection connection) {
		Gdx.app.log("server","a client was disconnected");
		Gdx.app.log("server","client disconnected : "+connection.toString());
		server.disconnect();
		super.disconnected(connection);
	}
}

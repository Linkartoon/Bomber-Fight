package com.gagus.bomberfight;

import com.esotericsoftware.kryo.Kryo;
import com.gagus.bomberfight.Buffers.BufferBombExploded;
import com.gagus.bomberfight.Buffers.BufferBombPosition;
import com.gagus.bomberfight.Buffers.BufferBonusAppeared;
import com.gagus.bomberfight.Buffers.BufferBonusDisappeared;
import com.gagus.bomberfight.Buffers.BufferClientConnected;
import com.gagus.bomberfight.Buffers.BufferClientDisconnected;
import com.gagus.bomberfight.Buffers.BufferEndGame;
import com.gagus.bomberfight.Buffers.BufferLaunchGame;
import com.gagus.bomberfight.Buffers.BufferPlayerDie;
import com.gagus.bomberfight.Buffers.BufferPlayerMove;
import com.gagus.bomberfight.Buffers.BufferPlayerPosition;
import com.gagus.bomberfight.Buffers.BufferPlayerPutBomb;
import com.gagus.bomberfight.Buffers.BufferServerDisconnected;
import com.gagus.bomberfight.Buffers.BufferStartGame;
import com.gagus.bomberfight.Buffers.BufferWallDestroyed;

/**
 * Created by Gaetan on 03/02/2018.
 */


public class KryoHelper {

	public static void registerClasses(Kryo kryo)
	{
		kryo.register(BufferClientConnected.class);
		kryo.register(BufferClientDisconnected.class);
		kryo.register(BufferStartGame.class);
		kryo.register(BufferServerDisconnected.class);
		kryo.register(BufferLaunchGame.class);
		kryo.register(BufferPlayerMove.class);
		kryo.register(BufferPlayerPutBomb.class);
		kryo.register(BufferPlayerPosition.class);
		kryo.register(com.badlogic.gdx.math.Vector2.class);
		kryo.register(BufferBombPosition.class);
		kryo.register(BufferPlayerPutBomb.class);
		kryo.register(BufferWallDestroyed.class);
		kryo.register(BufferBombExploded.class);
		kryo.register(com.badlogic.gdx.utils.Array.class);
		kryo.register(Object[].class);
		kryo.register(BufferBonusAppeared.class);
		kryo.register(BufferBonusDisappeared.class);
		kryo.register(BufferPlayerDie.class);
		kryo.register(BufferEndGame.class);
	}
}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.sge.main.Util;

public class BlueprintTile extends BaseTile
{
	int reset;
	int place;
	
	public BlueprintTile(int id, String sprite, int mapColor, int reset, int place)
	{
		super(id, sprite, false, false, 0, mapColor);
		this.reset = reset;
		this.place = place;
	}

	@Override
	public void mouseEvent(int tx, int ty, PlayerMP player, int action, int button, ServerWorld world)
	{
		if (action == 0 && button == 1 && Util.getDistance(tx, ty, player.getTileX(), player.getTileY()) <= 4)
		{
			world.setTileInWorld(tx, ty, 0, reset, true);
		}
		if (action == 0 && button == 2 && Util.getDistance(tx, ty, player.getTileX(), player.getTileY()) <= 4)
		{
			world.setTileInWorld(tx, ty, 0, place, true);
		}
	}

	@Override
	public void enteredTile(int tx, int ty, PlayerMP player, ServerWorld world)
	{
	}

	@Override
	public void leftTile(int tx, int ty, PlayerMP player, ServerWorld world)
	{
	}

	@Override
	public void bulletCollision(int tx, int ty, Bullet bullet, ServerWorld world)
	{
	}

}

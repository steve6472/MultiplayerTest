/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.sge.main.Util;

public class ProgressTile extends BaseTile
{
	int nextId = 0;
	int breakStage = 0;
	int breakChance = 0;
	
	public ProgressTile(int id, String sprite, boolean isSolid, boolean castShadow, int light, int mapColor, int progress, int nextId, int breakStage, int breakChance)
	{
		super(id, progress == -1 ? sprite : sprite.split("\\.")[0] + "_" + progress + ".png", isSolid, castShadow, light, mapColor);
		this.nextId = nextId;
		this.breakStage = breakStage;
		this.breakChance = breakChance;
		createHitParticles();
	}

	@Override
	public void mouseEvent(int tx, int ty, PlayerMP player, int action, int button, GameWorld world)
	{
		if (action == 0 && button == 2 && Util.getDistance(tx, ty, player.getTileX(), player.getTileY()) <= 4)
		{
			world.setTileInWorld(tx, ty, 0, nextId, true);
		}
	}

	@Override
	public void enteredTile(int tx, int ty, PlayerMP player, GameWorld world)
	{
	}

	@Override
	public void leftTile(int tx, int ty, PlayerMP player, GameWorld world)
	{
	}

	@Override
	public void bulletCollision(int tx, int ty, Bullet bullet, GameWorld world)
	{
		if (Util.decide(breakChance))
		{
			world.setTileInWorld(tx, ty, 0, breakStage, true);
			world.createTileBreakParticles(tx, ty, getId());
		} else
		{
			super.bulletCollision(tx, ty, bullet, world);
		}
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 8. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.sge.main.Util;

public class BreakableTile extends BaseTile
{
	int breakChance = 8;
	int breakStage = 0;

	/**
	 * Always must be solid
	 */
	public BreakableTile(int id, String sprite, boolean castShadow, int light, int mapColor, int breakChance, int breakStage)
	{
		super(id, sprite, true, castShadow, light, mapColor);
		this.breakChance = breakChance;
		this.breakStage = breakStage;
	}

	@Override
	public void mouseEvent(int tx, int ty, PlayerMP player, int action, int button, ServerWorld world)
	{
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

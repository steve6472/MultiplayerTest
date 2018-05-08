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

public class BaseTile extends ServerTile
{
	public boolean createHitParticles = false;

	public BaseTile(int id, String sprite, boolean isSolid, boolean castShadow, int light, int mapColor)
	{
		super(id, sprite, isSolid, castShadow, light, mapColor);
	}

	@Override
	public void mouseEvent(int tx, int ty, PlayerMP player, int action, int button, GameWorld world)
	{
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
		if (createHitParticles)
		{
			world.createTileHitParticles(tx, ty, bullet.getLocation().getIntX(), bullet.getLocation().getIntY(), getId());
		}
	}
	
	public BaseTile createHitParticles()
	{
		createHitParticles = true;
		return this;
	}

}

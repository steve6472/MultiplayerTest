/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.AABB;
import com.steve6472.sge.main.game.BaseEntity;

public class Bullet extends BaseEntity
{
	private static final long serialVersionUID = 7597621538512588912L;
	
	private int networkId;
	private int shooterNetworkId;

	@Override
	public void render(Screen screen)
	{
//		Game.drawSquare(loc.getIntX() - Game.camera.getX(), loc.getIntY() - Game.camera.getY(), 3, 3, 0xffff0000);
		Helper.pushLayer();
		Helper.translate(Game.camera.getWidth() / 2f, Game.camera.getHeight() / 2f, 0);
		Helper.translate(-(loc.getIntX() - Game.camera.getX()), -(loc.getIntY() - Game.camera.getY()), 0);
		Helper.translate(Util.getRandomFloat(1, -1), Util.getRandomFloat(1, -1), 0);
		Helper.scale(1.5f);
		Helper.rotate((float) Util.getRandomAngle(), 0f, 0f, 1f);
		Game.drawSpriteFromAtlas2(384f, 384f, Game.pixelModel3, Game.shader, Game.sprites);
		Helper.popLayer();
	}
	
	private int life = 0;
	public double drag = 1;
	
	public void tick(ServerGui sg, Server server)
	{
		for (PlayerMP p : sg.players)
		{
			if (getShooterNetworkId() == p.getNetworkId())
				continue;
			
			if (p.getBox() == null)
				continue;

			if (getBox().intersects(p.getBox()))
			{
				p.score -= 2;
				if (p.score < 0)
					p.score = 0;

				server.sendPacket(new SSetScore(p.score, p.getNetworkId()));

				PlayerMP shooter = server.getPlayer(getShooterNetworkId());

				if (shooter != null)
					server.sendPacket(new SSetScore(shooter.score(), getShooterNetworkId()));
				else
					System.err.println("Can't find player with networkId " + getShooterNetworkId());

				setDead();
			}
		}

		int bulletTileX = getLocation().getIntX() / 32;
		int bulletTileY = getLocation().getIntY() / 32;

		if (server.isTileLocOutOfBounds(bulletTileX, bulletTileY, server.getWorld()))
		{
			setDead();
			return;
		}

		if (ServerTile.getTile(server.getWorld().getTileInWorld(bulletTileX, bulletTileY, 0)).isSolid())
		{
			int id = ServerTile.getTile(server.getWorld().getTileInWorld(bulletTileX, bulletTileY, 0)).getId();
			if (id == 0)
			{
				setDead();
				return;
			}
			ServerTile.getTile(id).bulletCollision(bulletTileX, bulletTileY, this, server.getWorld());
			setDead();
		}
	}
	
	public void tick(Client client, ClientGui cg)
	{
		int bulletTileX = getLocation().getIntX() / 32;
		int bulletTileY = getLocation().getIntY() / 32;

		if (Game.isTileLocOutOfBounds(bulletTileX, bulletTileY, client.getWorld()))
		{
			setDead();
			return;
		}

		if (cg.solidTiles[client.getWorld().getTileInWorldSafe(bulletTileX, bulletTileY, 0)])
		{
			setDead();
		}
	}
	
	@Override
	public void tick()
	{
		move();
		speed *= drag;
		life++;
		if (life >= 90)
			setDead();
	}

	@Override
	public void initEntity(MainApplication game, Object... objects)
	{
		//					Network Id
		checkClass(objects, Integer.class, Integer.class);
		
		networkId = (int) objects[0];
		shooterNetworkId = (int) objects[1];
		
		setBox(new AABB(0, 0, 0, 0));
	}

	@Override
	public Sprite setSprite()
	{
		return null;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}
	
	public int getShooterNetworkId()
	{
		return shooterNetworkId;
	}

}

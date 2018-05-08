/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.net.InetAddress;

import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.game.AABB;
import com.steve6472.sge.main.game.BaseEntity;
import com.steve6472.sge.main.game.Vec2;
import com.steve6472.sge.main.game.inventory.Inventory;
import com.steve6472.sge.main.game.inventory.ItemSlot;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.GameTile;

public class PlayerMP extends BaseEntity
{
	private static final long serialVersionUID = -2737716142975997153L;
	private InetAddress ip;
	private int port;
	private int networkId;
	
	public Vec2 lastValidLocation = new Vec2();
	public int score = 0;
	public boolean checkLocation = true;
	public String name = "unnamed";
	public long lastUpdate = 0;
	public int worldId;

	public int lastChunkX = Integer.MIN_VALUE;
	public int lastChunkY = Integer.MIN_VALUE;
	
	public int lastTileX = Integer.MIN_VALUE;
	public int lastTileY = Integer.MIN_VALUE;
	
	/**
	 * 0 		- not loaded
	 * 1-127 	- load delay
	 * -1 		- loaded
	 */
	public byte[] visitedChunks;
	public SGArray<Integer> tickVCH;
	
	public byte slot = 0;
	
	private static int nextNetworkId;

	/**
	 * Server init
	 * @param ip
	 * @param port
	 * @param x
	 * @param y
	 * @param networkId
	 */
	public PlayerMP(InetAddress ip, int port, int x, int y)
	{
		this.ip = ip;
		this.port = port;
		this.networkId = nextNetworkId++;
		lastUpdate = System.currentTimeMillis();
		this.setLocation(x, y);
		
		tickVCH = new SGArray<Integer>(0, true, true);
		
		setInventory(new Inventory(this, ItemSlot.class, 4));

		lastChunkX = x / 32 / Chunk.chunkWidth;
		lastChunkY = y / 32 / Chunk.chunkHeight;
	}
	
	public void checkLocation()
	{
		checkLocation = true;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPlayerName()
	{
		return name;
	}
	
	/**
	 * Client init
	 * @param x
	 * @param y
	 * @param networkId
	 */
	public PlayerMP(int x, int y, int networkId, String name)
	{
		this.setLocation(x, y);
		this.name = name;
		this.networkId = networkId;
	}

	@Override
	public void render(Screen screen)
	{
	}

	@Override
	public void tick()
	{
	}

	@Override
	public void initEntity(MainApplication game, Object... objects)
	{
	}
	
	public void updateBox()
	{
		setBox(new AABB(loc.getX(), loc.getY(), loc.getX() + 32, loc.getY() + 32));
	}

	@Override
	public Sprite setSprite()
	{
		return null;
	}

	public int getPort()
	{
		return port;
	}
	
	public InetAddress getIp()
	{
		return ip;
	}
	
	public void setIp(InetAddress ip)
	{
		this.ip = ip;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

	public int score()
	{
		score++;
		return score;
	}
	
	public int getChunkX()
	{
		return (loc.getIntX() + 16) / Chunk.chunkWidth / GameTile.tileWidth;
	}
	
	public int getChunkY()
	{
		return (loc.getIntY() + 16) / Chunk.chunkHeight / GameTile.tileHeight;
	}
	
	public int getX()
	{
		return loc.getIntX() + 16;
	}
	
	public int getY()
	{
		return loc.getIntY() + 16;
	}
	
	public int getTileX()
	{
		return getX() / 32;
	}
	
	public int getTileY()
	{
		return getY() / 32;
	}

}

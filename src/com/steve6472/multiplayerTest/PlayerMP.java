/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.net.InetAddress;

import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.world.SSetChunk;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.AABB;
import com.steve6472.sge.main.game.BaseEntity;
import com.steve6472.sge.main.game.Vec2;
import com.steve6472.sge.main.game.inventory.Item;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.GameTile;
import com.steve6472.sge.main.game.world.World;
import com.steve6472.sge.main.networking.packet.IPacketHandler;
import com.steve6472.sge.main.networking.packet.Packet;

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
	
	public int inventory = -1;
	public int inventoryTileX = -1;
	public int inventoryTileY = -1;
	public int inventoryTileL = -1;
	
	/**
	 * 0 		- not loaded
	 * 1-127 	- load delay
	 * -1 		- loaded
	 */
	public byte[] visitedChunks;
	public SGArray<Integer> tickVCH;
	
	public byte slot = 0;
	private Server server;
	
	private static int nextNetworkId;
	
	SGArray<Integer> toBeRemoved = new SGArray<Integer>();
	
	public static final int REVEAL_RANGE = 5;
	public static final int DELETE_RANGE = 11;

	/**
	 * Server init
	 * @param ip
	 * @param port
	 * @param x
	 * @param y
	 * @param networkId
	 */
	public PlayerMP(InetAddress ip, int port, int x, int y, Server server)
	{
		this.ip = ip;
		this.port = port;
		this.networkId = nextNetworkId++;
		this.server = server;
		lastUpdate = System.currentTimeMillis();
		this.setLocation(x, y);
		
		tickVCH = new SGArray<Integer>(0, true, true);
		
		GameInventory gi = new GameInventory(this, 5, 8, Item.AIR);
		gi.getSlot(0).setItemId(27);
		gi.getSlot(1).setItemId(43);
		gi.getSlot(2).setItemId(38);
		
		setInventory(gi);

//		lastChunkX = x / 32 / Chunk.chunkWidth;
//		lastChunkY = y / 32 / Chunk.chunkHeight;
	}
	
	public int[] createClientInventory()
	{
		int[] inv = new int[5 * 8];
		
		GameInventory gi = (GameInventory) getInventory();
		
		for (int i = 0; i < 5 * 8; i++)
		{
			inv[i] = gi.getSlot(i).getItemId();
		}
		
		return inv;
	}
	
	public void closeInventory(World world)
	{
		inventory = -1;
		inventoryTileX = -1;
		inventoryTileY = -1;
		inventoryTileL = -1;
	}
	
	public void swapItems(int i1, int i2)
	{
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
	
	public void sendPacket(Packet<? extends IPacketHandler> packet)
	{
		server.sendPacket(packet, getIp(), getPort());
	}

	@Override
	public void render(Screen screen)
	{
	}

	@Override
	public void tick()
	{
		if (!checkLocation)
			return;

		int px = getX() - 16;
		int py = getY() - 16;
		
		tickRemove();
		tickChunks();
		
		int s = 4;

		int px00 = (px + s) / 32;
		int py00 = (py + s) / 32;

		int px10 = (px + 32 - s) / 32;
		int py10 = (py + s) / 32;

		int px01 = (px + s) / 32;
		int py01 = (py + 32 - s) / 32;

		int px11 = (px + 32 - s) / 32;
		int py11 = (py + 32 - s) / 32;

		boolean isValid = true;

		if (!isTileLocOutOfBounds(px00, py00, server.getWorld()))
			if (ServerTile.getTile(server.getWorld().getTileInWorld(px00, py00, 0)).isSolid())
				isValid = moveToLastValidLocation(this);

		if (!isTileLocOutOfBounds(px01, py01, server.getWorld()))
			if (ServerTile.getTile(server.getWorld().getTileInWorld(px01, py01, 0)).isSolid())
				isValid = moveToLastValidLocation(this);

		if (!isTileLocOutOfBounds(px10, py10, server.getWorld()))
			if (ServerTile.getTile(server.getWorld().getTileInWorld(px10, py10, 0)).isSolid())
				isValid = moveToLastValidLocation(this);

		if (!isTileLocOutOfBounds(px11, py11, server.getWorld()))
			if (ServerTile.getTile(server.getWorld().getTileInWorld(px11, py11, 0)).isSolid())
				isValid = moveToLastValidLocation(this);
		
		if (isValid)
			lastValidLocation = getNewLocation();
	}

	public boolean isTileLocOutOfBounds(int x, int y, GameWorld world)
	{
		return (x < 0 || y < 0 || x >= (World.worldWidth * Chunk.chunkWidth) || y >= (World.worldHeight * Chunk.chunkHeight));
	}

	private boolean moveToLastValidLocation(PlayerMP player)
	{
		sendPacket(new STeleportPlayer(player.lastValidLocation.getIntX(), player.lastValidLocation.getIntY(), player.getNetworkId()));

		return false;
	}
	
	public void tickChunks()
	{
		int cx = getChunkX();
		int cy = getChunkY();
		
		/*
		 * Check if moved
		 */
		if (cx != lastChunkX || cy != lastChunkY)
		{
			lastChunkX = cx;
			lastChunkY = cy;

			/*
			 * Add new chunks if in range
			 */
			for (int i = 0; i < REVEAL_RANGE; i++)
			{
				for (int j = 0; j < REVEAL_RANGE; j++)
				{
					/*
					 * Divided by 2 cut of a bug. Idk why this works but it works so whatever
					 */
					int x = Util.getNumberBetween(0, World.worldWidth, cx + i - REVEAL_RANGE / 2);
					int y = Util.getNumberBetween(0, World.worldHeight, cy + j - REVEAL_RANGE / 2);

					if (x >= 0 && y >= 0 && x < World.worldWidth && y < World.worldHeight)
					{
						byte b = visitedChunks[x + y * World.worldWidth];
						if (b == 0)
						{
							visitedChunks[x + y * World.worldWidth] = Server.lagChunkDelay;
							tickVCH.addObject(x + y * World.worldWidth);
							sendPacket(new SSetChunk(server.sg.world0.getChunk(x, y).getMap().getObject(0), x, y));
						}
					}
				}
			}
			
			int minx = Util.getNumberBetween(0, World.worldWidth, cx - DELETE_RANGE / 2);
			int miny = Util.getNumberBetween(0, World.worldHeight, cy - DELETE_RANGE / 2);
			
			int maxx = Util.getNumberBetween(0, World.worldWidth, cx + DELETE_RANGE / 2);
			int maxy = Util.getNumberBetween(0, World.worldHeight, cy + DELETE_RANGE / 2);

			/*
			 * Deleting chunks out of player's range
			 */
			for (int i = 0; i < World.worldWidth; i++)
			{
				for (int j = 0; j < World.worldHeight; j++)
				{
					if (!Util.isInRectangle(minx, miny, maxx, maxy, i, j))
					{
						if (visitedChunks[i + j * World.worldWidth] == -1)
						{
							sendPacket(new SSetChunk(null, i, j));
							visitedChunks[i + j * World.worldWidth] = 0;
						}
					}
				}
			}
		}
	}

	private void tickRemove()
	{
		for (int i = 0; i < tickVCH.getSize(); i++)
		{
			int index = tickVCH.getObject(i);
			byte b = visitedChunks[index];

			if (b > 0 && b <= Server.lagChunkDelay)
				visitedChunks[index]--;

			if (visitedChunks[index] == 0 || visitedChunks[index] == -1)
				toBeRemoved.addObject(i);
		}

		toBeRemoved.reverseArray();

		for (int i : toBeRemoved)
		{
			int index = tickVCH.getObject(i);
			byte b = visitedChunks[index];
			if (index >= 0 && index < World.worldWidth * World.worldHeight && b == 0)
			{
				visitedChunks[index] = Server.lagChunkDelay;
				tickVCH.addObject(index);
				sendPacket(new SSetChunk(server.getWorld().getChunk(index % World.worldWidth, index / World.worldHeight).getMap().getObject(0),
						index % World.worldWidth, index / World.worldHeight));
			}
			tickVCH.remove(i);
		}

		toBeRemoved.clear();
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

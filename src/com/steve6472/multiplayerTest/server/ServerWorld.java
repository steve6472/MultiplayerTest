/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 29. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.world.SChangeTile;
import com.steve6472.multiplayerTest.server.tiles.BaseTile;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.multiplayerTest.server.tiles.tileData.TileData;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.World;

public class ServerWorld extends World
{
	private final int worldId;
	protected final Server server;
	MainApplication mainApp;
	public SGArray<Integer> tileDataLocations;

	public ServerWorld(int worldId, Server server, MainApplication mainApp)
	{
		createBlankChunks(ServerChunk.class);
		
		this.server = server;
		this.mainApp = mainApp;
		this.worldId = worldId;
		
		tileDataLocations = new SGArray<Integer>();
	}
	
	public void tick()
	{
		for (int t : tileDataLocations)
		{
			int tileId = getTileInWorld(t, 0);
			ServerTile tile = ServerTile.getTile(tileId);
			if (tile instanceof BaseTile)
			{
				BaseTile baseTile = (BaseTile) tile;
				if (baseTile.hasTileData())
				{
					int x = t % (World.worldWidth * Chunk.chunkWidth);
					int y = t / (World.worldHeight * Chunk.chunkHeight);
					
					baseTile.getTileDataController().tick(getTileData(x, y, 0), tileId, x, y, 0, (ServerChunk) getChunkFromTileCoords(x, y), this);
				}
			}
		}
	}
	
	public int getWorldId()
	{
		return worldId;
	}
	
	public void setTileInWorld(int index, int layer, int id, boolean notifyClients)
	{
		super.setTileInWorldSafe(index, layer, id);
		
		int x = index % (Chunk.chunkWidth * World.worldWidth);
		int y = index / (Chunk.chunkHeight * World.worldHeight);
		
		int cx = x / Chunk.chunkWidth;
		int cy = y / Chunk.chunkHeight;
		
		if (notifyClients)
		{
			notifyClients(cx, cy, index, id);
		}
	}
	
	public void setTileInWorld(int x, int y, int layer, int id, boolean notifyClients)
	{
		super.setTileInWorldSafe(x, y, layer, id);
		
		int index = x + y * (World.worldWidth * Chunk.chunkWidth);
		
		int cx = x / Chunk.chunkWidth;
		int cy = y / Chunk.chunkHeight;
		
		if (notifyClients)
		{
			notifyClients(cx, cy, index, id);
		}
	}
	
	public TileData getTileData(int x, int y, int layer)
	{
		int cx = x / Chunk.chunkWidth;
		int cy = y / Chunk.chunkHeight;
		if (cx < 0 || cy < 0 || cx > worldWidth || cy > worldHeight)
			return null;
		ServerChunk c = (ServerChunk) getChunk(cx, cy);
		if (c != null)
		{
			return c.getTileData(x - cx * Chunk.chunkWidth, y - cy * Chunk.chunkHeight, layer);
		} else
		{
			System.out.println("Chunk is null");
			return null;
		}
	}
	
	public void notifyClients(int cx, int cy, int index, int id)
	{
		for (PlayerMP p : server.getPlayers())
		{
			if (p.visitedChunks[cx + cy * World.worldWidth] == -1)
			{
				p.sendPacket(new SChangeTile(index, id, getWorldId()));
			}
		}
	}
	
	public void createTileBreakParticles(int tx, int ty, int id)
	{
		for (PlayerMP pp : server.getPlayersInRange(tx, ty, 32))
			pp.sendPacket(new SSpawnParticle(tx * 32 + 16, ty * 32 + 16, id, 32, 0));
	}
	
	public void createTileHitParticles(int tx, int ty, int px, int py, int id)
	{
		for (PlayerMP pp : server.getPlayersInRange(tx, ty, 32))
			pp.sendPacket(new SSpawnParticle(px, py, id, 4, 0));
	}
}

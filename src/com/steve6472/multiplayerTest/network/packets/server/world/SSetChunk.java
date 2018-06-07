/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 30. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server.world;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.multiplayerTest.network.packets.client.CConfirmChunk;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SSetChunk extends SPacket
{
	
	int[] tiles;
	int chunkX;
	int chunkY;

	public SSetChunk(int[] tiles, int chunkX, int chunkY)
	{
		this.tiles = tiles;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
	}
	
	public SSetChunk()
	{
	}
	
	@Override
	public void output(DataStream output)
	{
		output.writeIntArr(tiles);
		output.writeInt(chunkX);
		output.writeInt(chunkY);
	}

	@Override
	public void input(DataStream input)
	{
		tiles = input.readIntArr();
		chunkX = input.readInt();
		chunkY = input.readInt();
	}

	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
//		System.out.println("Setting chunk " + packet.getChunkX() + "/" + packet.getChunkY());
		if (getTiles() == null)
		{
			clientGui.world.setChunk(getChunkX(), getChunkY(), null);
			
//			client.sendPacket(new CConfirmChunk(packet.getChunkX(), packet.getChunkY()));
		} else
		{
			Chunk c = new Chunk();
			c.setTiles(getTiles(), 0);
			clientGui.world.setChunk(getChunkX(), getChunkY(), c);

			client.sendPacket(new CConfirmChunk(getChunkX(), getChunkY()));
		}
	}
	
	public int[] getTiles()
	{
		return tiles;
	}
	
	public int getChunkX()
	{
		return chunkX;
	}
	
	public int getChunkY()
	{
		return chunkY;
	}

}

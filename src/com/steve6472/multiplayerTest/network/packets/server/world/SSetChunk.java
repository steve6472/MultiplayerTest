/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 30. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server.world;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SSetChunk extends Packet<IClientHandler>
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
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSetChunk(this);
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

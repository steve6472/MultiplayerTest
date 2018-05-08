/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CConfirmChunk extends Packet<IServerHandler>
{
	int chunkX;
	int chunkY;
	
	public CConfirmChunk(int chunkX, int chunkY)
	{
		this.chunkX = chunkX;
		this.chunkY = chunkY;
	}
	
	public CConfirmChunk()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(chunkX);
		output.writeInt(chunkY);
	}

	@Override
	public void input(DataStream input)
	{
		chunkX = input.readInt();
		chunkY = input.readInt();
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleChunkConfirm(this);
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

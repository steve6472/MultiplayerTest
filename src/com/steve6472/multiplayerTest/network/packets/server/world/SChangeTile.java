/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server.world;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SChangeTile extends Packet<IClientHandler>
{
	int index;
	int id;
	int worldId;
	
	public SChangeTile(int index, int id, int worldId)
	{
		this.index = index;
		this.id = id;
		this.worldId = worldId;
	}
	
	public SChangeTile()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(index);
		output.writeInt(id);
		output.writeInt(worldId);
	}

	@Override
	public void input(DataStream input)
	{
		this.index = input.readInt();
		this.id = input.readInt();
		this.worldId = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleChangeTile(this);
	}

	public int getIndex()
	{
		return index;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getWorldId()
	{
		return worldId;
	}

}

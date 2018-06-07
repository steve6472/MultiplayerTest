/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server.world;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SChangeTile extends SPacket
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
	
	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		clientGui.world.setTileInWorld(getIndex(), 0, getId(), false);
	}

}

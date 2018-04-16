/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server.world;

import com.steve6472.multiplayerTest.World;
import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SSetWorld extends Packet<IClientHandler>
{
	
	int worldId;
	
	public SSetWorld(World world)
	{
		this.worldId = world.getWorldId();
	}
	
	public SSetWorld()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(worldId);
	}

	@Override
	public void input(DataStream input)
	{
		this.worldId = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSetWorld(this);
	}
	
	public int getWorldId()
	{
		return worldId;
	}

}

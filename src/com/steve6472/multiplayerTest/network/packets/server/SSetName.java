/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SSetName extends Packet<IClientHandler>
{
	
	String name;
	int networkId;
	
	public SSetName(String name, int networkId)
	{
		this.name = name;
		this.networkId = networkId;
	}
	
	public SSetName()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(networkId);
		output.writeString(name);
	}

	@Override
	public void input(DataStream input)
	{
		this.networkId = input.readInt();
		this.name = input.readString();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSetName(this);
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CSetName extends Packet<IServerHandler>
{
	
	String name;
	
	public CSetName(String name)
	{
		this.name = name;
	}
	
	public CSetName()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeString(name);
	}

	@Override
	public void input(DataStream input)
	{
		this.name = input.readString();
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleSetName(this);
	}
	
	public String getName()
	{
		return name;
	}
}

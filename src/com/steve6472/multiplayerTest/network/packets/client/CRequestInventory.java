/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 6. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CRequestInventory extends Packet<IServerHandler>
{
	byte type;
	
	public CRequestInventory(byte type)
	{
		this.type = type;
	}
	
	public CRequestInventory()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeByte(type);
	}

	@Override
	public void input(DataStream input)
	{
		type = input.readByte();
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleInventoryRequest(this);
	}
	
	public byte getType()
	{
		return type;
	}

}

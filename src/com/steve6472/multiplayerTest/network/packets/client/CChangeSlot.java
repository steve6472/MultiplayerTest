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

public class CChangeSlot extends Packet<IServerHandler>
{
	byte slot;
	
	public CChangeSlot(byte slot)
	{
		this.slot = slot;
	}
	
	public CChangeSlot()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeByte(slot);
	}

	@Override
	public void input(DataStream input)
	{
		slot = input.readByte();
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleSlotChange(this);
	}
	
	public byte getSlot()
	{
		return slot;
	}

}

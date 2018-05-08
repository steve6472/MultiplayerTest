/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 6. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SChangeSlot extends Packet<IClientHandler>
{
	byte slot;
	int networkId;
	
	public SChangeSlot(byte slot, int networkId)
	{
		this.slot = slot;
		this.networkId = networkId;
	}
	
	public SChangeSlot()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeByte(slot);
		output.writeInt(networkId);
	}

	@Override
	public void input(DataStream input)
	{
		slot = input.readByte();
		networkId = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSlotChange(this);
	}
	
	public byte getSlot()
	{
		return slot;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

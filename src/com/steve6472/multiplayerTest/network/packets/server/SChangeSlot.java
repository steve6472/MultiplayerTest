/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 6. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SChangeSlot extends SPacket
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
	public void handlePacket(Client client, ClientGui clientGui)
	{
		if (client.networkId == getNetworkId())
		{
			clientGui.getClientController().setSlot(getSlot());;
		} else
		{
			 PlayerMP player = client.getPlayer(getNetworkId());
			 if (player != null)
				 player.slot = getSlot();
		}
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

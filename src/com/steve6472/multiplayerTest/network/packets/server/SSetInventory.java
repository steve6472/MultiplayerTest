/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SSetInventory extends Packet<IClientHandler>
{
	
	@Override
	public void output(DataStream output)
	{
	}

	@Override
	public void input(DataStream input)
	{
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
	}

}

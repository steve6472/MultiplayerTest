/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 24. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CUpdatePacket extends Packet<IServerHandler>
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
	public void handlePacket(IServerHandler handler)
	{
		handler.handleUpdate(this);
	}

}

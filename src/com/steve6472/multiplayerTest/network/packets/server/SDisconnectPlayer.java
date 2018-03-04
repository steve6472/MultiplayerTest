/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SDisconnectPlayer extends Packet<IClientHandler>
{
	int networkId = 0;

	public SDisconnectPlayer(PlayerMP player)
	{
		this.networkId = player.getNetworkId();
	}

	public SDisconnectPlayer()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(networkId);
	}

	@Override
	public void input(DataStream input)
	{
		networkId = input.readInt();
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleDisconnectPlayerPacket(this);
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SConnectPlayer extends Packet<IClientHandler>
{
	int x;
	int y;
	int networkId;
	
	public SConnectPlayer(PlayerMP player)
	{
		this.x = player.getLocation().getIntX();
		this.y = player.getLocation().getIntY();
		this.networkId = player.getNetworkId();
	}
	
	public SConnectPlayer()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(networkId);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
		this.networkId = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleConnectPlayerPacket(this);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

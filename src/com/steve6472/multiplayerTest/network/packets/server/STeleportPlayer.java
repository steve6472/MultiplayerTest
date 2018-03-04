/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class STeleportPlayer extends Packet<IClientHandler>
{
	
	int x;
	int y;
	int networkId;

	public STeleportPlayer(int x, int y, int networkId)
	{
		this.x = x;
		this.y = y;
		this.networkId = networkId;
	}
	
	public STeleportPlayer()
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
		x = input.readInt();
		y = input.readInt();
		networkId = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleTeleportPlayerPacket(this);
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

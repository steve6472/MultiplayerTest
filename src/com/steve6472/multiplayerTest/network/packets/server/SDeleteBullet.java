/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SDeleteBullet extends Packet<IClientHandler>
{

	int networkId;
	
	public SDeleteBullet(int networkId)
	{
		this.networkId = networkId;
	}

	public SDeleteBullet()
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
		handler.handleDeleteBulletPacket(this);
	}

}

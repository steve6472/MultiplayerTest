/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SSetNetworkId extends Packet<IClientHandler>
{
	
	int networkId;
	
	public SSetNetworkId(int networkId)
	{
		this.networkId = networkId;
	}
	
	public SSetNetworkId()
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
		this.networkId = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSetNetworkId(this);
	}

	public int getNetworkId()
	{
		return networkId;
	}
	
}

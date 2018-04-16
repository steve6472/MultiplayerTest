/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 14. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SRotate extends Packet<IClientHandler>
{

	double deg;
	int networkId;
	
	public SRotate(int networkId, double deg)
	{
		this.networkId = networkId;
		this.deg = deg;
	}
	
	public SRotate()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(networkId);
		output.writeDouble(deg);
	}

	@Override
	public void input(DataStream input)
	{
		this.networkId = input.readInt();
		this.deg = input.readDouble();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleRotation(this);
	}
	
	public double getDegree()
	{
		return deg;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

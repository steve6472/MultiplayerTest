/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 14. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CRotate extends Packet<IServerHandler>
{
	double deg;
	
	public CRotate(double deg)
	{
		this.deg = deg;
	}
	
	public CRotate()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeDouble(deg);
	}

	@Override
	public void input(DataStream input)
	{
		this.deg = input.readDouble();
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleRotation(this);
	}
	
	public double getDegree()
	{
		return deg;
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 14. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SRotate extends SPacket
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
	public void handlePacket(Client client, ClientGui clientGui)
	{
		PlayerMP player = client.getPlayer(getNetworkId());
		player.setAngle(getDegree());
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

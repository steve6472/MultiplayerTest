/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class STeleportPlayer extends SPacket
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
	public void handlePacket(Client client, ClientGui clientGui)
	{
		if (getNetworkId() == client.networkId)
		{
			clientGui.getClientController().setLocation(getX(), getY());
			return;
		}
		PlayerMP player = client.getPlayer(getNetworkId());
		if (player == null)
		{
			System.err.println("Can't find player with networkId " + getNetworkId() + " (Clients id: " + client.networkId + ")");
			return;
		}
		player.setLocation(getX(), getY());
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

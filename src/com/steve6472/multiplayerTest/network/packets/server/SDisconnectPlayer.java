/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SDisconnectPlayer extends SPacket
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
	public void handlePacket(Client client, ClientGui clientGui)
	{
		System.out.println("Player #" + getNetworkId() + " is disconnecting");
		int remove = -1;
		for (PlayerMP pl : clientGui.players)
		{
			remove++;
			if (pl.getNetworkId() == getNetworkId())
				break;
		}
		
		if (remove != -1)
			clientGui.players.remove(remove);
	}
}

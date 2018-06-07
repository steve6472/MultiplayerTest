/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SSetScore extends SPacket
{
	
	int score;
	int networkId;
	
	public SSetScore(int score, int networkId)
	{
		this.score = score;
		this.networkId = networkId;
	}
	
	public SSetScore()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(score);
		output.writeInt(networkId);
	}

	@Override
	public void input(DataStream input)
	{
		this.score = input.readInt();
		this.networkId = input.readInt();
	}
	
	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		if (getNetworkId() == client.networkId)
		{
			clientGui.score = getScore();
			return;
		}
		
		PlayerMP player = client.getPlayer(getNetworkId());
		
		if (player != null)
			player.score = getScore();
		else
			System.err.println("Can't find player with networkId " + getNetworkId());
	}

	public int getScore()
	{
		return score;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

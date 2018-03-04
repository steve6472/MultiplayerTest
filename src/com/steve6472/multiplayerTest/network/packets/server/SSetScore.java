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

public class SSetScore extends Packet<IClientHandler>
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
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSetScore(this);
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

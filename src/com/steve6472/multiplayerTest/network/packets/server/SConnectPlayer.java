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

public class SConnectPlayer extends SPacket
{
	int x;
	int y;
	int networkId;
	String name;
	
	public SConnectPlayer(PlayerMP player)
	{
		this.x = player.getLocation().getIntX();
		this.y = player.getLocation().getIntY();
		this.networkId = player.getNetworkId();
		this.name = player.getPlayerName();
	}
	
	public SConnectPlayer()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(networkId);
		output.writeString(name);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
		this.networkId = input.readInt();
		this.name = input.readString();
	}

	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		clientGui.players.add(new PlayerMP(getX(), getY(), getNetworkId(), getName()));
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
	
	public String getName()
	{
		return name;
	}

}

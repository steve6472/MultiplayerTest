/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 16. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.handlers.ServerHandler;
import com.steve6472.multiplayerTest.network.packets.CPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class CSetRenderDistance extends CPacket
{

	int renderDistance = 0;
	
	public CSetRenderDistance(int renderDistance)
	{
		this.renderDistance = renderDistance;
	}
	
	public CSetRenderDistance()
	{
	}

	@Override
	public void handlePacket(Server server, ServerGui serverGui)
	{
		PlayerMP player = server.getPlayer(this);
		if (player == null)
		{
			ServerHandler.printCantFindPlayerErrorMessage(getSender());
		} else
		{
			player.renderDistance = renderDistance;
		}
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(renderDistance);
	}

	@Override
	public void input(DataStream input)
	{
		this.renderDistance = input.readInt();
	}

}

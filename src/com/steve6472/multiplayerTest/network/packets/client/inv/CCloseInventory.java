/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 6. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client.inv;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.CPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class CCloseInventory extends CPacket
{

	public CCloseInventory()
	{
	}

	@Override
	public void handlePacket(Server server, ServerGui serverGui)
	{
		PlayerMP player = server.getPlayer(getSender());
		player.closeInventory(serverGui.world0);
	}

	@Override
	public void output(DataStream output)
	{
	}

	@Override
	public void input(DataStream input)
	{
	}

}

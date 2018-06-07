/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 27. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets;

import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.Packet;

public abstract class CPacket extends Packet<IServerHandler>
{
	@Override
	public void handlePacket(IServerHandler handler)
	{
	}
	
	public abstract void handlePacket(Server server, ServerGui serverGui);
}

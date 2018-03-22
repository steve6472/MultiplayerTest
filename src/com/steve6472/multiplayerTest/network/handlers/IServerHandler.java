/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CLeftPress;
import com.steve6472.multiplayerTest.network.packets.client.CLeftRelease;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.sge.main.networking.packet.IPacketHandler;

public interface IServerHandler extends IPacketHandler
{
	public void handleMovePacket(CMovePacket packet);
	
	public void handleLeftPressPacket(CLeftPress packet);
	
	public void handleLeftReleasePacket(CLeftRelease packet);

	public void handleRequestTile(CRequestTile packet);
	
	public void handleSetName(CSetName packet);
	
	public void handleChat(CChat packet);
}

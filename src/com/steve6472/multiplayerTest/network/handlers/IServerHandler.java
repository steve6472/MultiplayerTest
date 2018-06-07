/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import com.steve6472.multiplayerTest.network.packets.client.CChangeSlot;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CConfirmChunk;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CPing;
import com.steve6472.multiplayerTest.network.packets.client.CRequestInventory;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.client.CRotate;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.client.CUpdatePacket;
import com.steve6472.sge.main.networking.packet.IPacketHandler;

public interface IServerHandler extends IPacketHandler
{
	public void handleMovePacket(CMovePacket packet);
	
//	public void handleMouseButtonPacket(CMouseButton packet);
	
	public void handleRequestTile(CRequestTile packet);
	
	public void handleSetName(CSetName packet);
	
	public void handleChat(CChat packet);
	
	public void handleUpdate(CUpdatePacket packet);
	
	public void handlePing(CPing packet);
	
	public void handleRotation(CRotate packet);
	
	public void handleChunkConfirm(CConfirmChunk packet);
	
	public void handleSlotChange(CChangeSlot packet);

	public void handleInventoryRequest(CRequestInventory packet);
}

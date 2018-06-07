/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import com.steve6472.multiplayerTest.network.packets.server.*;
import com.steve6472.sge.main.networking.packet.IPacketHandler;

public interface IClientHandler extends IPacketHandler
{
//	public void handleTeleportPlayerPacket(STeleportPlayer packet);
//	
//	public void handleConnectPlayerPacket(SConnectPlayer packet);
//	
//	public void handleDisconnectPlayerPacket(SDisconnectPlayer packet);
//	
//	public void handleDeleteBulletPacket(SDeleteBullet packet);
//	
//	public void handleSpawnBulletPacket(SSpawnBullet packet);
//	
//	public void handleSpawnParticlePacket(SSpawnParticle packet);
//	
//	public void handleChangeTile(SChangeTile packet);
//	
//	public void handleSetNetworkId(SSetNetworkId packet);
//	
//	public void handleSetScore(SSetScore packet);
//	
//	public void handleSetName(SSetName packet);
//	
//	public void handleChat(SChat packet);
//	
//	public void handlePingResponse(SPingResponse packet);
//	
//	public void handleSetChunk(SSetChunk packet);
//	
	public void handleAddEvent(SAddEvent packet);
//	
	public void handleRunEvent(SRunEvent packet);
//	
//	public void handleRotation(SRotate packet);
//	
//	public void handleClientDataInit(SInitClientData packet);
//	
	public void handleAddAnimation(SAddAnimation packet);
//	
	public void handleRunAnimation(SRunAnimation packet);
//	
//	public void handleSlotChange(SChangeSlot packet);
//
//	public void handleOpenInventory(SOpenInventory sOpenInventory);
}

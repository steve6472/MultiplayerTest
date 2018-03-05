/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import com.steve6472.multiplayerTest.network.packets.server.SChangeTile;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSetWorld;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.sge.main.networking.packet.IPacketHandler;

public interface IClientHandler extends IPacketHandler
{
	public void handleTeleportPlayerPacket(STeleportPlayer packet);
	
	public void handleConnectPlayerPacket(SConnectPlayer packet);
	
	public void handleDisconnectPlayerPacket(SDisconnectPlayer packet);
	
	public void handleDeleteBulletPacket(SDeleteBullet packet);
	
	public void handleSpawnBulletPacket(SSpawnBullet packet);
	
	public void handleSpawnParticlePacket(SSpawnParticle packet);
	
	public void handleSetWorld(SSetWorld packet);
	
	public void handleChangeTile(SChangeTile packet);
	
	public void handleSetNetworkId(SSetNetworkId packet);
	
	public void handleSetScore(SSetScore packet);
	
	public void handleSetName(SSetName packet);
}

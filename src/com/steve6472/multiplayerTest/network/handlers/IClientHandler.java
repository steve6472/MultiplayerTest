/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import com.steve6472.multiplayerTest.network.packets.server.SAddEvent;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SPingResponse;
import com.steve6472.multiplayerTest.network.packets.server.SRotate;
import com.steve6472.multiplayerTest.network.packets.server.SRunEvent;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.world.SAddWorld;
import com.steve6472.multiplayerTest.network.packets.server.world.SChangeTile;
import com.steve6472.multiplayerTest.network.packets.server.world.SDeleteWorld;
import com.steve6472.multiplayerTest.network.packets.server.world.SReplaceWorld;
import com.steve6472.multiplayerTest.network.packets.server.world.SSetWorld;
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
	
	public void handleChat(SChat packet);
	
	public void handlePingResponse(SPingResponse packet);
	
	public void handleAddWorld(SAddWorld packet);
	
	public void handleDeleteWorld(SDeleteWorld packet);
	
	public void handleReplaceWorld(SReplaceWorld packet);
	
	public void handleAddEvent(SAddEvent packet);
	
	public void handleRunEvent(SRunEvent packet);
	
	public void handleRotation(SRotate packet);
}

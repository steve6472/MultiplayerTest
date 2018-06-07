/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import com.steve6472.multiplayerTest.Event;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.server.SAddAnimation;
import com.steve6472.multiplayerTest.network.packets.server.SAddEvent;
import com.steve6472.multiplayerTest.network.packets.server.SRunAnimation;
import com.steve6472.multiplayerTest.network.packets.server.SRunEvent;

public class ClientHandler implements IClientHandler
{
	
	private final ClientGui clientGui;
//	private Sprite[] particleTypes;
	
	public ClientHandler(Client client, ClientGui clientGui)
	{
		this.clientGui = clientGui;
	}
	
	/*
	 * World Handlers
	 */
	
	@Override
	public void handleAddEvent(SAddEvent packet)
	{
		clientGui.addEvent(packet.getEvent().getId(), packet.getEvent());
	}
	
	@Override
	public void handleRunEvent(SRunEvent packet)
	{
		Event event = clientGui.events.get(packet.getEventId());
		event.runEvent(clientGui, packet.getData());
	}
	
	@Override
	public void handleAddAnimation(SAddAnimation packet)
	{
		clientGui.addAnimation(packet.getAnimation().getId(), packet.getAnimation());
	}
	
	@Override
	public void handleRunAnimation(SRunAnimation packet)
	{
		clientGui.runningAnimations.addObject(clientGui.animations.get(packet.getAnimationId()).clone());
	}
	
//	@Override
//	public void handleChunkMove(SMoveChunk packet)
//	{
//		if (packet.isMove())
//		{
//			Chunk temp = clientGui.world.getChunk(packet.getChunkX(), packet.getChunkY());
//			clientGui.world.setChunk(packet.getChunkX(), packet.getChunkY(), clientGui.world.getChunk(packet.getMoveX(), packet.getMoveY()));
//			clientGui.world.setChunk(packet.getMoveX(), packet.getMoveY(), temp);
//		} else
//		{
//			clientGui.world.setChunk(packet.getMoveX(), packet.getMoveY(), clientGui.world.getChunk(packet.getChunkX(), packet.getChunkY()));
//		}
//	}

}

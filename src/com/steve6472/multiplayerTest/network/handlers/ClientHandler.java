/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;

import java.util.List;

import com.steve6472.multiplayerTest.ClientGui;
import com.steve6472.multiplayerTest.Event;
import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameParticle;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.client.CConfirmChunk;
import com.steve6472.multiplayerTest.network.packets.server.SAddAnimation;
import com.steve6472.multiplayerTest.network.packets.server.SAddEvent;
import com.steve6472.multiplayerTest.network.packets.server.SChangeSlot;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SPingResponse;
import com.steve6472.multiplayerTest.network.packets.server.SRotate;
import com.steve6472.multiplayerTest.network.packets.server.SRunAnimation;
import com.steve6472.multiplayerTest.network.packets.server.SRunEvent;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.world.SChangeTile;
import com.steve6472.multiplayerTest.network.packets.server.world.SInitClientData;
import com.steve6472.multiplayerTest.network.packets.server.world.SSetChunk;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.Atlas;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.GameTile;
import com.steve6472.sge.main.game.world.World;
import com.steve6472.sge.test.ShaderTest2;

public class ClientHandler implements IClientHandler
{
	
	private final Client client;
	private final ClientGui clientGui;
//	private Sprite[] particleTypes;
	
	public ClientHandler(Client client, ClientGui clientGui)
	{
		this.client = client;
		this.clientGui = clientGui;
	}

	@Override
	public void handleTeleportPlayerPacket(STeleportPlayer packet)
	{
		if (packet.getNetworkId() == client.networkId)
		{
			clientGui.getClientController().setLocation(packet.getX(), packet.getY());
			return;
		}
		PlayerMP player = client.getPlayer(packet.getNetworkId());
		if (player == null)
		{
			System.err.println("Can't find player with networkId " + packet.getNetworkId() + " (Clients id: " + client.networkId + ")");
			return;
		}
		player.setLocation(packet.getX(), packet.getY());
	}

	@Override
	public void handleConnectPlayerPacket(SConnectPlayer packet)
	{
		clientGui.players.add(new PlayerMP(packet.getX(), packet.getY(), packet.getNetworkId(), packet.getName()));
	}

	@Override
	public void handleDisconnectPlayerPacket(SDisconnectPlayer packet)
	{
		System.out.println("Player #" + packet.getNetworkId() + " is disconnecting");
		int remove = -1;
		for (PlayerMP pl : clientGui.players)
		{
			remove++;
			if (pl.getNetworkId() == packet.getNetworkId())
				break;
		}
		
		if (remove != -1)
			clientGui.players.remove(remove);
	}

	@Override
	public void handleDeleteBulletPacket(SDeleteBullet packet)
	{
		int bulletIndex = client.getBulletIndex(packet.getNetworkId());

		if (bulletIndex != -1)
		{
			clientGui.bullets.get(bulletIndex).setDead();
//			System.out.println(bulletIndex);
		}
	}

	@Override
	public void handleSpawnBulletPacket(SSpawnBullet packet)
	{
		clientGui.bullets.add(packet.createBullet());
	}

	@Override
	public void handleSpawnParticlePacket(SSpawnParticle packet)
	{
		//Util.getRandomInt(3, 1, Util.getRandomLong(i, Long.MIN_VALUE, packet.getSeed()))
		
		for (int i = 0; i < packet.getCount(); i++)
		{
			double ang = Util.getRandomDouble(360, 0, Util.getRandomLong(i, Long.MIN_VALUE, packet.getSeed()));
			GameParticle particle = new GameParticle(
					packet.getX(), 
					packet.getY(),
					ang,
					Util.getRandomInt(20, 10, Util.getRandomLong(i, Long.MIN_VALUE, packet.getSeed())),
					packet.getHitId(),
					0,
					1,
					packet.getSeed());
			particle.rotation = (float) ang + Util.getRandomFloat(30, -30);
			particle.bigData.setDouble(0, Util.getRandomDouble(1.48d + 0.5d, 1.48d - 0.5d));
			clientGui.world.particles.add(particle);
		}
	}
	
	@Override
	public void handleSetNetworkId(SSetNetworkId packet)
	{
		System.out.println("Setting client's networkId to " + packet.getNetworkId());
		client.networkId = packet.getNetworkId();
	}
	
	@Override
	public void handleSetScore(SSetScore packet)
	{
		if (packet.getNetworkId() == client.networkId)
		{
			clientGui.score = packet.getScore();
			return;
		}
		
		PlayerMP player = client.getPlayer(packet.getNetworkId());
		
		if (player != null)
			player.score = packet.getScore();
		else
			System.err.println("Can't find player with networkId " + packet.getNetworkId());
	}
	
	@Override
	public void handleSetName(SSetName packet)
	{
		PlayerMP player = client.getPlayer(packet.getNetworkId());
		player.setName(packet.getName());
	}
	
	@Override
	public void handleChat(SChat packet)
	{
//		System.out.println("Server sended message: " + packet.getText() + " From: " + packet.getNetworkId());
		//Server message
		if (packet.getNetworkId() == -1)
		{
			addMessage(packet.getText());
		} else
		{
			if (client.networkId == packet.getNetworkId())
			{
				addMessage("<" + ClientGui.name + "> " + packet.getText());
			} else
			{
				PlayerMP player = client.getPlayer(packet.getNetworkId());
				
				if (player != null)
					addMessage("<" + client.getPlayer(packet.getNetworkId()).getPlayerName() + "> " + packet.getText());
				else
					addMessage("<" + "invalidId" + "> " + packet.getText());
			}
		}
		
		if (clientGui.getClientController().getChatText().size() > 8)
			clientGui.getClientController().clearLastMessage();
	}
	
	/*
	 * World Handlers
	 */
	
	@Override
	public void handleChangeTile(SChangeTile packet)
	{
		clientGui.world.setTileInWorld(packet.getIndex(), 0, packet.getId(), false);
	}
	
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
	public void handleRotation(SRotate packet)
	{
		PlayerMP player = client.getPlayer(packet.getNetworkId());
		player.setAngle(packet.getDegree());
	}
	
	private void addMessage(String text)
	{
		clientGui.getClientController().addChatMessage(text);
		shiftRight(clientGui.getClientController().getChatText());
	}
	
	public void shiftRight(List<String> list) 
	{
		if (list.size() == 0)
			return;
		// make temp variable to hold last element
		String temp = list.get(list.size() - 1);

		// make a loop to run through the array list
		for (int i = list.size() - 1; i > 0; i--)
		{
			// set the last element to the value of the 2nd to last element
			list.set(i, list.get(i - 1));
		}
		// set the first element to be the last element
		list.set(0, temp);
	}
	
	@Override
	public void handlePingResponse(SPingResponse packet)
	{
		clientGui.getClientController().setPing(System.currentTimeMillis() - clientGui.getClientController().pingStart);
	}

	@Override
	public void handleSetChunk(SSetChunk packet)
	{
//		System.out.println("Setting chunk " + packet.getChunkX() + "/" + packet.getChunkY());
		if (packet.getTiles() == null)
		{
			clientGui.world.setChunk(packet.getChunkX(), packet.getChunkY(), null);
			
//			client.sendPacket(new CConfirmChunk(packet.getChunkX(), packet.getChunkY()));
		} else
		{
			Chunk c = new Chunk();
			c.setTiles(packet.getTiles(), 0);
			clientGui.world.setChunk(packet.getChunkX(), packet.getChunkY(), c);

			client.sendPacket(new CConfirmChunk(packet.getChunkX(), packet.getChunkY()));
		}
	}
	
	@Override
	public void handleClientDataInit(SInitClientData packet)
	{
		ClientGui.data = packet;
		ClientGui.update = true;
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
	
	@Override
	public void handleSlotChange(SChangeSlot packet)
	{
		if (client.networkId == packet.getNetworkId())
		{
			clientGui.getClientController().setSlot(packet.getSlot());;
		} else
		{
			 PlayerMP player = client.getPlayer(packet.getNetworkId());
			 if (player != null)
				 player.slot = packet.getSlot();
		}
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

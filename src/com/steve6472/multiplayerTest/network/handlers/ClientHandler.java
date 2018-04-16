/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import java.util.Iterator;
import java.util.List;

import com.steve6472.multiplayerTest.ClientGui;
import com.steve6472.multiplayerTest.Event;
import com.steve6472.multiplayerTest.GameParticle;
import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.World;
import com.steve6472.multiplayerTest.network.Client;
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
import com.steve6472.sge.main.Util;

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
			clientGui.loc.setLocation(packet.getX(), packet.getY());
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
		World world = clientGui.getWorld(packet.getWorldId());
		
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
			world.particles.add(particle);
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
		
		if (clientGui.chatText.size() > 8)
			clientGui.chatText.remove(8);
	}
	
	/*
	 * World Handlers
	 */
	
	@Override
	public void handleChangeTile(SChangeTile packet)
	{
		World world = clientGui.getWorld(packet.getWorldId());
		world.setTile(packet.getId(), packet.getIndex(), false);
	}

	@Override
	public void handleSetWorld(SSetWorld packet)
	{
		clientGui.world = clientGui.getWorld(packet.getWorldId());
	}
	
	@Override
	public void handleAddWorld(SAddWorld packet)
	{
		World world = new World(packet.getTilesX(), packet.getTilesY(), packet.getWorldId(), null, Game.camera, clientGui.getMainApp());
		world.setTiles(packet.getTiles());
		clientGui.worlds.add(world);
	}
	
	@Override
	public void handleDeleteWorld(SDeleteWorld packet)
	{
		for (Iterator<World> iter = clientGui.worlds.iterator(); iter.hasNext();)
		{
			World w = iter.next();
			if (w.getWorldId() == packet.getWorldId())
			{
				iter.remove();
				break;
			}
		}
	}
	
	@Override
	public void handleReplaceWorld(SReplaceWorld packet)
	{
		int i = 0;
		for (int index = 0; index < clientGui.worlds.size(); index++)
		{
			if (clientGui.worlds.get(index).getWorldId() == packet.getReplaceId())
			{
				i = index;
				break;
			}
		}
		World world = new World(packet.getTilesX(), packet.getTilesY(), packet.getWorldId(), null, Game.camera, clientGui.getMainApp());
		world.setTiles(packet.getTiles());
		clientGui.worlds.set(i, world);
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
		clientGui.chatText.add(text);
		shiftRight(clientGui.chatText);
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
		clientGui.ping = System.currentTimeMillis() - clientGui.pingStart;
	}

}

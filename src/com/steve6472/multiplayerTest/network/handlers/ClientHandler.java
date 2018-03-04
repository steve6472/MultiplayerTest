/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import java.util.Arrays;

import com.steve6472.multiplayerTest.ClientGui;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.World;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.server.SChangeTile;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SSetWorld;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.particle.AngledParticle;

public class ClientHandler implements IClientHandler
{
	
	private final Client client;
	private final ClientGui clientGui;
	private Sprite[] particleTypes;
	
	public ClientHandler(Client client, ClientGui clientGui)
	{
		this.client = client;
		this.clientGui = clientGui;

		int[] size2 = new int[2 * 2];
		Arrays.fill(size2, 0xffff0000);

		int[] size3 = new int[3 * 3];
		Arrays.fill(size3, 0xffff0000);

		int[] size4 = new int[4 * 4];
		Arrays.fill(size4, 0xffff0000);
		
		particleTypes = new Sprite[]
		{
				new Sprite(new int[] {0xffff0000}, 1, 1),
				new Sprite(size2, 2, 2),
				new Sprite(size3, 3, 3),
				new Sprite(size4, 4, 4)
		};
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
		clientGui.players.add(new PlayerMP(packet.getX(), packet.getY(), packet.getNetworkId()));
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
		switch (packet.getParticleType())
		{
		default:
			for (int i = 0; i < packet.getCount(); i++)
				clientGui.particles
						.add(new AngledParticle(packet.getX(), packet.getY(),
								Util.getRandomDouble(360, 0, Util.getRandomLong(i, Long.MIN_VALUE, packet.getSeed())),
								Util.getRandomInt(20, 10, Util.getRandomLong(i, Long.MIN_VALUE, packet.getSeed())),
								particleTypes[Util.getRandomInt(3, 1, Util.getRandomLong(i, Long.MIN_VALUE, packet.getSeed()))]));
			break;
		}
	}

	@Override
	public void handleSetWorld(SSetWorld packet)
	{
		World world = new World(packet.getTilesX(), packet.getTilesY(), packet.getWorldId());
		clientGui.world = world;
	}
	
	@Override
	public void handleChangeTile(SChangeTile packet)
	{
		clientGui.world.setTile(packet.getId(), packet.getIndex());
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

}

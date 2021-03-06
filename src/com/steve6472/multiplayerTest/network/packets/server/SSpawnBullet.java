/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SSpawnBullet extends SPacket
{
	
	int x;
	int y;
	double xa;
	double ya;
	int networkId;
	int shooterNetworkId;

	public SSpawnBullet(int x, int y, double angle, int networkId, int shooterNetworkId)
	{
		this.x = x;
		this.y = y;
		this.xa = ((Math.cos(Math.toRadians(angle + 90))) * 1);
		this.ya = ((Math.sin(Math.toRadians(angle + 90))) * 1);
		this.networkId = networkId;
		this.shooterNetworkId = shooterNetworkId;
	}
	
	public SSpawnBullet()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
		output.writeDouble(xa);
		output.writeDouble(ya);
		output.writeInt(networkId);
		output.writeInt(shooterNetworkId);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
		this.xa = input.readDouble();
		this.ya = input.readDouble();
		this.networkId = input.readInt();
		this.shooterNetworkId = input.readInt();
	}
	
	public Bullet createBullet()
	{
		Bullet b = (Bullet) Game.entityList.getEntity(0, networkId, shooterNetworkId);
		b.setSpeed(8);
		b.setLocation(x, y);
		b.setMotion(xa, ya);
		return b;
	}
	
	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		clientGui.bullets.add(createBullet());
	}

}

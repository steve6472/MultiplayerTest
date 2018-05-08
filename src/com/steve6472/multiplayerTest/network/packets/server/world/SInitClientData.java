/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 30. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server.world;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.game.Atlas;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SInitClientData extends Packet<IClientHandler>
{
	public int worldWidth;
	public int worldHeight;
	
	public int chunkWidth;
	public int chunkHeight;
	public int chunkLayers;

	public int[] tileTextures;
	public int atlasSize;
	
	public SInitClientData(int worldWidth, int worldHeight, int chunkWidth, int chunkHeight, int chunkLayers, Atlas atlas)
	{
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;
		this.chunkLayers = chunkLayers;
		
		this.tileTextures = atlas.getAtlas().getPixels();
		this.atlasSize = atlas.getSize();
	}
	
	public SInitClientData()
	{
	}
	
	@Override
	public void output(DataStream output)
	{
		output.writeInt(worldWidth);
		output.writeInt(worldHeight);
		
		output.writeInt(chunkWidth);
		output.writeInt(chunkHeight);
		output.writeInt(chunkLayers);

		output.writeIntArr(tileTextures);
		output.writeInt(atlasSize);
		
	}

	@Override
	public void input(DataStream input)
	{
		this.worldWidth = input.readInt();
		this.worldHeight = input.readInt();

		this.chunkWidth = input.readInt();
		this.chunkHeight = input.readInt();
		this.chunkLayers = input.readInt();
		
		this.tileTextures = input.readIntArr();
		this.atlasSize = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleClientDataInit(this);
	}

}

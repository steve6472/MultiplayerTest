/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 30. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server.world;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.main.game.Atlas;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SInitClientData extends SPacket
{
	public int worldWidth;
	public int worldHeight;
	
	public int chunkWidth;
	public int chunkHeight;
	public int chunkLayers;

	public int[] tileTextures;
	public int atlasSize;
	public boolean[] solid;
	
	public SInitClientData(int worldWidth, int worldHeight, int chunkWidth, int chunkHeight, int chunkLayers, Atlas atlas)
	{
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;
		this.chunkLayers = chunkLayers;
		
		this.tileTextures = atlas.getAtlas().getPixels();
		this.atlasSize = atlas.getSize();
		
		this.solid = new boolean[ServerTile.getTiles().getSize()];
		
		for (int i = 0; i < ServerTile.getTiles().getSize(); i++)
		{
			ServerTile tile = ServerTile.getTile(i);
			solid[i] = tile.isSolid();
		}
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
		
		output.writeBooleanArr(solid);
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
		
		this.solid = input.readBooleanArr();
	}
	
	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		ClientGui.data = this;
		ClientGui.update = true;
	}


}

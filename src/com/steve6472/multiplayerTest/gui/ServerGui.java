/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameChunk;
import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.server.SOpenInventory;
import com.steve6472.multiplayerTest.network.packets.server.SRunEvent;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.multiplayerTest.structures.Structure;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.components.ItemList;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.callbacks.KeyCallback;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.test.Camera;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.World;

public class ServerGui extends Gui
{
	public ServerGui(MainApplication mainApp)
	{
		super(mainApp);
		switchRender();
	}
	
	public boolean render = true;

	private static final long serialVersionUID = 8028366502068225295L;
	Server server;
	public ItemList playerList;
	public List<PlayerMP> players;
	public GameWorld world0;
	
	Sprite worldMap;
	
	@Override
	public void showEvent()
	{
		server = new Server(Integer.parseInt(MenuGui.port.getText()), this);
		server.start();
		
		world0 = generateWorld(0, 256 * 3);
		
		int[] pixels = new int[World.worldWidth * Chunk.chunkWidth * World.worldHeight * Chunk.chunkHeight];

		for (int x = 0; x < World.worldWidth * Chunk.chunkWidth; x++)
		{
			for (int y = 0; y < World.worldHeight * Chunk.chunkHeight; y++)
			{
				int tile = world0.getTileInWorldSafe(x, y, 0);
				int color = ServerTile.getTile(tile).getMapColor();
				pixels[x + y * (World.worldWidth * Chunk.chunkWidth)] = color;
			}
		}
		
		worldMap = new Sprite(pixels, World.worldWidth * Chunk.chunkWidth, World.worldHeight * Chunk.chunkHeight);
		
		
		getMainApp().getKeyHandler().addKeyCallback(new KeyCallback()
		{
			@Override
			public void invoke(int key, int scancode, int action, int mods)
			{
				if (key == GLFW.GLFW_KEY_R && action == GLFW.GLFW_PRESS && mods == GLFW.GLFW_MOD_SHIFT)
				{
					world0 = generateWorld(0, 256 * 3);
					updateTimer = 1;
					for (PlayerMP p : players)
					{
						p.lastChunkX = Integer.MIN_VALUE;
						p.lastChunkY = Integer.MIN_VALUE;
						Arrays.fill(p.visitedChunks, (byte) 0);
					}
				}
				
				if (key == GLFW.GLFW_KEY_K && action == GLFW.GLFW_PRESS && mods == GLFW.GLFW_MOD_SHIFT)
				{
					int x = Util.getRandomInt(World.worldWidth, 0);
					int y = Util.getRandomInt(World.worldHeight, 0);
					int structureId = Util.getRandomInt(Game.structures.length - 1, 0);
					int worldId = 0;
					
					for (PlayerMP p : players)
					{
						server.sendPacket(new SRunEvent(0, new DataStream()
								.writeInt(x)
								.writeInt(y)
								.writeInt(structureId)
								.writeInt(worldId)), 
								p);
					}
					Game.structures[structureId].generateStructure(x, y, world0);
				}
				
				if (key == GLFW.GLFW_KEY_O && action == GLFW.GLFW_PRESS && mods == GLFW.GLFW_MOD_SHIFT)
				{
					for (PlayerMP p : players)
						server.sendPacket(new SOpenInventory(p.createClientInventory(), 5, 8));
				}
			}
		});
	}
	
	public GameWorld generateWorld(int worldId, int structureCount)
	{
//		World world = new World(32 * 8, 18 * 8, worldId, server, MultiplayerTest.camera, getMainApp());
		GameWorld world = new GameWorld(worldId, server, getMainApp());
		world.createBlankChunks(GameChunk.class);
		
		for (int i = 0; i < World.worldWidth * Chunk.chunkWidth; i++)
		{
			for (int j = 0; j < World.worldHeight * Chunk.chunkHeight; j++)
			{
//				world.setTile(Tile.grass.getId(), i, j, false);
				world.setTileInWorld(i, j, 0, ServerTile.grass.getId(), false);
			}
		}

		for (int i = 0; i < structureCount; i++)
		{
			int x = Util.getRandomInt(World.worldWidth * Chunk.chunkWidth, 0);
			int y = Util.getRandomInt(World.worldHeight * Chunk.chunkHeight, 0);

			Structure str = Game.structures[Util.getRandomInt(Game.structures.length - 1, 0)];
			str.generateStructure(Util.getNumberBetween(0, World.worldWidth * Chunk.chunkWidth - str.getStructureWidth(), x),
					Util.getNumberBetween(0, World.worldHeight * Chunk.chunkHeight - str.getStructureHeight(), y), world);
//			str.generateStructure(x, y, world);
		}
		
		world.setTileInWorld(0, 0, ServerTile.rainbow.getId(), false);
		world.setTileInWorld(1, 0, ServerTile.chest.getId(), false);
		
		return world;
	}

	@Override
	public void createGui()
	{
		playerList = new ItemList();
		
		players = new ArrayList<PlayerMP>();
	}
	
	public static int selectedIndex = -1;
	
	double ang = 0;
	float val = 0;
	boolean inSelector = false;
	
	int hoveredTileX = -1;
	int hoveredTileY = -1;
	int selectedTile = 0;

	@Override
	public void guiTick()
	{
		server.tick();

		int mouseX = getMainApp().getMouseX();
		int mouseY = getMainApp().getMouseY();
		
		selectedIndex = (mouseX / 32) + (mouseY / 32) * 32;
		
//		if (inSelector = GuiUtils.isCursorInRectangle(getMainApp().getMouseHandler(), 0, 0, (int) (val * 128f) + 5, (int) (val * 128f) + 5))
//		{
//			ang += 3;
//			if (ang > 90) ang = 90;
//		} else
//		{
//			ang -= 3;
//			if (ang < 0) ang = 0;
//		}
		
		val = (float) Math.sin(Math.toRadians(ang));
		
		hoveredTileX = -1;
		hoveredTileY = -1;
		
		hoveredTileX = (int) ((mouseX / (val * 32)));
		hoveredTileY = (int) ((mouseY / (val * 32)));

		if (!getMainApp().getMouseHandler().isCursorInWindow() || selectedIndex > 18 * 32 || selectedIndex < 0 || inSelector)
		{
			selectedIndex = -1;
		}
		
		if (getMainApp().getMouseHandler().isMouseHolded() && selectedIndex == -1)
		{
			selectedTile = hoveredTileX + hoveredTileY * ServerTile.getAtlas().getSize();
		}

		if (getMainApp().getMouseHandler().isMouseHolded() && getMainApp().getMouseHandler().getButton() == 1 && selectedIndex != -1)
		{
//			world0.setTile(selectedTile, selectedIndex, true);
			world0.setTileInWorld(selectedIndex, 0, selectedTile, true);
		}

		if (getMainApp().getMouseHandler().isMouseHolded() && getMainApp().getMouseHandler().getButton() == 2 && selectedIndex != -1)
		{
//			selectedTile = world0.getTileId(selectedIndex);
			selectedTile = world0.getTileInWorld(selectedIndex, 0);
		}
		
//		MultiplayerTest.camera.setLocation(players.get(0).getLocation().getIntX(), players.get(0).getLocation().getIntY());
	}
	
	public void updateMap()
	{
		for (int x = 0; x < World.worldWidth * Chunk.chunkWidth; x++)
		{
			for (int y = 0; y < World.worldHeight * Chunk.chunkHeight; y++)
			{
				int tile = world0.getTileInWorldSafe(x, y, 0);
				int color = ServerTile.getTile(tile).getMapColor();
				worldMap.setPixel(x, y, color);
			}
		}
		
		glBindTexture(GL_TEXTURE_2D, worldMap.getId());

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, worldMap.getWidth(), worldMap.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, worldMap.getPixels());
		
		glDisable(GL_TEXTURE_2D);
	}
	
	int updateTimer = 0;

	@Override
	public void render(Screen screen)
	{
		if (!render)
			return;
		
		Helper.pushLayer();
		
		Helper.scale(World.worldWidth * Chunk.chunkWidth / 2, World.worldHeight * Chunk.chunkHeight / 2, 0);
		Game.drawSprite(Game.fullModel, Game.shader, worldMap);
		
		Helper.popLayer();
		
		Game.drawFont(mainApp, "UPS:" + mainApp.getFPS(), 5, 5);
		Game.drawFont(mainApp, "UpdateTimer:" + updateTimer, 5, 15);
		
		updateTimer--;
		if (updateTimer <= 0)
		{
			updateTimer = 60;
			updateMap();
		}
		
		
		
		/*
		for (PlayerMP p : players)
		{
			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();
//			String ip = p.getIp().getHostName() + ":" + p.getPort();
			String ip = p.getIp() + ":" + p.getPort();
			String id = "#" + p.getNetworkId();
			String score = "Score:" + p.score;
			String name = p.getPlayerName();
			Helper.pushLayer();
			
			Helper.color(1, 0, 0, 0);
			Helper.translate(getMainApp().getCurrentWidth() / 2 - px - 16, 0, 0);
			Helper.translate(0, getMainApp().getCurrentHeight() / 2 - py - 16, 0);
			Helper.scale(16);
			Helper.rotate((float) p.getAngle(), 0, 0, 1f);
			Game.drawSpriteFromAtlas(4f / 16f, 0f, Game.pixelModel32, Game.shader, Game.sprites);
			
			Helper.popLayer();
			Game.drawFont(getMainApp(), name, px + 16 - name.length() * 4, py - 16);
			Game.drawFont(getMainApp(), ip, px + 16 - ip.length() * 4, py - 8);
			Game.drawFont(getMainApp(), id, px + 16 - id.length() * 4, py);
			Game.drawFont(getMainApp(), score, px + 16 - score.length() * 4, py + 8);
		}

		int atlasSize = Tile.atlas.getSize();
		
		Game.drawFont(mainApp, "UPS:" + mainApp.getFPS(), 5, 5);
		Game.drawFont(mainApp, "Update Time:" + Server.updateTime, 5, 15);

		if (ang != 0)
		{
			Game.drawSquare(0, 0, val * atlasSize * 32, val * atlasSize * 32, 0xff000000);

			Tile.atlas.getAtlas().bind();
			Game.shader.bind();
			Game.shader.setUniform1f("sampler", 0);

			for (int i = 0; i < Tile.totalCount; i++)
			{

				Matrix4f pro = new Matrix4f()
						.translate(32 * 16 - val * 16f - (i % atlasSize) * val * 32, 18 * 16 - val * 16f - (i / atlasSize) * val * 32, 0)
						.scale(val * 16f);

				Tile tile = Tile.getTile(i);

				renderTile(0, 0, tile.getIndexX() + tile.getIndexY() * Tile.atlas.getSize(),
						hoveredTileX == tile.getIndexX() && hoveredTileY == tile.getIndexY(), Game.shader, pro, Game.camera);
			}
		}
		server.bullets.render(screen);*/
	}
	
	public static void renderTile(float x, float y, int tileId, boolean hovered, Shader shader, Matrix4f worldMat, Camera camera)
	{
		float tileIndexX = (tileId % ServerTile.getAtlas().getSize()) / (float) ServerTile.getAtlas().getSize();
		float tileIndexY = (tileId / ServerTile.getAtlas().getSize()) / (float) ServerTile.getAtlas().getSize();
		
		Matrix4f tilePos = new Matrix4f().translate(x * -2, y * -2, 0);
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(worldMat, target);
		target.mul(tilePos);
		
		shader.setUniform2f("texture", tileIndexX, tileIndexY);
		
		shader.setUniformMat4f("projection", target);
		if (hovered)
			shader.setUniform4f("col", 0, 0, 0.5f, 1);
		else
			shader.setUniform4f("col", 0, 0, 0, 0);
		
		Game.tileModel.render();
	}

}

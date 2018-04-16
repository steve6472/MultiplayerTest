/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.server.SRunEvent;
import com.steve6472.multiplayerTest.network.packets.server.world.SReplaceWorld;
import com.steve6472.multiplayerTest.network.packets.server.world.SSetWorld;
import com.steve6472.multiplayerTest.structures.Structure;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.GuiUtils;
import com.steve6472.sge.gui.components.ItemList;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.callbacks.KeyCallback;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.test.Camera;

public class ServerGui extends Gui
{
	public ServerGui(MainApplication mainApp)
	{
		super(mainApp);
		switchRender();
	}

	private static final long serialVersionUID = 8028366502068225295L;
	Server server;
	public ItemList playerList;
	public List<PlayerMP> players;
	public World world0, world1;
	
	@Override
	public void showEvent()
	{
		server = new Server(Integer.parseInt(MenuGui.port.getText()), this);
		server.start();
		
		world0 = generateWorld(0, 16);
//		world1 = generateWorld(1);
		
		getMainApp().getKeyHandler().addKeyCallback(new KeyCallback()
		{
			@Override
			public void invoke(int key, int scancode, int action, int mods)
			{
				if (key == GLFW.GLFW_KEY_R && action == GLFW.GLFW_PRESS && mods == GLFW.GLFW_MOD_SHIFT)
				{
					world0 = generateWorld(0, 128);
					for (PlayerMP p : players)
					{
						server.sendPacket(new SReplaceWorld(world0, 0), p.getIp(), p.getPort());
						if (p.worldId == 0)
							server.sendPacket(new SSetWorld(world0), p.getIp(), p.getPort());
					}
				}
				
				if (key == GLFW.GLFW_KEY_K && action == GLFW.GLFW_PRESS && mods == GLFW.GLFW_MOD_SHIFT)
				{
					int x = Util.getRandomInt(world0.getTilesX(), 0);
					int y = Util.getRandomInt(world0.getTilesY(), 0);
					int structureId = Util.getRandomInt(Game.structures.length - 1, 0);
					int worldId = 0;
					
					for (PlayerMP p : players)
					{
						server.sendPacket(new SRunEvent(0, new DataStream()
								.writeInt(x)
								.writeInt(y)
								.writeInt(structureId)
								.writeInt(worldId)), 
								p.getIp(), p.getPort());
					}
					Game.structures[structureId].generateStructure(x, y, world0);
				}
			}
		});
	}
	
	public World generateWorld(int worldId, int structureCount)
	{
//		World world = new World(32 * 8, 18 * 8, worldId, server, MultiplayerTest.camera, getMainApp());
		World world = new World(32, 18, worldId, server, Game.camera, getMainApp());
		
		for (int i = 0; i < world.getTilesX(); i++)
		{
			for (int j = 0; j < world.getTilesY(); j++)
			{
				world.setTile(Tile.grass.getId(), i, j, false);
			}
		}

		for (int i = 0; i < structureCount; i++)
		{
			int x = Util.getRandomInt(world.getTilesX(), 0);
			int y = Util.getRandomInt(world.getTilesY(), 0);
			
			Structure str = Game.structures[Util.getRandomInt(Game.structures.length - 1, 0)];
			str.generateStructure(x, y, world);
		}
		
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
		
		if (inSelector = GuiUtils.isCursorInRectangle(getMainApp().getMouseHandler(), 0, 0, (int) (val * 128f) + 5, (int) (val * 128f) + 5))
		{
			ang += 3;
			if (ang > 90) ang = 90;
		} else
		{
			ang -= 3;
			if (ang < 0) ang = 0;
		}
		
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
			selectedTile = hoveredTileX + hoveredTileY * Tile.atlas.getSize();
		}

		if (getMainApp().getMouseHandler().isMouseHolded() && getMainApp().getMouseHandler().getButton() == 1 && selectedIndex != -1)
		{
			world0.setTile(selectedTile, selectedIndex, true);
		}

		if (getMainApp().getMouseHandler().isMouseHolded() && getMainApp().getMouseHandler().getButton() == 2 && selectedIndex != -1)
		{
			selectedTile = world0.getTileId(selectedIndex);
		}
		
//		MultiplayerTest.camera.setLocation(players.get(0).getLocation().getIntX(), players.get(0).getLocation().getIntY());
	}
	
	public World getWorld(int worldId)
	{
		if (worldId == 0) return world0;
		if (worldId == 1) return world1;
		
		return world0;
	}

	@Override
	public void render(Screen screen)
	{
		world0.render(screen);
		

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
		server.bullets.render(screen);
	}
	
	public static void renderTile(float x, float y, int tileId, boolean hovered, Shader shader, Matrix4f worldMat, Camera camera)
	{
		float tileIndexX = (tileId % Tile.atlas.getSize()) / (float) Tile.atlas.getSize();
		float tileIndexY = (tileId / Tile.atlas.getSize()) / (float) Tile.atlas.getSize();
		
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

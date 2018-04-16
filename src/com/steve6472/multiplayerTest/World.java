/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.server.world.SChangeTile;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.IObject;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.game.particle.Particle;

public class World implements IObject
{
	private final int width;
	private final int height;
	private final int worldId;
	
	private final int[] tiles;
	
	public int need = -1;
	GameCamera camera;
	MainApplication mainApp;
	protected final Server server;
	public IObjectManipulator<Particle> particles;

	public World(int width, int height, int worldId, Server server, GameCamera camera, MainApplication mainApp)
	{
		this.server = server;
		this.mainApp = mainApp;
		this.width = width;
		this.height = height;
		this.worldId = worldId;
		this.camera = camera;
		this.tiles = new int[width * height];
		particles = new IObjectManipulator<Particle>();
	}
	
	int startX;
	int startY;
	int endX;
	int endY;
	
	double angle = 0;

	@Override
	public void render(Screen screen)
	{
		int out = 0;
		
		startX = Util.getNumberBetween(0, width, (camera.getX() + camera.getWidth() / 2) / 32 - (camera.getWidth() / 2) / 32 + out);
		startY = Util.getNumberBetween(0, height, (camera.getY() + camera.getHeight() / 2) / 32 - (camera.getHeight() / 2) / 32 + out);
		endX = Util.getNumberBetween(0, width, startX + camera.getWidth() / 32 - out * 2 + 1);
		endY = Util.getNumberBetween(0, height, startY + camera.getHeight() / 32 - out * 2 + 1);
		
		renderTiles();
		renderShades();
		
		angle = Util.countAngle(camera.getWidth() / 2, camera.getHeight() / 2, mainApp.getMouseX(), mainApp.getMouseY());
		
		angle = 0;
		//TODO: Create vertex summoner entity
//		GameParticle particle = new GameParticle(MultiplayerTest.camera.getWidth() / 2, MultiplayerTest.camera.getHeight() / 2, angle, 100, 10, 5, 0, Util.getRandomSeed());
//		particle.var2 = Util.getDistance(camera.getWidth() / 2, camera.getHeight() / 2, mainApp.getMouseX(), mainApp.getMouseY()) / 2d;
//		particle.smallData.setDouble(1, 0.02d);
//		particle.bigData.setDouble(0, 100);
//		particle.bigData.setDouble(1, 45);
//
//		particles.add(particle);
		
		particles.render(screen);
	}
	
	private void renderTiles()
	{
			Tile.atlas.getAtlas().bind();
			Game.shader.bind();
			Game.shader.setUniform1f("sampler", 0);
			
			
			for (int i = startX; i < endX; i++)
			{
				for (int j = startY; j < endY; j++)
				{
					renderTile(i, j, tiles[i + j * width], i + j * width == ServerGui.selectedIndex, Game.shader, camera);
				}
			}
	}
	
	private void renderShades()
	{
		Game.tileShade.bind();

		for (int i = startX; i < endX; i++)
		{
			for (int j = startY; j < endY; j++)
			{
				boolean t00 = getTileWithoutError(i - 1, j - 1	).castShadow();
				boolean t10 = getTileWithoutError(i, j - 1		).castShadow();
				boolean t20 = getTileWithoutError(i + 1, j - 1	).castShadow();
				
				boolean t01 = getTileWithoutError(i - 1, j		).castShadow();
				boolean t21 = getTileWithoutError(i + 1, j		).castShadow();
				
				boolean t02 = getTileWithoutError(i - 1, j + 1	).castShadow();
				boolean t12 = getTileWithoutError(i, j + 1		).castShadow();
				boolean t22 = getTileWithoutError(i + 1, j + 1	).castShadow();
				
				boolean r = getTile(i, j).castShadow();
				
				Shader ts = Game.tileShade;
				
				/*
				 * Inside Corners
				 */
				
				//Top Left
				if (!r && t01 && t10) renderTileShade(i - 0.25f, j - 0.25f, ts, camera, Game.shadeModelCornerIn2);
				//Top Right
				if (!r && t10 && t21) renderTileShade(i + 0.25f, j - 0.25f, ts, camera, Game.shadeModelCornerIn5);
				//Bottom Right
				if (!r && t21 && t12) renderTileShade(i + 0.25f, j + 0.25f, ts, camera, Game.shadeModelCornerIn0);
				//Bottom Left
				if (!r && t12 && t01) renderTileShade(i - 0.25f, j + 0.25f, ts, camera, Game.shadeModelCornerIn4);
				
				/*
				 * Full Tile Lines
				 */
				
				//Top
				if (r && !t00 && !t10 && !t20) renderTileShade(i - 0.25f, j - 0.75f, ts, camera, Game.shadeModelLine0);
				if (r && !t00 && !t10 && !t20) renderTileShade(i + 0.25f, j - 0.75f, ts, camera, Game.shadeModelLine0);
				//Bottom
				if (r && !t02 && !t12 && !t22) renderTileShade(i - 0.25f, j + 0.75f, ts, camera, Game.shadeModelLine2);
				if (r && !t02 && !t12 && !t22) renderTileShade(i + 0.25f, j + 0.75f, ts, camera, Game.shadeModelLine2);
				//Left
				if (r && !t00 && !t01 && !t02) renderTileShade(i - 0.75f, j - 0.25f, ts, camera, Game.shadeModelLine3);
				if (r && !t00 && !t01 && !t02) renderTileShade(i - 0.75f, j + 0.25f, ts, camera, Game.shadeModelLine3);
				//right
				if (r && !t20 && !t21 && !t22) renderTileShade(i + 0.75f, j - 0.25f, ts, camera, Game.shadeModelLine1);
				if (r && !t20 && !t21 && !t22) renderTileShade(i + 0.75f, j + 0.25f, ts, camera, Game.shadeModelLine1);
				
				/*
				 * Half Tile Lines
				 */
				
				//Top Left
				if (!r && t00 && t10 && t20 && !t21 && t01) renderTileShade(i + 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine2);
				if (!r && t00 && t10 && t20 && !t21 && t01) renderTileShade(i - 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine1);
				//Bottom Left
				if (!r && t00 && t01 && t01 && !t10 && t12) renderTileShade(i - 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine1);
				if (!r && t00 && t01 && t01 && !t10 && t12) renderTileShade(i + 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine0);
				//Bottom Right
				if (!r && t02 && t12 && t22 && !t01 && t21) renderTileShade(i - 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine0);
				if (!r && t02 && t12 && t22 && !t01 && t21) renderTileShade(i + 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine3);
				//Top Right
				if (!r && t22 && t21 && t20 && !t12 && t10) renderTileShade(i + 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine3);
				if (!r && t22 && t21 && t20 && !t12 && t10) renderTileShade(i - 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine2);
				
				/*
				 * L Shape
				 */
				
				//Bottom Right
				if (!r && t00 && t10 && !t20 && t01 && !t21) renderTileShade(i + 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine2);
				if (!r && t00 && t01 && !t02 && t10 && !t12) renderTileShade(i - 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine1);
				//Top Left
				if (!r && !t10 && !t20 && t21 && t22 && t12) renderTileShade(i + 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine3);
				if (!r && t22 && t12 && !t02 && t21 && !t01) renderTileShade(i - 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine0);
				//Top Right
				if (!r && !t00 && !t10 && t01 && t02 && t12) renderTileShade(i - 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine1);
				if (!r && !t21 && !t22 && t01 && t02 && t12) renderTileShade(i + 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine0);
				//Bottom Left
				if (!r && t10 && t20 && t21 && !t12 && !t22) renderTileShade(i + 0.25f, j + 0.25f, ts, camera, Game.shadeModelLine3);
				if (!r && !t00 && t10 && t20 && !t01 && t21) renderTileShade(i - 0.25f, j - 0.25f, ts, camera, Game.shadeModelLine2);
				
				/*
				 * Outside Corners
				 */
				
				//Top Left
				if (r && !t00 && !t10 && !t01) renderTileShade(i - 0.75f, j - 0.75f, ts, camera, Game.shadeModelCornerOut2);
				//Bottom Left
				if (r && !t02 && !t01 && !t12) renderTileShade(i - 0.75f, j + 0.75f, ts, camera, Game.shadeModelCornerOut4);
				//Bottom Right
				if (r && !t22 && !t12 && !t21) renderTileShade(i + 0.75f, j + 0.75f, ts, camera, Game.shadeModelCornerOut0);
				//Top Right
				if (r && !t20 && !t21 && !t10) renderTileShade(i + 0.75f, j - 0.75f, ts, camera, Game.shadeModelCornerOut5);
			}
		}
	}
	
	public static void renderTile(float x, float y, int tileId, boolean hovered, Shader shader, GameCamera camera)
	{
		float tileIndexX = (tileId % Tile.atlas.getSize()) / (float) Tile.atlas.getSize();
		float tileIndexY = (tileId / Tile.atlas.getSize()) / (float) Tile.atlas.getSize();
		
		Helper.pushLayer();
		
		Helper.scale(16);
		Helper.translate(31 + (float) camera.getX() / 16f, 17 + (float) camera.getY() / 16f, 0);
		
		Helper.translate(x * -2, y * -2, 0);
		
		Game.drawSpriteFromAtlas(tileIndexX, tileIndexY, Game.tileModel, shader, null);
		
		Helper.popLayer();
	}
	
	private boolean renderTileShade(float x, float y, Shader shader, GameCamera camera, Model model)
	{
		Helper.pushLayer();
		
		Helper.scale(16f);
		Helper.translate(31 + (float) camera.getX() / 16f, 17 + (float) camera.getY() / 16f, 0);
		
		Helper.translate(x * -2, y * -2, 0);
		Helper.scale(0.5f);
		
		Helper.color(0.0f, 0.0f, 0.0f, 0.5f);
		
		Game.drawSprite(model, shader, null);
		
		Helper.popLayer();
		
		return true;
	}

	@Override
	public void tick()
	{
		need = -1;
		
		for (int i = 0; i < width * height; i++)
		{
			if (tiles[i] == 0)
			{
				need = i;
				break;
			}
		}
		
		particles.tick(true);
	}
	
	public void setTile(int id, int x, int y, boolean notifyClients)
	{
		tiles[x + y * width] = id;
		
		if (notifyClients && server != null)
			server.sendPacket(new SChangeTile(x + y * width, id, getWorldId()));
	}
	
	public void setTile(int id, int index, boolean notifyClients)
	{
		if (index == -1)
			return;
		
		tiles[index] = id;
		
		if (notifyClients && server != null)
			server.sendPacket(new SChangeTile(index, id, getWorldId()));
	}
	
	public int getTileId(int x, int y)
	{
		return tiles[x + y * width];
	}
	
	public int getTileId(int index)
	{
		return tiles[index];
	}
	
	public Tile getTileWithoutError(int x, int y)
	{
		return getTileWithoutError(x, y, Tile.air);
	}
	
	public Tile getTileWithoutError(int x, int y, Tile errorTile)
	{
		if (isTileLocOutOfBounds(x, y))
			return errorTile;
		
		return Tile.getTile(getTileId(x, y));
	}
	
	public boolean isTileLocOutOfBounds(int x, int y)
	{
		return (x < 0 || y < 0 || x >= getTilesX() || y >= getTilesY());
	}
	
	public Tile getTile(int x, int y)
	{
		return Tile.getTile(getTileId(x, y));
	}
	
	public void setTiles(int tiles[])
	{
		for (int i = 0; i < tiles.length; i++)
		{
			this.tiles[i] = tiles[i];
		}
	}
	
	public int[] getTiles()
	{
		return tiles;
	}
	
	public int getTilesX()
	{
		return width;
	}
	
	public int getTilesY()
	{
		return height;
	}
	
	public int getWorldId()
	{
		return worldId;
	}

}

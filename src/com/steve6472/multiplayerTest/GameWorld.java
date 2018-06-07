/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 29. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import org.joml.Matrix4f;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.world.SChangeTile;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.multiplayerTest.server.tiles.tileData.TileData;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.game.particle.Particle;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.GameCamera;
import com.steve6472.sge.main.game.world.World;
import com.steve6472.sge.test.Camera;

public class GameWorld extends World
{
	private final int worldId;
	protected final Server server;
	MainApplication mainApp;
	public IObjectManipulator<Particle> particles;

	public GameWorld(int worldId, Server server, /*GameCamera camera, */MainApplication mainApp)
	{
		createBlankChunks(GameChunk.class);
		this.server = server;
		this.mainApp = mainApp;
		this.worldId = worldId;
//		this.camera = camera;
		particles = new IObjectManipulator<Particle>();
	}
	
	@Override
	public void render(GameCamera camera)
	{
		super.render(camera);
		
		if (server != null)
			ServerTile.getAtlas().getAtlas().bind();
		else
			ClientGui.getAtlas();
		Game.shader.bind();
		Game.shader.setUniform1f("sampler", 0);
		
		particles.render(null);
		
//		Game.tileShade.bind();
//		Game.tileShade.setUniform1f("sampler", 0);
//		Game.tileShade.setUniform4f("col", 0.0f, 0.0f, 0.0f, 0.5f);
//		iterateVisibleTiles(camera, (x, y, l, id) -> ShadeRenderer.renderShade(this, camera, x, y, l, id));
	}
	
	public void tick()
	{
		particles.tick(true);
	}
	
	public int getWorldId()
	{
		return worldId;
	}
	
	public void setTileInWorld(int index, int layer, int id, boolean notifyClients)
	{
		super.setTileInWorldSafe(index, layer, id);
		
		int x = index % (Chunk.chunkWidth * World.worldWidth);
		int y = index / (Chunk.chunkHeight * World.worldHeight);
		
		int cx = x / Chunk.chunkWidth;
		int cy = y / Chunk.chunkHeight;
		
		if (notifyClients)
		{
			notifyClients(cx, cy, index, id);
		}
	}
	
	public void setTileInWorld(int x, int y, int layer, int id, boolean notifyClients)
	{
		super.setTileInWorldSafe(x, y, layer, id);
		
		int index = x + y * (World.worldWidth * Chunk.chunkWidth);
		
		int cx = x / Chunk.chunkWidth;
		int cy = y / Chunk.chunkHeight;
		
		if (notifyClients)
		{
			notifyClients(cx, cy, index, id);
		}
	}
	
	public TileData getTileData(int x, int y, int layer)
	{
		int cx = x / Chunk.chunkWidth;
		int cy = y / Chunk.chunkHeight;
		if (cx < 0 || cy < 0 || cx > worldWidth || cy > worldHeight)
			return null;
		GameChunk c = (GameChunk) getChunk(cx, cy);
		if (c != null)
		{
			return c.getTileData(x - cx * Chunk.chunkWidth, y - cy * Chunk.chunkHeight, layer);
		} else
		{
			System.out.println("Chunk is null");
			return null;
		}
	}
	
	public void notifyClients(int cx, int cy, int index, int id)
	{
		if (server != null)
		{
			for (PlayerMP p : server.getPlayers())
			{
				if (p.visitedChunks[cx + cy * World.worldWidth] == -1)
				{
					server.sendPacket(new SChangeTile(index, id, getWorldId()), p);
				}
			}
		}
	}
	
	public void createTileBreakParticles(int tx, int ty, int id)
	{
		for (PlayerMP pp : server.getPlayersInRange(tx, ty, 32))
			server.sendPacket(new SSpawnParticle(tx * 32 + 16, ty * 32 + 16, id, 32, 0), pp);
	}
	
	public void createTileHitParticles(int tx, int ty, int bx, int by, int id)
	{
		for (PlayerMP pp : server.getPlayersInRange(tx, ty, 32))
			server.sendPacket(new SSpawnParticle(bx, by, id, 4, 0), pp);
	}
}


class ShadeRenderer
{
	public static void renderShade(GameWorld world, GameCamera camera, int i, int j, int l, int id)
	{
		boolean t00 = ServerTile.getTile(world.getTileInWorldSafe(i - 1, j - 1 ,0)).castShadow();
		boolean t10 = ServerTile.getTile(world.getTileInWorldSafe(i, j - 1	 ,0)).castShadow();
		boolean t20 = ServerTile.getTile(world.getTileInWorldSafe(i + 1, j - 1 ,0)).castShadow();
		
		boolean t01 = ServerTile.getTile(world.getTileInWorldSafe(i - 1, j	  ,0)).castShadow();
		boolean t21 = ServerTile.getTile(world.getTileInWorldSafe(i + 1, j	  ,0)).castShadow();
		
		boolean t02 = ServerTile.getTile(world.getTileInWorldSafe(i - 1, j + 1 ,0)).castShadow();
		boolean t12 = ServerTile.getTile(world.getTileInWorldSafe(i, j + 1	 ,0)).castShadow();
		boolean t22 = ServerTile.getTile(world.getTileInWorldSafe(i + 1, j + 1 ,0)).castShadow();
		
		boolean r = ServerTile.getTile(id).castShadow();
		
//		if (!t00 && !t10 && !t20 && !t01 && !t21 && !t02 && !t12 && !t22 && !r)
//		{
//			break;
//		}
		
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
	
	private static boolean renderTileShade(float x, float y, Shader shader, GameCamera camera, Model model)
	{
		Helper.pushLayer();
		
		Helper.scale(16f);
		Helper.translate(31 + (float) camera.getX() / 16f, 17 + (float) camera.getY() / 16f, 0);
		
		Helper.translate(x * -2, y * -2, 0);
		Helper.scale(0.5f);
		
//		Helper.color(0.0f, 0.0f, 0.0f, 0.5f);
		
		drawSprite(model, shader);
		
		Helper.popLayer();
		
		return true;
	}
	
	private static void drawSprite(Model model, Shader shader)
	{
		Matrix4f target = new Matrix4f();
		
		Camera camera = Game.camera;
		
		camera.getProjection().mul(Helper.toMatrix(), target);
		
		if (shader != null)
		{
//			shader.setUniform4f("col", Helper.getRed(), Helper.getGreen(), Helper.getBlue(), Helper.getAlpha());

			shader.setUniformMat4f("projection", target);
		}
		
		model.render();
	}
}
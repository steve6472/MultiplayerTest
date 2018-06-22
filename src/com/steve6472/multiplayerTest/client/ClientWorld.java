/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.client;

import org.joml.Matrix4f;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.IParticle;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.gfx.Camera;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.game.world.GameCamera;
import com.steve6472.sge.main.game.world.World;

public class ClientWorld extends World
{
	private final int worldId;
	public SGArray<IParticle> particles;
	
	public ClientWorld(int worldId)
	{
		this.worldId = worldId;
		
		particles = new SGArray<IParticle>();
	}
	
	@Override
	public void render(Camera camera)
	{
		super.render(camera);
		
		ClientGui.getAtlas().getAtlas().bind();
		Game.shader.bind();
		Game.shader.setUniform1f("sampler", 0);
		
//		Game.tileShade.bind();
//		Game.tileShade.setUniform1f("sampler", 0);
//		Game.tileShade.setUniform4f("col", 0.0f, 0.0f, 0.0f, 0.5f);
//		iterateVisibleTiles(camera, (x, y, l, id) -> ShadeRenderer.renderShade(this, camera, x, y, l, id));
		
		Game.particleShaderList.get(0).bind();
		
		if (ClientGui.atlas != null)
			ClientGui.atlas.getAtlas().bind();
		
		for (IParticle p : particles)
		{
			p.render();
		}
	}
	
	public void tick()
	{
		SGArray<Integer> dead = new SGArray<Integer>();
		
		for (IParticle p : particles)
		{
			p.tick();
			if (p.isDead())
				dead.add(particles.getIterIndex());
		}
		
		dead.reverseArray();
		
		for (int i : dead)
		{
			particles.remove(i);
		}
	}

	public int getWorldId()
	{
		return worldId;
	}

}

class ShadeRenderer
{
	public static void renderShade(ServerWorld world, GameCamera camera, int i, int j, int l, int id)
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

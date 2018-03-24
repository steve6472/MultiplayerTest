/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import org.joml.Matrix4f;

import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.IObject;
import com.steve6472.sge.test.Camera;

public class World implements IObject
{
	private final int width;
	private final int height;
	private final int worldId;
	
	private final int[] tiles;
	
	public int need = -1;
	Camera camera;
	MainApplication mainApp;

	public World(int width, int height, int worldId, Camera camera, MainApplication mainApp)
	{
		this.mainApp = mainApp;
		this.width = width;
		this.height = height;
		this.worldId = worldId;
		this.camera = camera;
		this.tiles = new int[width * height];
	}

	@Override
	public void render(Screen screen)
	{
		Matrix4f pro = new Matrix4f()
			.scale(16)
			.translate(-31, -17, 0);
		
		Tile.atlas.getAtlas().bind();
		MultiplayerTest.shader.bind();
		MultiplayerTest.shader.setUniform1f("sampler", 0);
		
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				renderTile(i - 31, j - 17, tiles[i + j * width], MultiplayerTest.shader, pro, camera);
			}
		}
		
		MultiplayerTest.tileShade.bind();
		
		for (int i = 1; i < width - 1; i++)
		{
			for (int j = 1; j < height - 1; j++)
			{
				boolean t00 = getTile(i - 1, j - 1	).castShadow();
				boolean t10 = getTile(i, j - 1		).castShadow();
				boolean t20 = getTile(i + 1, j - 1	).castShadow();
				
				boolean t01 = getTile(i - 1, j		).castShadow();
				boolean t21 = getTile(i + 1, j		).castShadow();
				
				boolean t02 = getTile(i - 1, j + 1	).castShadow();
				boolean t12 = getTile(i, j + 1		).castShadow();
				boolean t22 = getTile(i + 1, j + 1	).castShadow();
				
				boolean r = getTile(i, j).castShadow();
				
				Shader ts = MultiplayerTest.tileShade;
				
				/*
				 * Inside Corners
				 */
				
				//Top Left
				if (!r && t01 && t10) renderTileShade(i - 31 - 0.25f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelCornerIn2);
				//Top Right
				if (!r && t10 && t21) renderTileShade(i - 31 + 0.25f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelCornerIn5);
				//Bottom Right
				if (!r && t21 && t12) renderTileShade(i - 31 + 0.25f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelCornerIn0);
				//Bottom Left
				if (!r && t12 && t01) renderTileShade(i - 31 - 0.25f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelCornerIn4);
				
				/*
				 * Full Tile Lines
				 */
				
				//Top
				if (r && !t00 && !t10 && !t20) renderTileShade(i - 31 - 0.25f, j - 17 - 0.75f, ts, pro, camera, MultiplayerTest.shadeModelLine0);
				if (r && !t00 && !t10 && !t20) renderTileShade(i - 31 + 0.25f, j - 17 - 0.75f, ts, pro, camera, MultiplayerTest.shadeModelLine0);
				//Bottom
				if (r && !t02 && !t12 && !t22) renderTileShade(i - 31 - 0.25f, j - 17 + 0.75f, ts, pro, camera, MultiplayerTest.shadeModelLine2);
				if (r && !t02 && !t12 && !t22) renderTileShade(i - 31 + 0.25f, j - 17 + 0.75f, ts, pro, camera, MultiplayerTest.shadeModelLine2);
				//Left
				if (r && !t00 && !t01 && !t02) renderTileShade(i - 31 - 0.75f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine3);
				if (r && !t00 && !t01 && !t02) renderTileShade(i - 31 - 0.75f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine3);
				//right
				if (r && !t20 && !t21 && !t22) renderTileShade(i - 31 + 0.75f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine1);
				if (r && !t20 && !t21 && !t22) renderTileShade(i - 31 + 0.75f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine1);
				
				/*
				 * Half Tile Lines
				 */
				
				//Top Left
				if (!r && t00 && t10 && t20 && !t21 && t01) renderTileShade(i - 31 + 0.25f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine2);
				if (!r && t00 && t10 && t20 && !t21 && t01) renderTileShade(i - 31 - 0.25f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine1);
				//Bottom Left
				if (!r && t00 && t01 && t01 && !t10 && t12) renderTileShade(i - 31 - 0.25f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine1);
				if (!r && t00 && t01 && t01 && !t10 && t12) renderTileShade(i - 31 + 0.25f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine0);
				//Bottom Right
				if (!r && t02 && t12 && t22 && !t01 && t21) renderTileShade(i - 31 - 0.25f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine0);
				if (!r && t02 && t12 && t22 && !t01 && t21) renderTileShade(i - 31 + 0.25f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine3);
				//Top Right
				if (!r && t22 && t21 && t20 && !t12 && t10) renderTileShade(i - 31 + 0.25f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine3);
				if (!r && t22 && t21 && t20 && !t12 && t10) renderTileShade(i - 31 - 0.25f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine2);
				
				/*
				 * L Shape
				 */
				
				//Top
				if (!r && t00 && t10 && !t20 && t01 && !t21) renderTileShade(i - 31 + 0.25f, j - 17 - 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine2);
				//Left
				if (!r && t00 && t01 && !t02 && t10 && !t12) renderTileShade(i - 31 - 0.25f, j - 17 + 0.25f, ts, pro, camera, MultiplayerTest.shadeModelLine1);
				
				/*
				 * Outside Corners
				 */
				
				//Top Left
				if (r && !t00 && !t10 && !t01) renderTileShade(i - 31 - 0.75f, j - 17 - 0.75f, ts, pro, camera, MultiplayerTest.shadeModelCornerOut2);
				//Bottom Left
				if (r && !t02 && !t01 && !t12) renderTileShade(i - 31 - 0.75f, j - 17 + 0.75f, ts, pro, camera, MultiplayerTest.shadeModelCornerOut4);
				//Bottom Right
				if (r && !t22 && !t12 && !t21) renderTileShade(i - 31 + 0.75f, j - 17 + 0.75f, ts, pro, camera, MultiplayerTest.shadeModelCornerOut0);
				//Top Right
				if (r && !t20 && !t21 && !t10) renderTileShade(i - 31 + 0.75f, j - 17 - 0.75f, ts, pro, camera, MultiplayerTest.shadeModelCornerOut5);
			}
		}
//		renderTileShade(15 - 31, 8 - 17, MultiplayerTest.tileShade, pro, camera);
	}
	
	private void renderTile(float x, float y, int tileId, Shader shader, Matrix4f worldMat, Camera camera)
	{
		float tileIndexX = (tileId % Tile.atlas.getSize()) / (float) Tile.atlas.getSize();
		float tileIndexY = (tileId / Tile.atlas.getSize()) / (float) Tile.atlas.getSize();
		
		Matrix4f tilePos = new Matrix4f().translate(x * -2, y * -2, 0);
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(worldMat, target);
		target.mul(tilePos);
		
		shader.setUniform2f("texture", tileIndexX, tileIndexY);
		
		shader.setUniformMat4f("projection", target);
		
		MultiplayerTest.model.render();
	}
	
	private boolean renderTileShade(float x, float y, Shader shader, Matrix4f worldMat, Camera camera, Model model)
	{
		Matrix4f tilePos = new Matrix4f()
				.translate(x * -2, y * -2, 0)
				.scale(0.5f);
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(worldMat, target);
		target.mul(tilePos);
		
		shader.setUniformMat4f("projection", target);
		
		shader.setUniform4f("col", 0.0f, 0.0f, 0.0f, 0.5f);
		
		model.render();
		
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
	}
	
	public void setTile(int id, int x, int y)
	{
		tiles[x + y * width] = id;
	}
	
	public void setTile(int id, int index)
	{
		tiles[index] = id;
	}
	
	public int getTileId(int x, int y)
	{
		return tiles[x + y * width];
	}
	
	public int getTileId(int index)
	{
		return tiles[index];
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

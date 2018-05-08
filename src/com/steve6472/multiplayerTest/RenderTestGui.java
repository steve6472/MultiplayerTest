/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.SmallNineSlice;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.GameTile;
import com.steve6472.sge.main.game.world.World;

public class RenderTestGui extends Gui
{

	private static final long serialVersionUID = -7181572382358516523L;
	
	SmallNineSlice ans;

	public RenderTestGui(MainApplication mainApp)
	{
		super(mainApp);
	}
	
	Button b;
	
	SmallNineSlice hotbar;
	
	@Override
	public void showEvent()
	{
		ans = new SmallNineSlice(6, 0, 6, 6, MainApplication.sprites, Game.shader, Game.camera);
		ans.setCorner(0, 0, 2, 2);
		ans.setTopEdge(2, 0, 2, 2, 2);
		ans.setSideEdge(0, 2, 2, 2, 2);
		ans.createMiddle();
		ans.setScaleMultiplier(2f, 2f);
		ans.setScale(getMainApp().getWidth() / 4f - 3f, getMainApp().getHeight() / 4f - 3f, 1);
		
		hotbar = new SmallNineSlice(97, 97, 30, 30, Game.sprites, Game.shader, Game.camera);
		hotbar.setCorner(0, 0, 7, 7);
		hotbar.setTopEdge(7, 0, 16, 7, 0f);
		hotbar.setSideEdge(0, 7, 7, 16, 0f);
		hotbar.createMiddle();
		
		b = new Button(0, 0, 30, 10, 2, getMainApp(), Game.shader, Game.camera);
		
		GameTile.initGameTiles(ServerTile.getAtlas(), 32, 32, Game.shader, 31, 17);
		int worldSize = 2;
		int chunkSize = 8;
		Chunk.initChunks(chunkSize, chunkSize, 1);
		World.initWorlds(worldSize, worldSize);
		w = new World();
		c = new Chunk();
		w.createBlankChunks();
		
//		for (int i = 0; i < size * size; i++)
//			c.setTileIdSafe(i % size, i / size, 0, i % 11 + 1);
		long started = System.currentTimeMillis();
		for (int i = 0; i < worldSize * worldSize * chunkSize * chunkSize; i++)
		{
//			w.setTileInWorld(i, 0, i % 11 + 1);
			w.setTileInWorld(i, 0, 6);
		}
		System.out.println("Took " + (System.currentTimeMillis() - started));
		/*
		 * 250 
		 * 10/15 
		 * 1|1 
		 * 2#7
		 */
		w.setTileInWorld(250, 0, 9);
		
		/*
		 * 70 
		 * 70/0 
		 * 8|0 
		 * 6#0
		 */
		
//		for (int i = 0; i < 16; i++)
//		{
//			w.setChunk(i % size, i / size, c);
//		}
	}
	
	Chunk c;
	World w;

	@Override
	public void createGui()
	{
	}
	
	double ang;
	double speed = 1;

	@Override
	public void guiTick()
	{
		b.tick();
		b.update(0, 0, 100, 20, 4);
//		speed += 0.02;
//		Game.camera.x += (int) speed;
//		Game.camera.y += (int) speed;
		if (getMainApp().isKeyPressed(GLFW.GLFW_KEY_KP_ADD))
		{
			speed += 0.25;
		}
		if (getMainApp().isKeyPressed(GLFW.GLFW_KEY_KP_SUBTRACT))
		{
			speed -= 0.25;
		}
		if (getMainApp().isKeyPressed(GLFW.GLFW_KEY_W))
		{
			Game.camera.y -= speed;
		}
		if (getMainApp().isKeyPressed(GLFW.GLFW_KEY_S))
		{
			Game.camera.y += speed;
		}
		if (getMainApp().isKeyPressed(GLFW.GLFW_KEY_A))
		{
			Game.camera.x -= speed;
		}
		if (getMainApp().isKeyPressed(GLFW.GLFW_KEY_D))
		{
			Game.camera.x += speed;
		}
	}

	Matrix4f projectionMatrix;
	
	@Override
	public void render(Screen screen)
	{
		hotbar.render(0, 0);
		hotbar.setScale(32 * 4 / 2, 0, 1f / 8f);
//		w.render(Game.camera);
		Game.drawFont(getMainApp(), "Ups: " + getMainApp().getFPS(), 5, 5);
		Game.drawFont(getMainApp(), "Rendereed Tiles: " + w.renderedTiles, 5, 15);
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

//import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.steve6472.multiplayerTest.animations.SwingAnimationV3;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.server.world.SInitClientData;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.SmallNineSlice;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.animations.Animation;
import com.steve6472.sge.gfx.animations.IAnimationTick;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.Atlas;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.GameCamera;
import com.steve6472.sge.main.game.world.GameTile;
import com.steve6472.sge.main.game.world.World;
import com.steve6472.sge.main.networking.packet.ConnectPacket;
import com.steve6472.sge.test.ShaderTest2;
import com.steve6472.sge.gfx.Model;

public class ClientGui extends Gui
{
	private static final long serialVersionUID = -8970752667717685758L;
	
	Client client;
	public List<PlayerMP> players;
	public IObjectManipulator<Bullet> bullets;
	public GameWorld world;
	public int score;
	public Map<Integer, Event> events;
	public Map<Integer, Animation> animations;
	public SGArray<Animation> runningAnimations;
	
	ClientController clientController;
	
	SmallNineSlice hotbar;
	
	public void addEvent(int id, Event event)
	{
		if (events.containsKey(id))
		{
			throw new IllegalArgumentException("Duplicate event id:" + id);
		}
		events.put(id, event);
	}
	
	public void addAnimation(int id, Animation animation)
	{
		if (events.containsKey(id))
		{
			throw new IllegalArgumentException("Duplicate animation id:" + id);
		}
		animations.put(id, animation);
	}
	
	
	public static String name = "";

	public ClientGui(MainApplication mainApp)
	{
		super(mainApp);
		switchRender();
		
		events = new HashMap<Integer, Event>();
		runningAnimations = new SGArray<Animation>(0, true, true);
		
		hotbar = new SmallNineSlice(97, 97, 30, 30, Game.sprites, new Shader("shaders\\basev2"), Game.camera);
		hotbar.setCorner(0, 0, 7, 7);
		hotbar.setTopEdge(7, 0, 16, 7, 0f);
		hotbar.setSideEdge(0, 7, 7, 16, 0f);
		hotbar.createMiddle();
		hotbar.setScale(32 * 4 / 2, 0, 1f / 8f);
		hotbar.setScaleMultiplier(2f, 2f);
	}
	
	public GameWorld generateWorld(int worldId)
	{
		return new GameWorld(worldId, null, getMainApp());
	}

	@Override
	public void showEvent()
	{
		players = new ArrayList<PlayerMP>();
		bullets = new IObjectManipulator<Bullet>();
		
		client = new Client(MenuGui.ip.getText(), Integer.parseInt(MenuGui.port.getText()), this);
		client.start();
		client.sendPacket(new ConnectPacket());
		client.sendPacket(new CSetName(name));
		
		clientController = new ClientController(getMainApp(), client, this);
	}

	@Override
	public void createGui()
	{
	}

	@Override
	public void guiTick()
	{
		if (world != null)
			world.tick();
		
		double newRotation = Util.countAngle(mainApp.getCurrentWidth() / 2, mainApp.getCurrentHeight() / 2, mainApp.getMouseX(), mainApp.getMouseY());
		
		clientController.tick(getMainApp(), newRotation);
		
		SGArray<Integer> removeAnimations = new SGArray<Integer>(0, true, true);
		
		for (int i = 0; i < runningAnimations.getSize(); i++)
		{
			Animation a = runningAnimations.getObject(i);
			if (a.hasEnded())
			{
				if (a instanceof SwingAnimationV3)
					clientController.swing = false;
				removeAnimations.addObject(i);
				continue;
			}
			a.tick();
			if (a instanceof IAnimationTick)
			{
				((IAnimationTick) a).tick((float) newRotation, 0f, 0f);
			}
		}
		
		for (int i : removeAnimations)
		{
			runningAnimations.remove(i);
		}
		
		bullets.tick(true);
	}
	
	public static boolean update = false;
	public static SInitClientData data;
	public static Atlas atlas;
	
	private void update()
	{
		Game.tileSprite.setPixels(data.tileTextures);
		
		Game.tileSprite.setSize(data.atlasSize * 32, data.atlasSize * 32);
		
		int[] newPixels = new int[data.tileTextures.length];
		
		for (int i = 0; i < newPixels.length; i++)
		{
			int oldPixel = data.tileTextures[i];
			int r = Screen.getRed(oldPixel);
			int g = Screen.getGreen(oldPixel);
			int b = Screen.getBlue(oldPixel);
			int a = Screen.getAlpha(oldPixel);
			int newPixel = Screen.getColor(b, g, r, a);
			newPixels[i] = newPixel;
		}
		
		glBindTexture(GL_TEXTURE_2D, Game.tileSprite.getId());

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, data.atlasSize * 32, data.atlasSize * 32, 0, GL_RGBA, GL_UNSIGNED_BYTE, newPixels);
		
		glDisable(GL_TEXTURE_2D);

		Game.tileSprite.save(new File("atlas.png"));
		
		atlas = new Atlas(Game.tileSprite, data.atlasSize);
		Game.tileModel = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(32, 32, atlas.getAtlas()), ShaderTest2.createArray(0));
		GameTile.initGameTiles(atlas, 32, 32, new Shader("shaders\\basev2"), 31, 17);
		Chunk.initChunks(data.chunkWidth, data.chunkHeight, data.chunkLayers);
		World.initWorlds(data.worldWidth, data.worldHeight);
		
		world = generateWorld(0);
	}
	
	public static Atlas getAtlas()
	{
		return atlas;
	}
	
	@Override
	public void render(Screen screen)
	{
		if (update)
		{
			update();
			update = false;
		}
		
		if (world != null)
			world.render(Game.camera);
		
		for (PlayerMP p : players)
		{
			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();
			int x = px - getX() + getMainApp().getCurrentWidth() / 2 - 16;
			int y = py - getY() + getMainApp().getCurrentHeight() / 2 - 16;
			String score = "Score:" + p.score;
			String id = "#" + p.getNetworkId();
			String name = p.getPlayerName();
			
			Helper.pushLayer();
			
			Helper.color(1, 0, 0, 0);
			Helper.translate(getMainApp().getCurrentWidth() / 2 - x - 16, 0, 0);
			Helper.translate(0, getMainApp().getCurrentHeight() / 2 - y - 16, 0);
			Helper.scale(16);
			Helper.rotate((float) p.getAngle(), 0, 0, 1f);
			Game.drawSpriteFromAtlas(4f / 16f, 0f, Game.pixelModel32, Game.shader, Game.sprites);
			
			Helper.popLayer();
			
			Game.drawFont(mainApp, name, x + 16 - name.length() * 4, y + 16);
			Game.drawFont(mainApp, id, x + 16 - id.length() * 4, y);
			Game.drawFont(mainApp, score, x + 16 - score.length() * 4, y + 8);
		}
		
		GameItem renderedItem = null;
		
		if (clientController.getSlot() == 0)
			renderedItem = GameItem.thinSword;
		if (clientController.getSlot() == 1)
			renderedItem = GameItem.wallBlueprint;
		if (clientController.getSlot() == 2)
			renderedItem = GameItem.zappDagger;
		if (clientController.getSlot() == 3 || clientController.getSlot() == 4)
			renderedItem = GameItem.changingSword;
		
		
		/* Render Player */
		if (world != null)
		{
			Helper.pushLayer();
			
			float ang = (float) Util.countAngle(getMainApp().getCurrentWidth() / 2, getMainApp().getCurrentHeight() / 2, getMainApp().getMouseX(), getMainApp().getMouseY());
			
			Helper.scale(16);
			Helper.rotate(ang, 0, 0, 1);
			Helper.color(0, 0, 1, 0);
			Game.drawSpriteFromAtlas(4f / 16f, 0, Game.pixelModel32, Game.shader, Game.sprites);
			
			Helper.popLayer();

			if (!clientController.swing)
			{
				Helper.pushLayer();
				

				float rt = 30f / 32f;
				Helper.translate(-rt, -rt, 0);
				
				if (clientController.getSlot() == 1)
					Helper.rotate(-45, 0, 0, 1);
				
				Helper.rotate(ang + 45, 0, 0, 1);
				Helper.translate(rt, rt, 0);
				
				if (clientController.getSlot() == 1)
					Helper.translate(-10, -16, 0);
				
				Helper.translate(10, 32, 0);
				Helper.scale(16);

				Game.drawSpriteFromAtlas((float) renderedItem.getIndexX() / 16f, (float) renderedItem.getIndexY() / 16f, Game.pixelModel32,
						Game.shader, Game.sprites);

				Helper.popLayer();
			}
		}
		
		for (Animation a : runningAnimations)
		{
			a.render();
		}

		hotbar.render(0, getMainApp().getCurrentHeight() / -2f + 32f);
		
		Helper.pushLayer();
		
		Helper.scale(32);
		Helper.translate((clientController.getSlot() - 2) * -2, 4 * -2, 0);
		Game.drawSpriteFromAtlas(4f / 16f, 3f / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
		
		Helper.pushLayer();
		
		Helper.scale(16);
		Helper.translate(-4 * -2, 8 * -2, 0);
		Game.drawSpriteFromAtlas(11f / 16f, 1f / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		Helper.translate(-4, 0, 0);
		Game.drawSpriteFromAtlas(11f / 16f, 2f / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		Helper.translate(-4, 0, 0);
		Game.drawSpriteFromAtlas(6f / 16f, 2f / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
		
		bullets.render(screen);
		//Render Particles

		Game.drawFont(mainApp, "UPS:" + mainApp.getFPS(), 5, 5);
		Game.drawFont(mainApp, "Score: " + score, 5, 15);
		Game.drawFont(mainApp, "Ping: " + clientController.getPing(), 5, 25);
		if (world != null)
		{
			Game.drawFont(mainApp, "Rendered Tiles: " + world.renderedTiles, 5, 35);
			Game.drawFont(mainApp, "Particles: " + world.particles.getAll().size(), 5, 45);
		}
		Game.drawFont(mainApp, "X/Y: " + getX() + "/" + getY(), 5, 55);
		
		int baseY = getMainApp().getCurrentHeight() - 27;
		
		for (int i = 0; i < clientController.getChatText().size(); i++)
		{
			Game.drawFont(mainApp, clientController.getChatText().get(i), 5, baseY - 10 * i);
		}
		
		if (clientController.isOpenedChat())
		{
			Game.drawSquare(0, mainApp.getCurrentHeight() - 10, 8 * 64 + 4, 10, 0x80000000);
			Game.drawFont(mainApp, clientController.getChatFieldText(), 2, getMainApp().getCurrentHeight() - 9);
		}
		
//		GameItem.renderItemInWorld(0, 0, 64, 0, GameItem.sword);
		
//		GameCamera camera = MultiplayerTest.camera;
//
//		Matrix4f pro = new Matrix4f()
//				.translate(1f / (float) camera.getWidth() + camera.getWidth() / 2, 1f / (float) camera.getHeight() + camera.getHeight() / 2, 0)
//				.scale(4);
//		
//		MultiplayerTest.sprites.bind();
//		MultiplayerTest.shader.bind();
//		MultiplayerTest.shader.setUniform1f("sampler", 0);
//		
//		float rads = (float) Math.toRadians(size += 2);
//		float addSize = (float) Math.sin(rads) - (float) Math.cos(rads);
//		
//		Item.renderItem(0, 0, Item.greenFlower0, pro, 2.5f + addSize / 2f);
		
//		MultiplayerTest.drawSprite(0, 0, new Matrix4f().translate(0, 32, 0).scale(256, 512, 0).translate(1, 0, 0), MultiplayerTest.fullModel, MultiplayerTest.shader, MultiplayerTest.sprites);

//		renderSpriteInWorld(16, 16, MultiplayerTest.sprites);
	
	}
	
	public static void renderSpriteInWorld(float x, float y, Sprite sprite)
	{
		GameCamera camera = Game.camera;
		Helper.pushLayer();
		Helper.translate(256, 32, 0);
		Helper.translate(-x, -y, 0);
		Helper.translate(camera.getX(), camera.getY(), 0);
		Helper.scale(256, 256, 0);
		Game.drawSprite(Game.fullModel, Game.shader, sprite);
		Helper.popLayer();
	}
	
	public GameWorld getWorld()
	{
		return world;
	}
	
	public int getX()
	{
		return clientController.loc.getIntX();
	}
	
	public int getY()
	{
		return clientController.loc.getIntY();
	}
	
	public ClientController getClientController()
	{
		return clientController;
	}
	
	float size = 0;
}

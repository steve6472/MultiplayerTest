/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.steve6472.multiplayerTest.animations.Animation;
import com.steve6472.multiplayerTest.animations.SwingAnimation;
import com.steve6472.multiplayerTest.animations.SwingAnimationV2;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CMouseButton;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CPing;
import com.steve6472.multiplayerTest.network.packets.client.CRotate;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.client.CUpdatePacket;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.callbacks.CharCallback;
import com.steve6472.sge.main.callbacks.MouseButtonCallback;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.game.Vec2;
import com.steve6472.sge.main.game.inventory.Inventory;
import com.steve6472.sge.main.game.inventory.ItemSlot;
import com.steve6472.sge.main.networking.packet.ConnectPacket;

public class ClientGui extends Gui
{
	private static final long serialVersionUID = -8970752667717685758L;
	
	Client client;
	public Vec2 loc, oldLoc;
	public List<String> chatText;
	public List<PlayerMP> players;
	public IObjectManipulator<Bullet> bullets;
	public World world;
	public int score;
	private boolean openedChat = false;
	private String chatFieldText = "";
	private long lastUpdate = 0;
	public List<World> worlds;
	public Map<Integer, Event> events;
	public Inventory inventory;
	public SGArray<Animation> animations;
	
	public void addEvent(int id, Event event)
	{
		if (events.containsKey(id))
		{
			throw new IllegalArgumentException("Duplicate event id:" + id);
		}
		events.put(id, event);
	}
	
	public static String name = "";

	public ClientGui(MainApplication mainApp)
	{
		super(mainApp);
		switchRender();
		
		lastUpdate = System.currentTimeMillis();
		events = new HashMap<Integer, Event>();
		worlds = new ArrayList<World>();
		inventory = new Inventory(null, ItemSlot.class, 5);
		animations = new SGArray<Animation>(0, true, true);

		mainApp.getKeyHandler().addKeyCallback((key, scancode, action, mod) ->
		{
			if (key == GLFW_KEY_ENTER && action == GLFW_PRESS)
			{
				if (!openedChat)
				{
					openedChat = true;
				} else
				{
					if (!chatFieldText.isEmpty())
					{
						client.sendPacket(new CChat(chatFieldText));
					}
					chatFieldText = "";
					openedChat = false;
				}
			}
			
			if (!openedChat && action == GLFW_PRESS && key == GLFW_KEY_F)
			{
				animations.addObject(new SwingAnimation());
			}
			
			if (!openedChat && action == GLFW_PRESS && key == GLFW_KEY_G)
			{
				animations.addObject(new SwingAnimationV2());
			}
			
			if (openedChat && key == GLFW_KEY_BACKSPACE && (action == GLFW_PRESS || action == GLFW_REPEAT) && chatFieldText.length() >= 1)
			{
				chatFieldText = chatFieldText.substring(0, chatFieldText.length() - 1);
			}
		});

		mainApp.getKeyHandler().addCharCallback(new CharCallback()
		{
			@Override
			public void invoke(int codePoint)
			{
				if (openedChat)
					if (chatFieldText.length() < 63)
						chatFieldText += Character.toChars(codePoint)[0];
			}
		});
		
		this.world = generateWorld(0);
	}
	
	public World generateWorld(int worldId)
	{
//		World world = new World(32 * 8, 18 * 8, worldId, server, MultiplayerTest.camera, getMainApp());
		World world = new World(32, 18, worldId, null, Game.camera, getMainApp());
		
		for (int i = 0; i < world.getTilesX(); i++)
		{
			for (int j = 0; j < world.getTilesY(); j++)
			{
				world.setTile(Tile.grass.getId(), i, j, false);
			}
		}

		return world;
	}

	@Override
	public void showEvent()
	{
		players = new ArrayList<PlayerMP>();
		chatText = new ArrayList<String>();
		bullets = new IObjectManipulator<Bullet>();
		
//		loc = new Vec2(mainApp.getCenter().add(new Vec2(-16, -16)));
		loc = new Vec2(200, 200);
		oldLoc = loc.clone();
		
		client = new Client(MenuGui.ip.getText(), Integer.parseInt(MenuGui.port.getText()), this);
		client.start();
		client.sendPacket(new ConnectPacket());
		client.sendPacket(new CSetName(name));
		lastUpdate = System.currentTimeMillis();
		
		getMainApp().getMouseHandler().addMouseButtonCallback(new MouseButtonCallback()
		{
			@Override
			public void invoke(int x, int y, int button, int action, int mods)
			{
				if (action == GLFW_PRESS)
				{
					client.sendPacket(new CMouseButton(x, y, getMainApp().getMouseHandler().getButton(), 0));
				} else if (action == GLFW_RELEASE)
				{
					client.sendPacket(new CMouseButton(x, y, getMainApp().getMouseHandler().getButton(), 1));
				}
			}
		});
	}

	@Override
	public void createGui()
	{
	}
	
	int delay = 0;
	
	public long pingStart;
	public long ping;
	double lastRotation = 0;

	@Override
	public void guiTick()
	{
		if (System.currentTimeMillis() - lastUpdate >= 9 * 1000)
		{
			client.sendPacket(new CUpdatePacket());
			client.sendPacket(new CPing());
			pingStart = System.currentTimeMillis();
			lastUpdate = System.currentTimeMillis();
		}
		
		double newRotation = Util.countAngle(getMainApp().getCurrentWidth() / 2, getMainApp().getCurrentHeight() / 2, getMainApp().getMouseX(), getMainApp().getMouseY());
		
		if (newRotation != lastRotation && delay == 1)
		{
			lastRotation = newRotation;
			client.sendPacket(new CRotate(newRotation));
		}
		
		if (!openedChat)
		{
			double speed = 1;
			
			if (getMainApp().isKeyPressed(GLFW_KEY_LEFT_SHIFT))
				speed = 4;
			if (getMainApp().isKeyPressed(GLFW_KEY_LEFT_CONTROL))
				speed = 16;
			
			if (getMainApp().getKeyHandler().isKeyPressed(GLFW_KEY_W))
				loc.move2(newRotation, speed);

			if (getMainApp().getKeyHandler().isKeyPressed(GLFW_KEY_S))
				loc.move2(newRotation + 180, speed);

			delay++;
			if (delay >= 2)
			{
				if (!loc.equals(oldLoc))
				{
					client.sendPacket(new CMovePacket(loc.getIntX(), loc.getIntY()));
					oldLoc = loc.clone();
				}
				delay = 0;
			}

			if (world != null)
			{
				Game.camera.setLocation(loc.getIntX() - getMainApp().getCurrentWidth() / 2 + 16, loc.getIntY() - getMainApp().getCurrentHeight() / 2 + 16);
			}
		}
		
		SGArray<Integer> removeAnimations = new SGArray<Integer>(0, true, true);
		
		for (int i = 0; i < animations.getSize(); i++)
		{
			Animation a = animations.getObject(i);
			if (a.hasEnded())
			{
				removeAnimations.addObject(i);
				continue;
			}
			a.tick();
		}
		
		for (int i : removeAnimations)
		{
			animations.remove(i);
//			animations.addObject(new SwingAnimationV2());
		}
//		animations.addObject(new SwingAnimationV2());
		
		if (world != null)
			world.tick();
		
		bullets.tick(true);
	}
	
	@Override
	public void render(Screen screen)
	{
//		if (world != null)
//			world.render(screen);
		
		for (PlayerMP p : players)
		{
			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();
			int x = px - loc.getIntX() + getMainApp().getCurrentWidth() / 2 - 16;
			int y = py - loc.getIntY() + getMainApp().getCurrentHeight() / 2 - 16;
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

		/* Render Player */
		if (world != null)
		{
			Helper.pushLayer();
			
			Helper.scale(16);
			Helper.rotate((float) Util.countAngle(getMainApp().getCurrentWidth() / 2, getMainApp().getCurrentHeight() / 2, getMainApp().getMouseX(), getMainApp().getMouseY()), 0, 0, 1);
			Helper.color(0, 0, 1, 0);
			Game.drawSpriteFromAtlas(4f / 16f, 0, Game.pixelModel32, Game.shader, Game.sprites);
			
			Helper.popLayer();
		}
		
		for (Animation a : animations)
		{
			a.render();
		}

		bullets.render(screen);
		//Render Particles

		Game.drawFont(mainApp, "UPS:" + mainApp.getFPS(), 5, 5);
		Game.drawFont(mainApp, "Score: " + score, 5, 15);
		Game.drawFont(mainApp, "Ping: " + ping, 5, 25);
		
		int baseY = getMainApp().getCurrentHeight() - 27;
		
		for (int i = 0; i < chatText.size(); i++)
		{
			Game.drawFont(mainApp, chatText.get(i), 5, baseY - 10 * i);
		}
		
		if (openedChat)
		{
			Game.drawSquare(0, mainApp.getCurrentHeight() - 10, 8 * 64 + 4, 10, 0x80000000);
			Game.drawFont(mainApp, chatFieldText, 2, getMainApp().getCurrentHeight() - 9);
		}
		
		GameItem.renderItemInWorld(0, 0, 0, 45, GameItem.sword);
		
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
	
	float size = 0;
	
	public World getWorld(int worldId)
	{
		for (int index = 0; index < worlds.size(); index++)
		{
			if (worlds.get(index).getWorldId() == worldId)
			{
				return worlds.get(index);
			}
		}
		return null;
	}

}

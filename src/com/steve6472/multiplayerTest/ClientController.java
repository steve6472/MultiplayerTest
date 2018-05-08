/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 8. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import com.steve6472.multiplayerTest.animations.SwingAnimationV3;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.client.CChangeSlot;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CMouseButton;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CPing;
import com.steve6472.multiplayerTest.network.packets.client.CRotate;
import com.steve6472.multiplayerTest.network.packets.client.CUpdatePacket;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.callbacks.CharCallback;
import com.steve6472.sge.main.callbacks.MouseButtonCallback;
import com.steve6472.sge.main.game.Vec2;
import com.steve6472.sge.main.game.inventory.Inventory;
import com.steve6472.sge.main.game.inventory.ItemSlot;

public class ClientController
{
	public Vec2 loc, oldLoc;
	public List<String> chatText;
	boolean swing = false;
	public Inventory inventory;
	private String chatFieldText = "";
	private boolean openedChat = false;
	public byte slot = 0;
	
	Client client;
	ClientGui clientGui;

	public ClientController(MainApplication mainApp, Client client, ClientGui clientGui)
	{
		this.client = client;
		this.clientGui = clientGui;
		
		inventory = new Inventory(null, ItemSlot.class, 5);
		chatText = new ArrayList<String>();
		loc = new Vec2(Game.spawnX, Game.spawnY);
		oldLoc = loc.clone();
		
		lastUpdate = System.currentTimeMillis();
		
		setupHandlers(mainApp);
	}
	
	int delay = 0;
	
	public long pingStart;
	public long ping;
	double lastRotation = 0;
	private long lastUpdate = 0;
	
	public void tick(MainApplication mainApp, double newRotation)
	{
		if (System.currentTimeMillis() - lastUpdate >= 9 * 1000)
		{
			client.sendPacket(new CUpdatePacket());
			client.sendPacket(new CPing());
			pingStart = System.currentTimeMillis();
			lastUpdate = System.currentTimeMillis();
		}
		
		
		if (newRotation != lastRotation && delay == 1)
		{
			lastRotation = newRotation;
			client.sendPacket(new CRotate(newRotation));
		}
		
		if (!openedChat)
		{
			double speed = 1;
			
			if (mainApp.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
				speed = 4;
			if (mainApp.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
				speed = 16;
			
			if (mainApp.getKeyHandler().isKeyPressed(GLFW_KEY_W))
				loc.move2(newRotation, speed);

			if (mainApp.getKeyHandler().isKeyPressed(GLFW_KEY_S))
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

			if (clientGui.getWorld() != null)
			{
				Game.camera.setLocation(loc.getIntX() - mainApp.getCurrentWidth() / 2 + 16, loc.getIntY() - mainApp.getCurrentHeight() / 2 + 16);
			}
		}
	}
	
	public void setupHandlers(MainApplication mainApp)
	{
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
			
			if (!openedChat && action == GLFW_PRESS)
			{
				if (key == GLFW_KEY_1)
				{
					slot = 0;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == GLFW_KEY_2)
				{
					slot = 1;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == GLFW_KEY_3)
				{
					slot = 2;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == GLFW_KEY_4)
				{
					slot = 3;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == GLFW_KEY_5)
				{
					slot = 4;
					client.sendPacket(new CChangeSlot(slot));
				}
			}
			
			if (!openedChat && key == GLFW_KEY_F && !swing && slot == 0)
			{
				clientGui.runningAnimations.addObject(new SwingAnimationV3(GameItem.thinSword));
				swing = true;
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
		
		mainApp.getMouseHandler().addMouseButtonCallback(new MouseButtonCallback()
		{
			@Override
			public void invoke(int x, int y, int button, int action, int mods)
			{
				if (action == GLFW_PRESS)
				{
					client.sendPacket(new CMouseButton(x, y, mainApp.getMouseHandler().getButton(), 0));
				} else if (action == GLFW_RELEASE)
				{
					client.sendPacket(new CMouseButton(x, y, mainApp.getMouseHandler().getButton(), 1));
				}
			}
		});
	}
	
	public byte getSlot()
	{
		return slot;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public Vec2 getLoc()
	{
		return loc;
	}
	
	public Vec2 getOldLoc()
	{
		return oldLoc;
	}
	
	public long getPing()
	{
		return ping;
	}
	
	public List<String> getChatText()
	{
		return chatText;
	}
	
	public boolean isOpenedChat()
	{
		return openedChat;
	}
	
	public String getChatFieldText()
	{
		return chatFieldText;
	}
	
	public void setLocation(int x, int y)
	{
		this.loc.setLocation(x, y);
	}
	
	public void setPing(long ping)
	{
		this.ping = ping;
	}
	
	public void setSlot(byte slot)
	{
		this.slot = slot;
	}
	
	public void addChatMessage(String string)
	{
		chatText.add(string);
	}
	
	public void clearLastMessage()
	{
		chatText.remove(8);
	}

}

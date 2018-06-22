/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 8. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.gui;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameItem;
import com.steve6472.multiplayerTest.animations.SwingAnimationV3;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.client.CChangeSlot;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CMouseButton;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CPing;
import com.steve6472.multiplayerTest.network.packets.client.CRotate;
import com.steve6472.multiplayerTest.network.packets.client.CUpdatePacket;
import com.steve6472.multiplayerTest.server.GameInventory;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.callbacks.CharCallback;
import com.steve6472.sge.main.callbacks.MouseButtonCallback;
import com.steve6472.sge.main.game.Vec2;
import com.steve6472.sge.main.game.inventory.Item;

public class ClientController implements KeyList
{
	public Vec2 loc, oldLoc, newLoc;
	public List<String> chatText;
	boolean swing = false;
	public GameInventory inventory;
	private String chatFieldText = "";
	private boolean openedChat = false;
	public byte slot = 0;
	
	Client client;
	ClientGui clientGui;

	public ClientController(MainApplication mainApp, Client client, ClientGui clientGui)
	{
		this.client = client;
		this.clientGui = clientGui;
		
		inventory = new GameInventory(null, 10, 5, Item.AIR);
		chatText = new ArrayList<String>();
		loc = new Vec2(Game.spawnX, Game.spawnY);
		oldLoc = loc.clone();
		newLoc = new Vec2();
		
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
			
			newLoc.setLocation(loc);

			if (mainApp.isKeyPressed(GLFW_KEY_LEFT_ALT))
				speed = 0.1d;
			if (mainApp.isKeyPressed(GLFW_KEY_LEFT_SHIFT))
				speed = 4;
			if (mainApp.isKeyPressed(GLFW_KEY_LEFT_CONTROL))
				speed = 16;
			
			if (mainApp.getKeyHandler().isKeyPressed(W))
				newLoc.move2(newRotation, speed);

			if (mainApp.getKeyHandler().isKeyPressed(S))
				newLoc.move2(newRotation + 180, speed);
			
			if (!inTile(newLoc))
			{
				loc.setLocation(newLoc);
			}

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
	
	public boolean inTile(Vec2 loc)
	{
		int px = loc.getIntX();
		int py = loc.getIntY();
		
		int s = 4;
		
		int px00 = (px + s) / 32;
		int py00 = (py + s) / 32;

		int px10 = (px + 32 - s) / 32;
		int py10 = (py + s) / 32;

		int px01 = (px + s) / 32;
		int py01 = (py + 32 - s) / 32;

		int px11 = (px + 32 - s) / 32;
		int py11 = (py + 32 - s) / 32;
		
		if (client.getWorld() == null)
			return false;
		
		if (!Game.isTileLocOutOfBounds(px00, py00))
			if (clientGui.solidTiles[client.getWorld().getTileInWorldSafe(px00, py00, 0)])
				return true;

		if (!Game.isTileLocOutOfBounds(px01, py01))
			if (clientGui.solidTiles[client.getWorld().getTileInWorldSafe(px01, py01, 0)])
				return true;

		if (!Game.isTileLocOutOfBounds(px10, py10))
			if (clientGui.solidTiles[client.getWorld().getTileInWorldSafe(px10, py10, 0)])
				return true;

		if (!Game.isTileLocOutOfBounds(px11, py11))
			if (clientGui.solidTiles[client.getWorld().getTileInWorldSafe(px11, py11, 0)])
				return true;
		
		return false;
	}

	public void setupHandlers(MainApplication mainApp)
	{
		mainApp.getKeyHandler().addKeyCallback((key, scancode, action, mod) ->
		{
			if (key == ENTER && action == GLFW_PRESS)
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
				if (key == _1)
				{
					slot = 0;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == _2)
				{
					slot = 1;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == _3)
				{
					slot = 2;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == _4)
				{
					slot = 3;
					client.sendPacket(new CChangeSlot(slot));
				}

				if (key == _5)
				{
					slot = 4;
					client.sendPacket(new CChangeSlot(slot));
				}
			}
			
			if (!openedChat && key == F && !swing && slot == 0)
			{
				clientGui.runningAnimations.addObject(new SwingAnimationV3(GameItem.thinSword));
				swing = true;
			}
			
			if (openedChat && key == BACKSPACE && (action == GLFW_PRESS || action == GLFW_REPEAT) && chatFieldText.length() >= 1)
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
				int tx = (x - mainApp.getCurrentWidth() / 2 + getLoc().getIntX() + 16) / 32;
				int ty = (y - mainApp.getCurrentHeight() / 2 + getLoc().getIntY() + 16) / 32;
				
				if (action == GLFW_PRESS)
				{
					client.sendPacket(new CMouseButton(x, y, tx, ty, Game.camera.getWidth(), Game.camera.getHeight(), mainApp.getMouseHandler().getButton(), 0));
				} else if (action == GLFW_RELEASE)
				{
					client.sendPacket(new CMouseButton(x, y, tx, ty, Game.camera.getWidth(), Game.camera.getHeight(), mainApp.getMouseHandler().getButton(), 1));
				}
			}
		});
	}
	
	public byte getSlot()
	{
		return slot;
	}
	
	public boolean isSwing()
	{
		return swing;
	}
	
	public void setSwing(boolean swing)
	{
		this.swing = swing;
	}
	
	public GameInventory getInventory()
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

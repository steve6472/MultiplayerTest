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
import java.util.List;


import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CLeftPress;
import com.steve6472.multiplayerTest.network.packets.client.CLeftRelease;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.GuiUtils;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.callbacks.CharCallback;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.game.Vec2;
import com.steve6472.sge.main.game.particle.Particle;
import com.steve6472.sge.main.networking.packet.ConnectPacket;

public class ClientGui extends Gui
{

	private static final long serialVersionUID = -8970752667717685758L;
	Client client;
	public Vec2 loc, oldLoc;
	public List<String> chatText;
	public List<PlayerMP> players;
	public IObjectManipulator<Bullet> bullets;
	public IObjectManipulator<Particle> particles;
	public World world;
	public int score;
	private boolean openedChat = false;
	private String chatFieldText = "";
	
	public static String name = "";

	public ClientGui(MainApplication mainApp)
	{
		super(mainApp);
		switchRender();

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
	}

	@Override
	public void showEvent()
	{
		players = new ArrayList<PlayerMP>();
		chatText = new ArrayList<String>();
		particles = new IObjectManipulator<Particle>();
		bullets = new IObjectManipulator<Bullet>();
		
		loc = new Vec2(200, 200);
		oldLoc = loc.clone();
		
		client = new Client(MenuGui.ip.getText(), Integer.parseInt(MenuGui.port.getText()), this);
		client.start();
		client.sendPacket(new ConnectPacket());
		client.sendPacket(new CSetName(name));
	}

	@Override
	public void createGui()
	{
		GuiUtils.createBasicLayout(this);
	}
	
	int delay = 0;
	
	private boolean mousePressed = false;

	@Override
	public void guiTick()
	{
		if (!openedChat)
		{
			if (getMainApp().getKeyHandler().isKeyPressed(GLFW_KEY_W))
				loc.move(Util.countAngle(loc.clone().right(16).down(16), getMainApp().getMouseHandler().toVec()));

			if (getMainApp().getKeyHandler().isKeyPressed(GLFW_KEY_S))
				loc.move(Util.countAngle(loc.clone().right(16).down(16), getMainApp().getMouseHandler().toVec()) + 180);

			delay++;
			if (delay >= 2)
			{
				if (!loc.equals(oldLoc))
				{
					client.sendPacket(new CMovePacket(loc.getIntX(), loc.getIntY()));
					delay = 0;
					oldLoc = loc.clone();
				}
			}

			if (getMainApp().getMouseHandler().isMouseHolded() && getMainApp().getMouseHandler().getButton() == 3)
			{
				loc.setLocation(getMainApp().getMouseHandler().toVec().clone().left(16).up(16));
			}

			if (getMainApp().getMouseHandler().isMouseHolded() && !mousePressed && getMainApp().getMouseHandler().getButton() == 1)
			{
				client.sendPacket(new CLeftPress(getMainApp().getMouseHandler()));
				mousePressed = true;
			}

			if (!getMainApp().getMouseHandler().isMouseHolded() && mousePressed)
			{
				client.sendPacket(new CLeftRelease(getMainApp().getMouseHandler()));
				mousePressed = false;
			}
		}

		if (world != null)
			world.tick();

		if (world != null)
			if (world.need != -1)
				client.sendPacket(new CRequestTile(world.need));
		
		bullets.tick(true);
		particles.tick(true);
	}

	@Override
	public void render(Screen screen)
	{
//		Background.renderFrame(screen, getMainApp());
		
		if (world != null)
			world.render(screen);
		
		for (PlayerMP p : players)
		{
			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();
			screen.fillRect(px, py, 32, 32, 0xffff0000);
			String score = "Score:" + p.score;
			String id = "#" + p.getNetworkId();
			String name = p.getPlayerName();
			font.render(name, px + 16 - name.length() * 4, py + 16);
			font.render(id, px + 16 - id.length() * 4, py);
			font.render(score, px + 16 - score.length() * 4, py + 8);
		}

		font.render("Score: " + score, 5, 30);

		screen.fillRect(loc.getIntX(), loc.getIntY(), 32, 32, 0xff0000ff);

		bullets.render(screen);
		particles.render(screen);
		
		int baseY = getMainApp().getCurrentHeight() - 27;
		
		for (int i = 0; i < chatText.size(); i++)
		{
			font.render(chatText.get(i), 5, baseY - 10 * i);
		}
		
		if (openedChat)
		{
			screen.fillRect(0, getMainApp().getCurrentHeight() - 10, 8 * 64 + 4, 10, 0x80000000);
			font.render(chatFieldText, 2, getMainApp().getCurrentHeight() - 9);
		}
	}

}

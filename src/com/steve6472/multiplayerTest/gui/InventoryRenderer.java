/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.gui;

import org.lwjgl.glfw.GLFW;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameSlot;
import com.steve6472.multiplayerTest.network.packets.client.inv.CMoveItem;
import com.steve6472.multiplayerTest.server.GameInventory;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.SmallNineSlice;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.callbacks.MouseButtonCallback;
import com.steve6472.sge.main.game.inventory.Inventory;

public class InventoryRenderer
{
	private SmallNineSlice slot;

	Inventory<GameSlot> currentInventory;

	int countX = 0;
	int countY = 0;
	int selectedX = 0;
	int selectedY = 0;

	private boolean isVisible = false;

	int holded = 0;
	int holdedX;
	int holdedY;
	byte step = 0;

	public InventoryRenderer(MainApplication mainApp)
	{
		slot = new SmallNineSlice(160, 96, 32, 32, Game.sprites, new Shader("shaders\\basev2"), Game.camera);
		slot.setCorner(0, 0, 7, 7);
		slot.setTopEdge(7, 0, 32 - 14, 7, 1);
		slot.setSideEdge(0, 7, 7, 32 - 14, 1);
		slot.setScale(32 - 23, 32 - 23, 0.111f);
		slot.createMiddle();
		
		if (!(mainApp instanceof Game))
			throw new IllegalArgumentException("MainApp is not Game!");
		
		Game game = (Game) mainApp;

		mainApp.getMouseHandler().addMouseButtonCallback(new MouseButtonCallback()
		{
			@Override
			public void invoke(int x, int y, int button, int action, int mods)
			{
				if (isVisible && !(selectedX == -1 || selectedY == -1) && action == GLFW.GLFW_PRESS && button == GLFW.GLFW_MOUSE_BUTTON_1)
				{
					if (step == 1)
					{
						int temp = currentInventory.getSlot(selectedX + selectedY * countX).getItemId();
						currentInventory.getSlot(selectedX + selectedY * countX).setItemId(holded);
						currentInventory.getSlot(holdedX + holdedY * countX).setItemId(temp);
						holded = 0;
						
						step = 0;
					} else
					{
						int temp = holded;
						holded = currentInventory.getSlot(selectedX + selectedY * countX).getItemId();
						currentInventory.getSlot(selectedX + selectedY * countX).setItemId(temp);

						holdedX = selectedX;
						holdedY = selectedY;
						step++;
					}

					if (game.clientGui != null)
					{
						if (holded == 0)
						{
							step = 0;
							System.out.println("Swapping from " + (holdedX + holdedY * countX) + " to " + (selectedX + selectedY * countX));
							Util.printObjects(countX, countY);
							Util.printObjects(holdedX, holdedY);
							Util.printObjects(selectedX, selectedY);
							game.clientGui.client.sendPacket(new CMoveItem(holdedX + holdedY * countX, selectedX + selectedY * countX, 1, 1));
						}
					} else
					{
						System.err.println("Inventory was opened on Server. Moving items is not supported!");
					}
					
				}
			}
		});
	}

	public void showInventory(GameInventory inventory)
	{
		this.currentInventory = inventory;
		this.countX = inventory.getWidth();
		this.countY = inventory.getHeight();
		isVisible = true;
	}

	public Inventory<GameSlot> hideInventory()
	{
		isVisible = false;
		currentInventory = null;
		countX = 0;
		countY = 0;
		selectedX = 0;
		selectedY = 0;
		holded = 0;
		return currentInventory;
	}

	public void tick(MainApplication mainApp)
	{
		int centerX = mainApp.getCurrentWidth() / 2;
		int centerY = mainApp.getCurrentHeight() / 2;
		int mouseX = mainApp.getMouseX();
		int mouseY = mainApp.getMouseY();

		selectedX = -(centerX - mouseX - countX / 2 * 52 - 52 / 2) / 52;
		selectedY = -(centerY - mouseY - countY / 2 * 52 - 52 / 2) / 52;

		if (selectedX < 0 || selectedX >= countX)
			selectedX = -1;

		if (selectedY < 0 || selectedY >= countY)
			selectedY = -1;

//		if (mainApp.isKeyPressed(GLFW.GLFW_KEY_K))
//		{
//			for (int i = 0; i < countX * countY; i++)
//				((GameSlot) currentInventory.getSlot(i)).setItemId(Util.getRandomInt(65, 1));
//			holded = 0;
//		}
//
//		if (mainApp.isKeyPressed(GLFW.GLFW_KEY_B))
//		{
//			for (int i = 0; i < countX * countY; i++)
//				((GameSlot) currentInventory.getSlot(i)).setItemId(i + 1);
//			holded = 0;
//		}
	}

	public void render(MainApplication mainApp)
	{
		if (!isVisible)
			return;

		Game.drawFont(mainApp, "" + selectedX, 5, 100);
		Game.drawFont(mainApp, "" + selectedY, 5, 110);

		int halfX = countX / 2;
		int halfY = countY / 2;

		for (int i = 0; i < countX; i++)
		{
			for (int j = 0; j < countY; j++)
			{
				float x = -(i - halfX) * 52;
				float y = -(j - halfY) * 52;
				slot.render(x, y);

				int item = currentInventory.getSlot(i + j * countX).getItemId();

				if (item != 0)
				{

					Helper.pushLayer();
					Helper.translate(x, y, 0);
					Helper.scale(16f);

					int atlasSizeX = 16;
					int atlasSizeY = 16;

					Game.drawSpriteFromAtlas2(item % atlasSizeX * 32, item / atlasSizeY * 32, Game.pixelModel32, Game.shader, Game.sprites);

					Helper.popLayer();
				}
			}
		}

		if (!(selectedX == -1 || selectedY == -1))
		{
			float f = 1.855f;

			Helper.pushLayer();
			Helper.scale(28f);
			Helper.translate(selectedX * -f + halfX * f, selectedY * -f + halfY * f, 0);

			Game.drawSpriteFromAtlas2(192, 96, Game.pixelModel32, Game.shader, Game.sprites);

			Helper.popLayer();
		}

		if (holded != 0)
		{
			Helper.pushLayer();
			Helper.translate(-mainApp.getMouseX() + mainApp.getCurrentWidth() / 2f, -mainApp.getMouseY() + mainApp.getCurrentHeight() / 2f, 0);
			Helper.scale(16f);

			int atlasSizeX = 16;
			int atlasSizeY = 16;

			Game.drawSpriteFromAtlas2(holded % atlasSizeX * 32, holded / atlasSizeY * 32, Game.pixelModel32, Game.shader,
					Game.sprites);

			Helper.popLayer();
		}

	}
	
	public boolean isVisible()
	{
		return isVisible;
	}
}

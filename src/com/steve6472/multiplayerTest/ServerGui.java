/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.util.ArrayList;
import java.util.List;

import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.structures.Lake;
import com.steve6472.multiplayerTest.structures.Structure;
import com.steve6472.multiplayerTest.structures.WallStructure0;
import com.steve6472.multiplayerTest.structures.WallStructure1;
import com.steve6472.multiplayerTest.structures.WallStructure2;
import com.steve6472.multiplayerTest.structures.WallStructure3;
import com.steve6472.multiplayerTest.structures.FlowerStructure;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.components.ItemList;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.particle.WaterSplash;

public class ServerGui extends Gui
{
	
	public ServerGui(MainApplication mainApp)
	{
		super(mainApp);
		switchRender();
	}

	private static final long serialVersionUID = 8028366502068225295L;
	Server server;
	public ItemList playerList;
	public List<PlayerMP> players;
	public World world0, world1;
	
	@Override
	public void showEvent()
	{
		server = new Server(Integer.parseInt(MenuGui.port.getText()), this);
		server.start();
		
		world0 = generateWorld(0);
		world1 = generateWorld(1);
	}
	
	public World generateWorld(int worldId)
	{
		World world = new World(32, 18, worldId, MultiplayerTest.camera, getMainApp());
		
		for (int i = 0; i < world.getTilesX(); i++)
		{
			for (int j = 0; j < world.getTilesY(); j++)
			{
				world.setTile(Tile.grass.getId(), i, j);
			}
		}
		
		Structure[] structures = new Structure[] { new WallStructure0(), new WallStructure1(), new Lake(), new FlowerStructure() , new WallStructure3(), new WallStructure2() };
		
		for (int i = 0; i < 5; i++)
		{
			int x = Util.getRandomInt(world.getTilesX(), 0);
			int y = Util.getRandomInt(world.getTilesY(), 0);
			
			Structure str = structures[Util.getRandomInt(structures.length - 1, 0)];
			str.generateStructure(x, y, world);
		}
		
//		new WallStructure0().generateStructure(12, 14, world);
//		new WallStructure1().generateStructure(8, 8, world);
//		new Lake().generateStructure(18, 2, world);
//		new WallStructure2().generateStructure(14, 8, world);
//		new WallStructure3().generateStructure(2, 2, world);
//		
//		world.setTile(9, 3, 8);
		
		return world;
	}

	@Override
	public void createGui()
	{
//		GuiUtils.createBasicLayout(this);
		playerList = new ItemList();
//		playerList.setLocation(10, 35);
//		playerList.setSize(256, 20 * 8);
//		playerList.setVisibleItems(8);
//		addComponent(playerList);
		
		players = new ArrayList<PlayerMP>();
	}

	@Override
	public void guiTick()
	{
		server.tick();
	}
	
	public World getWorld(int worldId)
	{
		if (worldId == 0) return world0;
		if (worldId == 1) return world1;
		
		return world0;
	}

	@Override
	public void render(Screen screen)
	{
		world0.render(screen);
		

		for (PlayerMP p : players)
		{
			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();
//			String ip = p.getIp().getHostName() + ":" + p.getPort();
			String ip = p.getIp() + ":" + p.getPort();
			String id = "#" + p.getNetworkId();
			String score = "Score:" + p.score;
			String name = p.getPlayerName();
			MultiplayerTest.drawSquare(px, py, 32, 32, 0xffff0000);
			MultiplayerTest.drawFont(getMainApp(), name, px + 16 - name.length() * 4, py - 16);
			MultiplayerTest.drawFont(getMainApp(), ip, px + 16 - ip.length() * 4, py - 8);
			MultiplayerTest.drawFont(getMainApp(), id, px + 16 - id.length() * 4, py);
			MultiplayerTest.drawFont(getMainApp(), score, px + 16 - score.length() * 4, py + 8);
		}
		
		MultiplayerTest.drawFont(mainApp, "UPS:" + mainApp.getFPS(), 5, 5);
		
		
//		server.bullets.render(screen);
	}

}

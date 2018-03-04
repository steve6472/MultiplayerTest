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
import com.steve6472.multiplayerTest.structures.Structure;
import com.steve6472.multiplayerTest.structures.WallStructure0;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.GuiUtils;
import com.steve6472.sge.gui.components.ItemList;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.Util;

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
	public World world0;
	
	@Override
	public void showEvent()
	{
		server = new Server(1337, this);
		server.start();
		
		world0 = new World(32, 18, 0);
		
		for (int i = 0; i < world0.getTilesX(); i++)
		{
			for (int j = 0; j < world0.getTilesY(); j++)
			{
				world0.setTile(Tile.grass.getId(), i, j);
			}
		}
		
		Structure[] structures = new Structure[] { new WallStructure0() };
		
		for (int i = 0; i < 2; i++)
		{
			int x = Util.getRandomInt(world0.getTilesX(), 0);
			int y = Util.getRandomInt(world0.getTilesY(), 0);
			
			Structure str = structures[Util.getRandomInt(structures.length - 1, 0)];
			str.generateStructure(x, y, world0);
		}
	}

	@Override
	public void createGui()
	{
		GuiUtils.createBasicLayout(this);
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

	@Override
	public void render(Screen screen)
	{
		world0.render(screen);

		for (PlayerMP p : players)
		{
			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();
			String ip = p.getIp().getHostName() + ":" + p.getPort();
			String id = "#" + p.getNetworkId();
			String score = "Score:" + p.score;
			screen.fillRect(px, py, 32, 32, 0xffff0000);
			font.render(ip, px + 16 - ip.length() * 4, py - 8);
			font.render(id, px + 16 - id.length() * 4, py);
			font.render(score, px + 16 - score.length() * 4, py + 8);
		}
		
		server.bullets.render(screen);
		
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import java.util.List;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SChat extends SPacket
{
	
	String text;
	int networkId;
	
	public SChat(String text, int networkId)
	{
		this.text = text;
		this.networkId = networkId;
	}
	
	public SChat()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeString(text);
		output.writeInt(networkId);
	}

	@Override
	public void input(DataStream input)
	{
		text = input.readString();
		networkId = input.readInt();
	}

	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
//		System.out.println("Server sended message: " + packet.getText() + " From: " + packet.getNetworkId());
		//Server message
		if (getNetworkId() == -1)
		{
			addMessage(getText(), clientGui);
		} else
		{
			if (client.networkId == getNetworkId())
			{
				addMessage("<" + ClientGui.name + "> " + getText(), clientGui);
			} else
			{
				PlayerMP player = client.getPlayer(getNetworkId());
				
				if (player != null)
					addMessage("<" + client.getPlayer(getNetworkId()).getPlayerName() + "> " + getText(), clientGui);
				else
					addMessage("<" + "invalidId" + "> " + getText(), clientGui);
			}
		}
		
		if (clientGui.getClientController().getChatText().size() > 8)
			clientGui.getClientController().clearLastMessage();
	}
	
	private void addMessage(String text, ClientGui clientGui)
	{
		clientGui.getClientController().addChatMessage(text);
		shiftRight(clientGui.getClientController().getChatText());
	}
	
	public void shiftRight(List<String> list) 
	{
		if (list.size() == 0)
			return;
		// make temp variable to hold last element
		String temp = list.get(list.size() - 1);

		// make a loop to run through the array list
		for (int i = list.size() - 1; i > 0; i--)
		{
			// set the last element to the value of the 2nd to last element
			list.set(i, list.get(i - 1));
		}
		// set the first element to be the last element
		list.set(0, temp);
	}
	
	public String getText()
	{
		return text;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

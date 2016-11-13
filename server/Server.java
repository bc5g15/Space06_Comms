package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class Server{
	
	private static ArrayList<ClientHolder> clients = new ArrayList<ClientHolder>();
	
	public static void handleInstruction(String inst, ClientHolder c)
	{
		if(c.getShushed())
		{
			c.sendMessage("You are shushed!");
			return;
		}
		
		String[] codes = inst.trim().split(" ");
		if(codes[0].equals("/quit"))
		{
			c.sendMessage("/end");
			c.kill();
		}
		else if(codes[0].equals("/w"))
		{
			String message = inst.replace(codes[1], "");
			message = message.replaceAll("/w", "");
			whisper(codes[1], message, c);
		}
		else
		{
			sendMsg(inst, c);
		}
	}
	
	
	private static void sendMsg(String msg, ClientHolder c)
	{
		String whole = c.getChatName() + " " + msg;
		System.out.println(whole);
		
		Iterator<ClientHolder> i = clients.iterator();
		while(i.hasNext())
		{
			ClientHolder tempC = i.next();
			if(tempC!=c)
			{
				tempC.sendMessage(whole);
			}
		}
		
	}
	
	public static void addClient(ClientHolder c)
	{
		clients.add(c);
	}
	
	public static void removeClient(ClientHolder c)
	{
		clients.remove(c);
		System.err.println("Client Removed");
	}
	
	private static void whisper(String name, String msg, ClientHolder c)
	{
		HashMap<String, ClientHolder> names = new HashMap<String, ClientHolder>();
		//ArrayList<String> names = new ArrayList<String>();
		for(ClientHolder item : clients) names.put(item.getName(), item);
		
		if(names.containsKey(name))
		{
			ClientHolder target = names.get(name);
			String prefix = "From " + c.getChatName();
			target.sendMessage(prefix + " " + msg);
			c.sendMessage("To " + target.getChatName() + " " + msg);
		}
		else
		{
			c.sendMessage("No such user!");
		}
	}
	
	private static ClientHolder getUserFromName(String name)
	{
		HashMap<String, ClientHolder> names = new HashMap<String, ClientHolder>();
		for(ClientHolder item : clients) names.put(item.getName(), item);
		if(names.containsKey(name))
		{
			return names.get(name);
		}
		else return null;
	}
	
	private static void adminBroadcast(String msg)
	{
		System.out.println("<*> " + msg);
		Iterator<ClientHolder> i = clients.iterator();
		while(i.hasNext())
		{
			ClientHolder c = i.next();
			c.sendMessage("<*> " + msg);
		}
	}
	
	public static void adminCommand(String inst)
	{
		String[] codes = inst.trim().split(" ");
		
		if(codes[0].equals("/shush"))
		{
			shushUser(codes[1]);
			adminBroadcast(codes[1] + " has been shushed");
		}
		else if(codes[0].equals("/unshush"))
		{
			unshushUser(codes[1]);
			adminBroadcast(codes[1] + " has been unshushed");
		}
		else if(codes[0].equals("/kick"))
		{
			ClientHolder c = getUserFromName(codes[1]);
			c.sendMessage("You have been disconnected");
			c.sendMessage("/end");
			c.kill();
			//removeClient(c);
		}
		else if(codes[0].equals("/killServer"))
		{
			adminBroadcast("Server is killed!");
			System.exit(0);
		}
		else
		{
			adminBroadcast(inst);
		}
	}
	
	private static void shushUser(String s)
	{
		ClientHolder c = getUserFromName(s);
		if(c!=null)
		{
			c.setShushed(true);
			c.sendMessage("You are now shushed");
			
		}
	}
	
	private static void unshushUser(String s)
	{
		ClientHolder c = getUserFromName(s);
		if(c!=null)
		{
			c.setShushed(false);
			c.sendMessage("You are unshushed");
		}
	}

}

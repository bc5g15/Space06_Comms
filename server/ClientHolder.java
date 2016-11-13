package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHolder implements Runnable {
	private Socket s;
	private boolean active;
	private DataOutputStream dos;
	private DataInputStream dis;
	private String username = "";
	private boolean shushed = false;
	
	public ClientHolder(Socket s) throws IOException
	{
		this.s = s;
		this.active = true;
		this.dos = new DataOutputStream(s.getOutputStream());
		this.dis = new DataInputStream(s.getInputStream());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
			//Get a username
			dos.writeUTF("Please enter a username:");
			username = dis.readUTF().trim().split(" ")[0];
			System.out.println(username + " has connected");
			
			while(active)
			{
				String instruction = dis.readUTF();
				Server.handleInstruction(instruction, this);
				//Thread.sleep(50);
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			//System.err.println("Something went wrong!");
			System.out.println(username + " disconnected");
		}
		Server.removeClient(this);
	}

	public String getChatName()
	{
		return "<"+username+">";
	}
	
	public String getName()
	{
		return username;
	}
	
	public void sendMessage(String message)
	{
		try
		{
			dos.writeUTF(message);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void kill()
	{
		try
		{
			dos.close();
			dis.close();
			s.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setActive(boolean val)
	{
		this.active = val;
	}
	
	public boolean getShushed()
	{
		return shushed;
	}
	
	public void setShushed(boolean shushed)
	{
		this.shushed = shushed;
	}
}

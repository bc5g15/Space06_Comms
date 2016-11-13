package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable {
	
	ServerSocket ss;
	boolean active = true;
	
	public Listener() throws IOException
	{
		this.ss = new ServerSocket(4727);
	}
	
	public void kill()
	{
		try
		{
			ss.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		try
		{
			while(active)
			{
				Socket s = ss.accept();
				ClientHolder c = new ClientHolder(s);
				Server.addClient(c);
				Thread t = new Thread(c);
				t.start();
				Thread.sleep(50);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Something went wrong!");
		}
	}

}

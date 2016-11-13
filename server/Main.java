package server;

import java.util.Scanner;

public class Main {
	
	private static boolean active = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			Listener l = new Listener();
			Thread t = new Thread(l);
			t.start();
			readCommands();
			//At this point I should activate the server's command reader
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static void readCommands()
	{
		Scanner sc = new Scanner(System.in);
		while(true)
		{
			String inst = sc.nextLine();
			Server.adminCommand(inst);
		}
	}

}

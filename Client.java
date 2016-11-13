
import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

public class Client implements Runnable {
	
	private static Scanner sc = new Scanner(System.in);
	private static DataInputStream dis;
	private static Socket s;
	
	public static String getInput()
	{
		String s = sc.nextLine();
		return s;
	}
	
	/*
	 * I'm completely cheating here by using the run method to handle getting input
	 */
	public void run()
	{
		try
		{
			while(true)
			{
				String msg = dis.readUTF();
				if(msg.equals("/end")) end();
				System.out.println(msg);
				
			}
		}
		catch(Exception e)
		{
			//I don't care if this is stopped.
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			String ip = args[0];
			s = new Socket(ip, 4727);
			dis = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			
			//Get username message
			System.out.println(dis.readUTF());
			out.writeUTF(getInput());
			
			//Now cheat. Open up the input handler as a new thread.
			Thread t = new Thread(new Client());
			t.start();
			
			while(true)
			{
				String inst = getInput();
				
				out.writeUTF(inst);
				if(inst.equals("/quit")) break;
			}
			
		}
		catch(IOException e)
		{
			//e.printStackTrace();
			System.out.println("Chat terminated");
		}
		
	}
	
	private static void end()
	{
		try
		{
			dis.close();
			s.close();
			System.exit(0);
		}
		catch(Exception e)
		{
			//I really don't care if the system crashes while closing
		}
	}


}

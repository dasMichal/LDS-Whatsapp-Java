import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.TreeMap;

public class backgroundNetwork extends Thread
{
	int port ;

	public backgroundNetwork(int port)
	{
		this.port = port;
	}

	@Override
	public void run()
	{
		try
		{
			serverStart(port);
		} catch (IOException e)
		{
			//e.printStackTrace();
			//System.out.println(e);
			System.out.println("Server Start ist gestoppt");
		}

	}

	public static void serverStart(int port) throws IOException {

		//socket server port on which it will listen
		ServerSocket server = new ServerSocket(port);
		System.out.println("Network Tread Started on Port "+ port);
		//create the socket server object
		Socket socket = server.accept();

		while (true)
		{

			//System.out.println("Waiting for the client request");
			//creating socket and waiting for client connection
			//Socket socket = server.accept();

			DataInputStream din = new DataInputStream(socket.getInputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String[] data;

			byte messageType = din.readByte();
			String message = din.readUTF();

			try
			{
				data = message.split(",");
				//System.out.println("\n>>> Message Type: "+messageType);
				System.out.println(">> " + message);
				//System.out.println(">>> Player " + data[0] + " Column " + data[1]);
				System.out.print(">>> \r");
			} catch (Exception e)
			{
				//System.out.println("oups no splitting");
			}


			//out.close();
			//in.close();
			//socket.close();

			//terminate the server if client sends exit request
		}



		
		//System.out.println("Shutting down Socket server!!");
		//close the ServerSocket object
		//server.close();


	}



}

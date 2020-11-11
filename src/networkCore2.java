import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class networkCore2
{

	private static final String IPV4_REGEX =
			"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
	private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
	private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);

	//socket server port on which it will listen
	private static int port;
	private static String ip_adress;
	private static String[] data;


	static void reciverNet() throws IOException
	{

		//create the socket server object
		ServerSocket server = new ServerSocket(port);

		//creating socket and waiting for client connection
		Socket socket = server.accept();

		//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		DataInputStream din = new DataInputStream(socket.getInputStream());
		//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//String message = in.readLine();

		byte messageType = din.readByte();
		String message = din.readUTF();


		try
		{
			data = message.split(",");

			//System.out.println(">>> " + data[0] + " Column " + data[1]);
		} catch (Exception e)
		{
			System.out.println("No splitting Possible");
			System.out.println("Mayby Corrupt Data?");
			System.out.println("DEBUG Recived Data:" + message);
		}
		//close the ServerSocket object
		server.close();

	}

	static void transmittNet(String column, String playerID, byte type) throws IOException, InterruptedException
	{
		boolean transmitted = false;
		int trys = 0;

		while ((!transmitted) & (trys < 3))
		{
			try
			{
				//Socket socket = new Socket("localhost",port);
				//Socket socket = new Socket("192.168.178.55", port);
				//Socket socket = new Socket("192.168.178.62",port);
				Socket socket = new Socket(ip_adress, port);

				System.out.print("Connected\r");

				DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
				//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				//BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


				String ausgabe;
				String playerS;
				String columnS;
				playerS = String.valueOf(playerID);
				columnS = String.valueOf(column);

				ausgabe = (playerS + "," + columnS);


				//out.printf(ausgabe);
				dout.writeByte(type);
				dout.writeUTF(ausgabe);


				dout.flush();

				transmitted = true;

			} catch (IOException e)
			{
				trys++;
				//System.out.println(e);
				System.out.print("Connection refused. Try:" + trys + "/3\r");

				Thread.sleep(90);

			}
		}


	}

	public static boolean isValidInet4Address(String ip)
	{
		if (ip == null)
		{
			return false;
		}

		Matcher matcher = IPv4_PATTERN.matcher(ip);
		return matcher.matches();
	}


	public static boolean isIPv6StdAddress(final String input)
	{

		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	public static boolean isIPv6HexCompressedAddress(final String input)
	{

		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}

	public static boolean isValidInet6Address(final String ip)
	{

		if (ip == null)
		{
			return false;
		}

		return isIPv6StdAddress(ip) || isIPv6HexCompressedAddress(ip);
	}


	public static String[] getData()
	{
		return data;
	}

	public static void setIp_address(String adress)
	{
		networkCore2.ip_adress = adress;
	}

	public static void setPort(int port)
	{
		networkCore2.port = port;
	}
}









import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class networkClient2_chat extends networkCore2
{



    public static void main(String[] args) throws IOException, InterruptedException
    {

        int port;
        Scanner input = new Scanner(System.in);
        String eingabe;
        String ip_address;
        int num=0;
        boolean transmitted = false;
        boolean exit = false;
        int trys=0;

        do
        {
            InetAddress inetAddress = InetAddress.getLocalHost();

            System.out.println("Please enter the IP adress of the other Chatuser");
            System.out.println("Your IP Adress is " + inetAddress.getHostAddress());
            System.out.print(">>> ");
            ip_address = input.next();

            if ((isValidInet4Address(ip_address)) ^ (isValidInet6Address(ip_address)))
            {
                if (isValidInet6Address(ip_address))
                {
                    System.out.print("The IPv6 Address " + ip_address + " is valid\n");

                } else
                {
                    System.out.print("The IPv4 Address " + ip_address + " is valid\n");
                }

                setIp_address(ip_address);
            } else
            {
                System.out.print("The IP address " + ip_address + " isn't valid\n");
            }
        } while ((!isValidInet4Address(ip_address)) ^ (isValidInet6Address(ip_address)));



        System.out.println("\nEnter the port you want to use:");
        System.out.print(">>> \r");
        port = input.nextInt();


        backgroundNetwork myThread1 = new backgroundNetwork(port);
        //backgroundNetwork myThread2 = new backgroundNetwork();
        //myThread2.start();

        myThread1.setName("Thread 1");
        //myThread2.setName("Thread 2");


        while ((!transmitted) & (trys < 5))
        {

        try{

            myThread1.start();
            //Socket socket = new Socket("192.168.178.62",port);
            //Socket socket = new Socket("192.168.178.55",port);
            Socket socket = new Socket(ip_address,port);
            //Socket socket = new Socket("localhost",port);

            System.out.println("Connected");

            DataOutputStream dout =new DataOutputStream(socket.getOutputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            do
            {
                System.out.print(">>> \r");
                eingabe = input.nextLine();

                if (eingabe.equals("exit"))
                {
                    exit=true;
                }else
                {
                    dout.writeByte(1);
                    dout.writeUTF(eingabe);
                    dout.flush();



                }



                num++;

            }while(!exit);


            transmitted= true;
            myThread1.interrupt();

        }catch(Exception e)
        {
            trys++;
            //System.out.println(e);
            System.out.print("Connection refused. Try:"+trys+"/5\r");

            Thread.sleep(2000);

        }

        }
        //System.out.println("Joined "+myThread1.getName());
        //myThread1.join();

        System.out.println("No Connection.");
        System.out.println("Exiting");
        System.out.print("\033[H\033[2J");

    }

}



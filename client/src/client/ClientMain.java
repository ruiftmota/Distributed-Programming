package client;

import java.net.UnknownHostException;

import client.communication.ServerCommunication;
import client.communication.ServerDirectoryDownException;
import client.user_interface.UserInterfaceThread;

public class ClientMain
{
    public static void main(String[] args) throws Exception
    {    
        if(args.length != 2)
        {
            System.err.println("Wrong number of arguments!");
            System.err.println("Usage: <name of executable> <server directory ip> <server directory port number>");
            return; 
        }

        try
        {
            int serverDirectoryPort = Integer.parseInt(args[1]);
            String serverDirectoryIP = args[0];
            ServerCommunication serverCommunication = new ServerCommunication(serverDirectoryIP, serverDirectoryPort);
            UserInterfaceThread userInterfaceThread = new UserInterfaceThread(serverCommunication);
            userInterfaceThread.start();
            userInterfaceThread.join();
        }
        catch(NumberFormatException exception)
        {
            System.err.println("Unvalid port.");
        }
        catch(UnknownHostException exception)
        {
            System.err.println("Unvalid ip.");
        }
        catch(ServerDirectoryDownException exception)
        {
            System.err.println("The server directory is currently not accessible.");
        }
    }
}
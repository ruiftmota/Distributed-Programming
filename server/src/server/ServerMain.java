package server;

import server.communication.ServerCommunication;
import server.logic.BusinessLogic;

public class ServerMain
{
    public static void main(String[] args) throws Exception
    {

        if(args.length != 5)
        {
            System.out.println("Wrong number of arguments!");
            System.out.println("<server directory port number> <server directory ip> <database port number> <database ip>");
            return;
        }

        Integer portNumberSD = new Integer(args[0]);
        String ipAddressSD = new String(args[1]);
        Integer portNumberDataBase = new Integer(args[2]);
        String ipAddressDataBase = new String(args[3]);
        String databaseName = new String(args[4]);

        InterfaceThread interfaceThread = new InterfaceThread();
        interfaceThread.start();

        
        BusinessLogic serverLogic = new BusinessLogic(ipAddressDataBase, portNumberDataBase, databaseName);

        ServerCommunication serverCommunication = new ServerCommunication( serverLogic,
                                                                           ipAddressSD,
                                                                           portNumberSD );

        serverCommunication.run();

        interfaceThread.join();
    }
}
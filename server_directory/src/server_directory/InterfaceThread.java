package server_directory;

import java.util.ArrayList;
import java.util.Scanner;

import server_directory.communication.ServerDirectoryCommunication;
import server_directory.logic.ServerInfo;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;


/**
 * InterfaceThread
 */
public class InterfaceThread extends Thread
{
    private Scanner scanner = null;
    private ServerDirectoryCommunication serverDirectoryCommunication;

    public InterfaceThread(ServerDirectoryCommunication serverDirectoryCommunication)
    {
        this.serverDirectoryCommunication = serverDirectoryCommunication;
    }

    @Override
    public void run()
    {
        scanner = new Scanner(System.in);

        while(KeepAlive.getKeepAlive() == true)
        {
            printCommands();
            takeCommand();
        }

        scanner.close();
    }

        /**
     * Prints all available commands in the Server Directory
     */
    private void printCommands()
    {
        SynchronizedPrint.printLine("----------------------------" + "\n" +
                                    "Available commands:" + "\n"+ "\n" +
                                    "1. Shutdown" + "\n" +
                                    "2. List servers" + "\n" +
                                    "----------------------------");
    }

    /**
     * This funtions realizes the action described by each of the possible commands
     */
    private void takeCommand()
    {
        String command = scanner.nextLine();
        
        if(command.equals("Shutdown"))
        {
            KeepAlive.setKeepAlive(false);
            SynchronizedPrint.printLine("Closing..." + "\n" +
                                        "This may take a few seconds...");
        }
        else if(command.equals("List servers"))
        {
            ArrayList<ServerInfo> serverInfos = serverDirectoryCommunication.getServersList();

            SynchronizedPrint.printLine("----------------------------" + "\n" +
                                        "List of servers:");

            for(ServerInfo serverInfo : serverInfos)
            {
                SynchronizedPrint.printLine(serverInfo);
            }

            SynchronizedPrint.printLine("----------------------------" + "\n");
        }
    }

}
package server;

import java.util.Scanner;

import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;


/**
 * InterfaceThread
 */
public class InterfaceThread extends Thread
{

    public InterfaceThread()
    {
    }

    private Scanner scanner = null;

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
                                    "1. shutdown" + "\n" +
                                    "----------------------------");
    }

    /**
     * This funtions realizes the action described by each of the possible commands
     */
    private void takeCommand()
    {
        String command = scanner.nextLine();
        
        if(command.equals("shutdown"))
        {
            KeepAlive.setNormalShutdown(true);
            SynchronizedPrint.printLine("Closing..." + "\n" +
                                        "This may take a few seconds...");
        }
    }

}
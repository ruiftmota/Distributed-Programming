package server_directory;


import server_directory.communication.ServerDirectoryCommunication;
import server_directory.logic.ServerDirectoryLogic;

/**
 * Class from where everything relative to the serverDirectory will be ran.
 */
public class ServerDirectory
{

    private ServerDirectoryCommunication serverDirectoryCommunication;
    private ServerDirectoryLogic serverDirectoryLogic;

    /**
     * This functions gets the server directory to start
     * preparing communication and logic
     */
    public void run()
    {
        InterfaceThread interfaceThread = new InterfaceThread();
        interfaceThread.start();

        
        serverDirectoryLogic = new ServerDirectoryLogic();
        serverDirectoryCommunication = new ServerDirectoryCommunication(serverDirectoryLogic);
        serverDirectoryCommunication.run();


        try
        {
            interfaceThread.join();
        }
        catch(InterruptedException exception)
        {
            
        }
    }






}
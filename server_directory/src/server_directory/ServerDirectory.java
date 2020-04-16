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
        serverDirectoryLogic = new ServerDirectoryLogic();
        serverDirectoryCommunication = new ServerDirectoryCommunication(serverDirectoryLogic);
        serverDirectoryCommunication.run();

        InterfaceThread interfaceThread = new InterfaceThread(serverDirectoryCommunication);
        interfaceThread.start();


        try
        {
            interfaceThread.join();
        }
        catch(InterruptedException exception)
        {
            
        }
    }






}
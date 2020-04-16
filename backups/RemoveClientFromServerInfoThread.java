package server_directory.communication;

import java.net.DatagramPacket;

import server_directory.logic.ClientInfo;
import server_directory.logic.ServerDirectoryLogic;


/**
 * This class takes care of removing the info of the client when he logs out
 */
public class RemoveClientFromServerInfoThread extends Thread
{
    private DatagramPacket datagramPacket;
    private ServerDirectoryLogic serverDirectoryLogic;

    public RemoveClientFromServerInfoThread(    DatagramPacket datagramPacket, 
                                                ServerDirectoryLogic serverDirectoryLogic)
    {    
        this.datagramPacket = datagramPacket;
        this.serverDirectoryLogic = serverDirectoryLogic;

    }

    /**
     * Searches the client list and all the server's list to remove
     * the client if he exists anywhere
     */
    @Override
    public void run() {
        ClientInfo clientToRemove = new ClientInfo(datagramPacket.getAddress().getHostAddress(),datagramPacket.getPort());
        
        serverDirectoryLogic.removeClient(clientToRemove);
    }
}
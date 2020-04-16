package server_directory.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server_directory.logic.ClientInfo;
import server_directory.logic.ServerDirectoryLogic;
import server_directory.logic.ServerInfo;


/**
 * Thread that takes the client connection request and tha regists everything
 * that need to be stored
 */
public class TakeClientThread extends Thread
{
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    private ServerDirectoryLogic serverDirectoryLogic;

    private ClientInfo newClient;

    public TakeClientThread( String ip,
                             int port,
                             ServerDirectoryLogic serverDirectoryLogic,
                             DatagramSocket datagramSocket )
    {
        this.serverDirectoryLogic = serverDirectoryLogic;
        this.datagramSocket = datagramSocket;
        this.datagramPacket = datagramPacket;
    }

    /**
     * Takes care of calling the funtion that reads the request and
     * chooses the server and of calling the function that sends
     * the answer to the client
     */
    @Override
    public void run() 
    {
        ServerInfo choosenServer = addClientToList();
        
        sendInfoToClient(choosenServer);

    }


    /**
     * Prepares the object ClientInfo with the data from the packet
     * and adds it to the logic
     * @return
     */
    public ServerInfo addClientToList()
    {
        newClient = new ClientInfo(datagramPacket.getAddress().getHostName(), datagramPacket.getPort());

        return serverDirectoryLogic.addClient(newClient);
    }


    /**
     * Sends the info of the choosen server to the client
     * @param choosenServer
     */
    public void sendInfoToClient(ServerInfo choosenServer)
    {
        try 
        {
            datagramPacket = new DatagramPacket(
                choosenServer.toString().getBytes(),
                choosenServer.toString().length(),     
                InetAddress.getByName(newClient.getClientAddress()),
                newClient.getClientPort());        

            datagramSocket.send(datagramPacket);
                    
        } 
        catch (IOException e) 
        {
            System.err.println("Unable to answer client login request");
            System.err.println(e.getStackTrace());
        }

    }
        
    
}
package server_directory.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server_directory.logic.ServerDirectoryLogic;
import server_directory.logic.ServerInfo;

/**
 * This class receives a server's connection requests
 */
public class TakeServerThread extends Thread
{
    private ServerDirectoryLogic serverDirectoryLogic;
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private ServerInfo serverInfo;

    /**
     * Creates a new object ServerInfo, the sees if the server has already stated
     * that is ready to establish a connection
     * and finnally if the server is the first one to connect, sets it as the 
     * primmary Server
     * 
     * @param serverPortTCP
     * @param datagramSocket to answer the server
     * @param serverDatagramPacket with the address to store and answer the server
     * @param serverDirectoryLogic the logic so tha can request to add the server
     */
    public TakeServerThread(    String serverIP,
                                Integer serverPort,
                                Integer serverPortTCP, 
                                DatagramSocket datagramSocket,
                                ServerDirectoryLogic serverDirectoryLogic )
    {
        this.datagramSocket = datagramSocket;
        
        this.serverDirectoryLogic = serverDirectoryLogic;
        this.serverInfo = new ServerInfo(serverIP, serverPort, serverPortTCP);
        
        this.serverDirectoryLogic.addServer(serverInfo);
    }

    

    
    
    /**
     * Sends an empty packet to the server so that it knows that he has been 
     * registered successedfully
     */
    @Override
    public void run() 
    {
        try 
        {
            datagramPacket = new DatagramPacket(
                null,
                0,
                InetAddress.getByName(serverInfo.getServerIp()),
                serverInfo.getServerPort());
            
            datagramSocket.send(datagramPacket);

        } 
        catch (IOException e) 
        {
            System.err.println("Unable to answer server login request");
            System.err.println(e.getStackTrace());
        }
    }
}
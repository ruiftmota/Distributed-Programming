package server_directory.communication;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import server_directory.logic.ServerDirectoryLogic;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;

/**
 * ServerDirectoryCommunication
 */
public class ServerDirectoryCommunication {

    private ServerDirectoryLogic serverDirectoryLogic;

    private DatagramSocket datagramSocket;
    private int timeout = 10000;

    
    /**
     * This constructor initializes serverPort with the variable given as a parameter
     * @param serverPort
     */
    public ServerDirectoryCommunication(ServerDirectoryLogic serverDirectoryLogic)
    {
        this.serverDirectoryLogic = serverDirectoryLogic;
    }


    /**
     * This functions starts the ServerDirectory.
     * It takes care of launching all the base threads needed by the program.
     */
    public void run()
    {
        createDatagramSocketWithTimeout();

        ReceiveRequestsThread receiveRequestsThread = new ReceiveRequestsThread(datagramSocket, serverDirectoryLogic);
        receiveRequestsThread.start();

        /* try
        {
            receiveRequestsThread.join();
        }
        catch(InterruptedException exception)
        {

        }

        for(PingThread pingThread : serverDirectoryLogic.getServerPingThreads())
        {
            try
            {
                pingThread.join();
            }
            catch(InterruptedException exception)
            {
                
            }
        } */

        //datagramSocket.close();
    }


    /**
     * If the port given is available it creates the datagramSocket 
     * to receive requests of connection.
     * Sets keepAlive to false if it should fail creating the socket or
     * setting the timeout
     */
    public void createDatagramSocketWithTimeout()
    {
        try
        {

            datagramSocket = new DatagramSocket();
            SynchronizedPrint.printLine("Server Directory running on " + InetAddress.getLocalHost().getHostAddress() + ":" + datagramSocket.getLocalPort());
        
        }
        catch(SocketException | UnknownHostException e)
        {
            KeepAlive.emergencyExit(e);
            return;
        }

        try 
        {
            datagramSocket.setSoTimeout(timeout);
        } 
        catch (SocketException e) 
        {
            KeepAlive.emergencyExit(e);
            datagramSocket.close();
        }
    }
}
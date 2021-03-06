package server.communication;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.MyInteger;
import shared_data.utilities.SynchronizedPrint;


public class TakeClientsThread extends Thread
{
    private ClientToServerInterface businessLogic;
    private ServerSocket serverSocket;
    private int timeOutValue = 10000;
    private ArrayList<ManageClientThread> manageClientThreads = new ArrayList<ManageClientThread>();
    private MyInteger numberOfClients;
    private MyInteger numberOfServersInTheSystem;

    private DatagramSocket disseminateUpdatesSocket;

    public TakeClientsThread(MyInteger numberOfClients, MyInteger numberOfServersInTheSystem,  ClientToServerInterface businessLogic, DatagramSocket disseminateUpdatesSocket)
    {
        try
        {
            serverSocket = new ServerSocket(0);
            serverSocket.setSoTimeout(10000);
        }
        catch(IOException exception)
        {
            KeepAlive.emergencyExit(exception);
        }

        this.numberOfClients = numberOfClients;
        this.numberOfServersInTheSystem = numberOfServersInTheSystem;

        this.disseminateUpdatesSocket = disseminateUpdatesSocket;
        this.businessLogic = businessLogic;
    }

    public int getServerSocketPort()
    {
        if(serverSocket != null)
        {
            return serverSocket.getLocalPort();
        }
        else
        {
            return -1;
        }
    }

    @Override
    public void run()
    {
        try
        {
            SynchronizedPrint.printLine("Listening clients at " + InetAddress.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort());
        }
        catch(IOException exception)
        {
            KeepAlive.emergencyExit(exception);
        }


        try 
        {
            serverSocket.setSoTimeout(timeOutValue);  
        }
        catch (SocketException exception) 
        {
            KeepAlive.emergencyExit(exception);
        }
        
        while(KeepAlive.getKeepAlive() == true)
        {
            try 
            {

                Socket socket = serverSocket.accept();
                socket.setSoTimeout(timeOutValue);
                
                ManageClientThread manageClientThread = new ManageClientThread(numberOfClients, numberOfServersInTheSystem, businessLogic, socket, disseminateUpdatesSocket);
                manageClientThread.start();

                manageClientThreads.add(manageClientThread);

            }
            catch (SocketTimeoutException e) 
            {
                continue;

            }
            catch(Exception exception)
            {
                KeepAlive.emergencyExit(exception);
            }
            
        
        
        }

        for(ManageClientThread thread : manageClientThreads)
        {
            try
            {
                thread.join();
            }
            catch(InterruptedException exception)
            {

            }
        }
    }
    
}
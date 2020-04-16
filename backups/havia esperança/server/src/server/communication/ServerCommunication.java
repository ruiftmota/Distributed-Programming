package server.communication;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.server_to_server_directory.ClientInfo;
import shared_data.communication.server_to_server_directory.requests.EnterServerRequest;
import shared_data.communication.server_to_server_directory.responses.ServerIsPrimaryResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;
import shared_data.utilities.SynchronizedPrint;

/**
 * ServerCommunication
 */
public class ServerCommunication
{
    private ClientToServerInterface businessLogic;

    private DatagramSocket serverDirectorySocket;

    private MulticastSocket multicastSocket;
    private InetAddress multicastGroup;
    private final int MULTICAST_PORT = 5350;
    private final String MULTICAST_IP = "239.3.2.1";
    private final int MULTICAST_TIMEOUT = 1000;
    
    private InetAddress ipAddressSD;
    private int portNumberSD;

    private TakeClientsThread takeClientsThread;
    private PingThread pingThread;
    private SendDatabaseThread sendDatabaseThread;
    private ListenToUpdatesThread listenToUpdatesThread;

    private ArrayList<ClientInfo> loggedInClients = new ArrayList<ClientInfo>();


    public ServerCommunication( ClientToServerInterface businessLogic,
                                String ipAddressSD, 
                                int portNumberSD ) throws UnknownHostException
    {
        this.businessLogic = businessLogic;
        this.ipAddressSD = InetAddress.getByName(ipAddressSD);
        this.portNumberSD = portNumberSD;

        try {
            this.multicastSocket = new MulticastSocket(MULTICAST_PORT);
            multicastGroup = InetAddress.getByName(MULTICAST_IP);
            multicastSocket.joinGroup(multicastGroup);
            multicastSocket.setSoTimeout(MULTICAST_TIMEOUT);
        } catch (IOException e) {
            KeepAlive.emergencyExit(e);
        }

    }

    public void run()
    {
        //THREAD QUE ATENDE CLIENTES
        takeClientsThread = new TakeClientsThread(businessLogic, multicastSocket);
        takeClientsThread.start();
        
        pingThread = new PingThread(this);
        pingThread.start();

        sendDatabaseThread = new SendDatabaseThread(businessLogic);
        sendDatabaseThread.start();

        listenToUpdatesThread = new ListenToUpdatesThread(businessLogic, multicastSocket);
        listenToUpdatesThread.start();



        try 
        {
            this.serverDirectorySocket = new DatagramSocket();
            connectToDS();
        }
        catch(SocketTimeoutException exception)
        {
            KeepAlive.emergencyExit(exception);
        }
        catch (IOException exception) 
        {
            KeepAlive.emergencyExit(exception);
        }
        catch(ClassNotFoundException exception)
        {
            KeepAlive.emergencyExit(exception);
        }


        try
        {
            takeClientsThread.join();
        }
        catch(InterruptedException exception)
        {
            SynchronizedPrint.printLine("Problem waiting for TakeClientsThread");
        }

        try
        {
            sendDatabaseThread.join();
        }
        catch(InterruptedException exception)
        {
            SynchronizedPrint.printLine("Problem waiting for SendDatabaseThread");
        }

        try
        {
            pingThread.join();
        }
        catch(InterruptedException exception)
        {
            SynchronizedPrint.printLine("Problem waiting for PingThread");
        }

        try
        {
            listenToUpdatesThread.join();
        }
        catch(InterruptedException exception)
        {
            SynchronizedPrint.printLine("Problem waiting for NewUpdatesThread");
        }

        serverDirectorySocket.close();
    }


    //Envia a address ao DS e espera resposta
    private void connectToDS() throws SocketTimeoutException, IOException, ClassNotFoundException
    {
        EnterServerRequest enterServerRequest = new EnterServerRequest( InetAddress.getLocalHost().getHostAddress(),
                                                                        serverDirectorySocket.getLocalPort(),
                                                                        takeClientsThread.getServerSocketPort(),
                                                                        pingThread.getPort(),
                                                                        listenToUpdatesThread.getPort(),
                                                                        sendDatabaseThread.getPort() );

        String jsonRequest = Serialization.serializeObjectToJson(enterServerRequest);
        SendAndReceiveInformation.sendData(jsonRequest, serverDirectorySocket, ipAddressSD, portNumberSD);

        SynchronizedPrint.printLine("enviei request");

        String jsonResponse = (String)SendAndReceiveInformation.receiveData(serverDirectorySocket);
        ServerIsPrimaryResponse serverIsPrimaryResponse = (ServerIsPrimaryResponse)Serialization.deserializeObjectFromJson(jsonResponse);

        if(   (serverIsPrimaryResponse.getPrimaryServerIp().equals(InetAddress.getLocalHost().getHostName()) == true
               && serverIsPrimaryResponse.getPrimaryServerToSendDataBasePort() == listenToUpdatesThread.getPort())
           == false)
        {
            getDatabaseFromOtherServer(InetAddress.getByName(serverIsPrimaryResponse.getPrimaryServerIp()),
                                                             serverIsPrimaryResponse.getPrimaryServerToSendDataBasePort());
        }

        SynchronizedPrint.printLine("Connected to server directory successfully");
    }

    private void getDatabaseFromOtherServer(InetAddress ip, int port)
    {

    }


    

    public ArrayList<ClientInfo> getLoggedInClients()
    {
        ArrayList<ClientInfo> tempArray = new ArrayList<ClientInfo>();

        synchronized(loggedInClients)
        {
            for(ClientInfo clientInfo : loggedInClients)
            {
                try
                {
                    tempArray.add((ClientInfo)clientInfo.clone());
                }
                catch(CloneNotSupportedException exception)
                {
                    
                }
            }
        }

        return tempArray;
    }

}
package server.communication;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.server_to_server_directory.requests.EnterServerRequest;
import shared_data.communication.server_to_server_directory.responses.ServerIsPrimaryResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.MyInteger;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;
import shared_data.utilities.SynchronizedPrint;

/**
 * ServerCommunication
 */
public class ServerCommunication
{
    private static String multicastGroup = "239.3.2.1";
    private static int multicastPort = 12345;

    public static String getMulticastGroup()
    {
        return multicastGroup;
    }

    public static int getMulticastPort()
    {
        return multicastPort;
    }

    private ClientToServerInterface businessLogic;

    private DatagramSocket serverDirectorySocket;

    private DatagramSocket disseminateUpdatesSocket;
    
    private InetAddress ipAddressSD;
    private int portNumberSD;

    private TakeClientsThread takeClientsThread;
    private PingThread pingThread;
    private SendDatabaseThread sendDatabaseThread;
    private ListenToUpdatesThread listenToUpdatesThread;

    private MyInteger numberOfClients = new MyInteger(0);
    private MyInteger numberOfServersInTheSystem = new MyInteger(0);


    public ServerCommunication( ClientToServerInterface businessLogic,
                                String ipAddressSD, 
                                int portNumberSD ) throws UnknownHostException
    {
        this.businessLogic = businessLogic;
        this.ipAddressSD = InetAddress.getByName(ipAddressSD);
        this.portNumberSD = portNumberSD;

        try
        {
            this.disseminateUpdatesSocket = new DatagramSocket();
            this.disseminateUpdatesSocket.setSoTimeout(10000);

        }
        catch(IOException exception)
        {
            KeepAlive.emergencyExit(exception);
        }

    }

    public void run()
    {
        //THREAD QUE ATENDE CLIENTES
        takeClientsThread = new TakeClientsThread(numberOfClients, numberOfServersInTheSystem, businessLogic, disseminateUpdatesSocket);
        takeClientsThread.start();
        
        pingThread = new PingThread(numberOfClients, numberOfServersInTheSystem);
        pingThread.start();

        sendDatabaseThread = new SendDatabaseThread(businessLogic);
        sendDatabaseThread.start();

        listenToUpdatesThread = new ListenToUpdatesThread(businessLogic, disseminateUpdatesSocket.getLocalPort());
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

}
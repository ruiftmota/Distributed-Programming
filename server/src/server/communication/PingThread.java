package server.communication;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import shared_data.communication.server_to_server_directory.requests.PingRequest;
import shared_data.communication.server_to_server_directory.responses.PingResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.MyInteger;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;
import shared_data.utilities.SynchronizedPrint;

/**
 * PingThread
 */
public class PingThread extends Thread
{
    private final static int TIMEOUT = 10000;

    private MyInteger numberOfClients;
    private MyInteger numberOfServersInTheSystem;

    private DatagramSocket datagramSocket;

    public PingThread(MyInteger numberOfClients, MyInteger numberOfServersInTheSystem)
    {
        this.numberOfClients = numberOfClients;
        this.numberOfServersInTheSystem = numberOfServersInTheSystem;
        try
        {
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(TIMEOUT);
        }
        catch(SocketException exception)
        {
            KeepAlive.emergencyExit(exception);
            return;
        }
    }

    public int getPort()
    {
        return datagramSocket.getLocalPort();
    }

    @Override
    public void run()
    {
        while(KeepAlive.getKeepAlive() == true)
        {
            PingRequest pingRequest = null;

            try
            {
                String jsonObject = (String)SendAndReceiveInformation.receiveData(datagramSocket);
                pingRequest = (PingRequest)Serialization.deserializeObjectFromJson(jsonObject);
                numberOfServersInTheSystem.setInteger(pingRequest.getServerList());
            }
            catch(SocketTimeoutException exception)
            {
                continue;
            }
            catch(IOException | ClassNotFoundException exception)
            {
                KeepAlive.emergencyExit(exception);
            }

            try
            {
                PingResponse pingResponse = new PingResponse(numberOfClients.getInteger());
                String jsonObject = Serialization.serializeObjectToJson(pingResponse);
                SendAndReceiveInformation.sendData( jsonObject, datagramSocket, 
                                                    InetAddress.getByName(pingRequest.getSenderIP()),
                                                    pingRequest.getSenderPort() );

                SynchronizedPrint.printLine("Sended ping to SD");
            }
            catch(IOException exception)
            {
                KeepAlive.emergencyExit(exception);
            }
        }
    }

}
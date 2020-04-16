package server.communication;

import java.net.DatagramSocket;
import java.net.InetAddress;

import shared_data.communication.Request;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.MyInteger;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;
import shared_data.utilities.SynchronizedPrint;

/**
 * HandleNewUpdatesThread
 */
public class HandleNewUpdatesThread extends Thread
{
    private DatagramSocket disseminateUpdatesSocket;

    private MyInteger numberOfServersInTheSystem;

    private Request request;
    
    public HandleNewUpdatesThread(MyInteger numberOfServersInTheSystem, Request request, DatagramSocket disseminateUpdatesSocket)
    {
        this.numberOfServersInTheSystem = numberOfServersInTheSystem;
        this.disseminateUpdatesSocket = disseminateUpdatesSocket;
        this.request = request;
    }

    @Override
    public void run()
    {
        
        String requestInJson = Serialization.serializeObjectToJson(request);
        try
        {
            SynchronizedPrint.printLine("Dissiminating request");

            SendAndReceiveInformation.sendDataMulticast(requestInJson, disseminateUpdatesSocket, InetAddress.getByName(ServerCommunication.getMulticastGroup()), ServerCommunication.getMulticastPort(), numberOfServersInTheSystem);
  
            SynchronizedPrint.printLine("Successfull dissiminated request");
        }
        catch(Exception exception)
        {
            KeepAlive.emergencyExit(exception);
        }
    }
    
}
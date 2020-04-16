package server.communication;

import java.net.InetAddress;
import java.net.MulticastSocket;

import shared_data.communication.Request;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;

/**
 * HandleNewUpdatesThread
 */
public class HandleNewUpdatesThread extends Thread
{
    private MulticastSocket multicastSocket;

    private Request request;

    public HandleNewUpdatesThread(Request request, MulticastSocket multicastSocket)
    {
        this.multicastSocket = multicastSocket;
        this.request = request;
    }

    @Override
    public void run()
    {
        
        String requestInJson = Serialization.serializeObjectToJson(request);
        try
        {
            SendAndReceiveInformation.sendDataMulticast(requestInJson, multicastSocket, InetAddress.getByName("239.3.2.1"), multicastSocket.getLocalPort(), KeepAlive.getNumberOfServers());
  
        }
        catch(Exception exception)
        {
            KeepAlive.emergencyExit(exception);
        }
    }
    
}
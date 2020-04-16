package server_directory.communication;

import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import server_directory.logic.ServerDirectoryLogic;
import shared_data.communication.Request;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;


public class ReceiveRequestsThread extends Thread
{

    private DatagramSocket datagramSocket;

    private ServerDirectoryLogic serverDirectoryLogic;



    public ReceiveRequestsThread(   DatagramSocket datagramSocket,
                                    ServerDirectoryLogic serverDirectoryLogic)
    {
        this.datagramSocket = datagramSocket;
        this.serverDirectoryLogic = serverDirectoryLogic;
    }

    /**
     * This funtions receives a DatagramPacket with any sort of request
     * and launches a new thread to take care of that same request
     */
    @Override
    public void run() 
    {
        

        while(KeepAlive.getKeepAlive())
        {

            try
            {
                String requestInJson = SendAndReceiveInformation.receiveData(datagramSocket);
                Request request = (Request)Serialization.deserializeObjectFromJson(requestInJson);
                
                TreatRequestThread treatRequestThread = new TreatRequestThread(serverDirectoryLogic, request);
                treatRequestThread.setDaemon(true);
                treatRequestThread.start();

                
                
            }
            catch(SocketTimeoutException exception)
            {
                continue;
            }
            catch(Exception exception)
            {
                KeepAlive.emergencyExit(exception);
            }
            
        }
    }
}
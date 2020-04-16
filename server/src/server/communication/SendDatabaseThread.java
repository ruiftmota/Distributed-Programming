package server.communication;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;

/**
 * SendDatabaseThread
 */
public class SendDatabaseThread extends Thread
{
    private ClientToServerInterface businessLogic;

    private final static int TIMEOUT = 10000;

    private DatagramSocket datagramSocket;

    public SendDatabaseThread(ClientToServerInterface businessLogic)
    {
        this.businessLogic = businessLogic;

        try
        {
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(TIMEOUT);
        }
        catch
        (SocketException exception)
        {
            KeepAlive.emergencyExit(exception);
        }
    }

    public int getPort()
    {
        return datagramSocket.getLocalPort();
    }

    @Override
    public void run()
    {
        try
        {
            SynchronizedPrint.printLine("Listening requests to send full database at " + InetAddress.getLocalHost().getHostAddress() + ":" + this.getPort());

        }
        catch(UnknownHostException exception)
        {
            KeepAlive.emergencyExit(exception);
        }
    }

}
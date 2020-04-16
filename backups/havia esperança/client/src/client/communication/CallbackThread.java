package client.communication;

import java.net.Socket;
import java.util.Scanner;

import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;

/**
 * CallbackThread
 */
public class CallbackThread extends Thread
{
    private Socket socket;

    public CallbackThread(Socket socket)
    {
        this.socket = socket;
        try
        {
            socket.setSoTimeout(10000);
        }
        catch(Exception exception)
        { 
            KeepAlive.emergencyExit(exception);
        }
    }

    @Override
    public void run()
    {
        while(KeepAlive.getKeepAlive() == true)
        {
            try
            {
                Scanner scanner = new Scanner(socket.getInputStream());
                scanner.nextLine();
                SynchronizedPrint.printLine("The server ordered a normal shutdown. Closing application...");
                KeepAlive.setKeepAlive(false);
                scanner.close();
            }   
            catch(Exception exception)
            {
                KeepAlive.emergencyExit(exception);
            }
        }
    }
    
}
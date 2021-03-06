package client.communication;

import java.net.Socket;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.send_files_threads.ReceiveFileThread;
import shared_data.utilities.SynchronizedPrint;

/**
 * ClientReceiveFileThread
 */
public class ClientReceiveFileThread extends ReceiveFileThread
{

    private String songName;

    public ClientReceiveFileThread(Socket socket, ClientToServerInterface bussinessLogic, String songName)
    {
        super(socket, bussinessLogic);
        this.songName = songName;
    }

    @Override
    public void run()
    {
        super.runThread();

        SynchronizedPrint.printLine("The file \"" + songName + "\" was downloaded successfully.");
    }
    
}
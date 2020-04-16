package server.communication.send_receive_files;

import java.io.IOException;
import java.net.Socket;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.send_files_threads.SendFileThread;
import shared_data.utilities.SynchronizedPrint;

/**
 * DownloadSongThread
 */
public class DownloadSongThread extends SendFileThread
{
    private Socket clientDownloadSocket;

    public DownloadSongThread(String fileName, Socket clientDownloadSocket, ClientToServerInterface businessLogic)
    {
        super(fileName, clientDownloadSocket, businessLogic);
        this.clientDownloadSocket = clientDownloadSocket;
    }

    @Override
    public void run() 
    {
        
        super.runThread();

        SynchronizedPrint.printLine("The file was sent to the client successfully.");

        try 
        {
			clientDownloadSocket.close();
        }
        catch (IOException e) 
        {
            System.err.println("DownloadSongThread problem closing socket. Should not happen...");
        }
        
    }
    
}
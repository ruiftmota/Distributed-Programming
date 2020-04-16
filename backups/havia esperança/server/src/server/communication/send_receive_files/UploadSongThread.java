package server.communication.send_receive_files;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.Socket;

import server.communication.HandleNewUpdatesThread;
import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.send_files_threads.ReceiveFileThread;
import shared_data.communication.server_to_server.SendFileChunckRequest;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;

/**
 * UploadSongThread
 */
public class UploadSongThread extends ReceiveFileThread 
{    

    private final static int CHUNK_SIZE = 1900;

    private ClientToServerInterface businessLogic;

    private MulticastSocket multicastSocket;

    public UploadSongThread(Socket clientUploadSocket, ClientToServerInterface businessLogic, MulticastSocket multicastSocket)
    {
        super(clientUploadSocket, businessLogic);
        this.businessLogic = businessLogic;
    }


    @Override
    public void run() 
    {
        
        super.runThread();

        SynchronizedPrint.printLine("The file was downloaded successfully.");

        shareThroughMulticast(super.getName());
        
    }


    private void shareThroughMulticast(String fileName)
    {
        byte[] bytes;
        int numberBytesRead = 0;
        int offset = 0;
        
        do
        {
            try
            {
                bytes = businessLogic.readFileChunk(fileName, offset, CHUNK_SIZE);
                numberBytesRead = bytes.length;
                offset = offset + numberBytesRead;

                SendFileChunckRequest sendFileChunckRequest = new SendFileChunckRequest(bytes, fileName, numberBytesRead);
                HandleNewUpdatesThread handleNewUpdatesThread = new HandleNewUpdatesThread(sendFileChunckRequest, multicastSocket);
                handleNewUpdatesThread.start();
                handleNewUpdatesThread.join();
            }
            catch(IOException | InterruptedException exception)
            {
                KeepAlive.emergencyExit(exception);
            }
        }
        while(numberBytesRead > 0);




    }

    
}
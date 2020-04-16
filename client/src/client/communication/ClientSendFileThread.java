package client.communication;

import java.net.Socket;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.send_files_threads.SendFileThread;
import shared_data.utilities.SynchronizedPrint;

/**
 * ClientSendFileThread
 */
public class ClientSendFileThread extends SendFileThread
{

    private String fileName;

    public ClientSendFileThread(String fileName, Socket socket, ClientToServerInterface bussinessLogic)
    {
        super(fileName, socket, bussinessLogic);
        this.fileName = fileName;
    }

    
    @Override
    public void run()
    {
        super.runThread();

        SynchronizedPrint.printLine("The file \"" + fileName + "\" was uploaded successfully.");
    }
}
package shared_data.communication.client_to_server.send_files_threads;

import java.io.IOException;
import java.net.Socket;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.server_to_server.SendFileChunckRequest;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;

/**
 * ReceiveFileFromServer 
 * This class purpose is to receive the file the client wants to download
 */
public class ReceiveFileThread extends Thread
{    
    private Socket socket;

    private String fileName;

    private ClientToServerInterface bussinessLogic;

    public ReceiveFileThread(Socket socket, ClientToServerInterface bussinessLogic) 
    {
        this.socket = socket;
        this.bussinessLogic = bussinessLogic;
        
    }


    @Override
    public void run() {
        runThread();
    }

    public void runThread() 
    {

        try
        {
            SendFileChunckRequest request;
            while(true)
            {
                String requetInJson = SendAndReceiveInformation.receiveDataTCP(socket);
                request = (SendFileChunckRequest)Serialization.deserializeObjectFromJson(requetInJson);

                
                if(request.getLength() == 0 && request.getByteArray().length == 0)
                {
                    break;
                }

                bussinessLogic.writeFileChunck(request.getFileName(), request.getByteArray(), request.getLength());
            }

            this.fileName = request.getFileName();

            socket.close();

        }
        catch(NullPointerException | IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }  
    }

    public String getFileName()
    {
        return fileName;
    }

}
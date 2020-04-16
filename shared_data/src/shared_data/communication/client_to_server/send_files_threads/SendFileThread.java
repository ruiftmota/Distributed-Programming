package shared_data.communication.client_to_server.send_files_threads;

import java.io.IOException;
import java.net.Socket;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.server_to_server.SendFileChunckRequest;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;

/**
 * This thread is launched after the verification if the file exists and it's purpose
 * is to send the file to the server
 */
public class SendFileThread extends Thread
{
    private ClientToServerInterface bussinessLogic;

    private String fileName;
    private Socket socket;

    private final static int FILE_CHUNCK_SIZE = 1900;

    public SendFileThread(String fileName, Socket socket, ClientToServerInterface bussinessLogic) 
    {
        this.fileName = fileName;
        this.socket = socket;

        this.bussinessLogic = bussinessLogic;
    }

    
    @Override
    public void run() {
        runThread();
    }


    public void runThread()
    {
        try{

            int nbytes = 0;

            byte[] byteArray = new byte[FILE_CHUNCK_SIZE];
            while(true)
            {
                byteArray = bussinessLogic.readFileChunk(fileName, nbytes, FILE_CHUNCK_SIZE);
                SendFileChunckRequest sendFileChunckRequest = new SendFileChunckRequest(byteArray, fileName, byteArray.length);
                String requestInJson = Serialization.serializeObjectToJson(sendFileChunckRequest);
                
                SendAndReceiveInformation.sendDataTCP(requestInJson, socket);

                nbytes += byteArray.length;

                if(byteArray.length == 0)
                    break;
            }

            //socket.close();
        }
        catch(NullPointerException | IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
}
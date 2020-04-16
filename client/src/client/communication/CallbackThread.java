package client.communication;

import java.io.IOException;
import java.net.Socket;

import shared_data.communication.Request;
import shared_data.communication.client_to_server.requests.ShutdownRequest;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;

/**
 * CallbackThread
 */
public class CallbackThread extends Thread {
    private ServerCommunication serverCommunication;

    public CallbackThread(ServerCommunication serverCommunication) {
        this.serverCommunication = serverCommunication;
    }

    @Override
    public void run() {
        Socket socket = null;
        while (KeepAlive.getKeepAlive() == true) {
            try {
                socket = serverCommunication.getCallBackSocket();
                if(socket == null)
                {
                    continue;
                }
                String requestInJson = SendAndReceiveInformation.receiveDataTCP(socket);
                Request request = (Request) Serialization.deserializeObjectFromJson(requestInJson);

                if (request instanceof ShutdownRequest) {
                    KeepAlive.setKeepAlive(false);
                }

            } catch (Exception exception) {
                KeepAlive.emergencyExit(exception);
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            KeepAlive.emergencyExit(e);
        }
    }
    
}
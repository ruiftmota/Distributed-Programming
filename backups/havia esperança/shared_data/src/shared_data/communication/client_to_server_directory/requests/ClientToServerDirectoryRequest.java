package shared_data.communication.client_to_server_directory.requests;

import shared_data.communication.Request;

/**
 * ClientToServerDirectoryRequest
 */
public abstract class ClientToServerDirectoryRequest extends Request
{

    public ClientToServerDirectoryRequest(String senderIP, int senderPort)
    {
        super(senderIP, senderPort);
    }

    public String getSenderIP()
    {
        return super.getSenderIP();
    }

    public int getSenderPort()
    {
        return super.getSenderPort();
    }

    
    
}
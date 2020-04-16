package shared_data.communication.server_to_server_directory.requests;

import shared_data.communication.Request;

/**
 * ServerToDirectoryServerRequest
 */
public abstract class ServerToDirectoryServerRequest extends Request
{
    

    public ServerToDirectoryServerRequest(String senderIP, int senderPort)
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

    @Override
    public String toString() {
        return super.toString() + "ServerToDirectoryServerRequest []";
    }

    
    
}
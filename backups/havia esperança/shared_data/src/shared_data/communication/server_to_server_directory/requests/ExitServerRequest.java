package shared_data.communication.server_to_server_directory.requests;

/**
 * ExitServerRequest
 */
public class ExitServerRequest extends ServerToDirectoryServerRequest
{

    public ExitServerRequest(String senderIP, int senderPort)
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
package shared_data.communication.client_to_server_directory.requests;

public class ExitClientRequest extends ClientToServerDirectoryRequest
{
    
    public ExitClientRequest(String senderIP, int senderPort)
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
        return "ExitClientRequest [" + super.toString() +"]";
    }

}
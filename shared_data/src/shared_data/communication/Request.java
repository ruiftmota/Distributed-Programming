package shared_data.communication;


/**
 * Request
 */
public abstract class Request
{
    

    private String senderIP;
    private int senderPort;

    public Request()
    {
        this.senderIP = null;
        this.senderPort = -1;
    }

    public Request(String senderIP, int senderPort)
    {
        this.senderIP = senderIP;
        this.senderPort = senderPort;
    }

    public String getSenderIP()
    {
        return senderIP;
    }

    public int getSenderPort()
    {
        return senderPort;
    }

    @Override
    public String toString()
    {
        return "Request [senderIP=" + senderIP + ", senderPort=" + senderPort + "]";
    }

    

    
}
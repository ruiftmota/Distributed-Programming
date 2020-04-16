package shared_data.communication.server_to_server_directory.requests;

/**
 * EnterServerRequest
 */
public class EnterServerRequest extends ServerToDirectoryServerRequest
{
    private int listeningTCPPort;
    private int pingPort;
    private int newUpdatesPort;
    private int sendDatabasePort;

    public EnterServerRequest(String senderIP, int senderPort, int listeningTCPPort, int pingPort, int newUpdatesPort, int sendDatabasePort)
    {
        super(senderIP, senderPort);
        this.listeningTCPPort = listeningTCPPort;
        this.pingPort = pingPort;
        this.newUpdatesPort = newUpdatesPort;
        this.sendDatabasePort = sendDatabasePort;
    }

    public String getSenderIP()
    {
        return super.getSenderIP();
    }

    public int getSenderPort()
    {
        return super.getSenderPort();
    }

    public int getListeningTCPPort()
    {
        return listeningTCPPort;
    }

    public int getPingPort()
    {
        return pingPort;
    }

    public int getNewUpdatesPort()
    {
        return newUpdatesPort;
    }

    public int getSendDatabasePort()
    {
        return sendDatabasePort;
    }

    @Override
    public String toString() {
        return super.toString() + "EnterServerRequest [listeningTCPPort=" + listeningTCPPort + ", newUpdatesPort=" + newUpdatesPort
                + ", pingPort=" + pingPort + ", sendDatabasePort=" + sendDatabasePort + "]";
    }
    
    
    
}
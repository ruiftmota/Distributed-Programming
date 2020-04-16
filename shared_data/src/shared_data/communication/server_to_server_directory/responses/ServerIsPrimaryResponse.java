package shared_data.communication.server_to_server_directory.responses;


import shared_data.communication.Response;

/**
 * ServerIsPrimaryResponse
 */
public class ServerIsPrimaryResponse extends Response
{
    

    private String primaryServerToSendDataBaseIp;
    private int primaryServerToSendDataBasePort;

    public ServerIsPrimaryResponse(String primaryServerIp, int primaryServerToSendDataBasePort)
    {
        this.primaryServerToSendDataBaseIp = primaryServerIp;
        this.primaryServerToSendDataBasePort = primaryServerToSendDataBasePort;
    }

    public String getPrimaryServerIp()
    {
        return primaryServerToSendDataBaseIp;
    }

    public int getPrimaryServerToSendDataBasePort()
    {
        return primaryServerToSendDataBasePort;
    }

    @Override
    public String toString() {
        return "ServerIsPrimaryResponse [primaryServerIp=" + primaryServerToSendDataBaseIp + ", primaryServerToSendDataBasePort="
                + primaryServerToSendDataBasePort + "]";
    }
    
}
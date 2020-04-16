package shared_data.communication.client_to_server.requests;

/**
 * ConfigureCallBackRequest
 */
public class ConfigureCallBackRequest
{

    private int port;

    public ConfigureCallBackRequest(int port)
    {
        this.port = port;
    }

    public int getPort()
    {
        return port;
    }

    
}
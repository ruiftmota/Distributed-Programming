package shared_data.communication.server_to_server_directory.responses;


import shared_data.communication.Response;

/**
 * PingResponse
 */
public class PingResponse extends Response
{
    private int numberOfClientsInTheServer;

    public PingResponse(int numberOfClientsInTheServer)
    {
        this.numberOfClientsInTheServer = numberOfClientsInTheServer;
    }

    public int getNumberOfClients()
    {
        return numberOfClientsInTheServer;
    }
    
}
package shared_data.communication.server_to_server_directory.responses;

import java.util.ArrayList;

import shared_data.communication.Response;
import shared_data.communication.server_to_server_directory.ClientInfo;

/**
 * PingResponse
 */
public class PingResponse extends Response
{
    private ArrayList<ClientInfo> clientArrayList;

    public PingResponse(ArrayList<ClientInfo> clientArrayList)
    {
        this.clientArrayList = clientArrayList;
    }

    public ArrayList<ClientInfo> getClientsArrayList()
    {
        return clientArrayList;
    }
    
}
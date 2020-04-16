package shared_data.communication.client_to_server.responses;

import java.util.ArrayList;

import shared_data.communication.Response;
import shared_data.communication.client_to_server.EnumActionResult;


public class DownloadPlaylistsResponse extends Response {

    private EnumActionResult enumActionResult;
    private int serverSocketPort = -1;
    private ArrayList<String> filesNames;

    public DownloadPlaylistsResponse(EnumActionResult enumActionResult, int serverSocketPort, ArrayList<String> filesNames)
    {
        this.enumActionResult = enumActionResult;
        this.serverSocketPort = serverSocketPort;
    }

    public EnumActionResult getEnumActionResult()
    {
        return enumActionResult;
    }

    public int getPort()
    {
        return serverSocketPort;
    }
    
    public ArrayList<String> getFilesNames()
    {
        return filesNames;
    }
}
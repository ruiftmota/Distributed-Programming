package shared_data.communication.client_to_server.responses;

import shared_data.communication.Response;
import shared_data.communication.client_to_server.EnumActionResult;

/**
 * DownloadSongResponse
 */
public class DownloadSongResponse extends Response
{

    private EnumActionResult enumActionResult;
    private int serverSocketPort = -1;
    

    public DownloadSongResponse(EnumActionResult enumActionResult, int serverSocketPort)
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
    
}
package shared_data.communication.client_to_server.responses;

import shared_data.communication.Response;
import shared_data.communication.client_to_server.EnumActionResult;


public class UploadSongResponse extends Response
{

    private EnumActionResult enumActionResult;
    private int serverSocketPort = -1;
    

    public UploadSongResponse(EnumActionResult enumActionResult, int serverSocketPort)
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
package shared_data.communication.client_to_server.requests;
import shared_data.communication.Request;

/**
 * DownloadSongRequest
 */
public class DownloadSongRequest extends Request
{

    private String songName;

    public DownloadSongRequest(String songName)
    {
        this.songName = songName;
    }

    public String getSongName() {
        return songName;
    }


    
    
}
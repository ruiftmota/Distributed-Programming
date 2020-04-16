package shared_data.communication.client_to_server.requests;

import shared_data.communication.Request;
/**
 * DownloadPlaylistRequest
 */
public class DownloadPlaylistRequest  extends Request {

    private String playlistName;

    public DownloadPlaylistRequest(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistName() {
        return playlistName;
    }


    
}
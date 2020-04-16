package shared_data.communication.client_to_server.requests;

import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.communication.Request;

/**
 * RemovePlaylistRequest
 */
public class RemovePlaylistRequest extends Request
{

    private User user;
    private String playlistName;

    public RemovePlaylistRequest(User user, String playlistName) {
        this.user = user;
        this.playlistName = playlistName;
    }

    public User getUser() {
        return user;
    }


    public String getPlaylistName() {
        return playlistName;
    }


    
    
}
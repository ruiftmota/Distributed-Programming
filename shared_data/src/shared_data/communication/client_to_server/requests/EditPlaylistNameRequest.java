package shared_data.communication.client_to_server.requests;

import shared_data.communication.Request;
import shared_data.communication.client_to_server.businesslogic.User;

/**
 * EditPlaylistNameRequest
 */
public class EditPlaylistNameRequest extends Request
{

    private User user;
    private String playlistName;
    private String newPlaylistName;

    public EditPlaylistNameRequest(User user, String playlistName, String newPlaylistName) {
        this.user = user;
        this.playlistName = playlistName;
        this.newPlaylistName = newPlaylistName;
    }

    public User getUser() {
        return user;
    }


    public String getPlaylistName() {
        return playlistName;
    }

    

    public String getNewPlaylistName() {
        return newPlaylistName;
    }


    

}
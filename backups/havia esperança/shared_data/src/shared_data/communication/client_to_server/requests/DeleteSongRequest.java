package shared_data.communication.client_to_server.requests;

import shared_data.communication.Request;
import shared_data.communication.client_to_server.businesslogic.User;

/**
 * DeleteSongRequest
 */
public class DeleteSongRequest  extends Request
{

    private User user;
    private String songName;

    public DeleteSongRequest(User user, String songName) {
        this.user = user;
        this.songName = songName;
    }

    public User getUser() {
        return user;
    }


    public String getSongName() {
        return songName;
    }


    
}
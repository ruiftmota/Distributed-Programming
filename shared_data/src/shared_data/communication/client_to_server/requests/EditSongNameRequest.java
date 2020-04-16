package shared_data.communication.client_to_server.requests;

import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.communication.Request;

/**
 * EditSongName
 */
public class EditSongNameRequest extends Request {

    private User user;
    private String songName;
    private String newName;

    public EditSongNameRequest(User user, String songName, String newName) {
        this.user = user;
        this.songName = songName;
        this.newName = newName;
    }

    public User getUser() {
        return user;
    }

    public String getSongName() {
        return songName;
    }

    public String getNewName() {
        return newName;
    }

    

}
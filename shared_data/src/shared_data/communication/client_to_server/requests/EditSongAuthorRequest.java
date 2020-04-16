package shared_data.communication.client_to_server.requests;

import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.communication.Request;

/**
 * EditSongAuthor
 */
public class EditSongAuthorRequest extends Request {

    private User user;
    private String songName;
    private String newAuthorName;

    public EditSongAuthorRequest(User user, String songName, String newAuthorName) {
        this.user = user;
        this.songName = songName;
        this.newAuthorName = newAuthorName;
    }

    public User getUser() {
        return user;
    }


    public String getSongName() {
        return songName;
    }


    public String getNewAuthorName() {
        return newAuthorName;
    }

}
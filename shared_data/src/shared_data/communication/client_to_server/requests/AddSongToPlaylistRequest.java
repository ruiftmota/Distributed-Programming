package shared_data.communication.client_to_server.requests;


import shared_data.communication.Request;
import shared_data.communication.client_to_server.businesslogic.User;

/**
 * AddSongsToPlaylistRequest
 */
public class AddSongToPlaylistRequest extends Request
{

    private User user;
    private String playlistName;
    private String songToAdd;

    public AddSongToPlaylistRequest(User user, String playlistName, String songToAdd) {
        this.user = user;
        this.playlistName = playlistName;
        this.songToAdd = songToAdd;
    }

    public User getUser() {
        return user;
    }

    public String getPlaylistName() {
        return playlistName;
    }


    public String getSongToAdd() {
        return songToAdd;
    }


    
    
}
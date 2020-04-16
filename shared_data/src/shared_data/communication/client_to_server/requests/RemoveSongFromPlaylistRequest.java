package shared_data.communication.client_to_server.requests;


import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.communication.Request;

/**
 * RemoveSongsFromPlaylistRequest
 */
public class RemoveSongFromPlaylistRequest extends Request
{

    private User user;
    private String playlistName;
    private String songToRemove;

    public RemoveSongFromPlaylistRequest(User user, String playlistName, String songToRemove) {
        this.user = user;
        this.playlistName = playlistName;
        this.songToRemove = songToRemove;
    }

    public User getUser() {
        return user;
    }


    public String getPlaylistName() {
        return playlistName;
    }


    public String getSongToRemove() {
        return songToRemove;
    }


    
}
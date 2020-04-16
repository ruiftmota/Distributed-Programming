package shared_data.communication.client_to_server.requests;

import shared_data.communication.client_to_server.businesslogic.PlaylistFilter;
import shared_data.communication.Request;

/**
 * ListPlaylistsRequest
 */
public class ListPlaylistsRequest extends Request {

    private PlaylistFilter playlistFilter;

    public ListPlaylistsRequest(PlaylistFilter playlistFilter) {
        this.playlistFilter = playlistFilter;
    }

    public PlaylistFilter getPlaylistFilter() {
        return playlistFilter;
    }

    

}
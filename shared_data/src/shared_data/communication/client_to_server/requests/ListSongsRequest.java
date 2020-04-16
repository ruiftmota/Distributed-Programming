package shared_data.communication.client_to_server.requests;

import shared_data.communication.client_to_server.businesslogic.SongFilter;
import shared_data.communication.Request;

/**
 * ListSongsRequest
 */
public class ListSongsRequest extends Request
{

    private SongFilter songFilter;

    public ListSongsRequest(SongFilter songFilter) {
        this.songFilter = songFilter;
    }

    public SongFilter getSongFilter() {
        return songFilter;
    }

    
    
}
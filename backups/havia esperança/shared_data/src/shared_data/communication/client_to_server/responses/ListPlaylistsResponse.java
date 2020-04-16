package shared_data.communication.client_to_server.responses;

import java.util.ArrayList;

import shared_data.communication.Response;
import shared_data.communication.client_to_server.businesslogic.Playlist;

/**
 * ListPlaylistsResponse
 */
public class ListPlaylistsResponse extends Response
{

    private ArrayList<Playlist> playlists;

    public ListPlaylistsResponse(ArrayList<Playlist> playlists)
    {
        this.playlists = playlists;
    }

    public ArrayList<Playlist> getPlaylists()
    {
        return playlists;
    }

}
package shared_data.communication.client_to_server.responses;

import java.util.ArrayList;

import shared_data.communication.Response;
import shared_data.communication.client_to_server.businesslogic.Song;

/**
 * ListSongsResponse
 */
public class ListSongsResponse extends Response
{

    private ArrayList<Song> songs;

    public ListSongsResponse(ArrayList<Song> songs)
    {
        this.songs = songs;
    }
    
    public ArrayList<Song> getSongs()
    {
        return songs;
    }
}
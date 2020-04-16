package shared_data.communication.client_to_server.businesslogic;

import java.util.ArrayList;

/**
 * Playlist
 */
public class Playlist
{

    private String name;
    private ArrayList<Song> songList;

    public Playlist(String name, ArrayList<Song> songList) {
        this.name = name;
        this.songList = songList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

}
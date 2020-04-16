package shared_data.communication.client_to_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import shared_data.communication.client_to_server.businesslogic.Playlist;
import shared_data.communication.client_to_server.businesslogic.PlaylistFilter;
import shared_data.communication.client_to_server.businesslogic.Song;
import shared_data.communication.client_to_server.businesslogic.SongFilter;
import shared_data.communication.client_to_server.businesslogic.User;


public interface ClientToServerInterface
{
    EnumActionResult register(User user);
    EnumActionResult login(User user);

    EnumActionResult uploadSong(User user, String songName, String author, int year, String album, float duration, String category, String fileName);
    EnumActionResult downloadSong(String songName);

    default void writeFileChunck(String fileName, byte[] bytesToWrite, int length) throws IOException
    {
        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);

        fileOutputStream.write(bytesToWrite, 0, length);

        fileOutputStream.close();
    }
    
    default byte[] readFileChunk(String fileName, int offset, int length) throws IOException
    {
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);

        int available = fileInputStream.available();
        int nbytes = 0;
        byte[] bytesToRead;
        if(available < offset + length)
        {
            nbytes = available - offset;
        }
        else
        {
            nbytes = length;
        }

        bytesToRead = new byte[nbytes];

        fileInputStream.read(bytesToRead, offset, nbytes);

        fileInputStream.close();

        return bytesToRead;
    }

    EnumActionResult editSongName(User user, String songName, String newName);
    EnumActionResult editSongAuthor(User user, String songName, String newAuthorName);

    EnumActionResult deleteSong(User user, String songName);

    EnumActionResult createPlaylist(User user, String name);

    EnumActionResult editPlaylistName(User user, String playlistName, String newName);
    EnumActionResult addSongToPlaylist(User user, String playlistName, String songToAdd);
    EnumActionResult removeSongFromPlaylist(User user, String playlistName, String songToRemove);

    EnumActionResult removePlaylist(User user, String playlistName);

    EnumActionResult downloadPlaylist(String playlistName);

    ArrayList<Playlist> listPlaylists(PlaylistFilter ylistFilter);
    ArrayList<Song> listSongs(SongFilter filter);

    void leaveSystem();
}
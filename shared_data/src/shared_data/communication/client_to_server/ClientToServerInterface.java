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
    final static String MUSIC_PATH = "music_path";

    EnumActionResult register(User user);
    EnumActionResult login(User user);

    EnumActionResult uploadSong(User user, String songName, String author, int year, String album, float duration, String category, String fileName);
    String downloadSong(String songName);

    default void writeFileChunck(String fileName, byte[] bytesToWrite, int length) throws IOException
    {
        File folder = new File("music_path");
        File file = new File(folder, fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);

        fileOutputStream.write(bytesToWrite, 0, length);

        fileOutputStream.close();
    }
    
    default byte[] readFileChunk(String fileName, int offset, int length) throws IOException
    {
        File folder = new File("music_path");
        File file = new File(folder, fileName);
        FileInputStream file_input_stream = new FileInputStream(file);

        file_input_stream.skip((long)offset);

        int available_bytes = file_input_stream.available();

        int bytes_to_read;

        if(length < available_bytes)
        {
            bytes_to_read = length;
        }
        else
        {
            bytes_to_read = available_bytes;
        }

        byte[] file_bytes = new byte[bytes_to_read];

        if(file_input_stream.read(file_bytes, 0, bytes_to_read) <= 0)
        {
            file_bytes = new byte[0];
        }

        file_input_stream.close();

		return file_bytes;
    }

    EnumActionResult editSongName(User user, String songName, String newName);
    EnumActionResult editSongAuthor(User user, String songName, String newAuthorName);

    EnumActionResult deleteSong(User user, String songName);

    EnumActionResult createPlaylist(User user, String name);

    EnumActionResult editPlaylistName(User user, String playlistName, String newName);
    EnumActionResult addSongToPlaylist(User user, String playlistName, String songToAdd);
    EnumActionResult removeSongFromPlaylist(User user, String playlistName, String songToRemove);

    EnumActionResult removePlaylist(User user, String playlistName);

    ArrayList<String> downloadPlaylist(String playlistName);

    ArrayList<Playlist> listPlaylists(PlaylistFilter ylistFilter);
    ArrayList<Song> listSongs(SongFilter filter);

    void leaveSystem();
}
package server.logic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.EnumActionResult;
import shared_data.communication.client_to_server.businesslogic.Playlist;
import shared_data.communication.client_to_server.businesslogic.PlaylistFilter;
import shared_data.communication.client_to_server.businesslogic.Song;
import shared_data.communication.client_to_server.businesslogic.SongFilter;
import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.utilities.SynchronizedPrint;

/**
 * ServerLogic
 */
public class BusinessLogic implements ClientToServerInterface
{

    private InetAddress ipAddressDataBase;
    private int portNumberDataBase;

    public BusinessLogic(String ipAddressDataBase, int portNumberDataBase) throws UnknownHostException
    {
        this.ipAddressDataBase = InetAddress.getByName(ipAddressDataBase);
        this.portNumberDataBase = portNumberDataBase;

        configureDatabase();
    }

    private void configureDatabase()
    {
        
    }


    @Override
    public EnumActionResult register(User user)
    {
        SynchronizedPrint.printLine(EnumActionResult.REGISTER_SUCCESSFULL);

        return EnumActionResult.REGISTER_SUCCESSFULL;
    }

    @Override
    public EnumActionResult login(User user)
    {
        SynchronizedPrint.printLine(EnumActionResult.LOGIN_SUCCESS);

        return EnumActionResult.LOGIN_SUCCESS;
    }
    

    @Override
    public EnumActionResult uploadSong(User user, String songName, String author, int year, String album, float duration, String category, String fileName)
    {
        SynchronizedPrint.printLine(EnumActionResult.UPLOAD_SONG_SUCCESSFULL);

        return EnumActionResult.UPLOAD_SONG_SUCCESSFULL;
    }
    
    @Override
    public EnumActionResult downloadSong(String songName)
    {
        SynchronizedPrint.printLine(EnumActionResult.DOWNLOAD_SONG_SUCCESSFULL);

        return EnumActionResult.DOWNLOAD_SONG_SUCCESSFULL;
    }
    

    @Override
    public EnumActionResult editSongName(User user, String songName, String newName)
    {
        SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_NAME_SUCCESSFULL);

        return EnumActionResult.EDIT_SONG_NAME_SUCCESSFULL;
    }
    
    @Override
    public EnumActionResult editSongAuthor(User user, String songName, String newAuthorName)
    {
        SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_AUTHOR_SUCCESSFULL);

        return EnumActionResult.EDIT_SONG_AUTHOR_SUCCESSFULL;
    }
    

    @Override
    public EnumActionResult deleteSong(User user, String songName)
    {
        SynchronizedPrint.printLine(EnumActionResult.DELETE_SONG_NAME_SUCCESSFULL);

        return EnumActionResult.DELETE_SONG_NAME_SUCCESSFULL;
    }
    

    @Override
    public EnumActionResult createPlaylist(User user, String name)
    {
        SynchronizedPrint.printLine(EnumActionResult.CREATE_PLAYLIST_SUCCESSFULL);

        return EnumActionResult.CREATE_PLAYLIST_SUCCESSFULL;
    }
    

    @Override
    public EnumActionResult editPlaylistName(User user, String playlistName, String newName)
    {
        SynchronizedPrint.printLine(EnumActionResult.EDIT_PLAYLIST_NAME_SUCCESSFULL);

        return EnumActionResult.EDIT_PLAYLIST_NAME_SUCCESSFULL;
    }
    
    @Override
    public EnumActionResult addSongToPlaylist(User user, String playlistName, String songToAdd)
    {
        SynchronizedPrint.printLine(EnumActionResult.EDIT_PLAYLIST_NAME_SUCCESSFULL);


        return EnumActionResult.EDIT_PLAYLIST_NAME_SUCCESSFULL;
    }
    
    @Override
    public EnumActionResult removeSongFromPlaylist(User user, String playlistName, String songToRemove)
    {
        SynchronizedPrint.printLine(EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_SUCCESSFULL);

        return EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_SUCCESSFULL;
    }
    

    @Override
    public EnumActionResult removePlaylist(User user, String playlistName)
    {
        SynchronizedPrint.printLine(EnumActionResult.REMOVE_PLAYLIST_SUCCESSFULL);

        return EnumActionResult.REMOVE_PLAYLIST_SUCCESSFULL;
    }
    

    @Override
    public EnumActionResult downloadPlaylist(String playlistName)
    {
        SynchronizedPrint.printLine(EnumActionResult.DOWNLOAD_PLAYLIST_SUCCESSFULL);

        return EnumActionResult.DOWNLOAD_PLAYLIST_SUCCESSFULL;
    }
    

    @Override
    public ArrayList<Playlist> listPlaylists(PlaylistFilter ylistFilter)
    {
        SynchronizedPrint.printLine("List playlists");

        return new ArrayList<Playlist>();
    }
    
    @Override
    public ArrayList<Song> listSongs(SongFilter filter)
    {
        SynchronizedPrint.printLine("List Songs");

        return new ArrayList<Song>();
    }
    
    @Override
    public void leaveSystem()
    {
        SynchronizedPrint.printLine("Leavae System");
    }
}
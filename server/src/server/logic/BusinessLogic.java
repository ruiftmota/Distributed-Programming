package server.logic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.EnumActionResult;
import shared_data.communication.client_to_server.businesslogic.Playlist;
import shared_data.communication.client_to_server.businesslogic.PlaylistFilter;
import shared_data.communication.client_to_server.businesslogic.Song;
import shared_data.communication.client_to_server.businesslogic.SongFilter;
import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;

/**
 * ServerLogic
 */
public class BusinessLogic implements ClientToServerInterface
{

    private InetAddress ipAddressDataBase;
    private int portNumberDataBase;
    private String databaseName;

    private Connection connection;

    private String db_id = "root";
    private String db_pass = "admin";

    public BusinessLogic(String ipAddressDataBase, int portNumberDataBase, String databaseName) throws UnknownHostException
    {
        this.ipAddressDataBase = InetAddress.getByName(ipAddressDataBase);
        this.portNumberDataBase = portNumberDataBase;
        this.databaseName = databaseName;

        configureDatabase();
    }

    private void configureDatabase()
    {
        try 
        {
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            String conn = "jdbc:mysql:/" + ipAddressDataBase + ":" + portNumberDataBase + "/" + databaseName + "?useTimezone=true&serverTimezone=UTC";

            System.out.println(conn);
            
            connection = DriverManager.getConnection(
                                                    conn, 
                                                    db_id, 
                                                    db_pass);

            SynchronizedPrint.printLine("Database connection successful.");

        } 
        catch ( ClassNotFoundException | SQLException e) 
        {
            KeepAlive.emergencyExit(e);
        }
    }


    


    @Override
    public EnumActionResult register(User user)
    {

        try {
            int successful = 0;
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM User WHERE User.Username='" + user.getUsername() + "';";

            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                query = "INSERT INTO User" + "(Username, Name, Password)" + 
                "VALUES ('" + user.getUsername() + "','" + user.getName() + "','" + user.getPassword() + "');";
                successful = statement.executeUpdate(query);
            }

            
            
            statement.close();

            if(successful != 0)
            {
                SynchronizedPrint.printLine(EnumActionResult.REGISTER_SUCCESSFULL);
                return EnumActionResult.REGISTER_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }
        
        SynchronizedPrint.printLine(EnumActionResult.REGISTER_REFUSED);
        return EnumActionResult.REGISTER_REFUSED;
    }

    @Override
    public EnumActionResult login(User user)
    {
        SynchronizedPrint.printLine(EnumActionResult.LOGIN_SUCCESSFULL);

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM User WHERE User.Username='" + user.getUsername() + "';";

            ResultSet resultSet = statement.executeQuery(query);

            boolean result = resultSet.next();

            statement.close();

            if(result == true)
            {
                SynchronizedPrint.printLine(EnumActionResult.LOGIN_SUCCESSFULL);
                return EnumActionResult.LOGIN_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }

        SynchronizedPrint.printLine(EnumActionResult.LOGIN_REFUSED);
        return EnumActionResult.LOGIN_REFUSED;
    }
    

    @Override
    public EnumActionResult uploadSong(User user, String songName, String author, int year, String album, float duration, String category, String fileName)
    {
        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Song WHERE Song.Name='" + songName + "';";
    
            ResultSet resultSet = statement.executeQuery(query);
    
            if(resultSet.next() == true)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.UPLOAD_SONG_ALREADY_EXISTS);
                return EnumActionResult.UPLOAD_SONG_ALREADY_EXISTS; 
            }
            else
            {
                query = "INSERT INTO Song" + "(Name, Author, Path, Duration, Year, Category, Album, User_Username)" + 
                    "VALUES ('" + songName+ "','" + author + "','" + fileName + "'," +  duration + "," + year + ",'" + category + 
                    "','" + album + "','" + user.getUsername()+ "');";
            }


            int successful = statement.executeUpdate(query);
            System.out.println("here");

            statement.close();

            if(successful != 0)
            {
                SynchronizedPrint.printLine(EnumActionResult.UPLOAD_SONG_SUCCESSFULL);
                return EnumActionResult.UPLOAD_SONG_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }
        
        SynchronizedPrint.printLine(EnumActionResult.UPLOAD_NOT_SUCCESSFULL);
        return EnumActionResult.UPLOAD_NOT_SUCCESSFULL;
    }


    
    @Override
    public String downloadSong(String songName)
    {
        String path = null;
        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Song WHERE Song.Name='" + songName + "';";

            ResultSet resultSet = statement.executeQuery(query);

            boolean result = resultSet.next();
            if(result == true)
            {
                path = resultSet.getString("Path");
            }

            statement.close();

            if(result == true)
            {
                SynchronizedPrint.printLine(EnumActionResult.DOWNLOAD_SONG_SUCCESSFULL);
                return path; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }

        SynchronizedPrint.printLine(EnumActionResult.DOWNLOAD_SONG_FAILED);
        return path;

    }
    



    @Override
    public EnumActionResult editSongName(User user, String songName, String newName)
    {

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Song WHERE Song.Name='" + newName + "';";

            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == true)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_NAME_FAILED);
                return EnumActionResult.EDIT_SONG_NAME_FAILED; 
            }

            query = "SELECT * FROM Song WHERE Song.Name='" + songName + "';";
            resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_NAME_FAILED);
                return EnumActionResult.EDIT_SONG_NAME_FAILED; 
            }
            else
            {
                query = "UPDATE Song SET Name='" + newName + "' WHERE Name='" + songName + "';";
            }

            int successful = statement.executeUpdate(query);
            
            statement.close();

            if(successful != 0)
            {
                SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_NAME_SUCCESSFULL);
                return EnumActionResult.EDIT_SONG_NAME_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }
        
        SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_NAME_FAILED);
        return EnumActionResult.EDIT_SONG_NAME_FAILED;
        
    }

    
    
    @Override
    public EnumActionResult editSongAuthor(User user, String songName, String newAuthorName)
    {

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Song WHERE Song.Name='" + newAuthorName + "';";

            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == true)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_NAME_FAILED);
                return EnumActionResult.EDIT_SONG_NAME_FAILED; 
            }

            query = "SELECT * FROM Song WHERE Song.Name='" + songName + "';";
            resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_AUTHOR_FAILED);
                return EnumActionResult.EDIT_SONG_AUTHOR_FAILED; 
            }
            else
            {
                query = "UPDATE Song SET Author='" + newAuthorName + "' WHERE Name='" + songName + "';";
            }

            int successful = statement.executeUpdate(query);
            
            statement.close();

            if(successful != 0)
            {
                SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_AUTHOR_SUCCESSFULL);
                return EnumActionResult.EDIT_SONG_AUTHOR_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }
        
        SynchronizedPrint.printLine(EnumActionResult.EDIT_SONG_AUTHOR_FAILED);
        return EnumActionResult.EDIT_SONG_AUTHOR_FAILED;
        
    }


    
    

    @Override
    public EnumActionResult deleteSong(User user, String songName)
    {

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM User WHERE User.Username='" + user.getUsername() + "';";
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.DELETE_SONG_NAME_FAILED);
                return EnumActionResult.DELETE_SONG_NAME_FAILED;
            }
            if(!(resultSet.getString("Password").equals(user.getPassword()) && resultSet.getString("Username").equals(user.getUsername())))
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.DELETE_SONG_NAME_FAILED);
                return EnumActionResult.DELETE_SONG_NAME_FAILED;
            }

            query = "SELECT * FROM Song WHERE Song.Name='" + songName + "' AND Song.User_Username='" + user.getUsername() + "';";
            resultSet = statement.executeQuery(query);


            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.DELETE_SONG_NAME_FAILED);
                return EnumActionResult.DELETE_SONG_NAME_FAILED;
            }

            query = "DELETE FROM Song WHERE Song.Name='" + songName + "';";            
            int result = statement.executeUpdate(query);

            if(result == 0)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.DELETE_SONG_NAME_FAILED);
                return EnumActionResult.DELETE_SONG_NAME_FAILED;
            }


            statement.close();


            SynchronizedPrint.printLine(EnumActionResult.DELETE_SONG_NAME_SUCCESSFULL);
            return EnumActionResult.DELETE_SONG_NAME_SUCCESSFULL; 

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }

        SynchronizedPrint.printLine(EnumActionResult.DELETE_SONG_NAME_FAILED);
        return EnumActionResult.DELETE_SONG_NAME_FAILED;

    }
    

    




    @Override
    public EnumActionResult createPlaylist(User user, String name)
    {

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Playlist WHERE Playlist.Name='" + name + "';";

            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == true)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.CREATE_PLAYLIST_ALREADY_EXISTS);
                return EnumActionResult.CREATE_PLAYLIST_ALREADY_EXISTS; 
            }
            else
            {
                query = "INSERT INTO Playlist" + "(Name, User_Username)" + 
                    "VALUES ('" + name + "','" + user.getUsername() + "');";
            }

            int successful = statement.executeUpdate(query);
            
            statement.close();

            if(successful != 0)
            {
                SynchronizedPrint.printLine(EnumActionResult.CREATE_PLAYLIST_SUCCESSFULL);
                return EnumActionResult.CREATE_PLAYLIST_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }
        
        SynchronizedPrint.printLine(EnumActionResult.CREATE_PLAYLIST_FAILED);
        return EnumActionResult.CREATE_PLAYLIST_FAILED;
    }
    
    


    @Override
    public EnumActionResult editPlaylistName(User user, String playlistName, String newName)
    {

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Playlist WHERE Playlist.Name='" + playlistName + "';";

            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.EDIT_PLAYLIST_NAME_FAILED);
                return EnumActionResult.EDIT_PLAYLIST_NAME_FAILED; 
            }
            else
            {
                query = "UPDATE Playlist SET Name='" + newName + "' WHERE Name='" + playlistName + "';";
            }

            int successful = statement.executeUpdate(query);
            
            statement.close();

            if(successful != 0)
            {
                SynchronizedPrint.printLine(EnumActionResult.EDIT_PLAYLIST_NAME_SUCCESSFULL);
                return EnumActionResult.EDIT_PLAYLIST_NAME_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }
        
        SynchronizedPrint.printLine(EnumActionResult.EDIT_PLAYLIST_NAME_FAILED);
        return EnumActionResult.EDIT_PLAYLIST_NAME_FAILED;

    }
    
    

    @Override
    public EnumActionResult addSongToPlaylist(User user, String playlistName, String songToAdd)
    {

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Playlist WHERE Playlist.Name='" + playlistName + "';";

            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED);
                return EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED; 
            }


            query = "SELECT * FROM Song WHERE Song.Name='" + songToAdd + "';";

            resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED);
                return EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED; 
            }

            query = "SELECT * FROM Playlist_has_song WHERE Playlist_has_song.Playlist_Name='" + playlistName + "';";
            resultSet = statement.executeQuery(query);

            if(resultSet.next() == true)
            {
                if(resultSet.getString("Playlist_Name").equals(playlistName) &&
                    resultSet.getString("Song_Name").equals(songToAdd))
                {  
                    statement.close();
                    SynchronizedPrint.printLine(EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED);
                    return EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED;
                } 
            }


            query = "INSERT INTO Playlist_has_Song" + " (Playlist_Name, Song_Name)" + 
                "VALUES ('" + playlistName + "','" + songToAdd + "');";
        

            int successful = statement.executeUpdate(query);
            
            statement.close();

            if(successful != 0)
            {
                SynchronizedPrint.printLine(EnumActionResult.ADD_SONG_TO_PLAYLIST_SUCCESSFULL);
                return EnumActionResult.ADD_SONG_TO_PLAYLIST_SUCCESSFULL; 
            }

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }
        
        SynchronizedPrint.printLine(EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED);
        return EnumActionResult.ADD_SONG_TO_PLAYLIST_FAILED;

    }
    
    



    @Override
    public EnumActionResult removeSongFromPlaylist(User user, String playlistName, String songToRemove)
    {

        try
        {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM User WHERE User.Username='" + user.getUsername() + "';";
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED;
            }
            if(!(resultSet.getString("Password").equals(user.getPassword()) && resultSet.getString("Username").equals(user.getUsername())))
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED;
            }

            query = "SELECT * FROM Playlist WHERE Playlist.Name='" + playlistName + "' AND Playlist.User_Username='" + user.getUsername() + "';";
            resultSet = statement.executeQuery(query);


            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED;
            }

            query = "SELECT * FROM Playlist_has_song WHERE Playlist_has_song.Playlist_Name='" + playlistName + "';";
            resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED;
            }

            query = "DELETE FROM Playlist_has_song WHERE Playlist_has_song.Playlist_Name='" + playlistName + "';";            
            int result = statement.executeUpdate(query);
            
            statement.close();

            if(result !=0)
            {
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_SUCCESSFULL);
                return EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_SUCCESSFULL;
            }
        

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }

        SynchronizedPrint.printLine(EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED);
        return EnumActionResult.REMOVE_SONG_FROM_PLAYLIST_FAILED;
    }
    
    


    


    @Override
    public EnumActionResult removePlaylist(User user, String playlistName)
    {
        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM User WHERE User.Username='" + user.getUsername() + "';";
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_PLAYLIST_FAILED;
            }
            if(!(resultSet.getString("Password").equals(user.getPassword()) && resultSet.getString("Username").equals(user.getUsername())))
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_PLAYLIST_FAILED;
            }

            query = "SELECT * FROM Playlist WHERE Playlist.Name='" + playlistName + "' AND Playlist.User_Username='" + user.getUsername() + "';";
            resultSet = statement.executeQuery(query);


            if(resultSet.next() == false)
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_PLAYLIST_FAILED;
            }

            query = "DELETE FROM Playlist WHERE Playlist.Name='" + playlistName + "';";            
            int result = statement.executeUpdate(query);

            if(result == 0 )
            {
                statement.close();
                SynchronizedPrint.printLine(EnumActionResult.REMOVE_PLAYLIST_FAILED);
                return EnumActionResult.REMOVE_PLAYLIST_FAILED;
            }


            statement.close();

            
            SynchronizedPrint.printLine(EnumActionResult.REMOVE_PLAYLIST_SUCCESSFULL);
            return EnumActionResult.REMOVE_PLAYLIST_SUCCESSFULL; 
        

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }

        SynchronizedPrint.printLine(EnumActionResult.REMOVE_PLAYLIST_FAILED);
        return EnumActionResult.REMOVE_PLAYLIST_FAILED;
    }
    
    


    @Override
    public ArrayList<String> downloadPlaylist(String playlistName)
    {
        ArrayList<String> songListPaths = new ArrayList<String>();

        try {

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Playlist WHERE Playlist.Name='" + playlistName + "';";
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next() == false)
            {
                SynchronizedPrint.printLine(EnumActionResult.DOWNLOAD_PLAYLIST_FAILED);
                return new ArrayList<String>();
            }


            query = "SELECT * FROM Playlist_has_Song WHERE PlayList_has_song.Playlist_Name='" + playlistName + "';";

            resultSet = statement.executeQuery(query);
            ResultSet resultTemp;

            while(resultSet.next())
            {
                query = "SELECT * FROM Song WHERE Song.Name='" + resultSet.getString("Playlist_has_Song.Song_Name") + "';";

                resultTemp = statement.executeQuery(query);

                resultTemp.next();
                resultTemp.getString("Path");
                songListPaths.add(resultTemp.getString("Path"));
            }

            statement.close();

            SynchronizedPrint.printLine(EnumActionResult.DOWNLOAD_PLAYLIST_SUCCESSFULL);
            return songListPaths; 
            

        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }

        SynchronizedPrint.printLine(EnumActionResult.DOWNLOAD_PLAYLIST_FAILED);
        return new ArrayList<String>();


    }
    

    

    @Override
    public ArrayList<Playlist> listPlaylists(PlaylistFilter ylistFilter)
    {
        String query;
        
        ArrayList<Playlist> playlistList = new ArrayList<Playlist>();

        ArrayList<Song> songList = new ArrayList<Song>();
        ArrayList<String> playlistNames = new ArrayList<String>();

        try {
            Statement statement = connection.createStatement();
            
            query = "SELECT * FROM playlist;";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next())
            {
                String name = resultSet.getString("Name");
                playlistNames.add(name);
            }

            for(String playlistName : playlistNames)
            {
                query = "SELECT * " +
                    "FROM Song,Playlist_has_Song " + 
                    "WHERE Playlist_has_Song.Playlist_Name='" + playlistName + "';";

                
                resultSet = statement.executeQuery(query);

                while(resultSet.next())
                {
                    String name = resultSet.getString("Song.Name");
                    String author = resultSet.getString("Song.Author");
                    String path = resultSet.getString("Song.Path");
                    float duration = resultSet.getFloat("Song.Duration");
                    int year = resultSet.getInt("Song.Year");
                    String category = resultSet.getString("Song.Category");
                    String album = resultSet.getString("Song.Album");

                    Song songTemp = new Song(name, author, path, duration, year, category, album);
                    
                    songList.add(songTemp);
                }

                Playlist playlistTemp = new Playlist(playlistName, songList);

                playlistList.add(playlistTemp);
            }
            
            statement.close();

        } 
        catch (SQLException e) 
        {
            KeepAlive.emergencyExit(e);
        }


        SynchronizedPrint.printLine("List playlists");

        return new ArrayList<Playlist>();
    }


    

    
    @Override
    public ArrayList<Song> listSongs(SongFilter filter)
    {
        
        ArrayList<Song> songList = new ArrayList<Song>();

        try {

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Song;");

            while(resultSet.next())
            {
                String name = resultSet.getString("Name");
                String author = resultSet.getString("Author");
                String path = resultSet.getString("Path");
                float duration = resultSet.getFloat("Duration");
                int year = resultSet.getInt("Year");
                String category = resultSet.getString("Category");
                String album = resultSet.getString("Album");

                Song songTemp = new Song(name, author, path, duration, year, category, album);
                
                songList.add(songTemp);
            }
            
            statement.close();
        } catch (SQLException e) {
            KeepAlive.emergencyExit(e);
        }


        SynchronizedPrint.printLine("List Songs");

        return songList;
    }





    
    @Override
    public void leaveSystem()
    {
        SynchronizedPrint.printLine("Leave System");
    }



    public static void main(String[] args)
    {
        /* try {
            BusinessLogic businessLogic = new BusinessLogic("127.0.0.1", 3306);

            
            User user = new User("username", "password", "name");
            User user2 = new User("usernamae1", "passwor2d", "name2");
            

            businessLogic.uploadSong( user, "a",  "a", 1,  "a",  1,  "a", "a");
            businessLogic.downloadSong("a");

            businessLogic.createPlaylist(user, "c");
            
            businessLogic.downloadPlaylist("c");
            businessLogic.listSongs(new SongFilter());



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } */
    }

    
}
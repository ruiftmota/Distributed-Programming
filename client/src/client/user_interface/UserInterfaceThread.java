package client.user_interface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.EnumActionResult;
import shared_data.communication.client_to_server.businesslogic.Playlist;
import shared_data.communication.client_to_server.businesslogic.PlaylistFilter;
import shared_data.communication.client_to_server.businesslogic.Song;
import shared_data.communication.client_to_server.businesslogic.SongFilter;
import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SynchronizedPrint;

public class UserInterfaceThread extends Thread
{
    private final static String MUSIC_PATH = "music_path";

    private ClientToServerInterface clientToServerInterface;

    private User loggedUser = null;

    private Scanner scanner;

    private EnumPane actualPane = EnumPane.NON_AUTHENTICATED_PANE;

    public UserInterfaceThread(ClientToServerInterface clientToServerInterface)
    {
        this.clientToServerInterface = clientToServerInterface;
        this.scanner = new Scanner(System.in);
        
    }

    public static void main(String[] args) throws Exception
    {
        UserInterfaceThread userInterfaceThread = new UserInterfaceThread(null);
        userInterfaceThread.start();
        userInterfaceThread.join();
    }

    @Override
    public void run()
    {
        while(KeepAlive.getKeepAlive() == true)
        {
            clearScreen();

            switch(actualPane)
            {
                case NON_AUTHENTICATED_PANE:
                    runNonAuthenticatedPane();
                    break;

                case AUTHENTICATED_PANE:
                    runAuthenticatedPane();
                    break;

                case LOGIN_PANE:
                    runLoginPane();
                    break;

                case REGISTER_PANE:
                    runRegisterPane();
                    break;

                case UPLOAD_SONG_PANE:
                    runUploadSongPane();
                    break;

                case DOWNLOAD_SONG_PANE:
                    runDownloadSongPane();
                    break;

                case EDIT_SONG_NAME_PANE:
                    runEditSongNamePane();
                    break;

                case EDIT_SONG_AUTHOR_PANE:
                    runEditSongAuthorName();
                    break;

                case DELETE_SONG_PANE:
                    runDeleteSongPane();
                    break;

                case CREATE_PLAYLIST_PANE:
                    runCreatePlayListPane();
                    break;

                case EDIT_PLAYLIST_NAME_PANE:
                    runEditPlaylistNamePane();
                    break;

                case ADD_SONG_TO_PLAYLIST_PANE:
                    runAddSongToPlaylistPane();
                    break;

                case DOWNLOAD_PLAYLIST_PANE:
                    runDownloadPlaylistPane();
                    break;

                case LIST_PLAYLISTS_PANE:
                    runListPlaylistsPane();
                    break;

                case LIST_SONGS_PANE:
                    runListSongPane();
                    break;

                case REMOVE_PLAYLIST_PANE:
                    runRemovePlaylistPane();
                    break;

                case REMOVE_SONG_FROM_PLAYLIST_PANE:
                    runRemoveSongFromPlaylistPane();
                    break;

                case LIST_DOWNLOADED_SONGS_PANE:
                    runListDownloadedSongsPane();
                    break;
            }
        }

        scanner.close();
    }

    private void clearScreen()
    {
        
    }

    private void runNonAuthenticatedPane()
    {
        SynchronizedPrint.printLine("Available options:\n\n" +
                                    "1. Login" + "\n" +
                                    "2. Register" + "\n" +
                                    "0. Exit" + "\n");

        int option = -1;

        do
        {
            option = getOption();
            switch(option)
            {
                case 1:
                    actualPane = EnumPane.LOGIN_PANE;
                    break;

                case 2:
                    actualPane = EnumPane.REGISTER_PANE;
                    break;

                case 0:
                    KeepAlive.setKeepAlive(false);
                    break;

                default:
                    SynchronizedPrint.printLine("Invalid input. Try again!");
                    break;
            }
        } while(option == -1);
    }

    private void runAuthenticatedPane()
    {
        SynchronizedPrint.printLine("Available options:\n\n" +
                                    "1. Upload Song" + "\n" +
                                    "2. Download Song" + "\n" +
                                    "3. Edit Song Name" + "\n" +
                                    "4. Edit Song Author" + "\n" +
                                    "5. Delete Song" + "\n" +
                                    "6. Create Playlist" + "\n" +
                                    "7. Edit Playlist Name" + "\n" +
                                    "8. Add Song To Playlist" + "\n" +
                                    "9. Remove Song From Playlist" + "\n" +
                                    "10. Remove Playlist" + "\n" +
                                    "11. Download Playlist" + "\n" +
                                    "12. List Playlists" + "\n" +
                                    "13. List Songs" + "\n" +
                                    "14. List Downloaded Songs" + "\n" +
                                    "0. Exit" + "\n");

        int option = -1;

        do
        {
            option = getOption();
            switch(option)
            {
                case 1:
                    actualPane = EnumPane.UPLOAD_SONG_PANE;
                    break;

                case 2:
                    actualPane = EnumPane.DOWNLOAD_SONG_PANE;
                    break;

                case 3:
                    actualPane = EnumPane.EDIT_SONG_NAME_PANE;
                    break;

                case 4:
                    actualPane = EnumPane.EDIT_SONG_AUTHOR_PANE;
                    break;

                case 5:
                    actualPane = EnumPane.DELETE_SONG_PANE;
                    break;

                case 6:
                    actualPane = EnumPane.CREATE_PLAYLIST_PANE;
                    break;

                case 7:
                    actualPane = EnumPane.EDIT_PLAYLIST_NAME_PANE;
                    break;

                case 8:
                    actualPane = EnumPane.ADD_SONG_TO_PLAYLIST_PANE;
                    break;

                case 9:
                    actualPane = EnumPane.REMOVE_SONG_FROM_PLAYLIST_PANE;
                    break;

                case 10:
                    actualPane = EnumPane.REMOVE_PLAYLIST_PANE;
                    break;

                case 11:
                    actualPane = EnumPane.DOWNLOAD_PLAYLIST_PANE;
                    break;

                case 12:
                    actualPane = EnumPane.LIST_PLAYLISTS_PANE;
                    break;

                case 13:
                    actualPane = EnumPane.LIST_SONGS_PANE;
                    break;

                case 14:
                    actualPane = EnumPane.LIST_DOWNLOADED_SONGS_PANE;
                    break;

                case 0:
                    KeepAlive.setKeepAlive(false);
                    break;

                default:
                    SynchronizedPrint.printLine("Invalid input. Try again!");
                    break;
            }
        } while(option == -1);
    }

    private void runLoginPane()
    {
        SynchronizedPrint.printLine("Enter your login data in the follwing format: <username> <password>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.LOGIN_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.LOGIN_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.login(new User(parameters[0], parameters[1]));

        SynchronizedPrint.printLine(response);

        if(response == EnumActionResult.LOGIN_REFUSED)
        {
            actualPane = EnumPane.NON_AUTHENTICATED_PANE;
        }
        else
        {
            actualPane = EnumPane.AUTHENTICATED_PANE;
            loggedUser = new User(parameters[0], parameters[1]);
        }

    }

    private void runRegisterPane()
    {
        SynchronizedPrint.printLine("Enter your register data in the follwing format: <name> <username> <password>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.REGISTER_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.REGISTER_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.register(new User(parameters[0], parameters[1], parameters[2]));

        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.NON_AUTHENTICATED_PANE;
    }

    private void runUploadSongPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <song name> <author name> <year> <album> <duration> <category> <fileName>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.UPLOAD_SONG_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.UPLOAD_SONG_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response;

        try
        {
            response = this.clientToServerInterface.uploadSong(loggedUser, parameters[0], parameters[1], Integer.parseInt(parameters[2]), parameters[3], Float.parseFloat(parameters[4]),
                                                                                parameters[5], parameters[6]);
        }
        catch(NumberFormatException exception)
        {
            response = EnumActionResult.UPLOAD_NOT_SUCCESSFULL;
        }

        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runDownloadSongPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <song name>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.DOWNLOAD_SONG_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.DOWNLOAD_SONG_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        String response = this.clientToServerInterface.downloadSong(parameters[0]);

        if(response.equals(EnumActionResult.DOWNLOAD_SONG_FAILED.toString()))
        {
            SynchronizedPrint.printLine("The song was not found.");
        }
        else
        {
            SynchronizedPrint.printLine("The song is being downloaded, you will be notified when it's ready");
        }

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runEditSongNamePane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <song name> <new song name");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.EDIT_SONG_NAME_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.EDIT_SONG_NAME_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.editSongName(loggedUser, parameters[0], parameters[1]);

        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runEditSongAuthorName()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <song name> <new song author");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.EDIT_SONG_AUTHOR_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.EDIT_SONG_AUTHOR_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.editSongName(loggedUser, parameters[0], parameters[1]);

        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runDeleteSongPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <song name>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.DELETE_SONG_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.DELETE_SONG_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.deleteSong(loggedUser, parameters[0]);

        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runCreatePlayListPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <playlist name>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.CREATE_PLAYLIST_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.CREATE_PLAYLIST_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.createPlaylist(loggedUser, parameters[0]);

        SynchronizedPrint.printLine(response);


        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runEditPlaylistNamePane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <playlist name> <new name>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.EDIT_PLAYLIST_NAME_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.EDIT_PLAYLIST_NAME_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.editPlaylistName(loggedUser, parameters[0], parameters[1]);

        SynchronizedPrint.printLine(response);


        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runAddSongToPlaylistPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <playlist name> <song to add>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.ADD_SONG_TO_PLAYLIST_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.ADD_SONG_TO_PLAYLIST_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.addSongToPlaylist(loggedUser, parameters[0], parameters[1]);

        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runDownloadPlaylistPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <playlist name>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.DOWNLOAD_PLAYLIST_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.DOWNLOAD_PLAYLIST_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        ArrayList<String> response = this.clientToServerInterface.downloadPlaylist(parameters[0]);

        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runListPlaylistsPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: ");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.LIST_PLAYLISTS_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.LIST_PLAYLISTS_PANE.getNumberOfParameters());

        //String[] parameters = input.split(" ");

        ArrayList<Playlist> response = this.clientToServerInterface.listPlaylists(new PlaylistFilter());

        SynchronizedPrint.printLine("List of playlists:");


        for(Playlist playlist : response)
        {
            SynchronizedPrint.printLine(playlist.getName());
        }

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runListSongPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: ");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.LIST_SONGS_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.LIST_SONGS_PANE.getNumberOfParameters());

        //String[] parameters = input.split(" ");

        ArrayList<Song> response = this.clientToServerInterface.listSongs(new SongFilter());

        SynchronizedPrint.printLine("List of songs:");


        for(Song song : response)
        {
            SynchronizedPrint.printLine(song.getName());
        }

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runRemovePlaylistPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <playlist name>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.REMOVE_PLAYLIST_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.REMOVE_PLAYLIST_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.removePlaylist(loggedUser, parameters[0]);


        SynchronizedPrint.printLine(response);

        actualPane = EnumPane.AUTHENTICATED_PANE;
    }

    private void runRemoveSongFromPlaylistPane()
    {
        SynchronizedPrint.printLine("Enter the data in the follwing format: <playlist name> <song name>");

        String input;

        do
        {
            input = scanner.nextLine();
            if(countWords(input) != EnumPane.REMOVE_SONG_FROM_PLAYLIST_PANE.getNumberOfParameters())
            {
                SynchronizedPrint.printLine("Wrong input. Try again!");
            }
        }
        while(countWords(input) != EnumPane.REMOVE_SONG_FROM_PLAYLIST_PANE.getNumberOfParameters());

        String[] parameters = input.split(" ");

        EnumActionResult response = this.clientToServerInterface.removeSongFromPlaylist(loggedUser, parameters[0], parameters[1]);


        SynchronizedPrint.printLine(response);


        actualPane = EnumPane.AUTHENTICATED_PANE;
    }


    private void runListDownloadedSongsPane()
    {
        File file = new File(MUSIC_PATH);

        String[] filesList = file.list();

        SynchronizedPrint.printLine("List of downloaded songs:");

        for(int i = 0; i < filesList.length; i++)
        {
            SynchronizedPrint.printLine((i+1) + ". " + filesList[i]);
        }

        SynchronizedPrint.printLine("Press 0 to go back.");

        int option = -1;

        do
        {
            option = getOption();
            switch(option)
            {
                case 0:
                    actualPane = EnumPane.AUTHENTICATED_PANE;
                    break;

                case -1:
                    break;

                default:
                    SynchronizedPrint.printLine("Opening file...");
                    playSong(filesList[option - 1]);
                    break;
            }
        } while(option == -1);
    }

    private int getOption()
    {
        try
        {
            System.in.skip(System.in.available());
        }
        catch(Exception exception)
        {

        }

        String input = scanner.nextLine();

        for(int i = 0; i < input.length(); i++)
        {
            if((input.charAt(i) >= '0' && input.charAt(i) <= '9') == false)
            {
                return -1;
            }
        }

        return Integer.parseInt(input);        
    }

    private int countWords(String input)
    {
        Scanner scannerWords = new Scanner(input);

        int words = 0;

        while(scannerWords.hasNext())
        {
            scannerWords.next();
            words++;
        }

        scannerWords.close();

        return words;
    }

    private void playSong(String fileName)
    {
        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", "start " + MUSIC_PATH + "/" + fileName);
            processBuilder.start();
        }
        catch(IOException exception)
        {
            KeepAlive.emergencyExit(exception);
        }
    }
}
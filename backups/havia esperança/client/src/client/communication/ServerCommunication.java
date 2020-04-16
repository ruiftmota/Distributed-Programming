package client.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import shared_data.communication.Request;
import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.EnumActionResult;
import shared_data.communication.client_to_server.businesslogic.Playlist;
import shared_data.communication.client_to_server.businesslogic.PlaylistFilter;
import shared_data.communication.client_to_server.businesslogic.Song;
import shared_data.communication.client_to_server.businesslogic.SongFilter;
import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.communication.client_to_server.requests.AddSongToPlaylistRequest;
import shared_data.communication.client_to_server.requests.CreatePlaylistRequest;
import shared_data.communication.client_to_server.requests.DeleteSongRequest;
import shared_data.communication.client_to_server.requests.DownloadPlaylistRequest;
import shared_data.communication.client_to_server.requests.DownloadSongRequest;
import shared_data.communication.client_to_server.requests.EditPlaylistNameRequest;
import shared_data.communication.client_to_server.requests.EditSongAuthorRequest;
import shared_data.communication.client_to_server.requests.EditSongNameRequest;
import shared_data.communication.client_to_server.requests.LeaveSystemRequest;
import shared_data.communication.client_to_server.requests.LoginRequest;
import shared_data.communication.client_to_server.requests.RegisterRequest;
import shared_data.communication.client_to_server.requests.RemovePlaylistRequest;
import shared_data.communication.client_to_server.requests.RemoveSongFromPlaylistRequest;
import shared_data.communication.client_to_server.requests.UploadSongRequest;
import shared_data.communication.client_to_server.responses.DownloadPlaylistsResponse;
import shared_data.communication.client_to_server.responses.DownloadSongResponse;
import shared_data.communication.client_to_server.responses.UploadSongResponse;
import shared_data.communication.client_to_server_directory.requests.EnterClientRequest;
import shared_data.communication.client_to_server_directory.responses.ChoosenServerResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;

public class ServerCommunication implements ClientToServerInterface
{
    private final int TIMEOUT = 10000;
    private final String MUSIC_PATH = "/music_path/";

    private InetAddress serverDirectoryIP;
    private int serverDirectoryPort;
    private DatagramSocket serverDirectorySocket;
    private Socket socket;
    private CallbackThread callbackThread;

    public ServerCommunication(String serverDirectoryIP, int serverDirectoryPort) throws ClassNotFoundException, IOException, ServerDirectoryDownException, UnknownHostException
    {
        this.serverDirectoryPort = serverDirectoryPort;
        this.serverDirectoryIP = InetAddress.getByName(serverDirectoryIP);

        this.serverDirectorySocket = new DatagramSocket();
        serverDirectorySocket.setSoTimeout(TIMEOUT);

        askDirectoryForServer();
    }

    private void askDirectoryForServer() throws ServerDirectoryDownException, UnknownHostException, IOException, ClassNotFoundException
    {
        EnterClientRequest enterSystemRequest = new EnterClientRequest(InetAddress.getLocalHost().getHostAddress(),
                                                                       serverDirectorySocket.getLocalPort());

        String serializedRequest = Serialization.serializeObjectToJson(enterSystemRequest);
        SendAndReceiveInformation.sendData(serializedRequest, serverDirectorySocket, serverDirectoryIP, serverDirectoryPort);
        String responseInJsonFormat = SendAndReceiveInformation.receiveData(serverDirectorySocket);
        ChoosenServerResponse response = (ChoosenServerResponse)Serialization.deserializeObjectFromJson(responseInJsonFormat);

        InetAddress ip = InetAddress.getByName(response.getServerIp());
        int port = response.getServerListeningTCPPort();

        socket = new Socket(ip, port);

        //configureCallBacks();
    }

    public void configureCallBacks() throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(0);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

        printWriter.print(serverSocket.getLocalPort());
        printWriter.flush();
        
        Socket callbackSocket = serverSocket.accept();

        callbackThread = new CallbackThread(callbackSocket);
        callbackThread.start();

        serverSocket.close();
    }

    
    private Object sendRequestToServer(Request request)
    {
        Object requestResponse = null;

        while(KeepAlive.getKeepAlive() == true)
        {
            while(KeepAlive.getKeepAlive() == true)
            {
                String requestInJson = Serialization.serializeObjectToJson(request);
                try
                {
                    SendAndReceiveInformation.sendDataTCP(requestInJson, socket);
                }
                catch(IOException exception)
                {
                    try
                    {
                        askDirectoryForServer();
                        continue;
                    }
                    catch(Exception exception2)
                    {
                        KeepAlive.emergencyExit(exception2);
                    }

                }

                break;
            }

            try
            {
                String responseInJson = SendAndReceiveInformation.receiveDataTCP(socket);
                Object response = Serialization.deserializeObjectFromJson(responseInJson);
                
                requestResponse = response;

            }
            catch(IOException exception)
            {
                try
                {
                    askDirectoryForServer();
                    continue;
                }
                catch(Exception exception2)
                {
                    KeepAlive.emergencyExit(exception2);
                }
            }
            catch(ClassNotFoundException exception)
            {
                KeepAlive.emergencyExit(exception);
            }

            break;
        }

        return requestResponse;
    }


    @Override
    public EnumActionResult register(User user)
    {
        RegisterRequest registerRequest = new RegisterRequest(user);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(registerRequest);
        return response;
    }

    @Override
    public EnumActionResult login(User user)
    {
        LoginRequest loginRequest = new LoginRequest(user);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(loginRequest);
        return response;
    }
    

    @Override
    public EnumActionResult uploadSong(User user, String songName, String author, int year, String album, float duration, String category, String fileName)
    {
        UploadSongRequest uploadSongRequest = new UploadSongRequest( user, songName, author, 
                                                                     year, album, duration,
                                                                     category, fileName );

        UploadSongResponse uploadSongResponse = (UploadSongResponse)sendRequestToServer(uploadSongRequest);

        if(uploadSongResponse.getEnumActionResult() == EnumActionResult.UPLOAD_SONG_SUCCESSFULL)
        {
            try
            {
                Socket uploadFileSocket = new Socket(socket.getInetAddress(), uploadSongResponse.getPort());

                ClientSendFileThread clientSendFileThread = new ClientSendFileThread(MUSIC_PATH + fileName, uploadFileSocket, this);
                clientSendFileThread.setDaemon(true);
                clientSendFileThread.start();
            }
            catch(IOException exception)
            {
                KeepAlive.emergencyExit(exception);
            }
        }

        return uploadSongResponse.getEnumActionResult();
    }
    
    @Override
    public EnumActionResult downloadSong(String songName)
    {
        DownloadSongRequest downloadSongRequest = new DownloadSongRequest(songName);

        DownloadSongResponse downloadSongResponse = (DownloadSongResponse)sendRequestToServer(downloadSongRequest);

        if(downloadSongResponse.getEnumActionResult() == EnumActionResult.DOWNLOAD_SONG_SUCCESSFULL)
        {
            ClientReceiveFileThread clientReceiveFileThread = new ClientReceiveFileThread(socket, this, songName);
            clientReceiveFileThread.setDaemon(true);
            clientReceiveFileThread.start();
        }

        return downloadSongResponse.getEnumActionResult();
    }
    

    @Override
    public EnumActionResult editSongName(User user, String songName, String newName)
    {
        EditSongNameRequest editSongNameRequest = new EditSongNameRequest(user, songName, newName);        
        EnumActionResult response = (EnumActionResult)sendRequestToServer(editSongNameRequest);
        return response;
    }
    
    @Override
    public EnumActionResult editSongAuthor(User user, String songName, String newAuthorName)
    {
        EditSongAuthorRequest editSongAuthorRequest = new EditSongAuthorRequest(user, songName, newAuthorName);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(editSongAuthorRequest);
        return response;
    }
    

    @Override
    public EnumActionResult deleteSong(User user, String songName)
    {
        DeleteSongRequest deleteSongRequest = new DeleteSongRequest(user, songName);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(deleteSongRequest);
        return response;
    }
    

    @Override
    public EnumActionResult createPlaylist(User user, String name)
    {
        CreatePlaylistRequest createPlaylistRequest = new CreatePlaylistRequest(user, name);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(createPlaylistRequest);
        return response;
    }
    

    @Override
    public EnumActionResult editPlaylistName(User user, String playlistName, String newName)
    {
        EditPlaylistNameRequest editPlaylistNameRequest = new EditPlaylistNameRequest(user, playlistName, newName);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(editPlaylistNameRequest);
        return response;
    }
    
    @Override
    public EnumActionResult addSongToPlaylist(User user, String playlistName, String songToAdd)
    {
        AddSongToPlaylistRequest addSongToPlaylistRequest = new AddSongToPlaylistRequest(user, playlistName, songToAdd);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(addSongToPlaylistRequest);
        return response;

    }
    
    @Override
    public EnumActionResult removeSongFromPlaylist(User user, String playlistName, String songToRemove)
    {
        RemoveSongFromPlaylistRequest removeSongFromPlaylistRequest = new RemoveSongFromPlaylistRequest(user, playlistName, songToRemove);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(removeSongFromPlaylistRequest);
        return response;
    }
    

    @Override
    public EnumActionResult removePlaylist(User user, String playlistName)
    {
        RemovePlaylistRequest removePlaylistRequest = new RemovePlaylistRequest(user, playlistName);
        EnumActionResult response = (EnumActionResult)sendRequestToServer(removePlaylistRequest);
        return response;
    }
    

    @Override
    public EnumActionResult downloadPlaylist(String playlistName)
    {
        DownloadPlaylistRequest downloadPlaylistRequest = new DownloadPlaylistRequest(playlistName);

        DownloadPlaylistsResponse downloadPlaylistsResponse = (DownloadPlaylistsResponse)sendRequestToServer(downloadPlaylistRequest);

        if(downloadPlaylistsResponse.getEnumActionResult() == EnumActionResult.DOWNLOAD_PLAYLIST_SUCCESSFULL)
        {

        }

        return downloadPlaylistsResponse.getEnumActionResult();
    }
    

    @Override
    public ArrayList<Playlist> listPlaylists(PlaylistFilter playlistFilter)
    {
        return null;
    }
    
    @Override
    public ArrayList<Song> listSongs(SongFilter filter)
    {
        return null;
    }
    
    @Override
    public void leaveSystem()
    {
        LeaveSystemRequest leaveSystemRequest = new LeaveSystemRequest();
        sendRequestToServer(leaveSystemRequest);
    }

}
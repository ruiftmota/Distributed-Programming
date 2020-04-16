package server.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import server.communication.send_receive_files.DownloadSongThread;
import server.communication.send_receive_files.UploadSongThread;
import shared_data.communication.Request;
import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.EnumActionResult;
import shared_data.communication.client_to_server.businesslogic.Playlist;
import shared_data.communication.client_to_server.businesslogic.Song;
import shared_data.communication.client_to_server.requests.AddSongToPlaylistRequest;
import shared_data.communication.client_to_server.requests.CreatePlaylistRequest;
import shared_data.communication.client_to_server.requests.DeleteSongRequest;
import shared_data.communication.client_to_server.requests.DownloadPlaylistRequest;
import shared_data.communication.client_to_server.requests.DownloadSongRequest;
import shared_data.communication.client_to_server.requests.EditPlaylistNameRequest;
import shared_data.communication.client_to_server.requests.EditSongAuthorRequest;
import shared_data.communication.client_to_server.requests.EditSongNameRequest;
import shared_data.communication.client_to_server.requests.LeaveSystemRequest;
import shared_data.communication.client_to_server.requests.ListPlaylistsRequest;
import shared_data.communication.client_to_server.requests.ListSongsRequest;
import shared_data.communication.client_to_server.requests.LoginRequest;
import shared_data.communication.client_to_server.requests.RegisterRequest;
import shared_data.communication.client_to_server.requests.RemovePlaylistRequest;
import shared_data.communication.client_to_server.requests.RemoveSongFromPlaylistRequest;
import shared_data.communication.client_to_server.requests.UploadSongRequest;
import shared_data.communication.client_to_server.responses.DownloadPlaylistsResponse;
import shared_data.communication.client_to_server.responses.DownloadSongResponse;
import shared_data.communication.client_to_server.responses.ListPlaylistsResponse;
import shared_data.communication.client_to_server.responses.ListSongsResponse;
import shared_data.communication.client_to_server.responses.UploadSongResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;



public class ManageClientThread extends Thread
{
    private Scanner scanner;
    private Socket clientSocket;
    private Socket clientCallbackSocket;
    private ClientToServerInterface businessLogic;

    private MulticastSocket multicastSocket;

    public ManageClientThread(ClientToServerInterface businessLogic, Socket clientSocket, MulticastSocket multicastSocket)
    {
        this.businessLogic = businessLogic;
        this.clientSocket = clientSocket;
        this.multicastSocket = multicastSocket;

        /* try
        {
            scanner = new Scanner(clientSocket.getInputStream());
            int callbackPort = scanner.nextInt();
            clientCallbackSocket = new Socket(clientSocket.getInetAddress(), callbackPort);
        }
        catch(Exception exception)
        {
            KeepAlive.emergencyExit(exception);
        } */
    }

    public void exitNormally() throws IOException
    {
        PrintWriter printWriter = new PrintWriter(clientCallbackSocket.getOutputStream());
        printWriter.println("exit");
        printWriter.close();
        scanner.close();
    }

    @Override
    public void run()
    {
        int process = 0;

        while(KeepAlive.getKeepAlive() == true && process == 0)
        {
            try
            {

                String requestInJson = SendAndReceiveInformation.receiveDataTCP(clientSocket);
                Request request = (Request)Serialization.deserializeObjectFromJson(requestInJson);
                process = processRequest(request);

                
            }
            catch(ClassNotFoundException | IOException exception)
            {

            }
            
        }
        
        
    }

    /**
     * This function receives a request and calls the respective method in the businessLogic process the data
     * and save it in the database.
     * 
     * 
     * @param request - The request to be processed.
     */
    private int processRequest(Request request)
    {
        if(request instanceof RegisterRequest == true)
        {
            RegisterRequest registerRequest = (RegisterRequest)request;
            EnumActionResult response = businessLogic.register(registerRequest.getUser());
            sendResponse(response);
        }
        else if(request instanceof LoginRequest == true)
        {
            LoginRequest loginRequest = (LoginRequest)request;
            EnumActionResult response = businessLogic.login(loginRequest.getUser());
            sendResponse(response);
        }
        else if(request instanceof AddSongToPlaylistRequest)
        {
            AddSongToPlaylistRequest addSongToPlaylistRequest = (AddSongToPlaylistRequest)request;
            EnumActionResult response = businessLogic.addSongToPlaylist(
                                            addSongToPlaylistRequest.getUser(), 
                                            addSongToPlaylistRequest.getPlaylistName(),
                                            addSongToPlaylistRequest.getSongToAdd());
            sendResponse(response);
        }
        else if(request instanceof CreatePlaylistRequest)
        {
            CreatePlaylistRequest createPlaylistRequest = (CreatePlaylistRequest)request;
            EnumActionResult response = businessLogic.createPlaylist(
                                            createPlaylistRequest.getUser(),
                                            createPlaylistRequest.getName());
            sendResponse(response);
        }
        else if(request instanceof DeleteSongRequest)
        {
            DeleteSongRequest deleteSongRequest = (DeleteSongRequest)request;
            EnumActionResult response = businessLogic.deleteSong(deleteSongRequest.getUser(), deleteSongRequest.getSongName()); 
            sendResponse(response);          
        }
        else if(request instanceof EditPlaylistNameRequest)
        {
            EditPlaylistNameRequest editPlaylistNameRequest = (EditPlaylistNameRequest)request;
            EnumActionResult response = businessLogic.editPlaylistName(
                                            editPlaylistNameRequest.getUser(),
                                            editPlaylistNameRequest.getPlaylistName(),
                                            editPlaylistNameRequest.getNewPlaylistName());
            sendResponse(response);
        }
        else if(request instanceof EditSongAuthorRequest)
        {
            EditSongAuthorRequest editSongAuthorRequest = (EditSongAuthorRequest)request;
            EnumActionResult response = businessLogic.editSongAuthor(
                                            editSongAuthorRequest.getUser(),
                                            editSongAuthorRequest.getSongName(),
                                            editSongAuthorRequest.getNewAuthorName());
            sendResponse(response);
        }
        else if(request instanceof EditSongNameRequest)
        {
            EditSongNameRequest editSongNameRequest = (EditSongNameRequest)request;
            EnumActionResult response = businessLogic.editSongName(
                                            editSongNameRequest.getUser(),
                                            editSongNameRequest.getSongName(),
                                            editSongNameRequest.getNewName());
            sendResponse(response);
        }
        else if(request instanceof RemovePlaylistRequest)
        {
            RemovePlaylistRequest removePlaylistRequest = (RemovePlaylistRequest)request;
            EnumActionResult response = businessLogic.removePlaylist(   
                                            removePlaylistRequest.getUser(),
                                            removePlaylistRequest.getPlaylistName());
            sendResponse(response);
        }
        else if(request instanceof RemoveSongFromPlaylistRequest)
        {
            RemoveSongFromPlaylistRequest removeSongFromPlaylistRequest = (RemoveSongFromPlaylistRequest)request;
            EnumActionResult response = businessLogic.removeSongFromPlaylist(   
                                            removeSongFromPlaylistRequest.getUser(),
                                            removeSongFromPlaylistRequest.getPlaylistName(),
                                            removeSongFromPlaylistRequest.getSongToRemove());
            sendResponse(response);
        }
        else if(request instanceof UploadSongRequest)
        {
            UploadSongRequest uploadSongRequest = (UploadSongRequest)request;
            EnumActionResult response = businessLogic.uploadSong(   
                                            uploadSongRequest.getUser(),
                                            uploadSongRequest.getSongName(),
                                            uploadSongRequest.getAuthor(),
                                            uploadSongRequest.getYear(),
                                            uploadSongRequest.getAlbum(),
                                            uploadSongRequest.getDuration(),
                                            uploadSongRequest.getCategory(),
                                            uploadSongRequest.getFileName());
            sendResponse(response);

            
        }
        else if(request instanceof ListPlaylistsRequest)
        {
            ListPlaylistsRequest listPlaylistsRequest = (ListPlaylistsRequest)request;
            ArrayList<Playlist> playlists = businessLogic.listPlaylists(listPlaylistsRequest.getPlaylistFilter());

            ListPlaylistsResponse listPlaylistsResponse = new ListPlaylistsResponse(playlists);
            sendResponse(listPlaylistsResponse);
        }
        else if(request instanceof ListSongsRequest)
        {
            ListSongsRequest listSongsRequest = (ListSongsRequest)request;
            ArrayList<Song> songs = businessLogic.listSongs(listSongsRequest.getSongFilter());

            ListSongsResponse listSongsResponse = new ListSongsResponse(songs);
            sendResponse(listSongsResponse);
        }
        else if(request instanceof LeaveSystemRequest)
        {
            return -1;
        }
        else if(request instanceof UploadSongRequest)
        {
            UploadSongRequest uploadSongRequest = (UploadSongRequest)request;
            EnumActionResult response = businessLogic.uploadSong(
                                            uploadSongRequest.getUser(),
                                            uploadSongRequest.getSongName(),
                                            uploadSongRequest.getAuthor(),
                                            uploadSongRequest.getYear(),
                                            uploadSongRequest.getAlbum(),
                                            uploadSongRequest.getDuration(),
                                            uploadSongRequest.getCategory(),
                                            uploadSongRequest.getFileName());
            
            try
            {
                ServerSocket serverSocket = new ServerSocket(0);
                UploadSongResponse uploadSongResponse = new UploadSongResponse(response, serverSocket.getLocalPort());

                sendResponse(uploadSongResponse);

                Socket clientUploadSocket = serverSocket.accept();


                UploadSongThread uploadSongThread = new UploadSongThread(clientUploadSocket, businessLogic, multicastSocket);
                uploadSongThread.start();
                
                serverSocket.close();
            } 
            catch (IOException e) 
            {
                System.err.println("Client went off.");
                return -1;
            }

        }
        else if(request instanceof DownloadSongRequest)
        {
            DownloadSongRequest downloadSongRequest = (DownloadSongRequest)request;
            EnumActionResult response = businessLogic.downloadSong(downloadSongRequest.getSongName());

            try 
            {
                ServerSocket serverSocket = new ServerSocket(0);   
                DownloadSongResponse downloadSongResponse = new DownloadSongResponse(response, serverSocket.getLocalPort());

                sendResponse(downloadSongResponse);

                Socket clientDownloadSocket = serverSocket.accept();

                DownloadSongThread downloadSongThread = new DownloadSongThread(
                                                                downloadSongRequest.getSongName(),
                                                                clientDownloadSocket, businessLogic);
                downloadSongThread.start();
                                        
                serverSocket.close();
            }
            catch (Exception e) 
            {
                System.err.println("Client went off.");
                return -1;
            }
            

        }
        else if(request instanceof DownloadPlaylistRequest)
        {
            DownloadPlaylistRequest downloadPlaylistRequest = (DownloadPlaylistRequest)request;
            EnumActionResult response = businessLogic.downloadPlaylist(downloadPlaylistRequest.getPlaylistName());

            /* try 
            {
                ServerSocket serverSocket = new ServerSocket();   
                DownloadPlaylistsResponse downloadPlaylistsResponse = new DownloadPlaylistsResponse(response, serverSocket.getLocalPort());

                sendResponse(downloadSongResponse);

                Socket clientDownloadSocket = serverSocket.accept();

                DownloadSongThread downloadSongThread = new DownloadSongThread(
                                                                downloadSongRequest.getSongName(),
                                                                clientDownloadSocket, businessLogic);
                downloadSongThread.start();
                                        
                serverSocket.close();
            }
            catch (Exception e) 
            {
                System.err.println("Client went off.");
                return -1;
            } */
     }






        HandleNewUpdatesThread handleNewUpdatesThread = new HandleNewUpdatesThread(request, multicastSocket);
        handleNewUpdatesThread.setDaemon(true);
        handleNewUpdatesThread.start();

        return 0;
    }

    private void sendResponse(Object response)
    {
        String responseInJson = Serialization.serializeObjectToJson(response);

        try
        {
            SendAndReceiveInformation.sendDataTCP(responseInJson, clientSocket);
        }
        catch(IOException exception)
        {
            KeepAlive.emergencyExit(exception);
        }
    }


}
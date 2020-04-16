package server.communication;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

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
import shared_data.communication.client_to_server.responses.DownloadSongResponse;
import shared_data.communication.client_to_server.responses.ListPlaylistsResponse;
import shared_data.communication.client_to_server.responses.ListSongsResponse;
import shared_data.communication.client_to_server.responses.UploadSongResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.MyInteger;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;
import shared_data.utilities.SynchronizedPrint;



public class ManageClientThread extends Thread
{
    private Socket clientSocket;
    //private Socket clientCallbackSocket;
    private ClientToServerInterface businessLogic;

    private MyInteger numberOfClients;
    private MyInteger numberOfServersInTheSystem;

    private DatagramSocket disseminateUpdatesSocket;

    public ManageClientThread(MyInteger numberOfClients, MyInteger numberOfServersInTheSystem, ClientToServerInterface businessLogic, Socket clientSocket, DatagramSocket disseminateUpdatesSocket)
    {
        this.numberOfClients = numberOfClients;
        this.businessLogic = businessLogic;
        this.clientSocket = clientSocket;
        this.disseminateUpdatesSocket = disseminateUpdatesSocket;
        this.numberOfClients = numberOfClients;
        this.numberOfServersInTheSystem = numberOfServersInTheSystem;
        

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

    @Override
    public void run()
    {

        /* try
        {
            ServerSocket serverSocket = new ServerSocket(0);

            ConfigureCallBackRequest configureCallBackRequest = new ConfigureCallBackRequest(serverSocket.getLocalPort());

            String requestInJson = Serialization.serializeObjectToJson(configureCallBackRequest);
            SendAndReceiveInformation.sendDataTCP(requestInJson, clientSocket);

            this.clientCallbackSocket = serverSocket.accept();

            serverSocket.close();
        }
        catch(IOException exception)
        {

        } */


        int process = 0;

        numberOfClients.add(1);

        while(KeepAlive.getKeepAlive() == true)
        {
            try
            {

                String requestInJson = SendAndReceiveInformation.receiveDataTCP(clientSocket);
                Request request = (Request)Serialization.deserializeObjectFromJson(requestInJson);
                process = processRequest(request);

                if(process == -1)
                {
                    SynchronizedPrint.printLine("Client in " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " exited normally");
                    break;
                }
            }
            catch(SocketTimeoutException exception)
            {
                continue;
            }
            catch(ClassNotFoundException | IOException exception)
            {
                SynchronizedPrint.printLine("Client in " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " didn't exit normally");
                break;
            }
            
        }

        /* if(KeepAlive.getNormalShutdown() == true)
        {
            informClientToShutdown();
        } */
        
        numberOfClients.subtract(1);

        /* try
        {
            clientCallbackSocket.close();
            clientSocket.close();
        }
        catch(IOException exception)
        {

        } */
       
    }

    /* private void informClientToShutdown()
    {
        ShutdownRequest shutdownRequest = new ShutdownRequest();
        String requestInJson = Serialization.serializeObjectToJson(shutdownRequest);

        try
        {
            SendAndReceiveInformation.sendDataTCP(requestInJson, clientCallbackSocket);
        }
        catch(IOException exception)
        {

        }
    } */

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


                UploadSongThread uploadSongThread = new UploadSongThread(numberOfServersInTheSystem, clientUploadSocket, businessLogic, disseminateUpdatesSocket);
                uploadSongThread.setDaemon(true);
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
            String fileName = businessLogic.downloadSong(downloadSongRequest.getSongName());
            if(fileName != null) 
            {
                try 
                {
                    ServerSocket serverSocket = new ServerSocket(0);
                    DownloadSongResponse downloadSongResponse = new DownloadSongResponse(EnumActionResult.DOWNLOAD_SONG_SUCCESSFULL, serverSocket.getLocalPort(), fileName);

                    sendResponse(downloadSongResponse);

                    Socket clientDownloadSocket = serverSocket.accept();

                    DownloadSongThread downloadSongThread = new DownloadSongThread(fileName,
                                                                    clientDownloadSocket, businessLogic);
                    downloadSongThread.setDaemon(true);
                    downloadSongThread.start();
                                            
                    serverSocket.close();
                }
                catch (Exception e) 
                {
                    System.err.println("Client went off.");
                    return -1;
                }
            }
            else
            {
                DownloadSongResponse downloadSongResponse = new DownloadSongResponse(EnumActionResult.DOWNLOAD_SONG_FAILED, -1, null);
                sendResponse(downloadSongResponse);
            }
            

        }
        else if(request instanceof DownloadPlaylistRequest)
        {
            /* DownloadPlaylistRequest downloadPlaylistRequest = (DownloadPlaylistRequest)request;
            EnumActionResult response = businessLogic.downloadPlaylist(downloadPlaylistRequest.getPlaylistName()); */

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






        HandleNewUpdatesThread handleNewUpdatesThread = new HandleNewUpdatesThread(numberOfServersInTheSystem, request, disseminateUpdatesSocket);
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
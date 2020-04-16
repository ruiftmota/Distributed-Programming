package server.communication;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import shared_data.communication.Request;
import shared_data.communication.client_to_server.ClientToServerInterface;
import shared_data.communication.client_to_server.requests.AddSongToPlaylistRequest;
import shared_data.communication.client_to_server.requests.CreatePlaylistRequest;
import shared_data.communication.client_to_server.requests.DeleteSongRequest;
import shared_data.communication.client_to_server.requests.EditPlaylistNameRequest;
import shared_data.communication.client_to_server.requests.EditSongAuthorRequest;
import shared_data.communication.client_to_server.requests.EditSongNameRequest;
import shared_data.communication.client_to_server.requests.RegisterRequest;
import shared_data.communication.client_to_server.requests.RemovePlaylistRequest;
import shared_data.communication.client_to_server.requests.RemoveSongFromPlaylistRequest;
import shared_data.communication.client_to_server.requests.UploadSongRequest;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;

/**
 * NewUpdatesThread
 */
public class ListenToUpdatesThread extends Thread 
{

    private ClientToServerInterface businessLogic;
    private MulticastSocket multicastSocket;

    

    public ListenToUpdatesThread(ClientToServerInterface businessLogic, MulticastSocket multicastSocket)   
    {
        this.multicastSocket = multicastSocket;
        this.businessLogic = businessLogic;
        
    }

    public int getPort() 
    {
        return multicastSocket.getLocalPort();
    }

    @Override
    public void run() 
    {
        String requestInJson;

        while (KeepAlive.getKeepAlive()) 
        {
            try
            {
                requestInJson = SendAndReceiveInformation.receiveDataMulticast(multicastSocket);

                Request request = (Request)Serialization.deserializeObjectFromJson(requestInJson);

                if(request instanceof RegisterRequest)
                {
                    RegisterRequest registerRequest = (RegisterRequest)request;
                    businessLogic.register(registerRequest.getUser());
                }
                else if(request instanceof AddSongToPlaylistRequest)
                {
                    AddSongToPlaylistRequest addSongToPlaylistRequest = (AddSongToPlaylistRequest)request;
                    businessLogic.addSongToPlaylist(addSongToPlaylistRequest.getUser(), 
                                                    addSongToPlaylistRequest.getPlaylistName(),
                                                    addSongToPlaylistRequest.getSongToAdd());
                }
                else if(request instanceof CreatePlaylistRequest)
                {
                    CreatePlaylistRequest createPlaylistRequest = (CreatePlaylistRequest)request;
                    businessLogic.createPlaylist(createPlaylistRequest.getUser(),
                                                 createPlaylistRequest.getName());
                }
                else if(request instanceof DeleteSongRequest)
                {
                    DeleteSongRequest deleteSongRequest = (DeleteSongRequest)request;
                    businessLogic.deleteSong(deleteSongRequest.getUser(), deleteSongRequest.getSongName());           
                }
                else if(request instanceof EditPlaylistNameRequest)
                {
                    EditPlaylistNameRequest editPlaylistNameRequest = (EditPlaylistNameRequest)request;
                    businessLogic.editPlaylistName(editPlaylistNameRequest.getUser(),
                                                    editPlaylistNameRequest.getPlaylistName(),
                                                    editPlaylistNameRequest.getNewPlaylistName());
                }
                else if(request instanceof EditSongAuthorRequest)
                {
                    EditSongAuthorRequest editSongAuthorRequest = (EditSongAuthorRequest)request;
                    businessLogic.editSongAuthor(editSongAuthorRequest.getUser(),
                                                editSongAuthorRequest.getSongName(),
                                                editSongAuthorRequest.getNewAuthorName());
                }
                else if(request instanceof EditSongNameRequest)
                {
                    EditSongNameRequest editSongNameRequest = (EditSongNameRequest)request;
                    businessLogic.editSongName(editSongNameRequest.getUser(),
                                                editSongNameRequest.getSongName(),
                                                editSongNameRequest.getNewName());
                }
                else if(request instanceof RemovePlaylistRequest)
                {
                    RemovePlaylistRequest removePlaylistRequest = (RemovePlaylistRequest)request;
                    businessLogic.removePlaylist(   removePlaylistRequest.getUser(),
                                                    removePlaylistRequest.getPlaylistName());
                }
                else if(request instanceof RemoveSongFromPlaylistRequest)
                {
                    RemoveSongFromPlaylistRequest removeSongFromPlaylistRequest = (RemoveSongFromPlaylistRequest)request;
                    businessLogic.removeSongFromPlaylist(   removeSongFromPlaylistRequest.getUser(),
                                                            removeSongFromPlaylistRequest.getPlaylistName(),
                                                            removeSongFromPlaylistRequest.getSongToRemove());
                }
                else if(request instanceof UploadSongRequest)
                {
                    UploadSongRequest uploadSongRequest = (UploadSongRequest)request;
                    businessLogic.uploadSong(   uploadSongRequest.getUser(),
                                                uploadSongRequest.getSongName(),
                                                uploadSongRequest.getAuthor(),
                                                uploadSongRequest.getYear(),
                                                uploadSongRequest.getAlbum(),
                                                uploadSongRequest.getDuration(),
                                                uploadSongRequest.getCategory(),
                                                uploadSongRequest.getFileName());
                }

                //if(request instanceof SendFileRequest)

            }
            catch (SocketTimeoutException e) 
            { 
                continue;
            } 
            catch (ClassNotFoundException e) 
            {
                KeepAlive.emergencyExit(e);
            }
            catch (IOException e) 
            {
                KeepAlive.emergencyExit(e);
            }
        }
    }

    
}
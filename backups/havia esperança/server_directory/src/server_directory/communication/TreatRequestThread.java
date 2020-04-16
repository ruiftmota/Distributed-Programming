package server_directory.communication;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server_directory.logic.ServerDirectoryLogic;
import server_directory.logic.ServerInfo;
import shared_data.communication.Request;
import shared_data.communication.client_to_server_directory.requests.ClientToServerDirectoryRequest;
import shared_data.communication.client_to_server_directory.requests.EnterClientRequest;
import shared_data.communication.client_to_server_directory.requests.ExitClientRequest;
import shared_data.communication.client_to_server_directory.responses.ChoosenServerResponse;
import shared_data.communication.server_to_server_directory.ClientInfo;
import shared_data.communication.server_to_server_directory.requests.EnterServerRequest;
import shared_data.communication.server_to_server_directory.requests.ExitServerRequest;
import shared_data.communication.server_to_server_directory.requests.ServerToDirectoryServerRequest;
import shared_data.communication.server_to_server_directory.responses.ServerIsPrimaryResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;
import shared_data.utilities.SynchronizedPrint;

/**
 * TreatRequestThread
 */
public class TreatRequestThread extends Thread{

    private ServerDirectoryLogic serverDirectoryLogic;
    
    private Request request;

    public TreatRequestThread(  ServerDirectoryLogic serverDirectoryLogic, 
                                Request request)
    {
        this.serverDirectoryLogic = serverDirectoryLogic;
        this.request = request;
    }

    @Override
    public void run()
    {
        System.out.println(request);
        if(request instanceof ClientToServerDirectoryRequest == true)
        {
            ClientToServerDirectoryRequest clientToServerDirectoryRequest = (ClientToServerDirectoryRequest)request;

            if(clientToServerDirectoryRequest instanceof EnterClientRequest == true)
            {
                EnterClientRequest enterClientRequest = (EnterClientRequest)clientToServerDirectoryRequest;

                ClientInfo newClient = new ClientInfo(enterClientRequest.getSenderIP(), enterClientRequest.getSenderPort());
                ServerInfo serverInfo = serverDirectoryLogic.addClient(newClient);

                ChoosenServerResponse choosenServerResponse = new ChoosenServerResponse(
                    serverInfo.getServerIp(),
                    serverInfo.getServerListeningTCPPort());

                String response = Serialization.serializeObjectToJson(choosenServerResponse);
                try
                {
                    DatagramSocket answerSocket = new DatagramSocket();

                    SendAndReceiveInformation.sendData(response, answerSocket, InetAddress.getByName(enterClientRequest.getSenderIP()), enterClientRequest.getSenderPort());
                
                    answerSocket.close();
                }
                catch(IOException e)
                {
                    System.err.println("Unable to send choosen server to client");
                    System.err.println(e.getStackTrace());
                }

                SynchronizedPrint.printLine("Client from " + enterClientRequest.getSenderIP() + ":" + enterClientRequest.getSenderPort() + " entered successfully");
                
            }
            else if(clientToServerDirectoryRequest instanceof ExitClientRequest == true)
            {
                ExitClientRequest exitClientRequest = (ExitClientRequest)clientToServerDirectoryRequest;

                ClientInfo clientToRemove = new ClientInfo(exitClientRequest.getSenderIP(), exitClientRequest.getSenderPort());
                serverDirectoryLogic.removeClient(clientToRemove);
            }

        }
        else if(request instanceof ServerToDirectoryServerRequest == true)
        {
            ServerToDirectoryServerRequest serverToDirectoryServerRequest = (ServerToDirectoryServerRequest)request;
            

            if(serverToDirectoryServerRequest instanceof EnterServerRequest == true)
            {
                EnterServerRequest enterServerRequest = (EnterServerRequest)serverToDirectoryServerRequest;
                
                ServerInfo newServer = new ServerInfo(enterServerRequest.getSenderIP(), enterServerRequest.getSenderPort(), enterServerRequest.getListeningTCPPort(),
                                                      enterServerRequest.getPingPort(), enterServerRequest.getNewUpdatesPort(), enterServerRequest.getSendDatabasePort());
                ServerInfo primaryServerInfo = serverDirectoryLogic.addServer(newServer);

                

                ServerIsPrimaryResponse serverIsPrimaryResponse = new ServerIsPrimaryResponse(
                    primaryServerInfo.getServerIp(),
                    primaryServerInfo.getSendDataBasePort());

                String response = Serialization.serializeObjectToJson(serverIsPrimaryResponse);
                try
                {
                    DatagramSocket answerSocket = new DatagramSocket();

                    SendAndReceiveInformation.sendData(response, answerSocket, InetAddress.getByName(enterServerRequest.getSenderIP()), enterServerRequest.getSenderPort());

                    answerSocket.close();
                }
                catch(IOException e)
                {
                    KeepAlive.emergencyExit(e);
                }

                SynchronizedPrint.printLine("Server from " + enterServerRequest.getSenderIP() + ":" + enterServerRequest.getSenderPort() + " entered successfully");
                
                PingThread pingThread = new PingThread(serverDirectoryLogic, newServer);
                pingThread.start();
                newServer.setPingThread(pingThread);

            }
            else if(serverToDirectoryServerRequest instanceof ExitServerRequest == true)
            {
                ExitServerRequest exitServerRequest = (ExitServerRequest)serverToDirectoryServerRequest;
            
                ServerInfo serverToRemove = new ServerInfo(exitServerRequest.getSenderIP(), exitServerRequest.getSenderPort());
                serverDirectoryLogic.removeServer(serverToRemove);
            }

        }
    }
}
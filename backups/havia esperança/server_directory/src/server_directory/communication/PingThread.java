package server_directory.communication;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import server_directory.logic.ServerDirectoryLogic;
import server_directory.logic.ServerInfo;
import shared_data.communication.Response;
import shared_data.communication.server_to_server_directory.requests.PingRequest;
import shared_data.communication.server_to_server_directory.responses.PingResponse;
import shared_data.utilities.KeepAlive;
import shared_data.utilities.SendAndReceiveInformation;
import shared_data.utilities.Serialization;
import shared_data.utilities.SynchronizedPrint;

/**
 * PingThread
 */
public class PingThread extends Thread
{
    private ServerDirectoryLogic serverDirectoryLogic;
    private ServerInfo serverInfo;
    
    private String requestList;

    private final static int TIMEOUT = 10000;

    PingThread(ServerDirectoryLogic serverDirectoryLogic, ServerInfo serverInfo) 
    {
        this.serverDirectoryLogic = serverDirectoryLogic;
        this.serverInfo = serverInfo;
    }


    /**
     * Manda pings de 10 em 10 segundos aos servidores
     * Se não receber resposta ao fim de 3 tentativas, é declarado morto;
     */
    @Override
    public void run() 
    {
        int conta = 0;
        try
        {
            DatagramSocket pingSocket = new DatagramSocket();
            pingSocket.setSoTimeout(TIMEOUT);

            while(KeepAlive.getKeepAlive())
            {
                PingRequest pingRequest = new PingRequest(  InetAddress.getLocalHost().getHostAddress(),
                                                            pingSocket.getLocalPort(), 
                                                            serverDirectoryLogic.getNumberOfServers());

                String pingRequestJson = Serialization.serializeObjectToJson(pingRequest);
                try
                {
                    SendAndReceiveInformation.sendData(pingRequestJson, pingSocket, InetAddress.getByName(serverInfo.getServerIp()), serverInfo.getPingPort());
                
                    requestList = SendAndReceiveInformation.receiveData(pingSocket);
                    
                    conta = 0;

                    atualizaServerList(requestList);
                }
                catch(SocketTimeoutException a)
                {
                    conta++;
                    if(conta == 3)
                    {
                        serverDirectoryLogic.removeServer(serverInfo);
                        return;
                    }
                }
                catch(IOException | ClassNotFoundException e)
                {
                    KeepAlive.emergencyExit(e);
                }

                SynchronizedPrint.printLine("Ping from " + serverInfo.getServerIp() + ":" + serverInfo.getServerPort());

                try
                {
                    Thread.sleep(10000);
                }
                catch(InterruptedException exception)
                {

                }

            }

            pingSocket.close();
        }
        catch(IOException e)
        {
            KeepAlive.emergencyExit(e);
        }

    }


    private void atualizaServerList(String requestList) throws ClassNotFoundException
    {
        Response response = (Response)Serialization.deserializeObjectFromJson(requestList);

        if(response instanceof PingResponse)
        {
            PingResponse pingResponse = (PingResponse)response;          
            
            serverDirectoryLogic.updateClientsListOfServer(serverInfo, pingResponse.getClientsArrayList());
        }
        
    }

    
}
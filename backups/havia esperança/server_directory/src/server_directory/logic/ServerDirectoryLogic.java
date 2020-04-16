package server_directory.logic;

import java.util.ArrayList;

import server_directory.communication.PingThread;
import shared_data.communication.server_to_server_directory.ClientInfo;

/**
 * ServerDirectoryLogic
 */
public class ServerDirectoryLogic {

    private ArrayList<ServerInfo> serverList = new ArrayList<>();

    public ServerDirectoryLogic() { }


    /**
     * Adds a serverinfo to the list
     */
    public ServerInfo addServer(ServerInfo serverInfo)
    {
        if(searchServerExistence(serverInfo)==false)
        { 
            synchronized(serverList)
            {
                this.serverList.add(serverInfo);
            }
        }
        
        return getPrimaryServer();
    }

    /**
     * Returns the primary server. If it doesn't exist, first defines one and the returns it
     * @return ServerInfo
     */
    private ServerInfo getPrimaryServer()
    {
        ServerInfo primaryServer = null;

        synchronized(serverList)
        {
            for(ServerInfo server : serverList)
            {
                if(server.getIsPrimaryServer() == true)
                {
                    primaryServer = server;
                    break;
                }
            }
        }

        if(primaryServer != null)
            return primaryServer;

        synchronized(serverList)
        {
            if(serverList.size()>0){
                serverList.get(0).setPrimaryServer(true);
                primaryServer = serverList.get(0);
            }
        }
        
        return primaryServer;
    }

    /**
     * Receives the server to be removed
     * @param serverToRemove
     */
    public void removeServer(ServerInfo serverToRemove)
    {
        synchronized(serverList)
        {
            for(ServerInfo serverInfo: serverList)
            {
                if(serverToRemove.equals(serverInfo))
                {
                    serverList.remove(serverInfo);    
                }   
            }
        }
    }

    /**
     * Searches the serverList and sees if it has already connected
     * @return boolean if he exists or not
     */
    private boolean searchServerExistence(ServerInfo serverInfo)
    {
        boolean exists = false;
        synchronized(serverList){    
            for(ServerInfo aux : serverList)
            if(serverInfo.equals(aux) == true)
                exists = true;
        }
        return exists;
    }


    /**
     * The funtions returns true should it find the client is already logged
     * @param client new client trying to log
     * @return boolean with true if found someone, false otherwise
     */
    private ServerInfo searchClientExistance(ClientInfo client)
    {
        ServerInfo exists = null;
        synchronized(serverList)
        {
            for(ServerInfo server : serverList)
            {
                if(server.clientExists(client))
                {
                    exists = server;
                }
            }
        }
        return exists;
    }

    /**
     * returns a copy of the arrayList with all the servers
     * 
     * @return
     * @throws CloneNotSupportedException
     */
    public ArrayList<ServerInfo> getServersListCopy() throws CloneNotSupportedException
    {
        ArrayList<ServerInfo> newArray = new ArrayList<ServerInfo>();
        synchronized(serverList)
        {
            for(ServerInfo server : serverList){
                newArray.add((ServerInfo)server.clone());
            }
        }
        return newArray;
    }

    public int getNumberOfServers()
    {
        int tam=0;

        synchronized(serverList)
        {
            tam = serverList.size();
        }
        return tam;
    }
    

    /**
     * Receives the serverInfo where to update the ClientsList
     * @param serverInfo
     * @param newClientsList
     */
    public void updateClientsListOfServer(ServerInfo serverInfo, ArrayList<ClientInfo> newClientsList)
    {
        ServerInfo serverToUpdate = null;
        synchronized(serverList)
        {
            for(ServerInfo server : serverList)
            {
                if(server.equals(serverInfo))
                {
                    serverToUpdate = server;
                }
            }

            serverToUpdate.removeClientsWhoDontExist(newClientsList);
        }
        
    }



    /**
     * Receives the new Client Info, takes note of his information and returns the server 
     * with the least clients.
     * If the client already exists it returns server with the least
     * clients logged (beeing it the same or a different one)
     * @return ServerInfo of the server with the least clients that was picked
     */
    public ServerInfo addClient(ClientInfo newClient)
    {
        ServerInfo serverInfo = getServerWithLeastClients();
        addClientToServerInfo(serverInfo, newClient);
        return new ServerInfo(serverInfo);
    }



    /**
     * Receives the client to be removed from all lists
     * @param clientToRemove
     */
    public void removeClient(ClientInfo clientToRemove)
    {
        synchronized(serverList)
        {
            for(ServerInfo server : serverList)
            {
                    server.removeClient(clientToRemove);
            }
        }
    }


    public ArrayList<PingThread> getServerPingThreads()
    {
        ArrayList<PingThread> pingThreads = new ArrayList<PingThread>();

        for(ServerInfo serverInfo : serverList)
        {
            pingThreads.add(serverInfo.getPingThread());
        }

        return pingThreads;
    }


    /**
     * This functions associates a client info with a specific ServerInfo.
     */
    private void addClientToServerInfo(ServerInfo serverInfo, ClientInfo clientInfo)
    {
        synchronized(serverList)
        {
            serverInfo.addClient(clientInfo);   
        }
    }


    /**
     * Picks the server with the least clients connected
     * @return
     */
    private ServerInfo getServerWithLeastClients()
    {
        ServerInfo leastUsedServer;
        int minimum;
        synchronized(serverList)
        {
            if(serverList.isEmpty())
            {
                return null;
            }
                
            leastUsedServer = serverList.get(0);
            minimum = serverList.get(0).getNumberOfClients();
            
            for(ServerInfo aux : serverList)
            {
                if(aux.getNumberOfClients() < minimum)
                {
                    minimum = aux.getNumberOfClients();
                    leastUsedServer = aux;
                }
            }
        }

        return leastUsedServer;

    }
    
}
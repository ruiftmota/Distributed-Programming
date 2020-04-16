package server_directory.logic;

import java.util.ArrayList;

import server_directory.communication.PingThread;

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
        synchronized(serverList)
        {
            this.serverList.add(serverInfo);
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
            for(int i = 0; i < serverList.size(); i++)
            {
                if(serverToRemove.equals(serverList.get(i)) == true)
                {
                    serverList.remove(i);
                    i--;
                }
            }
        }
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
    public void updateClientsListOfServer(ServerInfo serverInfo, int numberOfClients)
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

            serverToUpdate.setNumberOfClients(numberOfClients);
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
     * Picks the server with the least clients connected
     * @return
     */
    public ServerInfo getServerWithLeastClients()
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
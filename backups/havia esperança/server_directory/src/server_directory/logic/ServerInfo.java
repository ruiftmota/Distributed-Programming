package server_directory.logic;

import java.util.List;

import server_directory.communication.PingThread;
import shared_data.communication.server_to_server_directory.ClientInfo;

import java.util.ArrayList;


public class ServerInfo
{
    private static int countServersCreated = 0;

    private String serverIp;
    private Integer serverPort;
    private Integer serverListeningTCPPort;
    private Integer pingPort;
    private Integer newUpdatesPort;
    private Integer sendDataBasePort;
    private int serverNumber;
    private Boolean primaryServer = false;
    
    private List<ClientInfo> localClientList;

    private PingThread pingThread = null;


    public ServerInfo(String serverIp, Integer serverPort, Integer serverListeningTCPPort, Integer pingPort, Integer newUpdatesPort, Integer sendDataBasePort)
    {
        this.serverIp = new String(serverIp);
        this.serverPort = new Integer(serverPort);
        this.serverListeningTCPPort = serverListeningTCPPort;
        this.pingPort = pingPort;
        this.newUpdatesPort = newUpdatesPort;
        this.sendDataBasePort = sendDataBasePort;
        serverNumber = ++countServersCreated;
        this.localClientList = new ArrayList<>();
    }

    /**
     * This constructor serves only to create a temporary object that can be compared with a regular ServerInfo
     * when a server wants to leave
     * @param serverIp
     * @param serverPort
     */
    public ServerInfo(String serverIp, Integer serverPort)
    {
        this.serverIp = new String(serverIp);
        this.serverPort = new Integer(serverPort);
    }

    /**
     * This constructor returns a copy of the serverInfo it received
     */
    public ServerInfo(ServerInfo serverInfo)
    {
        this.serverIp = serverInfo.serverIp;
        this.serverPort = serverInfo.serverPort;
        this.serverListeningTCPPort = serverInfo.serverListeningTCPPort;
        this.pingPort = serverInfo.pingPort;
        this.newUpdatesPort = serverInfo.newUpdatesPort;
        this.sendDataBasePort = serverInfo.sendDataBasePort;
        this.serverNumber = serverInfo.serverNumber;
        this.localClientList = new ArrayList<>();
        for(int i=0; i<localClientList.size() ; i++)
        {
            this.localClientList.add(serverInfo.localClientList.get(i));
        }
        
    }

    public String getServerIp() { return new String(serverIp); }
    public int getServerPort() { return serverPort; }
    public int getServerListeningTCPPort() { return serverListeningTCPPort; }
    public int getServerNumber() { return serverNumber; }

    public void setPingThread(PingThread pingThread)
    {
        this.pingThread = pingThread;
    }
    public PingThread getPingThread()
    {
        return pingThread;
    }
    
    public int getNumberOfClients() 
    { 
        int aux;
        synchronized(localClientList)
        {
            aux = localClientList.size(); 
        }
        return aux;
    }
    
    public void addClient(ClientInfo client) 
    { 
        synchronized(localClientList)
        {
            localClientList.add(client);
        }
    }

    public void removeClient(ClientInfo clientToRemove) 
    { 
        if(clientToRemove == null)
            return;
        synchronized(localClientList)
        {
            for(ClientInfo client : localClientList)
            {
                if(clientToRemove.equals(client))
                    localClientList.remove(client);
            }
        }
    }



    public boolean clientExists(ClientInfo client)
    {
        boolean exists = false;
        synchronized(localClientList)
        {
            for(ClientInfo c : localClientList)
            {
                if(c.equals(client))
                {
                    exists = true;
                }
            }
        }
        return exists;
    }



    public void removeClientsWhoDontExist(ArrayList<ClientInfo> newList){
        synchronized(localClientList)
        {
            ClientInfo aux = null; 
            for(ClientInfo client : newList)
            {
                for(ClientInfo c : localClientList)
                {
                    if(client.equals(c))
                    {
                        aux = null;
                        break;
                    }
                    else
                    {
                        aux = c;
                    }
                }
                removeClient(aux);
            }
        }
    }


    public Boolean getIsPrimaryServer()
    { 
        Boolean temp;
        synchronized(primaryServer){
            temp = primaryServer;
        }
        
        return temp; 
    }
    
    public void setPrimaryServer(Boolean primaryServer) 
    { 
        synchronized(primaryServer){
            this.primaryServer = primaryServer;
        }
    }


    @Override
    public String toString() 
    {
        String aux = serverIp + " " + serverPort.toString();
        return aux;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if(obj == null) return false;
        if(getClass() != obj.getClass())
            return false;
            
		ServerInfo aux = (ServerInfo) obj;
        if (aux.getServerIp().compareToIgnoreCase(this.getServerIp()) == 0 && aux.getServerPort() == this.getServerPort())
            return true;
        return false;
    }


    @Override
    public Object clone() throws CloneNotSupportedException 
    {
        return new ServerInfo(this);
    }

    public Integer getPingPort() {
        return pingPort;
    }

    public Integer getNewUpdatesPort() {
        return newUpdatesPort;
    }

    public Integer getSendDataBasePort() {
        return sendDataBasePort;
    }

}
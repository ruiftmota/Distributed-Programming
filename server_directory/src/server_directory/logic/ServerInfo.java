package server_directory.logic;

import server_directory.communication.PingThread;


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
    
    private int numberOfClients = 0;

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
        this.numberOfClients = serverInfo.numberOfClients;
        
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
        int temp;
        synchronized(this)
        {
            temp = numberOfClients;
        }
        return temp;
    }
    
    public void setNumberOfClients(int numberOfClients)
    {
        synchronized(this)
        {
            this.numberOfClients = numberOfClients;
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
        String aux = serverIp + " " + serverPort.toString() + " with " + this.getNumberOfClients() + " clients";
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
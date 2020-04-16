package shared_data.communication.server_to_server_directory;

public class ClientInfo{

    private String clientAddress;
    private int clientPort;

    public ClientInfo(String clientAddress, int clientPort)
    {
        this.clientAddress = new String(clientAddress);
        this.clientPort = clientPort;
    }


    @Override
    public Object clone() throws CloneNotSupportedException
    {
        ClientInfo clientInfo = new ClientInfo(this.clientAddress, this.clientPort);

        return clientInfo;
    }
    

    
    public String getClientAddress(){ return new String(clientAddress); }
    
    public int getClientPort(){ return clientPort; }

    
    /**
     * Compares two ClientInfo objects
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(!(obj instanceof ClientInfo))
            return false;
        ClientInfo clientInfo = (ClientInfo)obj;
        if(clientInfo.clientAddress.equals(clientAddress) && clientInfo.clientPort == clientPort)
            return true;
        return false;
    }

}
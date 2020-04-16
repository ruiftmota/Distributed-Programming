package shared_data.communication.server_to_server_directory.requests;

import shared_data.communication.Request;

/**
 * PingRequest
 */ public class PingRequest extends Request
{
    private int numberOfServers;
    
	public PingRequest(String senderIP, int senderPort, int numberOfServers) {
        super(senderIP, senderPort);
        this.numberOfServers = numberOfServers;
    }

	public String getSenderIP() {
		return super.getSenderIP();
	}

	public int getSenderPort() {
		return super.getSenderPort();
	}

	public int getServerList() {
		return numberOfServers;
	}
    
    

    
}
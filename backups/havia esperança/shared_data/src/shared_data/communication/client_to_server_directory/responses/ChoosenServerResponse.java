package shared_data.communication.client_to_server_directory.responses;

import shared_data.communication.Response;

/**
 * ChoosenServerResponse
 */
public class ChoosenServerResponse extends Response
{
    private String serverIp;
    private int serverListeningTCPPort;

    public ChoosenServerResponse(String serverIp, int serverListeningTCPPort) {
        this.serverIp = serverIp;
        this.serverListeningTCPPort = serverListeningTCPPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerListeningTCPPort() {
        return serverListeningTCPPort;
    }

    public void setServerListeningTCPPort(int serverListeningTCPPort) {
        this.serverListeningTCPPort = serverListeningTCPPort;
    }

    
    
}
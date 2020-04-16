package shared_data.communication.server_to_server;

import shared_data.communication.Request;

/**
 * SendFileChunckRequest
 */
public class SendFileChunckRequest extends Request
{
    private final static int MAX_FILE_CHUNCK = 1900;

    private byte[] byteArray = new byte[MAX_FILE_CHUNCK];
    private String fileName;
    private int length;

    public SendFileChunckRequest(byte[] byteArray, String fileName, int length) {
        this.byteArray = byteArray;
        this.fileName = fileName;
        this.length = length;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLength() {
        return length;
    }
    

    

    
}
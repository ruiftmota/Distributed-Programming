package shared_data.utilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


/**
 * SendAndReceiveInformation
 */
public class SendAndReceiveInformation
{
    private final static int BUFFER_SIZE = 1900;
    private final static int CHECKSUM_STRING_LENGTH = 3;

    private final static int hashTable[] = { 41, 17, 13, 31, 5, 23, 11}; 
    private final static int HASH_TABLE_SIZE = 7;





    /**
     * Receives a String in Json format and sends it to the given socket
     * @param objectInJsonFormat
     * @param socket
     * @throws IOException
     */
    public static void sendDataTCP(String objectInJsonFormat, Socket socket) throws IOException
    {
        ObjectOutputStream oOS = new ObjectOutputStream(socket.getOutputStream());
        
        oOS.writeUnshared(objectInJsonFormat);
        oOS.flush();
    }

    /**
     * Receives a socket from where it will try to read a String in Json representing an object
     * @param socket
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static String receiveDataTCP(Socket socket) throws ClassNotFoundException, IOException
    {
        ObjectInputStream oIS = new ObjectInputStream(socket.getInputStream());
        
        String objectInJson = (String)oIS.readObject();
        
        return objectInJson;
    }

    /**
     * UDP - unicast
     * Esta função encapsula o envio de uma string através de um DatagramSocket UDP. Garante que os datagramas
     * chegam ao destino sem corrupção e na ordem correta.
     * 
     * Notas: o que o receive() recebe é um byte que representa 0(packet não enviado com sucesso) ou 1(packet enviado com sucesso)
     * 
     * @param objectInJsonFormat
     * @param socket
     * @param ip
     * @param port
     * @throws IOException
     */


    public static void sendPing(String objectInJsonFormat, DatagramSocket socket, InetAddress ip, int port) throws IOException, SocketTimeoutException
    {
        byte[] bytes = Serialization.serializeObjectToByteArray(objectInJsonFormat);

        int sendedBytes = 0;

        while(sendedBytes < bytes.length)
        {
            
            int bufferSize = 0;

            if(bytes.length - sendedBytes < BUFFER_SIZE)
            {
                bufferSize = bytes.length - sendedBytes;
            }
            else
            {
                bufferSize = BUFFER_SIZE;
            }
            DatagramPacket packet = constructDatagramPacket(bytes, sendedBytes, bufferSize, ip , port);
            socket.send(packet);
            DatagramPacket responsePacket = new DatagramPacket(new byte[1], 1);

            socket.receive(responsePacket);

            if(responsePacket.getData()[0] == (byte)1)
            {
                sendedBytes = sendedBytes + bufferSize;
            }
        }

        while(true)
        {
            socket.send(new DatagramPacket(new byte[0], 0, ip, port));
            socket.receive(new DatagramPacket(new byte[0], 0));
            break;
        }

    }


    public static void sendData(String objectInJsonFormat, DatagramSocket socket, InetAddress ip, int port) throws IOException
    {
        byte[] bytes = Serialization.serializeObjectToByteArray(objectInJsonFormat);

        int sendedBytes = 0;

        while(sendedBytes < bytes.length)
        {
            
            int bufferSize = 0;

            if(bytes.length - sendedBytes < BUFFER_SIZE)
            {
                bufferSize = bytes.length - sendedBytes;
            }
            else
            {
                bufferSize = BUFFER_SIZE;
            }
            DatagramPacket packet = constructDatagramPacket(bytes, sendedBytes, bufferSize, ip , port);
            socket.send(packet);
            DatagramPacket responsePacket = new DatagramPacket(new byte[1], 1);

            try
            {
                socket.receive(responsePacket);
            }
            catch(SocketTimeoutException exception)
            {
                continue;
            }

            if(responsePacket.getData()[0] == (byte)1)
            {
                sendedBytes = sendedBytes + bufferSize;
            }
        }

        while(true)
        {
            socket.send(new DatagramPacket(new byte[0], 0, ip, port));
            try
            {
                socket.receive(new DatagramPacket(new byte[0], 0));
                break;
            }
            catch(SocketTimeoutException exception)
            {
                continue;
            }
        }

    }


    



    


    /**
     * UDP - unicast
     * Receives the data that is sent by SendData udp unicast,
     * ensuring that everything that is sent is properly received
     */
    public static String receiveData(DatagramSocket socket) throws IOException, ClassNotFoundException, SocketTimeoutException
    {
        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();

        String lastChecksum = "";

        while(true)
        {
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE + 1000], BUFFER_SIZE + 1000);

            socket.receive(packet);

            if(packet.getLength() == 0)
            {
                socket.send(new DatagramPacket(new byte[0], 0, packet.getAddress(), packet.getPort()));
                break;
            }


            byte[] information = getInfoVerifyingChecksum(packet);
            DatagramPacket answerPacket;
            if(information != null)
            {
                String actualChecksum = calculateChecksum(information, 0, information.length);
                if(actualChecksum.equals(lastChecksum) != true)
                {
                    arrayList.add(information);
                    lastChecksum = actualChecksum;
                }
                byte[] answer = new byte[1];
                answer[0] = (byte)1;

                answerPacket = new DatagramPacket(answer, answer.length, packet.getAddress(), packet.getPort());

                socket.send(answerPacket);
            }
            else
            {
                byte[] answer = new byte[1];
                answer[0] = (byte)0;

                answerPacket = new DatagramPacket(answer, answer.length, packet.getAddress(), packet.getPort());

                socket.send(answerPacket);
            }
        }

        int finalSize = 0;
        for(byte[] bytes : arrayList)
        {
            finalSize = finalSize + bytes.length;
        }

        byte[] finalBytesArray = new byte[finalSize];

        int i = 0;
        for(byte[] bytes : arrayList)
        {
            for(byte b : bytes)
            {
                finalBytesArray[i] = b;
                i++;
            }
        }

        
        String finalData = (String)Serialization.deserializeFromByteArrayToObject(finalBytesArray);
       

        return finalData;
    }
    

    /**
     * UDP - Multicast
     * @param bytes
     * @param socket
     * @param ip
     * @param port
     * @throws IOException
     */
    public static void sendDataMulticast(String objectInJson, DatagramSocket socket, InetAddress ip, int port, MyInteger numberOfServers) throws IOException
    {
        synchronized(socket)
        {   
            byte[] bytes = Serialization.serializeObjectToByteArray(objectInJson);

            int sendedBytes = 0;

            boolean sendAgain = false;

            while(sendedBytes < bytes.length)
            {
                sendAgain = false;
                
                int bufferSize = 0;

                if(bytes.length - sendedBytes < BUFFER_SIZE)
                {
                    bufferSize = bytes.length - sendedBytes;
                }
                else
                {
                    bufferSize = BUFFER_SIZE;
                }
                DatagramPacket packet = constructDatagramPacket(bytes, sendedBytes, bufferSize, ip , port);
                socket.send(packet);
                DatagramPacket responsePacket = new DatagramPacket(new byte[1], 1);

                for(int i = 0; i < numberOfServers.getInteger() - 1;)
                {
                    try
                    {
                        socket.receive(responsePacket);
                        if(responsePacket.getData()[0] == (byte)1)
                        {
                            i++;   
                        }
                        else
                        {
                            sendAgain = true;
                            break;
                        }
                    }
                    catch(SocketTimeoutException exception)
                    {
                        sendAgain = true;
                        break;
                    }
                }

                if(sendAgain == true)
                {
                    continue;
                }
                else
                {
                    sendedBytes = sendedBytes + bufferSize;
                }
            }

            while(true)
            {
                sendAgain = false;

                socket.send(new DatagramPacket(new byte[0], 0, ip, port));
                for(int i = 0; i < numberOfServers.getInteger() - 1;)
                {
                    try
                    {
                        socket.receive(new DatagramPacket(new byte[0], 0));
                        i++;
                    }
                    catch(SocketTimeoutException exception)
                    {
                        sendAgain = true;
                        break;
                    }
                }

                if(sendAgain == true)
                {
                    continue;
                }
                else
                {
                    break;
                }
            }
        }
    }
    

    
    public static String receiveDataMulticast(MulticastSocket socket, int disseminateUpdatesSocketPort)throws IOException, ClassNotFoundException, SocketTimeoutException
    {
        String finalData = null;

        ArrayList<byte[]> arrayList = new ArrayList<byte[]>();

        String lastChecksum = "";

        while(true)
        {
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE + 1000], BUFFER_SIZE + 1000);

            socket.receive(packet);
            if(packet.getAddress().getHostAddress().equals(InetAddress.getLocalHost().getHostAddress()) == true && disseminateUpdatesSocketPort == packet.getPort())
            {
                continue;
            }

            if(packet.getLength() == 0)
            {
                socket.send(new DatagramPacket(new byte[0], 0, packet.getAddress(), packet.getPort()));
                break;
            }


            byte[] information = getInfoVerifyingChecksum(packet);
            DatagramPacket answerPacket;
            if(information != null)
            {
                String actualChecksum = calculateChecksum(information, 0, information.length);
                if(actualChecksum.equals(lastChecksum) != true)
                {
                    arrayList.add(information);
                    lastChecksum = actualChecksum;
                }
                byte[] answer = new byte[1];
                answer[0] = (byte)1;

                answerPacket = new DatagramPacket(answer, answer.length, packet.getAddress(), packet.getPort());

                socket.send(answerPacket);
            }
            else
            {
                byte[] answer = new byte[1];
                answer[0] = (byte)0;

                answerPacket = new DatagramPacket(answer, answer.length, packet.getAddress(), packet.getPort());

                socket.send(answerPacket);
            }
        }

        int finalSize = 0;
        for(byte[] bytes : arrayList)
        {
            finalSize = finalSize + bytes.length;
        }

        byte[] finalBytesArray = new byte[finalSize];

        int i = 0;
        for(byte[] bytes : arrayList)
        {
            for(byte b : bytes)
            {
                finalBytesArray[i] = b;
                i++;
            }
        }

        
        finalData = (String)Serialization.deserializeFromByteArrayToObject(finalBytesArray);  

        return finalData;
    }





    /**
     * Calculates de checksum of the byteArray to send.
     * Calculates the length of the checksum.
     * Creates a String with max length 10 that will store the length of the checksum.
     * This String is composed by 7 bytes of headers and a max and 3 bytes of the actual checksumlength.
     * Then serializes that string that is much shorter than an Integer
     * The appends all the arrayBytes into a big one to be sent in the packet.
     * 
     */
    private static DatagramPacket constructDatagramPacket(byte[] byteArray, int offset, int length, InetAddress ip , int port) throws IOException
    {
        String checksum = calculateChecksum(byteArray, offset, length);
        byte[] checksumBytes = checksum.getBytes();

        int checksumSize = checksumBytes.length;

        StringBuilder temp = new StringBuilder(CHECKSUM_STRING_LENGTH);
        int lengthTamCheckSum = Integer.toString(checksumSize).length();

        for(int i=0; i< CHECKSUM_STRING_LENGTH - lengthTamCheckSum ; i++){
            temp.append("0");
        }
        temp.append(Integer.toString(checksumSize));

        byte[] checksumSizeBytes = temp.toString().getBytes();

        int totalSize = checksumSizeBytes.length + checksum.getBytes().length + length;
        

        byte[] finalByteArray = new byte[totalSize];
        int conta = 0;
        for(int i=0; i<checksumSizeBytes.length ; i++)
            finalByteArray[conta++] = checksumSizeBytes[i];
        for(int i = 0 ; i<checksum.getBytes().length ; i++)
            finalByteArray[conta++] = checksumBytes[i];
        for(int i=offset ; i < offset + length; i++)
            finalByteArray[conta++] = byteArray[i];
            
        return new DatagramPacket(finalByteArray, totalSize, ip, port);
    }






    private static byte[] getInfoVerifyingChecksum(DatagramPacket packet)throws IOException, ClassNotFoundException
    {
        int conta = 0;

        byte[] packetByteArray = new byte[packet.getLength()];
        for(int i = 0; i < packet.getLength(); i++)
        {
            packetByteArray[i] = packet.getData()[i];
        }


        byte[] checksumSizeBytes = new byte[CHECKSUM_STRING_LENGTH];

        for( ; conta<CHECKSUM_STRING_LENGTH ; conta++)
        {
            checksumSizeBytes[conta] = packetByteArray[conta];
        }
        String checkSumSizeString = new String(checksumSizeBytes,0,checksumSizeBytes.length);
        int checksumSize = Integer.parseInt(checkSumSizeString);

        byte[] checksumBytes = new byte[checksumSize]; 
        for( int i=0 ; i<checksumSize ; i++, conta++ )
        {
            checksumBytes[i] = packetByteArray[conta];
        }
        String checksum = new String(checksumBytes, 0, checksumSize);

        int byteArraySize = packet.getLength() - conta; //CRITICO
        byte[] byteArray = new byte[byteArraySize];
        for( int i=0 ; conta<packet.getLength() ; i++, conta++ )
        {
            byteArray[i] = packetByteArray[conta];
        }

        String supposedChecksum = calculateChecksum(byteArray, 0, byteArraySize);

        if(supposedChecksum.equals(checksum))
        {
            return byteArray;
        }
        else
        {
            return null;
        }
    }



    /**
     * Creates a checksum based on the established array of prime numbers and returns it in the
     * format of a String
     * @param byteArray byte array from where to form the checksum
     * @param offset offset where the checksum should start
     * @param length length of the byteArray from where to create the checksum
     * @return String with the checksum
     */
    private static String calculateChecksum(byte[] byteArray, int offset, int length)
    {
        long hashNumber = 0;
        long hashNumber2 = 0;
        
        for( int i = offset, s=0 ; i< offset + length ; i++, s++)
        {
            if (s == HASH_TABLE_SIZE)
            {
                s = 0;
            }
            hashNumber += byteArray[i] * hashTable[s];
        }
        
        for( int i = offset, s=6 ; i< offset + length ; i++, s--)
        {
            hashNumber2 += byteArray[i] * hashTable[s];
            if (s == 0)
            {
                s = 7;
            }
        }

        
        return Long.toString(hashNumber * 3) + Long.toString(hashNumber2 * 7) + Long.toString(hashNumber + hashNumber2);
    }
    
    
    public static void main(String[] args) throws Exception
    {

        
    }
}
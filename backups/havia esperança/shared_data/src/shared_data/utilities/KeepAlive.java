package shared_data.utilities;

public class KeepAlive
{
    private static Boolean keepAlive = true;

    private static int numberOfServers = 0;

    public static boolean getKeepAlive() { 
        boolean temp;
        synchronized(KeepAlive.keepAlive){
            temp = KeepAlive.keepAlive;
        }
        return temp; 
    }

    public static void setKeepAlive(boolean keepAlive) { 
        synchronized(KeepAlive.keepAlive){
            KeepAlive.keepAlive = keepAlive;
        }
    }

    public static void emergencyExit(Exception exception)
    {
        KeepAlive.setKeepAlive(false);
        exception.printStackTrace();
    }

    public static int getNumberOfServers() {
        return numberOfServers;
    }

    public static void setNumberOfServers(int numberOfServers) {
        KeepAlive.numberOfServers = numberOfServers;
    }

    
}
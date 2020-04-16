package shared_data.utilities;

public class KeepAlive
{
    private static MyBoolean keepAlive = new MyBoolean(true);

    private static MyBoolean normalShutdown = new MyBoolean(false);

    private static int numberOfServers = 0;

    public static boolean getKeepAlive() { 
        boolean temp;
        synchronized(KeepAlive.keepAlive){
            temp = KeepAlive.keepAlive.getBoolean();
        }
        return temp; 
    }

    public static void setKeepAlive(boolean keepAlive) { 
        synchronized(KeepAlive.keepAlive){
            KeepAlive.keepAlive.setBoolean(keepAlive);
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

    public static void setNormalShutdown(boolean normalShutdown)
    {
        synchronized(KeepAlive.normalShutdown)
        {
            KeepAlive.setKeepAlive(normalShutdown);
        }
    }

    public static boolean getNormalShutdown()
    {
        boolean temp;

        synchronized(KeepAlive.normalShutdown)
        {
            temp = normalShutdown.getBoolean();
        }

        return temp;
    }
}
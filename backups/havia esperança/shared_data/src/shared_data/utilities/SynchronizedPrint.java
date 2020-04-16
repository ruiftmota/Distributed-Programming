package shared_data.utilities;

/**
 * SynchronizedPrint
 */
public class SynchronizedPrint
{

    public static synchronized void printLine(Object object)
    {
        System.out.println("\n" + object + "\n");
    }
    
}
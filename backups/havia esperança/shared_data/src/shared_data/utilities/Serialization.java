package shared_data.utilities;

import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;

public class Serialization
{
    private static Gson gson = null;

    public static String serializeObjectToJson(Object object)
    {
        if(gson == null)
        {
            gson = new Gson();
        }
        
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(object.getClass().getCanonicalName() + "\n");

        stringBuilder.append(gson.toJson(object));

        return stringBuilder.toString();
    }

    
    public static Object deserializeObjectFromJson(String objectInJsonFormat) throws ClassNotFoundException
    {
        if(gson == null)
        {
            gson = new Gson();
        }

        Scanner scanner = new Scanner(objectInJsonFormat);

        Class<?> classOfObject = Class.forName(scanner.next());
        String json = scanner.next();

        scanner.close();

        return gson.fromJson(json, classOfObject);
    }

    /**
     * Serializes the object to By array
     * @param object to serialize
     * @return the byte array
     * @throws IOException
     */
    public static byte[] serializeObjectToByteArray(Object object) throws IOException
    {
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        ObjectOutputStream oOS = new ObjectOutputStream(bAOS);
        oOS.writeObject(object);
        oOS.close();

        return bAOS.toByteArray();
    }


    /**
     * Deserializes the the byte array back to an object
     * @param array the array from where to form an object
     * @return the object readen
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserializeFromByteArrayToObject(byte[] array) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bAIS = new ByteArrayInputStream(array);
        ObjectInputStream oIS = new ObjectInputStream(bAIS);
        Object obj = oIS.readObject();
        oIS.close();

        return obj;
    }
}
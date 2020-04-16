package shared_data.utilities;

/**
 * MyInteger
 */
public class MyInteger
{

    private int integer = 0;

    public MyInteger(int integer)
    {
        this.integer = integer;
    }

    public int getInteger()
    {
        int temp;

        synchronized(this)
        {
            temp = integer;
        }

        return temp;
    }

    public void setInteger(int integer)
    {
        synchronized(this)
        {
            this.integer = integer;
        }
    }

    public void add(int integer)
    {
        synchronized(this)
        {
            this.integer = this.integer + integer;
        }
    }

    public void subtract(int integer)
    {
        synchronized(this)
        {
            this.integer = this.integer - integer;
        }
    }
    
}
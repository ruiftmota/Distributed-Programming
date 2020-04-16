package shared_data.communication.client_to_server.businesslogic;

public class User
{
    private String name;
    private String username;
    private String password;

    public User(String username, String password, String name)
    {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.name = "";
    }

    public String getName()
    {
        return name;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
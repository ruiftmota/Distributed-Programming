package shared_data.communication.client_to_server.requests;

import shared_data.communication.Request;
import shared_data.communication.client_to_server.businesslogic.User;

/**
 * LoginRequest
 */
public class LoginRequest extends Request
{

    private User user;

    public LoginRequest(User user)
    {
        
        this.user = user;
    }

    public User getUser()
    {
        return user;
    }
    
}
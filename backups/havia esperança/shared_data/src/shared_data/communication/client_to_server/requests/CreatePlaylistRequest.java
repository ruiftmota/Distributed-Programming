package shared_data.communication.client_to_server.requests;

import shared_data.communication.Request;
import shared_data.communication.client_to_server.businesslogic.User;

/**
 * CreatePlaylistRequest
 */
public class CreatePlaylistRequest extends Request
{

    private User user;
    private String name;

    public CreatePlaylistRequest(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public User getUser() {
        return user;
    }


    public String getName() {
        return name;
    }
    
    
}
package shared_data.communication.server_to_server_directory;

public interface ServerToServerDirectoryInterface
{
    EnumActionResult loginServer(String ip, int port);
    EnumActionResult logoutServer(String ip, int port);
}
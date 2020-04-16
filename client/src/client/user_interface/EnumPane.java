package client.user_interface;

public enum EnumPane
{
    NON_AUTHENTICATED_PANE,
    AUTHENTICATED_PANE,
    
    LOGIN_PANE(2),
    REGISTER_PANE(3),
    UPLOAD_SONG_PANE(7),
    DOWNLOAD_SONG_PANE(1),
    EDIT_SONG_NAME_PANE(2),
    EDIT_SONG_AUTHOR_PANE(2),
    DELETE_SONG_PANE(1),
    CREATE_PLAYLIST_PANE(1),
    EDIT_PLAYLIST_NAME_PANE(2),
    ADD_SONG_TO_PLAYLIST_PANE(2),
    REMOVE_SONG_FROM_PLAYLIST_PANE(2),
    REMOVE_PLAYLIST_PANE(1),
    DOWNLOAD_PLAYLIST_PANE(1),
    LIST_PLAYLISTS_PANE,
    LIST_SONGS_PANE,
    LIST_DOWNLOADED_SONGS_PANE;

    private int numberOfParameters;

    private EnumPane(int numberOfParameters)
    {
        this.numberOfParameters = numberOfParameters;
    }

    private EnumPane()
    {
        this.numberOfParameters = 0;
    }

    public int getNumberOfParameters()
    {
        return numberOfParameters;
    }


}
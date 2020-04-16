package shared_data.communication.client_to_server.requests;

import shared_data.communication.client_to_server.businesslogic.User;
import shared_data.communication.Request;

/**
 * UploadSongRequest
 */
public class UploadSongRequest extends Request
{

    private User user;
    private String songName;
    private String author;
    private int year;
    private String album;
    private float duration;
    private String category;
    private String fileName;


    public UploadSongRequest(User user, String songName, String author, int year,
                             String album, float duration, String category, String fileName)
    {
        this.user = user;
        this.songName = songName;
        this.author = author;
        this.year = year;
        this.album = album;
        this.duration = duration;
        this.category = category;
        this.fileName = fileName;
    }

    
    public User getUser() {
        return user;
    }


    public String getSongName() {
        return songName;
    }


    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getAlbum() {
        return album;
    }


    public float getDuration() {
        return duration;
    }


    public String getCategory() {
        return category;
    }

    public String getFileName() {
        return fileName;
    }

    

    
}
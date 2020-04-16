package shared_data.communication.client_to_server.businesslogic;

/**
 * Song
 */
public class Song
{

    private String name;
    private String author;
    private String path;
    private float duration;
    private int year;
    private String category;
    private String album;

    public Song(String name, String author, String path, float duration, int year, String category, String album) {
        this.name = name;
        this.author = author;
        this.path = path;
        this.duration = duration;
        this.year = year;
        this.category = category;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPath() {
        return path;
    }

    public float getDuration() {
        return duration;
    }

    public int getYear() {
        return year;
    }

    public String getCategory() {
        return category;
    }

    public String getAlbum() {
        return album;
    }


}
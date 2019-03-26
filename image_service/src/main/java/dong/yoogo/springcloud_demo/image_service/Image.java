package dong.yoogo.springcloud_demo.image_service;


import java.io.Serializable;

public class Image implements Serializable {
    private final int id;
    private final String name;
    private final String url;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Image(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}

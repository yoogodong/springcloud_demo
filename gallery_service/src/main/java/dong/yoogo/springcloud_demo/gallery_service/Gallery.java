package dong.yoogo.springcloud_demo.gallery_service;

import java.util.List;

public class Gallery {
    private int id;

    private List<Object> images;

    public int getId() {
        return id;
    }

    public List<Object> getImages() {
        return images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImages(List<Object> images) {
        this.images = images;
    }
}

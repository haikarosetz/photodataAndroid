package photoes.k15.photodata.pojo;

/**
 * Created by root on 7/3/17.
 */

public class ResourceItem {

    private int id;
    private String path;
    private int event_id;

    public ResourceItem(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }
}

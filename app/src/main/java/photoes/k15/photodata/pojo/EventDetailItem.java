package photoes.k15.photodata.pojo;

import java.util.List;

import cz.msebera.android.httpclient.client.cache.Resource;

/**
 * Created by root on 6/2/17.
 */

public class EventDetailItem {

    private String latitude;
    private String longitude;
    private List<ResourceItem> resources;
    private String description;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<ResourceItem> getResources() {
        return resources;
    }

    public void setResources(List<ResourceItem> resources) {
        this.resources = resources;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package example.jackliu.nonewfriends;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by yoonlee on 3/5/18.
 */

@IgnoreExtraProperties
public class LocationData {
    public String sender;
    public double lat;
    public double lng;
    public long timestamp;

    public LocationData(){
    }

    public LocationData sender(String sender){
        this.sender = sender;
        return this;
    }

    public LocationData lat(double lat){
        this.lat = lat;
        return this;
    }

    public LocationData lng(double lng) {
        this.lng = lng;
        return this;
    }

    public LocationData timestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getSender() {
        return sender;
    }

    public double getLat(){
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public long getTimestamp(){
        return timestamp;
    }
}

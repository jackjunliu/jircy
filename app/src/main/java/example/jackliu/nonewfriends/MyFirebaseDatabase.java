package example.jackliu.nonewfriends;

import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yoonlee on 3/5/18.
 */

public class MyFirebaseDatabase {
    private static final String TAG = "MyFirebaseDatabase";

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private LocationData loc;

    private String user;

    public String getUser() {
        return user;
    }

    public LocationData getLocationData() {
        return loc;
    }

    public MyFirebaseDatabase(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser().getEmail();
        loc = new LocationData().sender(auth.getCurrentUser().getEmail());
    }

    public void addLocation(Location coordinates){
        loc.lat(coordinates.getLatitude())
                .lng(coordinates.getLongitude())
                .timestamp(System.currentTimeMillis() / 1000L);
        DatabaseReference myRef = database.getReference();
        int endEmail = user.indexOf('@');
        myRef.child("location").child(user.substring(0,endEmail))
                .setValue(loc);
    }
}
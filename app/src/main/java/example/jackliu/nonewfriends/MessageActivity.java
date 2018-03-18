package example.jackliu.nonewfriends;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.location.Location;
import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;



public class MessageActivity extends AppCompatActivity implements LocationListener{

    private static final String TAG = "MessageActivity";

    //declare firebase variable
    private FirebaseAuth auth;

    private EditText Radius, MessageBroadcast;

    private Button sendButton, sendMessageButton;

    LocationManager locationManager;

    double latitude, longitude;

    private LocationHelper.GPSCoordinates GPSlocation = new LocationHelper.GPSCoordinates(0,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get firebase instance
        auth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        LocationHelper.LocationCallback callback = new LocationHelper.LocationCallback() {
            @Override
            public void onNewLocationAvailable(LocationHelper.GPSCoordinates location) {
                GPSlocation = location;
                Log.i(TAG, "onNewLocationAvailable: Location updated" + location.longitude + " " + location.latitude);
            }
        };
        LocationHelper.requestSingleUpdate(this, callback, this);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
                    @Override
                    public void notificationReceived(OSNotification notification) {
                        String message = notification.toString();
                        Log.i(TAG, "notificationReceived: " + message);
                    }
                })
                .init();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        OneSignal.setEmail(email);

        OneSignal.sendTag("User_ID", email);
        setContentView(R.layout.activity_message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        //location permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        // [END handle_data_extras]

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(MessageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        sendMessageButton = findViewById(R.id.send_message_now);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBroadcast = (EditText) findViewById(R.id.message_input);
                String message = MessageBroadcast.getText().toString().trim();
                //get currentuser's location
                //send it to mapsactivity
                //post a new spot on the map
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(getApplicationContext(), "Enter a message!", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }
            }
        });
    }

    void getLocation() {
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        return;

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MessageActivity.this, "GPS is currently disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void emailCheck(final String msg) {

        auth.fetchProvidersForEmail(msg)
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        boolean check = !task.getResult().getProviders().isEmpty();

                        if (!check) {
                            Toast.makeText(getApplicationContext(), "Email does not exist",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void sendMessage(final String message) {
        /*
         Should broadcast a message to users in the area
         Begin by sending currentUser's location to the map
         Then we should have a separate page, where we see all those in the area, and their message
        */
        //send message to users
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //Tests if email exists, if it does, then sends notification to email owner.
//                    Log.d(TAG, msg);
//                    if (TextUtils.isEmpty(msg)) {
//                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    send_email = msg;

                    try {
                        Radius= (EditText) findViewById(R.id.desired_range);
                        String rangeMeters = String.valueOf(Integer.parseInt(Radius.getText().toString())).trim();
                        if (rangeMeters.isEmpty()){
                            rangeMeters = "2000";
                        }
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        //needs unique Authorization api key from onesignal - this is Yoon's
                        con.setRequestProperty("Authorization", "Basic OGZjYmE4YWMtZTY2Mi00MTY5LTk0MTYtOWNiZjNmZDJjODhi");
                        con.setRequestMethod("POST");

                        String lat = String.valueOf(LocationHelper.GPSCoordinates.latitude);
                        String lng = String.valueOf(LocationHelper.GPSCoordinates.longitude);
                        String strJsonBody = "{"
                                //+ "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                //needs unique app_id from OneSignal - this is Yoon's
                                + "\"app_id\": \"c9cf3c94-40f2-4f67-aeeb-e83db45aa5f6\","

                                + "\"filters\": [{\"field\": \"location\", \"radius\": \""+ rangeMeters + "\", \"lat\": \"" + lat + "\", \"long\": \"" + lng + "\"}],"

//                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + message + "\"}"
                                + "}";


                        Log.i(TAG, "run:strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        Log.i(TAG, "run:httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        Log.i(TAG, "run:jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }


}


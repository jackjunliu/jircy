package example.jackliu.nonewfriends;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import android.location.LocationManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;


public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    //declare firebase variable
    private FirebaseAuth auth;

    private EditText SendRecipient, MessageBroadcast;

    private Button sendButton, sendMessageButton;

    private double longitude, latitude;
    private Location LastLocation;
    public LocationManager loc_man;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get firebase instance
        auth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
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
            String channelId = getString(R.string.default_notification_channel_id);
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
        // [END handle_data_extras]

        Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.default_notification_channel_name));
                // [END subscribe_topics]

                // Log and toast
                String msg = getString(R.string.msg_subscribed);
                Log.d(TAG, msg);
                Toast.makeText(MessageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

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

        sendButton = findViewById(R.id.send_notification);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRecipient = (EditText) findViewById(R.id.send_email_input);
                //Testing if toast works
                //Toast.makeText(MessageActivity.this, msg, Toast.LENGTH_SHORT).show();
                String msg = SendRecipient.getText().toString().trim();
//                emailCheck(msg);
//                sendNotification(msg);
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

        int LOCATION_REFRESH_TIME = 1000; // 1000 ms
        int LOCATION_REFRESH_DISTANCE = 2000; //2 km

        loc_man = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loc_man.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, loc_listener);


    }



    private final LocationListener loc_listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LastLocation = location;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
            //turns off gps services
        }
    };
//    private void sendNotification(final String msg) {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                int SDK_INT = android.os.Build.VERSION.SDK_INT;
//                if (SDK_INT > 8) {
//                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                            .permitAll().build();
//                    StrictMode.setThreadPolicy(policy);
//                    String send_email;
//
//                    //Tests if email exists, if it does, then sends notification to email owner.
//                    Log.d(TAG, msg);
//                    if (TextUtils.isEmpty(msg)) {
//                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    send_email = msg;
//
//                    try {
//                        String jsonResponse;
//
//                        URL url = new URL("https://onesignal.com/api/v1/notifications");
//                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                        con.setUseCaches(false);
//                        con.setDoOutput(true);
//                        con.setDoInput(true);
//
//                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//                        //needs unique Authorization api key from onesignal - this is Yoon's
//                        con.setRequestProperty("Authorization", "Basic OGZjYmE4YWMtZTY2Mi00MTY5LTk0MTYtOWNiZjNmZDJjODhi");
//                        con.setRequestMethod("POST");
//
//                        String strJsonBody = "{"
//                                //needs unique app_id from OneSignal - this is Yoon's
//                                + "\"app_id\": \"c9cf3c94-40f2-4f67-aeeb-e83db45aa5f6\","
//
//                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
//
//                                + "\"data\": {\"foo\": \"bar\"},"
//                                + "\"contents\": {\"en\": \"" + messageBox + "\"}"
//                                + "}";
//
//
//                        System.out.println("strJsonBody:\n" + strJsonBody);
//
//                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
//                        con.setFixedLengthStreamingMode(sendBytes.length);
//
//                        OutputStream outputStream = con.getOutputStream();
//                        outputStream.write(sendBytes);
//
//                        int httpResponse = con.getResponseCode();
//                        System.out.println("httpResponse: " + httpResponse);
//
//                        if (httpResponse >= HttpURLConnection.HTTP_OK
//                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
//                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
//                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
//                            scanner.close();
//                        } else {
//                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
//                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
//                            scanner.close();
//                        }
//                        System.out.println("jsonResponse:\n" + jsonResponse);
//
//                    } catch (Throwable t) {
//                        t.printStackTrace();
//                    }
//                }
//            }
//        });
//    }


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

            //update user location

            //send message to users

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    //Tests if email exists, if it does, then sends notification to email owner.
//                    Log.d(TAG, msg);
//                    if (TextUtils.isEmpty(msg)) {
//                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    send_email = msg;
                    send_email = "jiraiyaliu@gmail.com";

                    try {
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

                        String strJsonBody = "{"
                                //+ "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"
                                //needs unique app_id from OneSignal - this is Yoon's
                                + "\"app_id\": \"c9cf3c94-40f2-4f67-aeeb-e83db45aa5f6\","

                                + "\"filters\": [{\"field\": \"location\", \"radius\": \"2000\", \"lat\": \" " + latitude + "\", \"long\": \"" + longitude + "\"],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + message + "\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

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
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }


}

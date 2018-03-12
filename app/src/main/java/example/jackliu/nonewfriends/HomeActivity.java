package example.jackliu.nonewfriends;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by jackliu on 2/7/18.
 */

public class HomeActivity extends AppCompatActivity {
    //put in button code
    private Button OpenMapButton, NotificationButton;
    private TextView email;
    private Button signOut;
    private Button sendMsg;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private static final String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Buttons
        OpenMapButton = (Button) findViewById(R.id.open_map_button);
        NotificationButton = (Button) findViewById(R.id.notification_button);
        signOut = (Button) findViewById(R.id.sign_out);
        email = (TextView) findViewById(R.id.email);
        sendMsg = (Button) findViewById(R.id.send_message_button);

        OpenMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(HomeActivity.this, MessageActivity.class));
            }
        });

        //Notification Button that makes a Popup Yes/No
        NotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setMessage("Would you like some ice cream?");

                alertDialogBuilder.setPositiveButton("Absolutely", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(HomeActivity.this,"You have gotten Chocolate Ice Cream!",Toast.LENGTH_LONG).show();
                    }
                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut(); //signs people out since they disagree
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //checks if the user is logged out or not, if so, display login page
        //remember that if user is logged in, it (is) NULL
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        email.setText(user.getEmail());

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}


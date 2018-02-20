package example.jackliu.nonewfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



/**
 * Created by Ronald Hoang on 2/14/2018.
 */

public class sentMsgScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set view
        setContentView(R.layout.sentmsgscreen);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000); //1 seconds
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        };
        thread.start();

    }
}



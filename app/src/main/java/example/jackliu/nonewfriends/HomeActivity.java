package example.jackliu.nonewfriends;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.content.Intent;



/**
 * Created by jackliu on 2/7/18.
 */

public class HomeActivity extends AppCompatActivity {
    //put in button code
    private Button OpenMapButton, NotificationButton;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenMapButton = (Button) findViewById(R.id.open_map_button);
        NotificationButton = (Button) findViewById(R.id.notification_button);

        OpenMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Implement map page here
                Intent openMap = new Intent(HomeActivity.this, SignupActivity.class);
                startActivity(openMap);
            }
        });

        NotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

}

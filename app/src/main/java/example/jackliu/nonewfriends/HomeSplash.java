package example.jackliu.nonewfriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToSignup(View view) {
        Intent intent = new Intent(HomeSplash.this, SignupActivity.class);
        startActivity(intent);
    }
    public void goToMaps(View view){
        Intent intent = new Intent(HomeSplash.this, MapsActivity.class);
        startActivity(intent);
    }
}

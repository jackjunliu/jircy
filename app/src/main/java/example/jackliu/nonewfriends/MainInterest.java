package example.jackliu.nonewfriends;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

/**
 * Created by Charles on 2/21/2018.
 */

public class MainInterest extends AppCompatActivity {
    private TextView choose;
    private Button saveButton;
    private CheckBox drinkBox, sportBox, boardBox, videoBox, studyBox, eatBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set view
        setContentView(R.layout.activity_interest);
        Intent intent = new Intent(getApplicationContext(), MainInterest.class);
        startActivity(intent);
    }
}

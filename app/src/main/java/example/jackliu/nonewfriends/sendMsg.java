package example.jackliu.nonewfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class sendMsg extends AppCompatActivity {
    private Button sendbutton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_page);
        sendbutton = (Button) findViewById(R.id.sendButton) ;

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(sendMsg.this, sentMsgScreen.class));
                finish();
            }
        });
    }
}

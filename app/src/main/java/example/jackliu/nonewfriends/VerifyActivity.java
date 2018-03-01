package example.jackliu.nonewfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by irenepatt on 2/21/18.
 */

public class VerifyActivity extends AppCompatActivity {

    //get current user
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //get firebase auth instance
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private TextView email;
    private Button SendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        email = (TextView) findViewById(R.id.email);

        SendEmail = (Button) findViewById(R.id.send_email_button);

        email.setText(user.getEmail());

        SendEmail.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             user.sendEmailVerification()
                                                     .addOnCompleteListener(VerifyActivity.this, new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if (task.isSuccessful()) {
                                                                 //email is sent to verify
                                                                 Toast.makeText(VerifyActivity.this, "An email is sent to user's email address", Toast.LENGTH_SHORT).show();
                                                                 startActivity(new Intent(VerifyActivity.this, LoginActivity.class));
                                                                 finish();
                                                                 return;
                                                             } else {
                                                                 //email not sent
                                                                 Toast.makeText(VerifyActivity.this, "Email not sent" + task.getException(),
                                                                         Toast.LENGTH_SHORT).show();
                                                                 return;
                                                             }
                                                         }
                                                     });
                                         }
                                     });


    }


}

//mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
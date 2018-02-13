package example.jackliu.nonewfriends;

/**
 * Created by irenepatt on 2/6/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    //declare firebase variable
    private FirebaseAuth auth;

    //text field
    private EditText EmailField, PasswordField;

    //buttons
    private Button LogInButton, SignUpButton, ResetPasswordButton;

    //progress bar
    private ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //get firebase instance
        auth = FirebaseAuth.getInstance();

        LogInButton = (Button) findViewById(R.id.log_in_button);
        SignUpButton = (Button) findViewById(R.id.sign_up_button);

        EmailField = (EditText) findViewById(R.id.email_input);
        PasswordField = (EditText) findViewById(R.id.password_input);

        progBar = (ProgressBar) findViewById(R.id.progress_bar);
        ResetPasswordButton = (Button) findViewById(R.id.password_reset_button);

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //grab email and password
                String email = EmailField.getText().toString().trim();
                String password = PasswordField.getText().toString().trim();

                //check if email field is empty
                if (TextUtils.isEmpty(email)) {
                    //throw a msg
                    Toast.makeText(getApplicationContext(), "Please enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if password field is empty
                if (TextUtils.isEmpty(password) ) {
                    //throw a msg
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if password too short
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum of 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                //show progress bar
                progBar.setVisibility(View.VISIBLE);

                //authenticate user through firebase
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            progBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                        });
            }
        });


        ResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPassword.class));
                //finish();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progBar.setVisibility(View.GONE);
    }


}

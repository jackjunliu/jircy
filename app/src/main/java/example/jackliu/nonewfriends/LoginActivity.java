package example.jackliu.nonewfriends;

/**
 * Created by irenepatt on 2/4/18.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity{

    //declare firebase variable
    private FirebaseAuth auth;

    //text field
    private EditText EmailField, PasswordField;

    //buttons
    private Button LogInButton, SignUpButton, ResetButton;

    //progress bar
    private ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    if (!(ContextCompat.checkSelfPermission(this.getApplicationContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)) {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null && user.isEmailVerified()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        EmailField = (EditText) findViewById(R.id.email);
        PasswordField = (EditText) findViewById(R.id.password);
        progBar = (ProgressBar) findViewById(R.id.progressBar);
        SignUpButton = (Button) findViewById(R.id.btn_signup);
        LogInButton = (Button) findViewById(R.id.btn_login);
        ResetButton = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ResetPassword.class));
                }

        });

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailField.getText().toString();
                final String password = PasswordField.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        PasswordField.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    checkVerification();

                                }
                            }
                        });
            }
        });
    }

        private void checkVerification() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user.isEmailVerified()) {
                //user is verified,
                //Toast.makeText(VerifyActivity.this, "Successfully verified", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            } else {
                //email is not verified
                Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                return;
            }
        }


}
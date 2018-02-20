package example.jackliu.nonewfriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by irenepatt on 2/11/18.
 */

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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity{

    private EditText inputEmail;
    private Button resetButton, backButton;
    private FirebaseAuth auth;
    private ProgressBar progBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.email_input);
        resetButton = (Button) findViewById(R.id.password_reset_button);
        backButton = (Button) findViewById(R.id.back_button);

        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_input = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email_input)) {
                    Toast.makeText(getApplication(), "Enter registered email address", Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.sendPasswordResetEmail(email_input)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                            public void onComplete(@NonNull Task<Void> task ) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPassword.this, "Instructions have been sent to reset your password!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                                    startActivity(intent);
//                                    finish();
                                } else {
                                    Toast.makeText(ResetPassword.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                                }


                        }
                        });

            }

        });
    }

}

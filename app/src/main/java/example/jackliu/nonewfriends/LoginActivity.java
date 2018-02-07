package example.jackliu.nonewfriends;

/**
 * Created by irenepatt on 2/4/18.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class LoginActivity extends AppCompatActivity{

    //declare firebase variable
    private FirebaseAuth auth;

    //text field
    private EditText EmailField, PasswordField;

    //buttons
    private Button LogInButton, SignUpButton;

    //progress bar
    private ProgressBar progBar;



}

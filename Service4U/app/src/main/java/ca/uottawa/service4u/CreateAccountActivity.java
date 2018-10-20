package ca.uottawa.service4u;

import java.util.Map;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    public void createAccount(View view) {
        if (!validateForm()) {
            return;
        }

        String email = ((EditText) findViewById(R.id.fieldEmail2)).getText().toString();
        String password = ((EditText) findViewById(R.id.fieldPassword2)).getText().toString();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //this is wrong -- get user type from checked radio
                            String userType = findViewById(R.id.userTypeRadio).toString();

                            /* add custom claims to authorize database
                            //requires Firebase Admin SDK
                            Map<String, Object> claims = user.getCustomClaims();
                            claims.put("user_type", userType);

                            mAuth.setCustomUserClaims(user.getUid(), claims);
                            */

                            addUserToDatabase(user);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult (intent,0);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Account creation failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = ((EditText) findViewById(R.id.fieldEmail2)).getText().toString();
        if (TextUtils.isEmpty(email)) {
            ((EditText) findViewById(R.id.fieldEmail2)).setError("Required.");
            valid = false;
        } else {
            ((EditText) findViewById(R.id.fieldEmail2)).setError(null);
        }

        //require 6 length
        String password = ((EditText) findViewById(R.id.fieldPassword2)).getText().toString();
        if (TextUtils.isEmpty(password)) {
            ((EditText) findViewById(R.id.fieldPassword2)).setError("Required.");
            valid = false;
        } else {
            ((EditText) findViewById(R.id.fieldPassword2)).setError(null);
        }

        String password2 = ((EditText) findViewById(R.id.fieldPassword3)).getText().toString();
        if (TextUtils.isEmpty(password2)) {
            ((EditText) findViewById(R.id.fieldPassword3)).setError("Required.");
            valid = false;
        } else if (!password.equals(password2)){
            ((EditText) findViewById(R.id.fieldPassword3)).setError("Passwords must match.");
            valid = false;
        } else {
            ((EditText)findViewById(R.id.fieldPassword3)).setError(null);
        }

        return valid;
    }


    public void addUserToDatabase(FirebaseUser user){
        //use the data in the fields to add user to database
        //do not add password to database as it's already stored in Firebase's Authentication
        user.getUid();
        user.getEmail();
        findViewById(R.id.userTypeRadio);
        findViewById(R.id.fieldPhone);
    }

}

package ca.uottawa.service4u;

import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    public DatabaseReference databaseProcducts;
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
        String username = ((EditText) findViewById(R.id.fieldUsername)).getText().toString().trim();
        String[] arrayName = username.split(" ", 2);
        final String firstName = arrayName[0];
        final String lastName = arrayName[1];
        final String phoneNumber = ((EditText) findViewById(R.id.fieldPhone)).getText().toString();
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String userType = "none";
                            //this is wrong -- get user type from checked radio
                            //String userType = findViewById(R.id.userTypeRadio).toString();
                            RadioButton admin, homeOwner, serviceProvider;
                            admin = (RadioButton) findViewById(R.id.radioButton);
                            homeOwner = (RadioButton) findViewById(R.id.radioButton2);
                            serviceProvider = (RadioButton) findViewById(R.id.radioButton3);
                            if(admin.isChecked()){
                                userType = "admin";
                            }
                            if(homeOwner.isChecked()){
                                userType = "home owner";
                            }
                            if(serviceProvider.isChecked()){
                                userType = "service provider";
                            }

                            /* add custom claims to authorize database
                            //requires Firebase Admin SDK
                            Map<String, Object> claims = user.getCustomClaims();
                            claims.put("user_type", userType);

                            mAuth.setCustomUserClaims(user.getUid(), claims);
                            */

                            addUserToDatabase(user);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult (intent,0);
                            String id = databaseProcducts.push().getKey();
                            User user_1 = new User(firstName, lastName, userType, phoneNumber);
                            //databaseProcducts.child(id).setValue(user_1);
                            Toast.makeText(CreateAccountActivity.this, "Account creation complete.",
                                    Toast.LENGTH_SHORT).show();
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
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher match = pattern.matcher(email);
            if(!match.matches()){
                ((EditText) findViewById(R.id.fieldEmail2)).setError("Invalid Email Address");
                valid = false;
            }
            else{
                ((EditText) findViewById(R.id.fieldEmail2)).setError(null);
            }

        }
        String username = ((EditText) findViewById(R.id.fieldUsername)).getText().toString();
        if (TextUtils.isEmpty(username)) {
            ((EditText) findViewById(R.id.fieldUsername)).setError("Required.");
            valid = false;
        }
        else {
                ((EditText) findViewById(R.id.fieldUsername)).setError(null);
        }

        //require 6 length
        String password = ((EditText) findViewById(R.id.fieldPassword2)).getText().toString();
        if (TextUtils.isEmpty(password)) {
            ((EditText) findViewById(R.id.fieldPassword2)).setError("Required.");
            valid = false;
        }

        else {
            if(password.length()<= 6){
                ((EditText) findViewById(R.id.fieldPassword2)).setError("Requires at least 6 characters");
                valid = false;
            }
            else {
                ((EditText) findViewById(R.id.fieldPassword2)).setError(null);
            }
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

        String phoneNumber = ((EditText) findViewById(R.id.fieldPhone)).getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            ((EditText) findViewById(R.id.fieldPhone)).setError("Required.");
            valid = false;
        }
        else {
            ((EditText) findViewById(R.id.fieldPhone)).setError(null);
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

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;


public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final String dbTAG = "Database";

    // [START declare_auth]


    protected FirebaseAuth mAuth;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseUsers;

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
        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");
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

                            addUserToDatabase(user);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult (intent,0);

                            Toast.makeText(CreateAccountActivity.this, "Account creation complete.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            Log.w(TAG, "createUserWithEmail:failure", e);
                            String exMessage = e.getMessage();

                            if (exMessage.equals("The email address is badly formatted.")) {
                                ((EditText) findViewById(R.id.fieldEmail2)).setError("Invalid Email Address");
                            } else if (exMessage.equals("The given password is invalid. [ Password should be at least 6 characters ]")) {
                                ((EditText) findViewById(R.id.fieldPassword2)).setError("Password must be at least 6 characters");
                            } else {
                                Toast.makeText(CreateAccountActivity.this, "Account creation failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
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

        //phone number required for service providers and homeowners
        RadioButton admin = (RadioButton) findViewById(R.id.radioButton);
        String phoneNumber = ((EditText) findViewById(R.id.fieldPhone)).getText().toString();
        if (TextUtils.isEmpty(phoneNumber) && !admin.isChecked()) {
            ((EditText) findViewById(R.id.fieldPhone)).setError("Required unless admin.");
            valid = false;
        }
        else {
            ((EditText) findViewById(R.id.fieldPhone)).setError(null);
        }

        RadioButton homeowner = (RadioButton) findViewById(R.id.radioButton3);
        String address = ((EditText) findViewById(R.id.fieldAddress)).getText().toString();
        if (TextUtils.isEmpty(address) && homeowner.isChecked()) {
            ((EditText) findViewById(R.id.fieldAddress)).setError("Required for homeowners.");
            valid = false;
        }
        else {
            ((EditText) findViewById(R.id.fieldAddress)).setError(null);
        }


        return valid;
    }


    public void addUserToDatabase(FirebaseUser user){
        //use the data in the fields to add user to database
        //do not add password to database as it's already stored in Firebase's Authentication
        String userid = user.getUid();

        //String email = user.getEmail();

        //get name
        String username = ((EditText) findViewById(R.id.fieldUsername)).getText().toString().trim();
        String[] arrayName = username.split(" ", 2);
        String firstName = arrayName[0];
        String lastName = arrayName[1];

        //get phone
        String phoneNumber = ((EditText) findViewById(R.id.fieldPhone)).getText().toString();

        //get usertype
        String userType = "none";
        RadioButton admin, homeOwner, serviceProvider;
        admin = (RadioButton) findViewById(R.id.radioButton);
        homeOwner = (RadioButton) findViewById(R.id.radioButton2);
        serviceProvider = (RadioButton) findViewById(R.id.radioButton3);
        if(admin.isChecked()){
            userType = "admin";
        }
        if(homeOwner.isChecked()){
            userType = "homeowner";
        }
        if(serviceProvider.isChecked()){
            userType = "service provider";
        }

        //get address
        String address = ((EditText) findViewById(R.id.fieldAddress)).getText().toString();

        //add new user
        User newUser = new User(firstName, lastName, userType, phoneNumber, address);
        databaseUsers.child(userid).setValue(newUser);
    }

}

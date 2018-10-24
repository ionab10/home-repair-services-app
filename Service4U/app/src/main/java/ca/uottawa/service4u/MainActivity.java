package ca.uottawa.service4u;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "EmailPassword";
    private static final String dbTAG = "Database";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseUsers;

    String userType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.myAccountButton).setOnClickListener(this);


        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
    // [END on_start_check_user]

    private void createAccount() {
        //Application Context and Activity
        Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
        startActivityForResult (intent,0);
    }

    private void myAccount() {
        //Application Context and Activity
        Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
        startActivityForResult (intent,0);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get userType
                            databaseUsers.child(user.getUid()).child("userType").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    String value = dataSnapshot.getValue(String.class);
                                    Log.d(dbTAG, "Value is: " + value);
                                    userType = value;  //this doesn't work
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w(dbTAG, "Failed to read value.", error.toException());
                                }
                            });

                            updateUI(user);

                            //Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            //startActivityForResult (intent,0);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            mStatusTextView.setText(String.format("You are signed in as %s",userType));

            mDetailTextView.setText("TO DO");

            if (userType.equals("admin")) {
                findViewById(R.id.allUsersList).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.allUsersList).setVisibility(View.GONE);
            }

            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);


        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.allUsersList).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.emailCreateAccountButton) {
            createAccount();
        } else if (i == R.id.myAccountButton) {
            myAccount();
        } else if (i == R.id.emailSignInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signOutButton) {
            signOut();
        }
    }


}

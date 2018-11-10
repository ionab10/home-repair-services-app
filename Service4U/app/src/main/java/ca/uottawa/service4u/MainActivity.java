package ca.uottawa.service4u;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    private static final String TAG = "EmailPassword";
    private static final String dbTAG = "Database";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView userTypeTextView;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseUsers;
    List<User> allUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        userTypeTextView = findViewById(R.id.userTypeText);

        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.myAccountButton).setOnClickListener(this);
        findViewById(R.id.servicesBtn).setOnClickListener(this);
        findViewById(R.id.myJobsBtn).setOnClickListener(this);
        findViewById(R.id.bookJobBtn).setOnClickListener(this);


        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");
        allUsers = new ArrayList<>();

        ((TextView)findViewById(R.id.usersList)).setMovementMethod(new ScrollingMovementMethod());

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsers.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User appUser = postSnapshot.getValue(User.class);
                    allUsers.add(appUser);
                }

                String userList = "";
                for (User appUser : allUsers){
                    userList = userList + " "
                            + appUser.getfirstName() + " "
                            + appUser.getlastName() + " ("
                            + appUser.getuserType() + ")\n";
                }

                ((TextView)findViewById(R.id.usersList)).setText(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        currentUser();

    }
    // [END on_start_check_user]

    public void currentUser(){
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //get userType
            databaseUsers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User appUser = dataSnapshot.getValue(User.class);
                    Log.d(dbTAG, "User type is: " + appUser.getuserType());

                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user, appUser);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(dbTAG, "Failed to read value.", error.toException());
                }
            });

        }
        else {
            updateUI(null, null);
        }
    }

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

        mStatusTextView.setText("Signing in...");
        mDetailTextView.setText("Please wait.");

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            currentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null, null);
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
        updateUI(null, null);
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

    private void updateUI(FirebaseUser user, User appUser) {
        if (user != null) {

            mStatusTextView.setText(String.format("Hello %s %s", appUser.getfirstName(), appUser.getlastName()));
            mDetailTextView.setText(String.format("You are signed in as %s",appUser.getuserType()));
            userTypeTextView.setText(appUser.getuserType());


            if (appUser.getuserType().equals("admin")) {
                findViewById(R.id.allUsersList).setVisibility(View.VISIBLE);
                findViewById(R.id.servicesBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.bookJobBtn).setVisibility(View.GONE);
                findViewById(R.id.myJobsBtn).setVisibility(View.GONE);

            } else if (appUser.getuserType().equals("homeowner")) {
                findViewById(R.id.allUsersList).setVisibility(View.GONE);
                findViewById(R.id.servicesBtn).setVisibility(View.GONE);
                findViewById(R.id.bookJobBtn).setVisibility(View.VISIBLE);
                findViewById(R.id.myJobsBtn).setVisibility(View.VISIBLE);

            } else if (appUser.getuserType().equals("service provider")) {
                findViewById(R.id.allUsersList).setVisibility(View.GONE);
                findViewById(R.id.servicesBtn).setVisibility(View.GONE);
                findViewById(R.id.bookJobBtn).setVisibility(View.GONE);
                findViewById(R.id.myJobsBtn).setVisibility(View.VISIBLE);
            }

            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);


        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(R.string.please_signin);

            mPasswordField.setText("");

            findViewById(R.id.allUsersList).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
        }
    }

    public void allServices(){
        Intent intent = new Intent(getApplicationContext(), ServicesActivity.class);
        startActivityForResult (intent,0);
    }

    public void myJobs(){
        Intent intent = new Intent(getApplicationContext(), MyJobs.class);
        startActivityForResult (intent,0);
    }

    public void bookJob(){
        Intent intent = new Intent(getApplicationContext(), BookJobActivity.class);
        intent.putExtra("userType", userTypeTextView.getText().toString());
        startActivityForResult (intent,0);
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
        } else if (i == R.id.servicesBtn) {
            allServices();
        } else if (i == R.id.bookJobBtn) {
            bookJob();
        } else if (i == R.id.myJobsBtn) {
            myJobs();
        }
    }


}

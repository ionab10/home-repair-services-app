package ca.uottawa.service4u;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccountActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final String dbTAG = "Database";

    // [START declare_auth]
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseUsers;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");

        FirebaseUser user = mAuth.getCurrentUser();

        databaseUsers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ServiceAppUser appUser = dataSnapshot.getValue(ServiceAppUser.class);
                Log.d(dbTAG, "App user is: " + appUser.firstName);

                ((TextView) findViewById(R.id.firstName)).setText(String.format("First name: %s", appUser.firstName));
                ((TextView) findViewById(R.id.lastName)).setText(String.format("Last name: %s", appUser.lastName));
                ((TextView) findViewById(R.id.accountType)).setText(String.format("Account type: %s", appUser.userType));
                ((TextView) findViewById(R.id.phoneNumber)).setText(String.format("Phone: %s", appUser.phone));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(dbTAG, "Failed to read value.", error.toException());
            }
        });

        ((TextView) findViewById(R.id.emailVerified)).setText(getString(R.string.emailpassword_status_fmt,
                user.getEmail(), user.isEmailVerified()));
        ((TextView) findViewById(R.id.firebaseUID)).setText(getString(R.string.firebase_status_fmt, user.getUid()));
        ((TextView) findViewById(R.id.dateCreated)).setText(getString(R.string.meta_firebase_ui,Long.toString(user.getMetadata().getCreationTimestamp())));
    }



    public void sendEmailVerification(View view) {
        // Disable button
        findViewById(R.id.verifyEmailButton).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button


                        if (task.isSuccessful()) {
                            Toast.makeText(MyAccountActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            View b = findViewById(R.id.verifyEmailButton);
                            b.setVisibility(View.GONE);

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(MyAccountActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                            findViewById(R.id.verifyEmailButton).setEnabled(true);
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
    public void onClick(View view){
        Intent i = new Intent(this, MainActivity.class); //dont write package context android will do that
        startActivity(i);
    }
}



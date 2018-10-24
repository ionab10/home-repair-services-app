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

public class MyAccountActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // [START declare_auth]
    public FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

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



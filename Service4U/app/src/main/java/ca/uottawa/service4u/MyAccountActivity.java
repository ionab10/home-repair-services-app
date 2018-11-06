package ca.uottawa.service4u;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
                User appUser = dataSnapshot.getValue(User.class);
                Log.d(dbTAG, "App user is: " + appUser.getfirstName());

                ((TextView) findViewById(R.id.firstName)).setText(String.format("First name: %s", appUser.getfirstName()));
                ((TextView) findViewById(R.id.lastName)).setText(String.format("Last name: %s", appUser.getlastName()));
                ((TextView) findViewById(R.id.accountType)).setText(String.format("Account type: %s", appUser.getuserType()));
                ((TextView) findViewById(R.id.phoneNumber)).setText(String.format("Phone: %s", appUser.getphoneNumber()));
                ((TextView) findViewById(R.id.address)).setText(String.format("Address: %s", appUser.getAddress()));

                ((TextView) findViewById(R.id.editFirstName)).setText(appUser.getfirstName());
                ((TextView) findViewById(R.id.editLastName)).setText(appUser.getlastName());
                ((TextView) findViewById(R.id.editPhoneNumber)).setText(appUser.getphoneNumber());
                ((TextView) findViewById(R.id.editAddress)).setText(appUser.getAddress());

                if (appUser.getuserType().equals("service provider")){
                    findViewById(R.id.availabilityBtn).setVisibility(View.VISIBLE);
                    findViewById(R.id.myServicesBtn).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.availabilityBtn).setVisibility(View.GONE);
                    findViewById(R.id.myServicesBtn).setVisibility(View.GONE);
                }


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
    public void returnToMain(View view){
        Intent i = new Intent(this, MainActivity.class); //dont write package context android will do that
        startActivity(i);
    }

    public void myAvailability(View view){
        Intent i = new Intent(this, AvailabilityCalendar.class); //dont write package context android will do that
        startActivity(i);
    }

    public void myServices(View view){
        Intent i = new Intent(this, MyServices.class); //dont write package context android will do that
        startActivity(i);
    }


    public void editAccountDetails(View view){
        Button b = (Button) findViewById(R.id.editAccountBtn);

        if (b.getText().toString().equals(getString(R.string.edit_account))) {

            Log.w("debug", "edit account");

            ((TextView) findViewById(R.id.firstName)).setText("First name:");
            ((TextView) findViewById(R.id.lastName)).setText("Last name:");
            ((TextView) findViewById(R.id.phoneNumber)).setText("Phone:");
            ((TextView) findViewById(R.id.address)).setText("Address:");

            findViewById(R.id.editFirstName).setVisibility(View.VISIBLE);
            findViewById(R.id.editLastName).setVisibility(View.VISIBLE);
            findViewById(R.id.editPhoneNumber).setVisibility(View.VISIBLE);
            findViewById(R.id.editAddress).setVisibility(View.VISIBLE);

            b.setText(R.string.done);
        }
        else if (b.getText().toString().equals(getString(R.string.done))) {
            Log.w("debug", "done editing account");


            if (!validateFields()) {
                return;
            }


            String firstName = ((EditText)findViewById(R.id.editFirstName)).getText().toString();
            String lastName = ((EditText)findViewById(R.id.editLastName)).getText().toString();
            String phoneNumber = ((EditText)findViewById(R.id.editPhoneNumber)).getText().toString();
            String address = ((EditText)findViewById(R.id.editAddress)).getText().toString();


            FirebaseUser user = mAuth.getCurrentUser();
            DatabaseReference dR = databaseUsers.child(user.getUid());
            dR.child("firstName").setValue(firstName);
            dR.child("lastName").setValue(lastName);
            dR.child("phoneNumber").setValue(phoneNumber);
            dR.child("address").setValue(address);

            ((TextView) findViewById(R.id.firstName)).setText(String.format("First name: %s", firstName));
            ((TextView) findViewById(R.id.lastName)).setText(String.format("Last name: %s", lastName));
            ((TextView) findViewById(R.id.phoneNumber)).setText(String.format("Phone: %s", phoneNumber));
            ((TextView) findViewById(R.id.address)).setText(String.format("Address: %s", address));

            findViewById(R.id.editFirstName).setVisibility(View.GONE);
            findViewById(R.id.editLastName).setVisibility(View.GONE);
            findViewById(R.id.editPhoneNumber).setVisibility(View.GONE);
            findViewById(R.id.editAddress).setVisibility(View.GONE);

            b.setText(R.string.edit_account);
        }


    }

    public boolean validateFields(){

        String firstName = ((EditText)findViewById(R.id.editFirstName)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.editLastName)).getText().toString();
        String phoneNumber = ((EditText)findViewById(R.id.editPhoneNumber)).getText().toString();
        String address = ((EditText)findViewById(R.id.editAddress)).getText().toString();
        boolean validFlag = true;


        if(TextUtils.isEmpty(firstName)){
            ((EditText)findViewById(R.id.editFirstName)).setError("Required");
            validFlag = false;
        }

        if(TextUtils.isEmpty(lastName)){
            ((EditText)findViewById(R.id.editLastName)).setError("Required");
            validFlag = false;
        }
        if(!firstName.matches("[a-zA-Z]+")){
            ((EditText)findViewById(R.id.editFirstName)).setError("Invalid Name");
            validFlag = false;
        }

        if(!lastName.matches("[a-zA-Z]+")){
            ((EditText)findViewById(R.id.editLastName)).setError("Invalid Name");
            validFlag = false;
        }

        if(TextUtils.isEmpty(phoneNumber)){
            ((EditText)findViewById(R.id.editPhoneNumber)).setError("Required");
            validFlag = false;
        }
        if(phoneNumber.length() != 10){
            ((EditText)findViewById(R.id.editPhoneNumber)).setError("Invalid length");
            validFlag = false;
        }

        if(!phoneNumber.matches("[0-9]+") ){
            ((EditText)findViewById(R.id.editPhoneNumber)).setError("Invalid Phone Number");
            validFlag = false;
        }

        if(TextUtils.isEmpty(address)){
            ((EditText)findViewById(R.id.editAddress)).setError("Required");
            validFlag = false;
        }




        return validFlag;
    }




}



package ca.uottawa.service4u;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyAccountActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final String dbTAG = "Database";

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

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

                ((EditText) findViewById(R.id.editFirstName)).setText(appUser.getfirstName());
                ((EditText) findViewById(R.id.editLastName)).setText(appUser.getlastName());
                ((EditText) findViewById(R.id.editPhoneNumber)).setText(appUser.getphoneNumber());
                ((EditText) findViewById(R.id.editAddress)).setText(appUser.getAddress());

                if (appUser.getuserType().equals("service provider")){
                    ServiceProvider sp = dataSnapshot.getValue(ServiceProvider.class);

                    findViewById(R.id.scroll2edit).setVisibility(View.VISIBLE);
                    findViewById(R.id.companyNameLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.licensedLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.rating).setVisibility(View.VISIBLE);
                    findViewById(R.id.descriptionLayout).setVisibility(View.VISIBLE);

                    ((CheckBox) findViewById(R.id.licensedCB)).setChecked(sp.licensed);
                    if (sp.licensed){
                        ((TextView) findViewById(R.id.licensed)).setText(String.format("Licensed: %s", "Yes"));
                    } else {
                        ((TextView) findViewById(R.id.licensed)).setText(String.format("Licensed: %s", "No"));
                    }

                    ((TextView) findViewById(R.id.companyName)).setText(String.format("Company: %s", sp.companyName));
                    ((TextView) findViewById(R.id.rating)).setText(String.format("Rating: %.1f", sp.rating));
                    ((TextView) findViewById(R.id.description)).setText(String.format("\"%s\"", sp.description));

                    ((EditText) findViewById(R.id.editCompanyName)).setText(sp.companyName);
                    ((EditText) findViewById(R.id.editDescription)).setText(sp.description);

                    findViewById(R.id.availabilityBtn).setVisibility(View.VISIBLE);
                    findViewById(R.id.myServicesBtn).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.scroll2edit).setVisibility(View.GONE);
                    findViewById(R.id.companyNameLayout).setVisibility(View.GONE);
                    findViewById(R.id.licensedLayout).setVisibility(View.GONE);
                    findViewById(R.id.rating).setVisibility(View.GONE);
                    findViewById(R.id.descriptionLayout).setVisibility(View.GONE);
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
        ((TextView) findViewById(R.id.dateCreated)).setText(getString(R.string.meta_firebase_ui,
                datetimeFormat.format(new Date(user.getMetadata().getCreationTimestamp()))));
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
        finish();
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
            ((TextView) findViewById(R.id.companyName)).setText("Company:");
            ((TextView) findViewById(R.id.licensed)).setText("Licensed:");
            ((TextView) findViewById(R.id.phoneNumber)).setText("Phone:");
            ((TextView) findViewById(R.id.address)).setText("Address:");
            ((TextView) findViewById(R.id.description)).setText("Description: ");

            findViewById(R.id.editFirstName).setVisibility(View.VISIBLE);
            findViewById(R.id.editLastName).setVisibility(View.VISIBLE);
            findViewById(R.id.editCompanyName).setVisibility(View.VISIBLE);
            findViewById(R.id.licensedCB).setVisibility(View.VISIBLE);
            findViewById(R.id.editPhoneNumber).setVisibility(View.VISIBLE);
            findViewById(R.id.editAddress).setVisibility(View.VISIBLE);
            findViewById(R.id.editDescription).setVisibility(View.VISIBLE);

            b.setText(R.string.done);
        }
        else if (b.getText().toString().equals(getString(R.string.done))) {

            if (!validateFields()) {
                return;
            }

            Log.w("debug", "done editing account");

            String firstName = ((EditText)findViewById(R.id.editFirstName)).getText().toString();
            String lastName = ((EditText)findViewById(R.id.editLastName)).getText().toString();
            String companyName = ((EditText)findViewById(R.id.editCompanyName)).getText().toString();
            boolean licensed = ((CheckBox)findViewById(R.id.licensedCB)).isChecked();
            String phoneNumber = ((EditText)findViewById(R.id.editPhoneNumber)).getText().toString();
            String address = ((EditText)findViewById(R.id.editAddress)).getText().toString();
            String description = ((EditText)findViewById(R.id.editDescription)).getText().toString();


            FirebaseUser user = mAuth.getCurrentUser();
            DatabaseReference dR = databaseUsers.child(user.getUid());
            dR.child("firstName").setValue(firstName);
            dR.child("lastName").setValue(lastName);
            dR.child("companyName").setValue(companyName);
            dR.child("phoneNumber").setValue(phoneNumber);
            dR.child("address").setValue(address);
            dR.child("description").setValue(description);

            ((TextView) findViewById(R.id.firstName)).setText(String.format("First name: %s", firstName));
            ((TextView) findViewById(R.id.lastName)).setText(String.format("Last name: %s", lastName));
            ((TextView) findViewById(R.id.companyName)).setText(String.format("Company name: %s", companyName));
            ((TextView) findViewById(R.id.phoneNumber)).setText(String.format("Phone: %s", phoneNumber));
            ((TextView) findViewById(R.id.address)).setText(String.format("Address: %s", address));
            ((TextView) findViewById(R.id.description)).setText(String.format("%s", description));

            if (licensed){
                dR.child("licensed").setValue(true);
                ((TextView) findViewById(R.id.licensed)).setText(String.format("Licensed: %s", "Yes"));
            } else {
                dR.child("licensed").setValue(false);
                ((TextView) findViewById(R.id.licensed)).setText(String.format("Licensed: %s", "No"));
            }

            findViewById(R.id.editFirstName).setVisibility(View.GONE);
            findViewById(R.id.editLastName).setVisibility(View.GONE);
            findViewById(R.id.editCompanyName).setVisibility(View.GONE);
            findViewById(R.id.licensedCB).setVisibility(View.GONE);
            findViewById(R.id.editPhoneNumber).setVisibility(View.GONE);
            findViewById(R.id.editAddress).setVisibility(View.GONE);
            findViewById(R.id.editDescription).setVisibility(View.GONE);

            b.setText(R.string.edit_account);
        }


    }

    public boolean validateFields(){

        String firstName = ((EditText)findViewById(R.id.editFirstName)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.editLastName)).getText().toString();
        String companyName = ((EditText)findViewById(R.id.editCompanyName)).getText().toString();
        String phoneNumber = ((EditText)findViewById(R.id.editPhoneNumber)).getText().toString();
        String address = ((EditText)findViewById(R.id.editAddress)).getText().toString();
        boolean validFlag = true;


        if(TextUtils.isEmpty(firstName)){
            ((EditText)findViewById(R.id.editFirstName)).setError("Required");
            validFlag = false;
        } else if(!firstName.matches("[-a-zA-Z\\s]+")){
            ((EditText)findViewById(R.id.editFirstName)).setError("Invalid Name");
            validFlag = false;
        } else {
            ((EditText)findViewById(R.id.editFirstName)).setError(null);
        }

        if(TextUtils.isEmpty(lastName)){
            ((EditText)findViewById(R.id.editLastName)).setError("Required");
            validFlag = false;
        } else if(!lastName.matches("[-a-zA-Z\\s]+")) {
            ((EditText) findViewById(R.id.editLastName)).setError("Invalid Name");
            validFlag = false;
        } else {
            ((EditText)findViewById(R.id.editLastName)).setError(null);
        }

        if(!companyName.matches("[-a-zA-Z!.\\s]+") && findViewById(R.id.companyNameLayout).getVisibility() == View.VISIBLE){
            ((EditText)findViewById(R.id.editCompanyName)).setError("Invalid Name");
            validFlag = false;
        } else {
            ((EditText)findViewById(R.id.editCompanyName)).setError(null);
        }

        if(TextUtils.isEmpty(phoneNumber)){
            ((EditText)findViewById(R.id.editPhoneNumber)).setError("Required");
            validFlag = false;
        } else if(phoneNumber.length() != 10){
            ((EditText)findViewById(R.id.editPhoneNumber)).setError("Invalid length");
            validFlag = false;
        } else if(!phoneNumber.matches("[0-9]+") ){
            ((EditText)findViewById(R.id.editPhoneNumber)).setError("Invalid Phone Number");
            validFlag = false;
        } else {
            ((EditText)findViewById(R.id.editPhoneNumber)).setError(null);
        }

        if(TextUtils.isEmpty(address)){
            ((EditText)findViewById(R.id.editAddress)).setError("Required");
            validFlag = false;
        } else {
            ((EditText)findViewById(R.id.editAddress)).setError(null);
        }

        return validFlag;
    }




}



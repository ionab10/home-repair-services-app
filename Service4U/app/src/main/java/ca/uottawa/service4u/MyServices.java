package ca.uottawa.service4u;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyServices extends AppCompatActivity {

    private static final String dbTAG = "Database";

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseUsers;
    protected DatabaseReference databaseServices;

    List<Service> myServices;
    List<Service> allServices;
    ListView listAllServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

        listAllServices = (ListView) findViewById(R.id.listAllServices);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");
        databaseServices = database.getReference("Services");

        allServices = new ArrayList<Service>();
        myServices = new ArrayList<Service>();


        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allServices.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service service = postSnapshot.getValue(Service.class);
                    allServices.add(service);
                }

                updateForm();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //get userType
            databaseUsers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    ServiceProvider appUser = dataSnapshot.getValue(ServiceProvider.class);

                    if (appUser.services != null) {
                        myServices = appUser.services;
                    } else {
                        myServices = new ArrayList<Service>();
                    }
                    Log.d(dbTAG, "Services: " + myServices.toString());

                    updateForm();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e(dbTAG, "Failed to read value.", error.toException());
                }
            });

        }

    }

    private void updateForm(){
        Log.d("Form", "Updating form");

        AllServicesList servicesAdapter = new AllServicesList(MyServices.this, allServices, myServices);
        listAllServices.setAdapter(servicesAdapter);

    }

    public void associateServices(View view){

        Service service;

        for (int i = 0; i < listAllServices.getChildCount(); i++) {
            LinearLayout ll = (LinearLayout) listAllServices.getChildAt(i);
            CheckBox cb = (CheckBox) ll.getChildAt(0);

            service = allServices.get(i);

            if (cb.isChecked()){
                if (!myServices.contains(service)) {
                    myServices.add(service);
                }
            } else {
                myServices.remove(service);
            }

        }

        Log.v("dbTAG", "Associating services: " + myServices.toString());

        FirebaseUser user = mAuth.getCurrentUser();
        databaseUsers.child(user.getUid()).child("services").setValue(myServices);

        Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
        startActivityForResult (intent,0);
    }
}

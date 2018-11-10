package ca.uottawa.service4u;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyJobs extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseUsers;
    protected DatabaseReference databaseJobs;

    ListView listMyJobs;
    List<Job> jobs;
    JobList jobAdapter;

    String userType;

    FirebaseUser user;

    String currentJobID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Intent myIntent = getIntent(); // gets the previously created intent
        userType = myIntent.getStringExtra("userType");

        database = FirebaseDatabase.getInstance();

        jobs = new ArrayList<Job>();

        listMyJobs = findViewById(R.id.listMyJobs);
        databaseJobs = database.getReference("Jobs");

        databaseJobs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobs.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Job job = postSnapshot.getValue(Job.class);
                    if ((userType == "service provider") && (job.serviceProviderID == user.getUid())) {
                        jobs.add(job);
                    } else if ((userType == "homeowner") && (job.homewownerID == user.getUid())) {
                        jobs.add(job);
                    }
                }

                jobAdapter = new JobList(MyJobs.this, jobs);
                listMyJobs.setAdapter(jobAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listMyJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Job job = jobs.get(position);
                currentJobID = job.id;
                jobDetails(currentJobID);
            }
        });

    }

    public void jobDetails(String jobID){
        Intent intent = new Intent(getApplicationContext(), JobActivity.class);
        intent.putExtra("jobID", jobID);
        startActivityForResult (intent,0);
    }
}

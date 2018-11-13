package ca.uottawa.service4u;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MyJobs extends AppCompatActivity {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

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
                    if ((userType.equals("service provider")) && (job.serviceProviderID.equals(user.getUid()))) {
                        jobs.add(job);
                    } else if ((userType.equals("homeowner")) && (job.homewownerID.equals(user.getUid()))) {
                        jobs.add(job);
                    }
                }

                Log.d("jobs", jobs.toString());


                Collections.sort(jobs, (Job j1, Job j2) -> (Long.compare(j1.startTime, j2.startTime)));


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
        intent.putExtra("userType", userType);
        startActivityForResult (intent,0);
    }

    public class JobList extends ArrayAdapter<Job> {

        private Activity context;
        List<Job> jobs;

        public JobList(Activity context, List<Job> jobs) {
            super(context, R.layout.layout_job_list, jobs);
            this.context = context;
            this.jobs = jobs;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_job_list, null, true);

            TextView textViewJobName = (TextView) listViewItem.findViewById(R.id.jobNameText);
            TextView textViewDate = (TextView) listViewItem.findViewById(R.id.jobDatetimeText);
            TextView textViewHours = (TextView) listViewItem.findViewById(R.id.jobHoursText);

            Job job = jobs.get(position);
            textViewJobName.setText(job.title);
            textViewDate.setText(String.valueOf(datetimeFormat.format(new Date(job.startTime))));
            textViewHours.setText(String.valueOf(((double)(job.endTime - job.startTime))/1000/60/60));
            return listViewItem;
        }
    }
}

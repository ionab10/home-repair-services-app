package ca.uottawa.service4u;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class JobActivity extends AppCompatActivity {

    String jobID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        Intent myIntent = getIntent(); // gets the previously created intent
        jobID = myIntent.getStringExtra("jobID");
    }
}

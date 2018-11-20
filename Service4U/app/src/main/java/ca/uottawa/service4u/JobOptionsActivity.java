package ca.uottawa.service4u;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobOptionsActivity extends AppCompatActivity {

    private static final String SERVICE_NAME = "service";
    private static final String SERVICE_RATE = "service_rate";
    private static final String SERVICE_ID = "service_id";
    private static final String TIME_LENGTH = "time_length";
    private static final String JOB_OPTIONS = "options";

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_options);

        Intent myIntent = getIntent(); // gets the previously created intent
        String serviceName = myIntent.getStringExtra(SERVICE_NAME);
        String serviceID = myIntent.getStringExtra(SERVICE_ID);
        double serviceRate = myIntent.getDoubleExtra(SERVICE_RATE,0);
        double timeLength = myIntent.getDoubleExtra(TIME_LENGTH,0);
        Log.v("timeLength", String.valueOf(timeLength));
        ArrayList<PotentialJob> options = myIntent.getParcelableArrayListExtra(JOB_OPTIONS);

        Log.d(JOB_OPTIONS, options.toString());

        TextView noOptionsText = findViewById(R.id.noOptionsText);
        if (options.isEmpty()) {
            noOptionsText.setVisibility(View.VISIBLE);
        } else {
            noOptionsText.setVisibility(View.GONE);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), serviceID, serviceName, serviceRate, timeLength, options);

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String JOB_OPTION = "job_option";

        FirebaseAuth mAuth;
        FirebaseDatabase database;
        DatabaseReference databaseJobs;
        DatabaseReference databaseUsers;
        FirebaseUser user;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String serviceID, String serviceName, double serviceRate, double timeLength, PotentialJob jobOption) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelable(JOB_OPTION, jobOption);
            args.putString(SERVICE_NAME,serviceName);
            args.putString(SERVICE_ID,serviceID);
            args.putDouble(SERVICE_RATE, serviceRate);
            args.putDouble(TIME_LENGTH, timeLength);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_job_options, container, false);
            Bundle args = getArguments();

            PotentialJob option = args.getParcelable(JOB_OPTION);

            ((TextView) rootView.findViewById(R.id.serviceTitleText)).setText(args.getString(SERVICE_NAME));
            ((TextView) rootView.findViewById(R.id.datetimeText)).setText("Date: " + datetimeFormat.format(new Date(option.startTime)));
            ((TextView) rootView.findViewById(R.id.timeLengthText)).setText(String.format("Time: %.1f hours",args.getDouble(TIME_LENGTH)));
            double price = args.getDouble(SERVICE_RATE) * args.getDouble(TIME_LENGTH);
            ((TextView) rootView.findViewById(R.id.priceText)).setText(String.format("Price: $%.2f",price));
            ((TextView) rootView.findViewById(R.id.providerNameText)).setText(String.format("Name: %s %s", option.providerFirstName, option.providerLastName));
            ((RatingBar) rootView.findViewById(R.id.providerRatingBar)).setRating((float) option.providerRating);

            database = FirebaseDatabase.getInstance();
            databaseJobs = database.getReference("Jobs");
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();

            rootView.findViewById(R.id.bookJobBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Job job = new Job();

                    String id = databaseJobs.push().getKey();

                    job.id = id;
                    job.homewownerID = user.getUid();
                    job.serviceProviderID = option.providerID;
                    job.startTime = option.startTime;
                    job.endTime = option.endTime;
                    job.totalPrice = price;
                    job.serviceID = args.getString(SERVICE_ID);
                    job.title = args.getString(SERVICE_NAME);

                    databaseJobs.child(id).setValue(job);

                    updateAvailability(job.serviceProviderID, job.startTime, job.endTime);

                    Intent intent = new Intent(v.getContext(), MyJobs.class);
                    intent.putExtra("userType", "homeowner");
                    startActivityForResult (intent,0);

                }
            });

            return rootView;
        }

        public void updateAvailability(String providerID, long startTime, long endTime){

            databaseUsers = database.getReference("Users");

            databaseUsers.child(providerID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ServiceProvider appUser = dataSnapshot.getValue(ServiceProvider.class);
                    List<TimeInterval> myAvailability;
                    List<TimeInterval> myBooked;

                    if (appUser.booked != null) {
                        myBooked = appUser.booked;
                    } else {
                        myBooked = new ArrayList<TimeInterval>();
                    }

                    // add to provider_ID booked
                    myBooked = new TimeInterval(startTime,endTime).union(myBooked);
                    databaseUsers.child(providerID).child("booked").setValue(myBooked);


                    if (appUser.availability != null) {
                        myAvailability = appUser.availability;
                    } else {
                        myAvailability = new ArrayList<TimeInterval>();
                    }

                    // remove from provider_ID availability
                    myAvailability = new TimeInterval(startTime,endTime).difference(myAvailability);
                    databaseUsers.child(providerID).child("availability").setValue(myAvailability);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<PotentialJob> options;
        String serviceName;
        String serviceID;
        double serviceRate;
        double timeLength;

        public SectionsPagerAdapter(FragmentManager fm, String serviceID, String serviceName, double serviceRate, double timeLength, ArrayList<PotentialJob> options) {
            super(fm);
            this.options = options;
            this.serviceName = serviceName;
            this.serviceID = serviceID;
            this.serviceRate = serviceRate;
            this.timeLength = timeLength;

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(serviceID, serviceName, serviceRate, timeLength, options.get(position));
        }

        @Override
        public int getCount() {
            return options.size();
        }
    }
}

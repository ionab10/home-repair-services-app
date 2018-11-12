package ca.uottawa.service4u;

import android.content.Intent;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JobOptionsActivity extends AppCompatActivity {

    private static final String SERVICE_NAME = "service";
    private static final String SERVICE_RATE = "service_rate";
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
        double serviceRate = myIntent.getDoubleExtra(SERVICE_RATE,0);
        double timeLength = myIntent.getDoubleExtra(TIME_LENGTH,0);
        Log.v("timeLength", String.valueOf(timeLength));
        ArrayList<PotentialJob> options = myIntent.getParcelableArrayListExtra(JOB_OPTIONS);

        Log.d(JOB_OPTIONS, options.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView noOptionsText = findViewById(R.id.noOptionsText);
        if (options.isEmpty()) {
            noOptionsText.setVisibility(View.VISIBLE);
        } else {
            noOptionsText.setVisibility(View.GONE);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), serviceName, serviceRate, timeLength, options);

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
        String serviceName;
        double serviceRate;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String serviceName, double serviceRate, double timeLength, PotentialJob jobOption) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelable(JOB_OPTION, jobOption);
            args.putString(SERVICE_NAME,serviceName);
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
            ((TextView) rootView.findViewById(R.id.datetimeText)).setText(datetimeFormat.format(new Date(option.startTime)));
            ((TextView) rootView.findViewById(R.id.timeLengthText)).setText(String.format("%.1f hours",args.getDouble(TIME_LENGTH)));
            double price = args.getDouble(SERVICE_RATE) * args.getDouble(TIME_LENGTH);
            ((TextView) rootView.findViewById(R.id.priceText)).setText(String.format("$%.2f",price));
            ((TextView) rootView.findViewById(R.id.providerNameText)).setText(String.format("%s %s", option.providerFirstName, option.providerLastName));
            ((RatingBar) rootView.findViewById(R.id.providerRatingBar)).setRating((float) option.providerRating);


            //TODO Make this prettier

            rootView.findViewById(R.id.bookJobBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Job job = new Job();

                    //TODO add job

                /*
                String id = databaseJobs.push().getKey();
                job.setId(id);
                databaseJobs.child(id).setValue(job);
                */

                }
            });

            return rootView;
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<PotentialJob> options;
        String serviceName;
        double serviceRate;
        double timeLength;

        public SectionsPagerAdapter(FragmentManager fm, String serviceName, double serviceRate, double timeLength, ArrayList<PotentialJob> options) {
            super(fm);
            this.options = options;
            this.serviceName = serviceName;
            this.serviceRate = serviceRate;
            this.timeLength = timeLength;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(serviceName, serviceRate, timeLength, options.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return options.size();
        }
    }
}

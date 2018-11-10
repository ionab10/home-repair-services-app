package ca.uottawa.service4u;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JobList extends ArrayAdapter<Job> {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

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
        textViewHours.setText(String.valueOf((job.endTime - job.startTime)/60/60/1000));
        return listViewItem;
    }
}

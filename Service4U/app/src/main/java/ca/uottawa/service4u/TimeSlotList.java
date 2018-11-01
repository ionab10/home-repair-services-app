package ca.uottawa.service4u;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class TimeSlotList extends ArrayAdapter<String> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    private Activity context;
    List<String> timeSlots;
    private List<Long> availability;
    private String dateString;


    public TimeSlotList(Activity context, List<String> timeSlots, List<Long> availability, String dateString) {
        super(context, R.layout.layout_timeslot_list, timeSlots);
        this.context = context;
        this.timeSlots = timeSlots;
        this.availability = availability;
        this.dateString = dateString;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_timeslot_list, null, true);

        TextView timeSlotText = (TextView) listViewItem.findViewById(R.id.timeSlotText);
        String timeSlot = timeSlots.get(position);
        timeSlotText.setText(timeSlot);

        CheckBox cb = (CheckBox) listViewItem.findViewById(R.id.timeSlotCheckBox);
        long dt;

        String dtStr = String.format("%s %s", dateString, timeSlots.get(position));
        try{
            dt = dateFormat.parse(dtStr).getTime();

            if (availability.contains(dt)){
                Log.v("Form", "available " + dt);
                cb.setChecked(true);
            } else {
                Log.v("Form", "unavailable " + dt);
                cb.setChecked(false);
            }
        } catch (Exception e){
            Log.v("parser", "unable to parse date " + dtStr);
        }

        return listViewItem;
    }


}

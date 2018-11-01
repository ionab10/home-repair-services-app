package ca.uottawa.service4u;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

public class AvailabilityCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_calendar);

        CalendarView calendarView=(CalendarView) findViewById(R.id.availabilityCal);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                Intent intent = new Intent(getApplicationContext(), AvailabilityActivity.class);
                intent.putExtra("year", year);
                month = month+1;    //months start at 0 instead of 1 for some weird reason
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivityForResult (intent,0);
            }
        });
    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
        startActivityForResult (intent,0);
    }
}

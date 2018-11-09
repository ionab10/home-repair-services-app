package ca.uottawa.service4u;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookJob extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_job);
    }

    public Date addDays(Date date, int n) {
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }

    public List<PotentialJob> findProviders(Service service, Map<Integer,List<TimeInterval>> availability, double hours, int urgency){
        List<PotentialJob> options = new ArrayList<PotentialJob>();
        List<ServiceProvider> providers = service.getAssociatedProviders();

        Date date = new Date(); //defaults to today
        long start = -1;
        List<TimeInterval> timeSlots;

        for (int i=0; i< urgency; i++) {

            calendar.setTime(date);
            timeSlots = availability.get(calendar.get(Calendar.DAY_OF_WEEK));   //get availability for day of week

            for (ServiceProvider p : providers) {
                start = p.available(timeSlots,hours);
                if (start >= 0) {
                    PotentialJob option = new PotentialJob(start, (long) (start + hours*60*60), p);
                    options.add(option);
                }
            }

            date = addDays(date, 1);
        }

        return options;
    }
}

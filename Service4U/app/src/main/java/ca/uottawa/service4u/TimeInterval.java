package ca.uottawa.service4u;

import java.util.Calendar;
import java.util.Date;

public class TimeInterval {
    public long start;
    public long end;

    public TimeInterval() {
    }

    public TimeInterval(long start, long end) {
        this.start = start;
        this.end = end;
    }


    public boolean contains (long datetime){
        if ((start <= datetime) && (datetime <= end)){
            return true;
        } else {
            return false;
        }

    }

}


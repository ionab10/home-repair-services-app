package ca.uottawa.service4u;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        if ((start <= datetime) && (datetime < end)){
            return true;
        } else {
            return false;
        }

    }

    @Override
    public String toString(){
        return String.format("(%s,%s)", new Date(start).toString(), new Date(end).toString());
    }

    public TimeInterval intersection(TimeInterval ti){
        long s1 = ti.start;
        long e1 = ti.end;

        return new TimeInterval(Math.max(start,s1), Math.min(end, e1));
    }

    public long length(){
        return end - start;
    }

}


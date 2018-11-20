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

        return new TimeInterval(Math.max(this.start,s1), Math.min(this.end, e1));
    }

    public long length(){
        return end - start;
    }


    //TODO Iona
    public List<TimeInterval> union(List<TimeInterval> timeIntervalList){
        List<TimeInterval> timeIntervals = new ArrayList<TimeInterval>();
        long start = this.start;
        long end = this.end;

        for (TimeInterval ti: timeIntervalList){
            if (((ti.start <= this.start) && (ti.end >= this.start))
                    || ((ti.start <= this.end) && (ti.end >= this.end))){
                start = Math.min(start, ti.start);
                end = Math.max(end, ti.end);
            } else if ((ti.end < this.start) || (ti.start > this.end)){
                timeIntervals.add(ti);
            }
        }

        timeIntervals.add(new TimeInterval(start,end));

        return timeIntervals;
    }

    //TODO Iona
    public List<TimeInterval> difference(List<TimeInterval> timeIntervalList){
        List<TimeInterval> timeIntervals = new ArrayList<TimeInterval>();

        for (TimeInterval ti: timeIntervalList){
            if ((ti.start <= this.start) && (ti.end >= this.start) && (ti.start != this.start)){
                timeIntervals.add(new TimeInterval(ti.start,this.start));
            }
            if ((ti.start <= this.end) && (ti.end >= this.end) && (ti.end != this.end)) {
                timeIntervals.add(new TimeInterval(this.end,ti.end));
            }
            if ((ti.start > this.end) || (ti.end < this.start)){
                timeIntervals.add(ti);
            }
        }

        return timeIntervals;

    }

}


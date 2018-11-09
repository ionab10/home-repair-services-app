package ca.uottawa.service4u;

import android.os.Parcel;
import android.os.Parcelable;

class PotentialJob implements Parcelable{
    public long startTime;
    public long endTime;
    public ServiceProvider provider;

    public PotentialJob(long startTime, long endTime, ServiceProvider provider) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.provider = provider;
    }

    protected PotentialJob(Parcel in) {
        startTime = in.readLong();
        endTime = in.readLong();
    }

    public static final Creator<PotentialJob> CREATOR = new Creator<PotentialJob>() {
        @Override
        public PotentialJob createFromParcel(Parcel in) {
            return new PotentialJob(in);
        }

        @Override
        public PotentialJob[] newArray(int size) {
            return new PotentialJob[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime);
        dest.writeLong(endTime);
    }
}

package ca.uottawa.service4u;

import android.os.Parcel;
import android.os.Parcelable;

class PotentialJob implements Parcelable{
    public long startTime;
    public long endTime;
    public String providerFirstName;
    public String providerLastName;
    public String providerID;
    public double providerRating;

    public PotentialJob(long startTime, long endTime, String providerFirstName, String providerLastName, String providerID,  double providerRating) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.providerFirstName = providerFirstName;
        this.providerLastName = providerLastName;
        this.providerID = providerID;
        this.providerRating = providerRating;
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

    public PotentialJob(Parcel in) {
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.providerFirstName = in.readString();
        this.providerLastName = in.readString();
        this.providerID = in.readString();
        this.providerRating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(providerFirstName);
        dest.writeString(providerLastName);
        dest.writeString(providerID);
        dest.writeDouble(providerRating);
    }
}

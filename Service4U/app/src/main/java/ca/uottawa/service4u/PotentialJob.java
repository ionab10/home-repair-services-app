package ca.uottawa.service4u;

class PotentialJob {
    public long startTime;
    public long endTime;
    public ServiceProvider provider;

    public PotentialJob(long startTime, long endTime, ServiceProvider provider) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.provider = provider;
    }

}

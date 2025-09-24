package ru.otus.hw.domain;

public class HiringStatistics {
    private int hrRejected = 0;
    private int technicalRejected = 0;
    private int accepted = 0;

    public synchronized void incrementHrRejected() {
        hrRejected++;
    }

    public synchronized void incrementTechnicalRejected() {
        technicalRejected++;
    }

    public synchronized void incrementAccepted() {
        accepted++;
    }

    public int getHrRejected() {
        return hrRejected;
    }

    public int getTechnicalRejected() {
        return technicalRejected;
    }

    public int getAccepted() {
        return accepted;
    }

    @Override
    public String toString() {
        return "Statistics{HR rejected=" + hrRejected +
                ", Technical rejected=" + technicalRejected +
                ", Accepted=" + accepted + '}';
    }
}

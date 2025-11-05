package review2;

import java.sql.Timestamp;

public class Donation {
    private int id;
    private int donorId;
    private int athleteId;
    private double amount;
    private Timestamp timestamp;

    public Donation() {}

    public Donation(int id, int donorId, int athleteId, double amount, Timestamp timestamp) {
        this.id = id;
        this.donorId = donorId;
        this.athleteId = athleteId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }
    public int getAthleteId() { return athleteId; }
    public void setAthleteId(int athleteId) { this.athleteId = athleteId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}

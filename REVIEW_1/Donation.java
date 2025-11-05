// File: Donation.java
import java.util.Date;

public class Donation {
    private String donorName;
    private String athleteName;
    private double amount;
    private Date date;

    public Donation(String donorName, String athleteName, double amount) {
        this.donorName = donorName;
        this.athleteName = athleteName;
        this.amount = amount;
        this.date = new Date();
    }

    public String getDonorName() {
        return donorName;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}
package review2;

/**
 * Donor model for donors table joined to users table.
 */
public class Donor {
    private int id;
    private int userId;
    private double totalDonated;

    public Donor() {}

    public Donor(int id, int userId, double totalDonated) {
        this.id = id;
        this.userId = userId;
        this.totalDonated = totalDonated;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getTotalDonated() { return totalDonated; }
    public void setTotalDonated(double totalDonated) { this.totalDonated = totalDonated; }
    public void addDonation(double amount) { this.totalDonated += amount; }
}

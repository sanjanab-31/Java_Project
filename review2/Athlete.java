package review2;

/**
 * Athlete model representing a row in athletes table joined to a user.
 */
public class Athlete {
    private int id; // athlete id (athletes table)
    private int userId; // reference to users.id
    private String sport;
    private int medals;
    private int achievementsCount;
    private double fundingGoal;
    private double totalFundsRaised;

    public Athlete() {}

    public Athlete(int id, int userId, String sport, int medals, int achievementsCount, double fundingGoal, double totalFundsRaised) {
        this.id = id;
        this.userId = userId;
        this.sport = sport;
        this.medals = medals;
        this.achievementsCount = achievementsCount;
        this.fundingGoal = fundingGoal;
        this.totalFundsRaised = totalFundsRaised;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }
    public int getMedals() { return medals; }
    public void setMedals(int medals) { this.medals = medals; }
    public int getAchievementsCount() { return achievementsCount; }
    public void setAchievementsCount(int achievementsCount) { this.achievementsCount = achievementsCount; }
    public double getFundingGoal() { return fundingGoal; }
    public void setFundingGoal(double fundingGoal) { this.fundingGoal = fundingGoal; }
    public double getTotalFundsRaised() { return totalFundsRaised; }
    public void setTotalFundsRaised(double totalFundsRaised) { this.totalFundsRaised = totalFundsRaised; }

    public void addFunds(double amount) { this.totalFundsRaised += amount; }

    public String getBadge() {
        if (medals >= 5) return "🥇 Gold Star Performer";
        if (medals >= 3) return "🥈 Silver Achiever";
        if (medals >= 1) return "🥉 Bronze Talent";
        return "No Badge";
    }
}

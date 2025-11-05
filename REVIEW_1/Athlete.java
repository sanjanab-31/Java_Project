// File: Athlete.java
import java.util.Scanner;

public class Athlete extends User {
    private String sport;
    private int medals;
    private int achievementsCount;
    private double fundingGoal;
    private double totalFundsRaised;

    public Athlete(String name, String username, String password, String sport, int medals, int achievementsCount, double fundingGoal, double totalFundsRaised) {
        super(name, username, password);
        this.sport = sport;
        this.medals = medals;
        this.achievementsCount = achievementsCount;
        this.fundingGoal = fundingGoal;
        this.totalFundsRaised = totalFundsRaised;
    }

    public String getSport() {
        return sport;
    }

    public int getMedals() {
        return medals;
    }

    public int getAchievementsCount() {
        return achievementsCount;
    }

    public double getFundingGoal() {
        return fundingGoal;
    }

    public double getTotalFundsRaised() {
        return totalFundsRaised;
    }

    public void addFunds(double amount) {
        this.totalFundsRaised += amount;
    }

    public String getBadge() {
        if (medals >= 5) {
            return "🥇 Gold Star Performer";
        } else if (medals >= 3) {
            return "🥈 Silver Achiever";
        } else if (medals >= 1) {
            return "🥉 Bronze Talent";
        } else {
            return "No Badge";
        }
    }

    public void viewProgress() {
        double progress = (totalFundsRaised / fundingGoal) * 100;
        System.out.println("Goal: ₹" + fundingGoal + " | Raised: ₹" + totalFundsRaised);
        System.out.print("Progress: ");
        int bars = (int) (progress / 10);
        for (int i = 0; i < bars; i++) {
            System.out.print("|");
        }
        System.out.println(" " + (int) progress + "% Achieved 🎉");
    }

    @Override
    public void viewDashboard(DonationPlatform platform) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Athlete Dashboard ===");
            System.out.println("1. View Profile");
            System.out.println("2. View Progress");
            System.out.println("3. Update Achievements");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("Name: " + getName());
                System.out.println("Sport: " + sport);
                System.out.println("Medals: " + medals);
                System.out.println("Achievements: " + achievementsCount);
                System.out.println("Funding Goal: ₹" + fundingGoal);
                System.out.println("Funds Raised: ₹" + totalFundsRaised);
                System.out.println("Badge: " + getBadge());
            } else if (choice == 2) {
                viewProgress();
            } else if (choice == 3) {
                System.out.print("Enter new medals count: ");
                medals = scanner.nextInt();
                System.out.print("Enter new achievements count: ");
                achievementsCount = scanner.nextInt();
                System.out.println("Updated!");
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}
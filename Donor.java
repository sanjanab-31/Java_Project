// File: Donor.java
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Donor extends User {
    private List<Donation> donationHistory = new ArrayList<>();

    public Donor(String name, String username, String password) {
        super(name, username, password);
    }

    public void addDonation(Donation donation) {
        donationHistory.add(donation);
    }

    public void viewDonationHistory() {
        if (donationHistory.isEmpty()) {
            System.out.println("No donations yet.");
            return;
        }
        System.out.println("\n=== Donation History ===");
        for (Donation d : donationHistory) {
            System.out.println("To: " + d.getAthleteName() + " | Amount: ₹" + d.getAmount() + " | Date: " + d.getDate());
        }
    }

    @Override
    public void viewDashboard(DonationPlatform platform) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Donor Dashboard ===");
            System.out.println("1. Browse Athletes");
            System.out.println("2. Filter Athletes");
            System.out.println("3. Make Donation");
            System.out.println("4. View Donation History");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                platform.browseAthletes();
            } else if (choice == 2) {
                System.out.print("Filter by sport (or leave blank): ");
                String sport = scanner.nextLine();
                System.out.print("Minimum medals (or -1 for no filter): ");
                int minMedals = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Sort by urgent needs? (y/n): ");
                boolean urgent = scanner.nextLine().equalsIgnoreCase("y");
                platform.filterAthletes(sport, minMedals, urgent);
            } else if (choice == 3) {
                System.out.print("Enter athlete name to donate to: ");
                String athleteName = scanner.nextLine();
                System.out.print("Enter amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                try {
                    platform.makeDonation(this, athleteName, amount);
                    System.out.println("Donation successful! 🎉");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (choice == 4) {
                viewDonationHistory();
            } else if (choice == 5) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}
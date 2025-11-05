// File: Admin.java
import java.util.Scanner;

public class Admin extends User {
    public Admin(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void viewDashboard(DonationPlatform platform) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Admin Dashboard ===");
            System.out.println("1. View System Stats");
            System.out.println("2. View Leaderboard");
            System.out.println("3. View All Users");
            System.out.println("4. View All Donations");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                platform.viewSystemStats();
            } else if (choice == 2) {
                platform.showLeaderboard();
            } else if (choice == 3) {
                platform.viewAllUsers();
            } else if (choice == 4) {
                platform.viewAllDonations();
            } else if (choice == 5) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}
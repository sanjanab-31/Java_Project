// File: Main.java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DonationPlatform platform = new DonationPlatform();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to AthleteConnect+ ===");
        System.out.println("ASCII Banner: \n" +
                "  _____   _____   _____   _____   _____   _____   \n" +
                " /     \\ /     \\ /     \\ /     \\ /     \\ /     \\ \n" +
                "|  A   ||  T   ||  H   ||  L   ||  E   ||  T   | \n" +
                " \\_____//_____//_____//_____//_____//_____/ \n" +
                "Connect Athletes with Donors! 🎉");

        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                platform.registerUser(scanner);
            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                try {
                    User user = platform.loginUser(username, password);
                    user.viewDashboard(platform);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }
}
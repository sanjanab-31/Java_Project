package review2;

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
            int choice;
            try { choice = Integer.parseInt(scanner.nextLine()); } catch (Exception e) { System.out.println("Invalid option."); continue; }

            if (choice == 1) {
                platform.registerUser(scanner);
            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                try {
                    User user = platform.loginUser(username, password);
                    // show role-specific dashboards
                    if ("ATHLETE".equals(user.getRole())) {
                        while (true) {
                            System.out.println("\n=== Athlete Dashboard ===");
                            System.out.println("1. View Profile");
                            System.out.println("2. View Progress");
                            System.out.println("3. Update Achievements");
                            System.out.println("4. Logout");
                            System.out.print("Choose an option: ");
                            int c = Integer.parseInt(scanner.nextLine());
                            Athlete athlete = platform.getAthleteByUserId(user.getId());
                            if (c == 1) {
                                System.out.println("Name: " + user.getName());
                                System.out.println("Sport: " + athlete.getSport());
                                System.out.println("Medals: " + athlete.getMedals());
                                System.out.println("Achievements: " + athlete.getAchievementsCount());
                                System.out.println("Funding Goal: ₹" + athlete.getFundingGoal());
                                System.out.println("Funds Raised: ₹" + athlete.getTotalFundsRaised());
                                System.out.println("Badge: " + athlete.getBadge());
                            } else if (c == 2) {
                                double progress = (athlete.getTotalFundsRaised() / (athlete.getFundingGoal() == 0 ? 1 : athlete.getFundingGoal())) * 100;
                                System.out.println("Goal: ₹" + athlete.getFundingGoal() + " | Raised: ₹" + athlete.getTotalFundsRaised());
                                System.out.print("Progress: ");
                                int bars = (int) (progress / 10);
                                for (int i = 0; i < bars; i++) System.out.print("|");
                                System.out.println(" " + (int) progress + "% Achieved 🎉");
                            } else if (c == 3) {
                                System.out.print("Enter new medals count: ");
                                int medals = Integer.parseInt(scanner.nextLine());
                                System.out.print("Enter new achievements count: ");
                                int achievements = Integer.parseInt(scanner.nextLine());
                                platform.updateAthleteMedalsAndAchievements(athlete.getId(), medals, achievements);
                                System.out.println("Updated!");
                            } else if (c == 4) break;
                            else System.out.println("Invalid option.");
                        }
                    } else if ("DONOR".equals(user.getRole())) {
                        while (true) {
                            System.out.println("\n=== Donor Dashboard ===");
                            System.out.println("1. Browse Athletes");
                            System.out.println("2. Filter Athletes");
                            System.out.println("3. Make Donation");
                            System.out.println("4. View Donation History");
                            System.out.println("5. Logout");
                            System.out.print("Choose an option: ");
                            int c = Integer.parseInt(scanner.nextLine());
                            if (c == 1) platform.browseAthletes();
                            else if (c == 2) {
                                System.out.print("Filter by sport (or leave blank): ");
                                String sport = scanner.nextLine();
                                System.out.print("Minimum medals (or -1 for no filter): ");
                                int minMedals = Integer.parseInt(scanner.nextLine());
                                System.out.print("Sort by urgent needs? (y/n): ");
                                boolean urgent = scanner.nextLine().equalsIgnoreCase("y");
                                platform.filterAthletes(sport, minMedals, urgent);
                            } else if (c == 3) {
                                System.out.print("Enter athlete name to donate to: ");
                                String athleteName = scanner.nextLine();
                                System.out.print("Enter amount: ");
                                double amount = Double.parseDouble(scanner.nextLine());
                                try {
                                    platform.makeDonation(user, athleteName, amount);
                                    System.out.println("Donation successful! 🎉");
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            } else if (c == 4) {
                                platform.viewDonationHistory(user);
                            } else if (c == 5) break;
                            else System.out.println("Invalid option.");
                        }
                    } else if ("ADMIN".equals(user.getRole())) {
                        while (true) {
                            System.out.println("\n=== Admin Dashboard ===");
                            System.out.println("1. View System Stats");
                            System.out.println("2. View Leaderboard");
                            System.out.println("3. View All Users");
                            System.out.println("4. View All Donations");
                            System.out.println("5. Logout");
                            System.out.print("Choose an option: ");
                            int c = Integer.parseInt(scanner.nextLine());
                            if (c == 1) platform.viewSystemStats();
                            else if (c == 2) platform.showLeaderboard();
                            else if (c == 3) platform.viewAllUsers();
                            else if (c == 4) platform.viewAllDonations();
                            else if (c == 5) break;
                            else System.out.println("Invalid option.");
                        }
                    }
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

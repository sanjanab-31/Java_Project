// File: DonationPlatform.java
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class DonationPlatform {
    private List<User> users = new ArrayList<>();
    private List<Donation> donations = new ArrayList<>();

    public DonationPlatform() {
        // Preload sample athletes
        users.add(new Athlete("Rahul Sharma", "rahul", "pass", "Boxing", 5, 10, 5000, 2000));
        users.add(new Athlete("Priya Mehta", "priya", "pass", "Running", 3, 5, 4000, 1200));
        users.add(new Athlete("Arjun Rao", "arjun", "pass", "Swimming", 2, 4, 3000, 800));
        users.add(new Athlete("Sneha Iyer", "sneha", "pass", "Badminton", 1, 2, 3500, 1500));
        users.add(new Athlete("Vikram Das", "vikram", "pass", "Weightlifting", 0, 1, 6000, 500));

        // Preload sample donor and admin
        users.add(new Donor("Sample Donor", "donor", "pass"));
        users.add(new Admin("Admin", "admin", "admin"));
    }

    public void registerUser(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Register as: 1. Athlete 2. Donor 3. Admin");
        int type = scanner.nextInt();
        scanner.nextLine();

        if (type == 1) {
            System.out.print("Enter sport: ");
            String sport = scanner.nextLine();
            System.out.print("Enter medals: ");
            int medals = scanner.nextInt();
            System.out.print("Enter achievements count: ");
            int achievements = scanner.nextInt();
            System.out.print("Enter funding goal: ");
            double goal = scanner.nextDouble();
            users.add(new Athlete(name, username, password, sport, medals, achievements, goal, 0));
        } else if (type == 2) {
            users.add(new Donor(name, username, password));
        } else if (type == 3) {
            users.add(new Admin(name, username, password));
        } else {
            System.out.println("Invalid type.");
            return;
        }
        System.out.println("Registration successful! 🎉");
    }

    public User loginUser(String username, String password) throws Exception {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new Exception("Invalid credentials.");
    }

    public void browseAthletes() {
        System.out.println("\n=== Available Athletes ===");
        for (User user : users) {
            if (user instanceof Athlete) {
                Athlete a = (Athlete) user;
                System.out.println(a.getName() + " | Sport: " + a.getSport() + " | Medals: " + a.getMedals() + " | Raised: ₹" + a.getTotalFundsRaised() + "/" + a.getFundingGoal() + " | Badge: " + a.getBadge());
            }
        }
    }

    public void filterAthletes(String sport, int minMedals, boolean urgent) {
        List<Athlete> filtered = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Athlete) {
                Athlete a = (Athlete) user;
                boolean matches = true;
                if (!sport.isEmpty() && !a.getSport().equalsIgnoreCase(sport)) matches = false;
                if (minMedals > -1 && a.getMedals() < minMedals) matches = false;
                if (matches) filtered.add(a);
            }
        }
        if (urgent) {
            filtered.sort(Comparator.comparingDouble(a -> a.getTotalFundsRaised() / a.getFundingGoal()));
        }
        System.out.println("\n=== Filtered Athletes ===");
        for (Athlete a : filtered) {
            System.out.println(a.getName() + " | Sport: " + a.getSport() + " | Medals: " + a.getMedals() + " | Raised: ₹" + a.getTotalFundsRaised() + "/" + a.getFundingGoal() + " | Badge: " + a.getBadge());
        }
    }

    public void makeDonation(Donor donor, String athleteName, double amount) throws Exception {
        if (amount <= 0) throw new Exception("Amount must be positive.");
        for (User user : users) {
            if (user instanceof Athlete && user.getName().equals(athleteName)) {
                Athlete a = (Athlete) user;
                a.addFunds(amount);
                Donation d = new Donation(donor.getName(), athleteName, amount);
                donations.add(d);
                donor.addDonation(d);
                return;
            }
        }
        throw new Exception("Athlete not found.");
    }

    public void showLeaderboard() {
        List<Athlete> athletes = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Athlete) athletes.add((Athlete) user);
        }
        Collections.sort(athletes, Comparator.comparingInt(Athlete::getMedals).reversed()
                .thenComparingDouble(Athlete::getTotalFundsRaised).reversed());

        System.out.println("\n=== Leaderboard ===");
        int rank = 1;
        for (Athlete a : athletes) {
            System.out.println(rank + ". " + a.getName() + " | Medals: " + a.getMedals() + " | Funds: ₹" + a.getTotalFundsRaised() + " | Badge: " + a.getBadge());
            rank++;
        }
    }

    public void viewSystemStats() {
        int athletes = 0, donors = 0, admins = 0;
        double totalDonations = 0;
        for (User u : users) {
            if (u instanceof Athlete) athletes++;
            else if (u instanceof Donor) donors++;
            else if (u instanceof Admin) admins++;
        }
        for (Donation d : donations) {
            totalDonations += d.getAmount();
        }
        System.out.println("\n=== System Stats ===");
        System.out.println("Total Users: " + users.size());
        System.out.println("Athletes: " + athletes + " | Donors: " + donors + " | Admins: " + admins);
        System.out.println("Total Donations: ₹" + totalDonations);
    }

    public void viewAllUsers() {
        System.out.println("\n=== All Users ===");
        for (User u : users) {
            System.out.println(u.getName() + " (" + u.getClass().getSimpleName() + ")");
        }
    }

    public void viewAllDonations() {
        if (donations.isEmpty()) {
            System.out.println("No donations yet.");
            return;
        }
        System.out.println("\n=== All Donations ===");
        for (Donation d : donations) {
            System.out.println("From: " + d.getDonorName() + " | To: " + d.getAthleteName() + " | Amount: ₹" + d.getAmount() + " | Date: " + d.getDate());
        }
    }
}
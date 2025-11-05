package review2;

import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 * DonationPlatform wiring DAOs and providing console-facing operations.
 */
public class DonationPlatform {
    private UserDAO userDAO = new UserDAO();
    private AthleteDAO athleteDAO = new AthleteDAO();
    private DonorDAO donorDAO = new DonorDAO();
    private DonationDAO donationDAO = new DonationDAO();

    public DonationPlatform() {
        DBHelper.initializeDatabase();
    }

    public void registerUser(Scanner scanner) {
        try {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            if (userDAO.usernameExists(username)) {
                System.out.println("Username already taken. Choose another.");
                return;
            }
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.println("Register as: 1. Athlete 2. Donor 3. Admin");
            int type = Integer.parseInt(scanner.nextLine());

            String role = (type == 1) ? "ATHLETE" : (type == 2) ? "DONOR" : "ADMIN";
            User u = new User();
            u.setName(name);
            u.setUsername(username);
            u.setPasswordHash(PasswordUtils.hashPassword(password));
            u.setRole(role);
            int userId = userDAO.createUser(u);

            if (type == 1) {
                System.out.print("Enter sport: ");
                String sport = scanner.nextLine();
                System.out.print("Enter medals: ");
                int medals = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter achievements count: ");
                int achievements = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter funding goal: ");
                double goal = Double.parseDouble(scanner.nextLine());
                Athlete a = new Athlete();
                a.setUserId(userId);
                a.setSport(sport);
                a.setMedals(medals);
                a.setAchievementsCount(achievements);
                a.setFundingGoal(goal);
                a.setTotalFundsRaised(0);
                athleteDAO.createAthlete(a);
            } else if (type == 2) {
                donorDAO.createDonor(userId);
            } else if (type == 3) {
                // create admin row
                try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO admins (user_id) VALUES (?)")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }
            }
            System.out.println("Registration successful! 🎉");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public User loginUser(String username, String password) throws Exception {
        User u = userDAO.getByUsername(username);
        if (u == null) throw new Exception("Invalid credentials.");
        if (!PasswordUtils.verifyPassword(password, u.getPasswordHash())) throw new Exception("Invalid credentials.");
        return u;
    }

    public void browseAthletes() {
        List<Athlete> athletes = athleteDAO.listAll();
        System.out.println("\n=== Available Athletes ===");
        for (Athlete a : athletes) {
            User u = userDAO.getById(a.getUserId());
            System.out.println(u.getName() + " | Sport: " + a.getSport() + " | Medals: " + a.getMedals() + " | Raised: ₹" + a.getTotalFundsRaised() + "/" + a.getFundingGoal() + " | Badge: " + a.getBadge());
        }
    }

    public void filterAthletes(String sport, int minMedals, boolean urgent) {
        List<Athlete> filtered = athleteDAO.filter(sport, minMedals, urgent);
        System.out.println("\n=== Filtered Athletes ===");
        for (Athlete a : filtered) {
            User u = userDAO.getById(a.getUserId());
            System.out.println(u.getName() + " | Sport: " + a.getSport() + " | Medals: " + a.getMedals() + " | Raised: ₹" + a.getTotalFundsRaised() + "/" + a.getFundingGoal() + " | Badge: " + a.getBadge());
        }
    }

    public void makeDonation(User donorUser, String athleteName, double amount) throws Exception {
        if (amount <= 0) throw new Exception("Amount must be positive.");
        // find athlete row by matching user.name
        List<Athlete> athletes = athleteDAO.listAll();
        Athlete target = null;
        User athleteUser = null;
        for (Athlete a : athletes) {
            User u = userDAO.getById(a.getUserId());
            if (u.getName().equals(athleteName)) { target = a; athleteUser = u; break; }
        }
        if (target == null) throw new Exception("Athlete not found.");

        // find donor row
        Donor donor = donorDAO.getByUserId(donorUser.getId());
        if (donor == null) throw new Exception("Donor account not found.");

        // update athlete funds
        double newTotal = target.getTotalFundsRaised() + amount;
        athleteDAO.updateFunds(target.getId(), newTotal);

        // create donation
        int donationId = donationDAO.createDonation(donor.getId(), target.getId(), amount);

        // update donor total
        donorDAO.addToTotal(donor.getId(), amount);
    }

    public void showLeaderboard() {
        List<Athlete> athletes = athleteDAO.listAll();
        // sort by medals desc then funds desc
        athletes.sort((a,b) -> {
            int m = Integer.compare(b.getMedals(), a.getMedals());
            if (m != 0) return m;
            return Double.compare(b.getTotalFundsRaised(), a.getTotalFundsRaised());
        });
        System.out.println("\n=== Leaderboard ===");
        int rank = 1;
        for (Athlete a : athletes) {
            User u = userDAO.getById(a.getUserId());
            System.out.println(rank + ". " + u.getName() + " | Medals: " + a.getMedals() + " | Funds: ₹" + a.getTotalFundsRaised() + " | Badge: " + a.getBadge());
            rank++;
        }
    }

    public void viewSystemStats() {
        int athletes = athleteDAO.listAll().size();
        int donors = userDAO.listAll().stream().filter(u -> "DONOR".equals(u.getRole())).toArray().length;
        int admins = userDAO.listAll().stream().filter(u -> "ADMIN".equals(u.getRole())).toArray().length;
        double totalDonations = donationDAO.listAll().stream().mapToDouble(Donation::getAmount).sum();
        System.out.println("\n=== System Stats ===");
        System.out.println("Total Users: " + userDAO.listAll().size());
        System.out.println("Athletes: " + athletes + " | Donors: " + donors + " | Admins: " + admins);
        System.out.println("Total Donations: ₹" + totalDonations);
    }

    public void viewAllUsers() {
        System.out.println("\n=== All Users ===");
        for (User u : userDAO.listAll()) {
            System.out.println(u.getName() + " (" + u.getRole() + ")");
        }
    }

    public void viewAllDonations() {
        List<Donation> all = donationDAO.listAll();
        if (all.isEmpty()) { System.out.println("No donations yet."); return; }
        System.out.println("\n=== All Donations ===");
        for (Donation d : all) {
            Donor donor = donorDAO.getById(d.getDonorId());
            Athlete athlete = athleteDAO.getById(d.getAthleteId());
            User donorUser = userDAO.getById(donor.getUserId());
            User athleteUser = userDAO.getById(athlete.getUserId());
            System.out.println("From: " + donorUser.getName() + " | To: " + athleteUser.getName() + " | Amount: ₹" + d.getAmount() + " | Date: " + d.getTimestamp());
        }
    }

    public void viewDonationHistory(User donorUser) {
        Donor donor = donorDAO.getByUserId(donorUser.getId());
        if (donor == null) { System.out.println("No donations yet."); return; }
        List<Donation> list = donationDAO.listByDonorId(donor.getId());
        if (list.isEmpty()) { System.out.println("No donations yet."); return; }
        System.out.println("\n=== Donation History ===");
        for (Donation d : list) {
            Athlete a = athleteDAO.getById(d.getAthleteId());
            User athleteUser = userDAO.getById(a.getUserId());
            System.out.println("To: " + athleteUser.getName() + " | Amount: ₹" + d.getAmount() + " | Date: " + d.getTimestamp());
        }
    }

    // Helper accessors for console UI (keeps DAOs encapsulated)
    public Athlete getAthleteByUserId(int userId) {
        return athleteDAO.getByUserId(userId);
    }

    public void updateAthleteMedalsAndAchievements(int athleteId, int medals, int achievements) {
        athleteDAO.updateMedalsAndAchievements(athleteId, medals, achievements);
    }
}

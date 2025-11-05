package review2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * DBHelper manages the JDBC connection and database initialization.
 *
 * NOTE: Update DB_URL, DB_USER and DB_PASS to match your MySQL setup.
 */
public class DBHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/athleteconnect?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root"; // <-- update if needed

    static {
        try {
            // load the MySQL JDBC driver (optional for newer JDBC versions)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // If driver is not found, further getConnection will fail. Print helpful message.
            System.err.println("MySQL JDBC Driver not found. Add the connector JAR to classpath.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Initialize database schema if not present and pre-load sample data if empty.
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            // Create tables if not exists
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "username VARCHAR(100) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(50) NOT NULL" +
                    ") ENGINE=InnoDB;");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS athletes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL UNIQUE, " +
                    "sport VARCHAR(100), " +
                    "medals INT DEFAULT 0, " +
                    "achievements_count INT DEFAULT 0, " +
                    "funding_goal DOUBLE DEFAULT 0, " +
                    "total_funds_raised DOUBLE DEFAULT 0, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB;");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS donors (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL UNIQUE, " +
                    "total_donated DOUBLE DEFAULT 0, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB;");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS admins (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL UNIQUE, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB;");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS donations (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "donor_id INT NOT NULL, " +
                    "athlete_id INT NOT NULL, " +
                    "amount DOUBLE NOT NULL, " +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (donor_id) REFERENCES donors(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (athlete_id) REFERENCES athletes(id) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB;");

            // Preload sample data if users table is empty
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count == 0) preloadSampleData(conn);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    private static void preloadSampleData(Connection conn) {
        // Insert sample users: 5 athletes, 1 donor, 1 admin (passwords: 'pass' or 'admin')
        try (Statement st = conn.createStatement()) {
            String passHash = PasswordUtils.hashPassword("pass");
            String adminHash = PasswordUtils.hashPassword("admin");

            // Athletes
            st.executeUpdate("INSERT INTO users (name, username, password, role) VALUES " +
                    "('Rahul Sharma','rahul','" + passHash + "','ATHLETE')," +
                    "('Priya Mehta','priya','" + passHash + "','ATHLETE')," +
                    "('Arjun Rao','arjun','" + passHash + "','ATHLETE')," +
                    "('Sneha Iyer','sneha','" + passHash + "','ATHLETE')," +
                    "('Vikram Das','vikram','" + passHash + "','ATHLETE')");

            // Donor
            st.executeUpdate("INSERT INTO users (name, username, password, role) VALUES " +
                    "('Sample Donor','donor','" + passHash + "','DONOR')");

            // Admin
            st.executeUpdate("INSERT INTO users (name, username, password, role) VALUES " +
                    "('Admin','admin','" + adminHash + "','ADMIN')");

            // Link athlete details (we need to fetch inserted user ids).
            // Use a separate Statement for the SELECT to avoid closing the ResultSet when executing updates on 'st'.
            try (Statement st2 = conn.createStatement(); ResultSet rs = st2.executeQuery("SELECT id, username FROM users")) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    switch (username) {
                        case "rahul":
                            st.executeUpdate("INSERT INTO athletes (user_id, sport, medals, achievements_count, funding_goal, total_funds_raised) VALUES (" + id + ", 'Boxing', 5, 10, 5000, 2000)");
                            break;
                        case "priya":
                            st.executeUpdate("INSERT INTO athletes (user_id, sport, medals, achievements_count, funding_goal, total_funds_raised) VALUES (" + id + ", 'Running', 3, 5, 4000, 1200)");
                            break;
                        case "arjun":
                            st.executeUpdate("INSERT INTO athletes (user_id, sport, medals, achievements_count, funding_goal, total_funds_raised) VALUES (" + id + ", 'Swimming', 2, 4, 3000, 800)");
                            break;
                        case "sneha":
                            st.executeUpdate("INSERT INTO athletes (user_id, sport, medals, achievements_count, funding_goal, total_funds_raised) VALUES (" + id + ", 'Badminton', 1, 2, 3500, 1500)");
                            break;
                        case "vikram":
                            st.executeUpdate("INSERT INTO athletes (user_id, sport, medals, achievements_count, funding_goal, total_funds_raised) VALUES (" + id + ", 'Weightlifting', 0, 1, 6000, 500)");
                            break;
                        case "donor":
                            st.executeUpdate("INSERT INTO donors (user_id, total_donated) VALUES (" + id + ", 0)");
                            break;
                        case "admin":
                            st.executeUpdate("INSERT INTO admins (user_id) VALUES (" + id + ")");
                            break;
                    }
                }
            }

            System.out.println("Preloaded sample data into database.");
        } catch (SQLException e) {
            System.err.println("Error preloading data: " + e.getMessage());
        }
    }
}

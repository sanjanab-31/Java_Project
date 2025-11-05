package review2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {
    public int createDonation(int donorId, int athleteId, double amount) {
        String sql = "INSERT INTO donations (donor_id, athlete_id, amount) VALUES (?, ?, ?)";
    try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, donorId);
            ps.setInt(2, athleteId);
            ps.setDouble(3, amount);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return -1;
    }

    public List<Donation> listAll() {
        List<Donation> out = new ArrayList<>();
        String sql = "SELECT id, donor_id, athlete_id, amount, timestamp FROM donations ORDER BY timestamp DESC";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Donation(rs.getInt("id"), rs.getInt("donor_id"), rs.getInt("athlete_id"), rs.getDouble("amount"), rs.getTimestamp("timestamp")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    public List<Donation> listByDonorId(int donorId) {
        List<Donation> out = new ArrayList<>();
        String sql = "SELECT id, donor_id, athlete_id, amount, timestamp FROM donations WHERE donor_id = ? ORDER BY timestamp DESC";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, donorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(new Donation(rs.getInt("id"), rs.getInt("donor_id"), rs.getInt("athlete_id"), rs.getDouble("amount"), rs.getTimestamp("timestamp")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }
}

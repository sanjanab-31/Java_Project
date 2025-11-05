package review2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DonorDAO {
    public int createDonor(int userId) {
        String sql = "INSERT INTO donors (user_id, total_donated) VALUES (?, 0)";
    try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return -1;
    }

    public Donor getByUserId(int userId) {
        String sql = "SELECT id, user_id, total_donated FROM donors WHERE user_id = ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return new Donor(rs.getInt("id"), rs.getInt("user_id"), rs.getDouble("total_donated")); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public Donor getById(int id) {
        String sql = "SELECT id, user_id, total_donated FROM donors WHERE id = ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return new Donor(rs.getInt("id"), rs.getInt("user_id"), rs.getDouble("total_donated")); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public void addToTotal(int donorId, double amount) {
        String sql = "UPDATE donors SET total_donated = total_donated + ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, donorId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}

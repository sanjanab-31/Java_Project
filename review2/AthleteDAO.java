package review2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AthleteDAO {
    public int createAthlete(Athlete a) {
        String sql = "INSERT INTO athletes (user_id, sport, medals, achievements_count, funding_goal, total_funds_raised) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getSport());
            ps.setInt(3, a.getMedals());
            ps.setInt(4, a.getAchievementsCount());
            ps.setDouble(5, a.getFundingGoal());
            ps.setDouble(6, a.getTotalFundsRaised());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return -1;
    }

    public Athlete getByUserId(int userId) {
        String sql = "SELECT id, user_id, sport, medals, achievements_count, funding_goal, total_funds_raised FROM athletes WHERE user_id = ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public Athlete getById(int id) {
        String sql = "SELECT id, user_id, sport, medals, achievements_count, funding_goal, total_funds_raised FROM athletes WHERE id = ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return map(rs); }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public List<Athlete> listAll() {
        List<Athlete> out = new ArrayList<>();
        String sql = "SELECT id, user_id, sport, medals, achievements_count, funding_goal, total_funds_raised FROM athletes";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) { throw new RuntimeException(e); }
        return out;
    }

    public List<Athlete> filter(String sport, int minMedals, boolean urgent) {
        List<Athlete> all = listAll();
        List<Athlete> filtered = new ArrayList<>();
        for (Athlete a : all) {
            boolean matches = true;
            if (sport != null && !sport.isEmpty() && !a.getSport().equalsIgnoreCase(sport)) matches = false;
            if (minMedals > -1 && a.getMedals() < minMedals) matches = false;
            if (matches) filtered.add(a);
        }
        if (urgent) filtered.sort(Comparator.comparingDouble(a -> a.getTotalFundsRaised() / (a.getFundingGoal() == 0 ? 1 : a.getFundingGoal())));
        return filtered;
    }

    public void updateFunds(int athleteId, double newTotal) {
        String sql = "UPDATE athletes SET total_funds_raised = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newTotal);
            ps.setInt(2, athleteId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void updateMedalsAndAchievements(int athleteId, int medals, int achievements) {
        String sql = "UPDATE athletes SET medals = ?, achievements_count = ? WHERE id = ?";
        try (Connection conn = DBHelper.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medals);
            ps.setInt(2, achievements);
            ps.setInt(3, athleteId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private Athlete map(ResultSet rs) throws SQLException {
        return new Athlete(rs.getInt("id"), rs.getInt("user_id"), rs.getString("sport"), rs.getInt("medals"), rs.getInt("achievements_count"), rs.getDouble("funding_goal"), rs.getDouble("total_funds_raised"));
    }
}

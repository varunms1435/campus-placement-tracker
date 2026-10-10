package dao;

import model.Interview;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InterviewDAO {

    // the one format used to store date and time in the database
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // turns one row of the result into an Interview object
    private Interview mapRow(ResultSet rs) throws SQLException {
        return new Interview(
                rs.getInt("interview_id"),
                rs.getInt("application_id"),
                rs.getString("round_name"),
                LocalDateTime.parse(rs.getString("schedule_time"), FMT),
                rs.getString("notes"));
    }

    // CREATE
    public boolean addInterview(Interview i) {
        String sql = "INSERT INTO interview (application_id, round_name, schedule_time, notes) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, i.getApplicationId());
            ps.setString(2, i.getRoundName());
            ps.setString(3, i.getScheduleTime().format(FMT));
            ps.setString(4, i.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding interview: " + e.getMessage());
            return false;
        }
    }

    // READ: all interviews
    public List<Interview> getAllInterviews() {
        List<Interview> list = new ArrayList<>();
        String sql = "SELECT * FROM interview ORDER BY schedule_time";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error reading interviews: " + e.getMessage());
        }
        return list;
    }

    // READ: one interview by ID (returns null if not found)
    public Interview getInterviewById(int id) {
        String sql = "SELECT * FROM interview WHERE interview_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding interview: " + e.getMessage());
        }
        return null;
    }

    // READ: all interviews for one application
    public List<Interview> getInterviewsByApplication(int applicationId) {
        List<Interview> list = new ArrayList<>();
        String sql = "SELECT * FROM interview WHERE application_id = ? ORDER BY schedule_time";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading interviews: " + e.getMessage());
        }
        return list;
    }

    // READ: interviews from now onwards, soonest first
    public List<Interview> getUpcomingInterviews() {
        List<Interview> list = new ArrayList<>();
        String sql = "SELECT * FROM interview WHERE schedule_time >= ? ORDER BY schedule_time";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, LocalDateTime.now().format(FMT));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading interviews: " + e.getMessage());
        }
        return list;
    }

    // UPDATE
    public boolean updateInterview(Interview i) {
        String sql = "UPDATE interview SET application_id = ?, round_name = ?, "
                   + "schedule_time = ?, notes = ? WHERE interview_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, i.getApplicationId());
            ps.setString(2, i.getRoundName());
            ps.setString(3, i.getScheduleTime().format(FMT));
            ps.setString(4, i.getNotes());
            ps.setInt(5, i.getInterviewId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating interview: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteInterview(int id) {
        String sql = "DELETE FROM interview WHERE interview_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting interview: " + e.getMessage());
            return false;
        }
    }
}
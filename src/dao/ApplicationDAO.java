package dao;

import model.Application;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    // turns one row of the result into an Application object
    private Application mapRow(ResultSet rs) throws SQLException {
        return new Application(
                rs.getInt("application_id"),
                rs.getInt("company_id"),
                rs.getString("job_role"),
                rs.getDouble("package_lpa"),
                LocalDate.parse(rs.getString("applied_date")),
                rs.getString("status"));
    }

    // CREATE
    public boolean addApplication(Application a) {
        String sql = "INSERT INTO application (company_id, job_role, package_lpa, applied_date, status) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getCompanyId());
            ps.setString(2, a.getJobRole());
            ps.setDouble(3, a.getPackageLpa());
            ps.setString(4, a.getAppliedDate().toString());
            ps.setString(5, a.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding application: " + e.getMessage());
            return false;
        }
    }

    // READ: all applications
    public List<Application> getAllApplications() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM application ORDER BY application_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error reading applications: " + e.getMessage());
        }
        return list;
    }

    // READ: one application by ID (returns null if not found)
    public Application getApplicationById(int id) {
        String sql = "SELECT * FROM application WHERE application_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding application: " + e.getMessage());
        }
        return null;
    }

    // READ: all applications for one company
    public List<Application> getApplicationsByCompany(int companyId) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM application WHERE company_id = ? ORDER BY application_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading applications: " + e.getMessage());
        }
        return list;
    }

    // READ: all applications with a given status
    public List<Application> getApplicationsByStatus(String status) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT * FROM application WHERE status = ? ORDER BY application_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading applications: " + e.getMessage());
        }
        return list;
    }

    // UPDATE: change all details of an application
    public boolean updateApplication(Application a) {
        String sql = "UPDATE application SET company_id = ?, job_role = ?, package_lpa = ?, "
                   + "applied_date = ?, status = ? WHERE application_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getCompanyId());
            ps.setString(2, a.getJobRole());
            ps.setDouble(3, a.getPackageLpa());
            ps.setString(4, a.getAppliedDate().toString());
            ps.setString(5, a.getStatus());
            ps.setInt(6, a.getApplicationId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating application: " + e.getMessage());
            return false;
        }
    }

    // UPDATE: change only the status
    public boolean updateStatus(int applicationId, String newStatus) {
        String sql = "UPDATE application SET status = ? WHERE application_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, applicationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    // DELETE (its interviews are removed too)
    public boolean deleteApplication(int id) {
        String sql = "DELETE FROM application WHERE application_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting application: " + e.getMessage());
            return false;
        }
    }
}

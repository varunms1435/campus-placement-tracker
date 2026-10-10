package dao;

import model.Company;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {

    // CREATE: add a new company
    public boolean addCompany(Company c) {
        String sql = "INSERT INTO company (name, location, website) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getLocation());
            ps.setString(3, c.getWebsite());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding company: " + e.getMessage());
            return false;
        }
    }

    // READ: get all companies
    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT * FROM company ORDER BY company_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Company(
                        rs.getInt("company_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getString("website")));
            }
        } catch (SQLException e) {
            System.out.println("Error reading companies: " + e.getMessage());
        }
        return list;
    }

    // READ: get one company by its ID (returns null if not found)
    public Company getCompanyById(int id) {
        String sql = "SELECT * FROM company WHERE company_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Company(
                            rs.getInt("company_id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getString("website"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding company: " + e.getMessage());
        }
        return null;
    }

    // UPDATE: change an existing company
    public boolean updateCompany(Company c) {
        String sql = "UPDATE company SET name = ?, location = ?, website = ? WHERE company_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getLocation());
            ps.setString(3, c.getWebsite());
            ps.setInt(4, c.getCompanyId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating company: " + e.getMessage());
            return false;
        }
    }

    // DELETE: remove a company (its applications and interviews are removed too)
    public boolean deleteCompany(int id) {
        String sql = "DELETE FROM company WHERE company_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting company: " + e.getMessage());
            return false;
        }
    }
}

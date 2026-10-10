package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:placement.db";

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(URL);
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");   // makes ON DELETE CASCADE work
        }
        return con;
    }
}
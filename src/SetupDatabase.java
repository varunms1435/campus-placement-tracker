import util.DBConnection;
import java.sql.Connection;
import java.sql.Statement;

public class SetupDatabase {
    public static void main(String[] args) {
        String[] steps = {
            "DROP TABLE IF EXISTS interview",
            "DROP TABLE IF EXISTS application",
            "DROP TABLE IF EXISTS company",

            """
            CREATE TABLE company (
                company_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name       TEXT NOT NULL UNIQUE,
                location   TEXT,
                website    TEXT
            )
            """,

            """
            CREATE TABLE application (
                application_id INTEGER PRIMARY KEY AUTOINCREMENT,
                company_id     INTEGER NOT NULL,
                job_role       TEXT NOT NULL,
                package_lpa    REAL,
                applied_date   TEXT NOT NULL,
                status         TEXT NOT NULL DEFAULT 'Applied'
                    CHECK (status IN ('Applied','Test Scheduled','Interview','Selected','Rejected')),
                FOREIGN KEY (company_id) REFERENCES company(company_id) ON DELETE CASCADE
            )
            """,

            """
            CREATE TABLE interview (
                interview_id   INTEGER PRIMARY KEY AUTOINCREMENT,
                application_id INTEGER NOT NULL,
                round_name     TEXT NOT NULL,
                schedule_time  TEXT NOT NULL,
                notes          TEXT,
                FOREIGN KEY (application_id) REFERENCES application(application_id) ON DELETE CASCADE
            )
            """,

            "INSERT INTO company (name, location, website) VALUES " +
            "('TCS','Bengaluru','www.tcs.com'), ('Infosys','Mysuru','www.infosys.com')",

            "INSERT INTO application (company_id, job_role, package_lpa, applied_date, status) VALUES " +
            "(1,'Software Engineer',7.00,'2026-09-15','Applied'), " +
            "(2,'Systems Engineer',6.50,'2026-09-20','Test Scheduled')"
        };

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {
            for (String sql : steps) {
                st.execute(sql);
            }
            System.out.println("Database and tables created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

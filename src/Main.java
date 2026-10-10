import dao.ApplicationDAO;
import dao.CompanyDAO;
import dao.InterviewDAO;
import model.Application;
import model.Company;
import model.Interview;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final CompanyDAO companyDAO = new CompanyDAO();
    private static final ApplicationDAO applicationDAO = new ApplicationDAO();
    private static final InterviewDAO interviewDAO = new InterviewDAO();

    private static final String[] STATUSES =
            {"Applied", "Test Scheduled", "Interview", "Selected", "Rejected"};
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ================= INPUT HELPERS =================

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static String readRequired(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (!s.isEmpty()) return s;
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number, for example 7.5");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(readLine(prompt));
            } catch (DateTimeParseException e) {
                System.out.println("Use the format yyyy-MM-dd, for example 2026-10-15");
            }
        }
    }

    private static LocalDateTime readDateTime(String prompt) {
        while (true) {
            try {
                return LocalDateTime.parse(readLine(prompt), DT_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Use the format yyyy-MM-dd HH:mm, for example 2026-10-15 14:30");
            }
        }
    }

    // "keep current value" versions: pressing Enter keeps what is already there
    private static String askText(String prompt, String current) {
        String shown = (current == null) ? "" : current;
        String s = readLine(prompt + " [" + shown + "]: ");
        return s.isEmpty() ? current : s;
    }

    private static double askDouble(String prompt, double current) {
        while (true) {
            String s = readLine(prompt + " [" + current + "]: ");
            if (s.isEmpty()) return current;
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number, for example 7.5");
            }
        }
    }

    private static LocalDate askDate(String prompt, LocalDate current) {
        while (true) {
            String s = readLine(prompt + " [" + current + "]: ");
            if (s.isEmpty()) return current;
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Use the format yyyy-MM-dd, for example 2026-10-15");
            }
        }
    }

    private static LocalDateTime askDateTime(String prompt, LocalDateTime current) {
        while (true) {
            String s = readLine(prompt + " [" + current.format(DT_FMT) + "]: ");
            if (s.isEmpty()) return current;
            try {
                return LocalDateTime.parse(s, DT_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Use the format yyyy-MM-dd HH:mm, for example 2026-10-15 14:30");
            }
        }
    }

    private static String chooseStatus() {
        for (int i = 0; i < STATUSES.length; i++) {
            System.out.println((i + 1) + ". " + STATUSES[i]);
        }
        while (true) {
            int n = readInt("Choose status (1-" + STATUSES.length + "): ");
            if (n >= 1 && n <= STATUSES.length) return STATUSES[n - 1];
            System.out.println("Please choose a number from the list.");
        }
    }

    private static boolean confirm(String message) {
        return readLine(message + " (y/n): ").equalsIgnoreCase("y");
    }

    private static void printAll(List<?> list, String emptyMessage) {
        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Object o : list) {
            System.out.println(o);
        }
    }

    // ================= MAIN MENU =================

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n===== CAMPUS PLACEMENT TRACKER =====");
            System.out.println("1. Manage companies");
            System.out.println("2. Manage applications");
            System.out.println("3. Manage interviews");
            System.out.println("0. Exit");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> companyMenu();
                case 2 -> applicationMenu();
                case 3 -> interviewMenu();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye!");
    }

    // ================= COMPANIES =================

    private static void companyMenu() {
        while (true) {
            System.out.println("\n--- COMPANIES ---");
            System.out.println("1. Add company");
            System.out.println("2. View all companies");
            System.out.println("3. Update company");
            System.out.println("4. Delete company");
            System.out.println("0. Back");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addCompany();
                case 2 -> printAll(companyDAO.getAllCompanies(), "No companies yet.");
                case 3 -> updateCompany();
                case 4 -> deleteCompany();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addCompany() {
        String name = readRequired("Company name: ");
        String location = readLine("Location (optional): ");
        String website = readLine("Website (optional): ");
        if (companyDAO.addCompany(new Company(name, location, website))) {
            System.out.println("Company added.");
        }
    }

    private static void updateCompany() {
        int id = readInt("Company ID to update: ");
        Company c = companyDAO.getCompanyById(id);
        if (c == null) {
            System.out.println("No company with that ID.");
            return;
        }
        System.out.println("Press Enter to keep the current value.");
        c.setName(askText("Name", c.getName()));
        c.setLocation(askText("Location", c.getLocation()));
        c.setWebsite(askText("Website", c.getWebsite()));
        if (companyDAO.updateCompany(c)) {
            System.out.println("Company updated.");
        }
    }

    private static void deleteCompany() {
        int id = readInt("Company ID to delete: ");
        Company c = companyDAO.getCompanyById(id);
        if (c == null) {
            System.out.println("No company with that ID.");
            return;
        }
        System.out.println(c);
        if (confirm("This also deletes its applications and interviews. Delete?")) {
            if (companyDAO.deleteCompany(id)) {
                System.out.println("Company deleted.");
            }
        } else {
            System.out.println("Cancelled.");
        }
    }

    // ================= APPLICATIONS =================

    private static void applicationMenu() {
        while (true) {
            System.out.println("\n--- APPLICATIONS ---");
            System.out.println("1. Add application");
            System.out.println("2. View all applications");
            System.out.println("3. View applications of a company");
            System.out.println("4. Search applications by status");
            System.out.println("5. Update status");
            System.out.println("6. Edit application details");
            System.out.println("7. Delete application");
            System.out.println("0. Back");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addApplication();
                case 2 -> printAll(applicationDAO.getAllApplications(), "No applications yet.");
                case 3 -> viewApplicationsOfCompany();
                case 4 -> searchByStatus();
                case 5 -> updateApplicationStatus();
                case 6 -> editApplication();
                case 7 -> deleteApplication();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addApplication() {
        int companyId = readInt("Company ID: ");
        if (companyDAO.getCompanyById(companyId) == null) {
            System.out.println("No company with that ID. Add the company first.");
            return;
        }
        String role = readRequired("Job role: ");
        double lpa = readDouble("Package in LPA: ");
        LocalDate date = readDate("Applied date (yyyy-MM-dd): ");
        String status = chooseStatus();
        if (applicationDAO.addApplication(new Application(companyId, role, lpa, date, status))) {
            System.out.println("Application added.");
        }
    }

    private static void viewApplicationsOfCompany() {
        int companyId = readInt("Company ID: ");
        printAll(applicationDAO.getApplicationsByCompany(companyId),
                "No applications for that company.");
    }

    private static void searchByStatus() {
        String status = chooseStatus();
        printAll(applicationDAO.getApplicationsByStatus(status),
                "No applications with status " + status + ".");
    }

    private static void updateApplicationStatus() {
        int id = readInt("Application ID: ");
        Application a = applicationDAO.getApplicationById(id);
        if (a == null) {
            System.out.println("No application with that ID.");
            return;
        }
        System.out.println(a);
        String status = chooseStatus();
        if (applicationDAO.updateStatus(id, status)) {
            System.out.println("Status updated.");
        }
    }

    private static void editApplication() {
        int id = readInt("Application ID to edit: ");
        Application a = applicationDAO.getApplicationById(id);
        if (a == null) {
            System.out.println("No application with that ID.");
            return;
        }
        System.out.println("Press Enter to keep the current value.");
        a.setJobRole(askText("Job role", a.getJobRole()));
        a.setPackageLpa(askDouble("Package (LPA)", a.getPackageLpa()));
        a.setAppliedDate(askDate("Applied date", a.getAppliedDate()));
        if (applicationDAO.updateApplication(a)) {
            System.out.println("Application updated.");
        }
    }

    private static void deleteApplication() {
        int id = readInt("Application ID to delete: ");
        Application a = applicationDAO.getApplicationById(id);
        if (a == null) {
            System.out.println("No application with that ID.");
            return;
        }
        System.out.println(a);
        if (confirm("This also deletes its interviews. Delete?")) {
            if (applicationDAO.deleteApplication(id)) {
                System.out.println("Application deleted.");
            }
        } else {
            System.out.println("Cancelled.");
        }
    }

    // ================= INTERVIEWS =================

    private static void interviewMenu() {
        while (true) {
            System.out.println("\n--- INTERVIEWS ---");
            System.out.println("1. Add interview");
            System.out.println("2. View all interviews");
            System.out.println("3. View interviews of an application");
            System.out.println("4. View upcoming interviews");
            System.out.println("5. Edit interview");
            System.out.println("6. Delete interview");
            System.out.println("0. Back");
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addInterview();
                case 2 -> printAll(interviewDAO.getAllInterviews(), "No interviews yet.");
                case 3 -> viewInterviewsOfApplication();
                case 4 -> printAll(interviewDAO.getUpcomingInterviews(), "No upcoming interviews.");
                case 5 -> editInterview();
                case 6 -> deleteInterview();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addInterview() {
        int applicationId = readInt("Application ID: ");
        if (applicationDAO.getApplicationById(applicationId) == null) {
            System.out.println("No application with that ID. Add the application first.");
            return;
        }
        String round = readRequired("Round name (e.g. Technical, HR): ");
        LocalDateTime time = readDateTime("Date and time (yyyy-MM-dd HH:mm): ");
        String notes = readLine("Notes (optional): ");
        if (interviewDAO.addInterview(new Interview(applicationId, round, time, notes))) {
            System.out.println("Interview added.");
        }
    }

    private static void viewInterviewsOfApplication() {
        int applicationId = readInt("Application ID: ");
        printAll(interviewDAO.getInterviewsByApplication(applicationId),
                "No interviews for that application.");
    }

    private static void editInterview() {
        int id = readInt("Interview ID to edit: ");
        Interview i = interviewDAO.getInterviewById(id);
        if (i == null) {
            System.out.println("No interview with that ID.");
            return;
        }
        System.out.println("Press Enter to keep the current value.");
        i.setRoundName(askText("Round name", i.getRoundName()));
        i.setScheduleTime(askDateTime("Date and time", i.getScheduleTime()));
        i.setNotes(askText("Notes", i.getNotes()));
        if (interviewDAO.updateInterview(i)) {
            System.out.println("Interview updated.");
        }
    }

    private static void deleteInterview() {
        int id = readInt("Interview ID to delete: ");
        Interview i = interviewDAO.getInterviewById(id);
        if (i == null) {
            System.out.println("No interview with that ID.");
            return;
        }
        System.out.println(i);
        if (confirm("Delete this interview?")) {
            if (interviewDAO.deleteInterview(id)) {
                System.out.println("Interview deleted.");
            }
        } else {
            System.out.println("Cancelled.");
        }
    }
}
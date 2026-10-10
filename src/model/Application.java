package model;

import java.time.LocalDate;

public class Application {
    private int applicationId;
    private int companyId;
    private String jobRole;
    private double packageLpa;
    private LocalDate appliedDate;
    private String status;

    // used when adding a new application
    public Application(int companyId, String jobRole, double packageLpa,
                       LocalDate appliedDate, String status) {
        this.companyId = companyId;
        this.jobRole = jobRole;
        this.packageLpa = packageLpa;
        this.appliedDate = appliedDate;
        this.status = status;
    }

    // used when reading an application from the database
    public Application(int applicationId, int companyId, String jobRole,
                       double packageLpa, LocalDate appliedDate, String status) {
        this.applicationId = applicationId;
        this.companyId = companyId;
        this.jobRole = jobRole;
        this.packageLpa = packageLpa;
        this.appliedDate = appliedDate;
        this.status = status;
    }

    public int getApplicationId() { return applicationId; }
    public int getCompanyId() { return companyId; }
    public String getJobRole() { return jobRole; }
    public double getPackageLpa() { return packageLpa; }
    public LocalDate getAppliedDate() { return appliedDate; }
    public String getStatus() { return status; }

    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }
    public void setPackageLpa(double packageLpa) { this.packageLpa = packageLpa; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return applicationId + " | company " + companyId + " | " + jobRole
                + " | " + packageLpa + " LPA | " + appliedDate + " | " + status;
    }
}
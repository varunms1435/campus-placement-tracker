package model;

public class Company {
    private int companyId;
    private String name;
    private String location;
    private String website;

    // used when adding a new company (the database creates the ID)
    public Company(String name, String location, String website) {
        this.name = name;
        this.location = location;
        this.website = website;
    }

    // used when reading a company from the database
    public Company(int companyId, String name, String location, String website) {
        this.companyId = companyId;
        this.name = name;
        this.location = location;
        this.website = website;
    }

    public int getCompanyId() { return companyId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getWebsite() { return website; }

    public void setCompanyId(int companyId) { this.companyId = companyId; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setWebsite(String website) { this.website = website; }

    @Override
    public String toString() {
        return companyId + " | " + name + " | " + location + " | " + website;
    }
}

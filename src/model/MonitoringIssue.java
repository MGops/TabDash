package src.model;

public class MonitoringIssue {
    private String issueType;
    private String notes;
    private boolean active;

    public MonitoringIssue(String issueType, String notes) {
        this.issueType = issueType;
        this.notes = notes;
        this.active = true;
    }

    // Getters
    public String getIssueType() {return issueType;}
    public String getNotes() {return notes;}
    public boolean isActive() {return active;}
    

    // Setters
    public void setIssueType(String issueType) {this.issueType = issueType;}
    public void setNotes(String notes) {this.notes = notes;}
    public void setActive(boolean active) {this.active = active;}
    
    @Override
    public String toString() {
        return issueType + ": " + notes;
    }
}

package src;

import java.util.HashMap;
import java.util.Date;

public class Patient {
    private String patientId;
    private HashMap<String, Integer> medications;
    private boolean mh03Completed;
    private String sectionStatus;
    private Date admissionDate;
    private Date detentionDate;
    private Date originalDetentionDate;
    private String capacity;
    private boolean soadRequested;
    private Date soadDate;
    private String soadReference;
    private boolean s62Completed;
    private Date s62Date;
    private boolean t3Provided;
    private Date t3Date;
    private Date t3ReviewDate;
    private boolean t2Completed;
    private Date t2Date;
    private Date t2ReviewDate;
   
    public Patient(String patientId) {
        this.patientId = patientId;
        this.medications = new HashMap<>();

    }

    private void initialiseMHAFields() {
        this.mh03Completed = false;
        this.sectionStatus = "Informal";
        this.admissionDate = new Date();
        this.capacity = "No";
        this.soadRequested = false;
        this.s62Completed = false;
        this.t3Provided = false;
        this.t2Completed = false;
    }

    public String getPatientId() {
        return patientId;
    }

    public void addMedication(String medication, Integer acbScore) {
        medications.put(medication, acbScore);
    }

    public void removeMedication(String medication) {
        medications.remove(medication);
    }

    public int getTotalACBScore() {
        Integer totalACBScore = 0;

        for (Integer acbScore : medications.values()) {
            totalACBScore += acbScore;
        }
        return totalACBScore;
    }

    public HashMap<String, Integer> getMedications() {
        return medications;
    }

    public boolean isMh03Completed() {return mh03Completed;}
    public void setMh03Completed(boolean mh03Completed) {this.mh03Completed = mh03Completed;}

    public String getSectionStatus() {return sectionStatus;}
    public void setSectionStatus(String sectionStatus) {this.sectionStatus = sectionStatus;}

    public Date getAdmissionDate() {return admissionDate;}
    public void setAdmissionDate(Date admissionDate) {this.admissionDate = admissionDate;}

    public Date getDetentionDate() {return detentionDate;}
    public void setDetentionDate(Date detentionDate) {this.detentionDate = detentionDate;}

    public Date getOriginalDetentionDate() {return originalDetentionDate;}
    public void setOriginalDetentionDate(Date originalDetentionDate) {this.originalDetentionDate = originalDetentionDate;}

    public String getCapacity() {return capacity;}
    public void setCapacity(String capacity) {this.capacity = capacity;}

    public boolean isSoadRequested() {return soadRequested;}
    public void setSoadRequested(boolean soadRequested) {this.soadRequested = soadRequested;}

    public Date getSoadDate() {return soadDate;}
    public void setSoadDate(Date soadDate) {this.soadDate = soadDate;}

    public String getSoadReference() {return soadReference;}
    public void setSoadReference(String soadReference) {this.soadReference = soadReference;}
}

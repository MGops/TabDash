package src;

import java.util.HashMap;
import java.util.Date;

public class Patient {
    private String patientId;
    private HashMap<String, Medication> medications;
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
    private Date tribunalDate;
    private String tribunalType;
    private Date reportDueDate;
    private boolean emergencyMedicalLeave;
    private String otherLeave;
   
    public Patient(String patientId) {
        this.patientId = patientId;
        this.medications = new HashMap<String, Medication>();
        initialiseMHAFields();
    }

    private void initialiseMHAFields() {
        this.mh03Completed = false;
        this.sectionStatus = "Informal";
        this.admissionDate = new Date();
        this.detentionDate = null;
        this.capacity = "No";
        this.soadRequested = false;
        this.soadDate = null;
        this.soadReference = null;
        this.s62Completed = false;
        this.s62Date = null;
        this.t3Provided = false;
        this.t3Date = null;
        this.t3ReviewDate = null;
        this.t2Completed = false;
        this.t2Date = null;
        this.t2ReviewDate = null;
        this.tribunalDate = null;
        this.tribunalType = null;
        this.reportDueDate = null;
        this.emergencyMedicalLeave = false;
        this.otherLeave = "";
    }

    public void resetMHAdata() {
        initialiseMHAFields();
    }

    public String getPatientId() {
        return patientId;
    }

    public void addMedication(String medicationName, Integer acbScore) {
        Medication med = new Medication(medicationName);
        med.setAcbScore(acbScore);
        medications.put(medicationName, med);
    }

    public void removeMedication(String medication) {
        medications.remove(medication);
    }

    public int getTotalACBScore() {
        Integer totalACBScore = 0;
        for (Medication med : medications.values()) {
            if (med.getAcbScore() != null) {
                totalACBScore += med.getAcbScore();
            }
        }
        return totalACBScore;
    }

    public HashMap<String, Medication> getMedications() {
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

    public boolean isS62Completed() {return s62Completed;}
    public void sets62Completed(boolean s62Completed) {this.s62Completed = s62Completed;}

    public Date getS62Date() {return s62Date;}
    public void setS62Date(Date s62Date) {this.s62Date = s62Date;}

    public boolean isT3Provided() {return t3Provided;}
    public void setT3Provided(boolean t3Provided) {this.t3Provided = t3Provided;}

    public Date getT3Date() {return t3Date;}
    public void setT3Date(Date t3Date) {this.t3Date = t3Date;}

    public Date getT3ReviewDate() {return t3ReviewDate;}
    public void setT3ReviewDate(Date t3ReviewDate) {this.t3ReviewDate = t3ReviewDate;}

    public boolean isT2Completed() {return t2Completed;}
    public void setT2Completed(boolean t2Completed) {this.t2Completed = t2Completed;}

    public Date getT2Date() {return t2Date;}
    public void setT2Date(Date t2Date) {this.t2Date = t2Date;}

    public Date getT2ReviewDate() {return t2ReviewDate;}
    public void setT2ReviewDate(Date t2ReviewDate) {this.t2ReviewDate = t2ReviewDate;}

    public Date getTribunalDate() {return tribunalDate;}
    public void setTribunalDate(Date tribunalDate) {this.tribunalDate = tribunalDate;}

    public String getTribunalType() {return tribunalType;}
    public void setTribunalType(String tribunalType) {this.tribunalType= tribunalType;}

    public Date getReportDueDate() {return reportDueDate;}
    public void setReportDueDate(Date reportDueDate) {this.reportDueDate = reportDueDate;}

    public boolean isEmergencyMedicalLeave() {
        return emergencyMedicalLeave;
    }

    public void setEmergencyMedicalLeave(boolean emergencyMedicalLeave) {
        this.emergencyMedicalLeave = emergencyMedicalLeave;
    }

    public String getOtherLeave() {
        return otherLeave;
    }

    public void setOtherLeave(String otherLeave) {
        this.otherLeave = otherLeave;
    }
}
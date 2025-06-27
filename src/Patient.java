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
}

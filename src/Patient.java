package src;

import java.util.HashMap;

public class Patient {
    private String patientId;
    private HashMap<String, Integer> medications;
   
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

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
}

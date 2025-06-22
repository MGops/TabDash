package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDataManager {
    private static final String PATIENTS_DIR = "data/patients/";
    
    // Save one patient's medications to their own file
    public static void savePatient(Patient patient) {
        // Create directory if it doesn't exist
        File dir = new File(PATIENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // Create filename from patient ID (replace spaces with underscores)
        String filename = PATIENTS_DIR + patient.getPatientId().replace(" ", "_") + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String medName : patient.getMedications().keySet()) {
                Integer acbScore = patient.getMedications().get(medName);
                writer.write(medName + "," + acbScore);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving patient " + patient.getPatientId() + ": " + e.getMessage());
        }
    }
    
    // Load one patient's medications from their file
    public static void loadPatient(Patient patient) {
        String filename = PATIENTS_DIR + patient.getPatientId().replace(" ", "_") + ".txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String medName = parts[0].trim();
                    Integer acbScore = Integer.parseInt(parts[1].trim());
                    patient.addMedication(medName, acbScore);
                }
            }
        } catch (FileNotFoundException e) {
            // No file exists yet - that's fine for new patients
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading patient " + patient.getPatientId() + ": " + e.getMessage());
        }
    }
}
package src;

import java.io.*;
import java.util.*;

public class PatientDataManager {
    private static final String DATA_FILE = "data/patients.json";

    public static void saveAllPatients(List<Patient> patients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))){
            writer.write("{\n}");

            for (int i = 0; i < patients.size(); i++) {
                Patient patient = patients.get(i);
                writer.write("  \"" + patient.getPatientId() + "\":{\n");
                writer.write("    \"medications\": {\n");

                HashMap<String, Integer> meds = patient.getMedications();
                String[] medNames = meds.keySet().toArray(new String[0]);

                for (int j = 0; j < medNames.length; j++) {
                    String medName = medNames[j];
                    Integer acbScore = meds.get(medName);
                    writer.write("    \"" + medName + "\": " + acbScore);
                    if (j <medNames.length -1) writer.write(",");
                    writer.write("\n");
                }

                writer.write("    }\n");
                writer.write("  }");
                if (i < patients.size() - 1) writer.write(",");
                writer.write("\n");
            }
            
            writer.write("\n");
        } catch (Exception e) {
            System.err.println("Error saving patients: " + e.getMessage());
        }
    }

    
    public static List<Patient> loadAllPatients() {
        List<Patient> patients = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            Patient currentPatient = null;

            while ((line = reader.readLine()) != null)  {
                line = line.trim();
                
                if (line.startsWith("\"") && line.contains("\":")) {
                    String patientId = line.substring(1, line.indexOf("\":"));
                    currentPatient = new Patient(patientId);
                    patients.add(currentPatient);
                }

                else if (currentPatient != null && line.startsWith("\"") && line.contains(":") && !line.contains("medications")) {
                    String medName = line.substring(1, line.indexOf("\":"));
                    String scoreStr = line.substring(line.indexOf(":") + 1).replace(",", "").trim();
                    try {
                        Integer acbScore = Integer.parseInt(scoreStr);
                        currentPatient.addMedication(medName, acbScore);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid ACB score for " + medName + ": " + scoreStr);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing patient data found. Starting fresh");
        } catch (IOException e) {
            System.err.println("Error loading patients: " + e.getMessage());
        }
        return patients;
    }
}

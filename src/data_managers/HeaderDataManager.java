package src.data_managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import src.model.Patient;

public class HeaderDataManager {
    private static final String HEADER_DIR = "data/header/";

    public static void savePatientHeaderData(Patient patient) {
        File dir = new File(HEADER_DIR);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        String filename = HEADER_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write("care_coordinator=" +(patient.getCareCoordinator() != null ? patient.getCareCoordinator() : ""));
            writer.newLine();
            writer.write("resus_status=" + (patient.getResusStatus() != null ? patient.getResusStatus() : "For resus"));
            writer.newLine();
            writer.write("named_nurse=" + (patient.getNamedNurse() != null ? patient.getNamedNurse() : ""));
            writer.newLine();
            writer.write("cmht=" + (patient.getCmht() != null ? patient.getCmht() : ""));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving header data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }

    public static void loadPatientHeaderData(Patient patient) {
        String filename = HEADER_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    switch (key) {
                        case "care_coordinator":
                            patient.setCareCoordinator(value.isEmpty() ? "" : value);
                            break;
                        case "resus_status":
                            patient.setResusStatus(value.isEmpty() ? "For resus" : value);
                            break;
                        case "named_nurse":
                            patient.setNamedNurse(value.isEmpty() ? "" : value);
                            break;
                        case "cmht":
                            patient.setCmht(value.isEmpty() ? "" : value);
                            break;
                        default:
                            System.out.println("Unknown header field: " + key);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No header data found for " + patient.getPatientId() + " - using defaults");
        } catch (IOException e) {
            System.err.println("Error loading header data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }
}

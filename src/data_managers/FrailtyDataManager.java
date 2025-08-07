package src.data_managers;

import java.io.*;
import src.model.Patient;

public class FrailtyDataManager {
    private static final String FRAILTY_DIR = "data/frailty/";

    public static void savePatientFrailtyData(Patient patient) {
        File dir = new File(FRAILTY_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = FRAILTY_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("falls_count=" + patient.getFallsCount());
            writer.newLine();
            writer.write("falls_button_color=" + (patient.getFallsButtonColour() != null ? patient.getFallsButtonColour() : "RED"));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving frailty data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }

    public static void loadPatientFrailtyData(Patient patient) {
        String filename = FRAILTY_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    switch (key) {
                        case "falls_count":
                            try {
                                patient.setFallsCount(Integer.parseInt(value));
                            } catch (NumberFormatException e) {
                                patient.setFallsCount(0);
                            }
                            break;
                        case "falls_button_color":
                            patient.setFallsButtonColour(value.isEmpty() ? "RED" : value);
                            break;
                        default:
                            System.out.println("Unknown frailty field: " + key);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No frailty data found for " + patient.getPatientId() + " - using defaults");
        } catch (IOException e) {
            System.err.println("Error loading frailty data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }
}
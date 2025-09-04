package src.data_managers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import src.model.Patient;

public class FrailtyDataManager {
    private static final String FRAILTY_DIR = "data/frailty/";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
            writer.write("last_fall_date=" + formatDate(patient.getLastFallDate()));
            writer.newLine();
            writer.write("vte_assessment_date=" + formatDate(patient.getVteAssessmentDate()));
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
                        case "last_fall_date":
                            patient.setLastFallDate(parseDate(value));;
                            break;
                        case "vte_assessment_date":
                            patient.setVteAssessmentDate(parseDate(value));
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


    private static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : null;
    }


    private static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.equals("null") || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            System.err.println("Error parsing last falls date: " + dateStr);
            return null;
        }
    }
}
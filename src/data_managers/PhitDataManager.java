package src.data_managers;

import java.beans.SimpleBeanInfo;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import src.model.Patient;

public class PhitDataManager {
    private static final String PHIT_DIR = "data/phit/";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void savePatientPhitData(Patient patient) {
        File dir = new File(PHIT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = PHIT_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write("lipid_profile_date=" + formatDate(patient.getLipidProfileDate()));
            writer.newLine();
            writer.write("hba1c_date=" + formatDate(patient.getHba1cDate()));
            writer.newLine();
            writer.write("tft_date=" + formatDate(patient.getTftDate()));
            writer.newLine();
            writer.write("prolactin_date=" + formatDate(patient.getProlactinDate()));
            writer.newLine();
            writer.write("b12_date=" + formatDate(patient.getB12Date()));
            writer.newLine();
            writer.write("folate_date=" + formatDate(patient.getFolateDate()));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving PHIT data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }


    public static void loadPatientPhitData(Patient patient) {
        String filename = PHIT_DIR + patient.getPatientId().replace(" ", "_") +  ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();

                    switch (key) {
                        case "lipid_profile_date":
                            patient.setLipidProfileDate(parseDate(value));
                            break;
                        case "hba1c_date":
                            patient.setHba1cDate(parseDate(value));
                            break;
                        case "tft_date":
                            patient.setTftDate(parseDate(value));
                            break; 
                        case "prolactin_date":
                            patient.setProlactinDate(parseDate(value));
                            break;
                        case "b12_date":
                            patient.setB12Date(parseDate(value));
                            break;
                        case "folate_date":
                            patient.setFolateDate(parseDate(value));
                            break;
                        default:
                            System.out.println("Unknown PHIT field: " + key);
                            break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No PHIT data found for " + patient.getPatientId());
        } catch (IOException e) {
            System.err.println("Error loading PHIT data for " + patient.getPatientId() + ": " + e.getMessage());
        }
    }


    private static String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : "null";
    }


    private static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.equals("null") || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            System.err.println("Error parsing PHIT date: " + dateStr);
            return null;
        }
    }
}

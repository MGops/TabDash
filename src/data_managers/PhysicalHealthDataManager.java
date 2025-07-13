package src.data_managers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import src.model.Patient;

public class PhysicalHealthDataManager {
    private static final String PHYSICAL_HEALTH_DIR = "data/physical_health/";

    public static void savePatientPhysicalHealth(Patient patient) {
        File dir = new File(PHYSICAL_HEALTH_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Create filenames from patient IDs- replace spaces with underscores
        String filename = PHYSICAL_HEALTH_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String condition : patient.getPhysicalHealthConditions()) {
                writer.write(condition);
                writer.newLine();
            }
            System.out.println("Saved physical health conditions for " + patient.getPatientId());
        } catch (IOException e) {
            System.err.println("Error saving physical health data for " + patient.getPatientId());
        }
    }

    public static void loadPatientPhysicalHealth(Patient patient) {
        String filename = PHYSICAL_HEALTH_DIR + patient.getPatientId().replace(" ", "_") + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = reader.readLine()) != null) {
                String condition = line.trim();
                if (!condition.isEmpty()) {
                    patient.addPhysicalHealthConditions(condition);
                }
            }
            System.out.println("Loaded " + patient.getPhysicalHealthConditions().size() + " physical health conditions for " + patient.getPatientId());
        } catch (FileNotFoundException e) {
            System.out.println("No physical health data found for " + patient.getPatientId());
        } catch (IOException e) {
            System.err.println("Error loading physical health data for " + patient.getPatientId());
        }
    }
}

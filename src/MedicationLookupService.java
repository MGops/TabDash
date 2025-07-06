package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

// Acts as a lookup service that reads classes csv and allows app to find the class and subclass of med
public class MedicationLookupService {
    private static final String CLASSES_FILE = "data/medication_classes.csv";
    private Map<String, MedicationClassInfo> medicationClasses;

    public static class MedicationClassInfo {
        public final String drugClass;
        public final String drugSubclass;

        public MedicationClassInfo(String drugClass, String drugSubclass) {
            this.drugClass = drugClass;
            this.drugSubclass = drugSubclass;
        }
    }

    public MedicationLookupService() {
        medicationClasses = new HashMap<>();
        loadMedicationClasses();
    }

    // Reads csv line by line
    private void loadMedicationClasses() {
        try(BufferedReader reader = new BufferedReader(new FileReader(CLASSES_FILE))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String medName = parts[0].trim().toLowerCase();
                    String drugClass = parts[1].trim();
                    String drugSubclass = parts[2].trim();
                    if (drugSubclass.isEmpty()) {
                        drugSubclass = null;
                    }
                    // Stores information in a HashMap for quick lookup
                    medicationClasses.put(medName, new MedicationClassInfo(drugClass, drugSubclass));
                }
            }
            System.out.println("Loaded " + medicationClasses.size() + " medication class mappings");
        } catch (IOException e) {
            System.err.println("Error loading medication classes: " + e.getMessage());
        }
    }

    public MedicationClassInfo getClassInfo(String medicationName) {
        return medicationClasses.get(medicationName.toLowerCase());
    }
}

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
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    String medName = parts[0].trim().toLowerCase();
                    String drugClass = parts[1].trim();
                    String drugSubclass = "";
                    if (parts.length >= 3) {
                        drugSubclass = parts[2].trim();
                    } 
                    // Handle empty subclass
                    if (drugSubclass.isEmpty()) {
                        drugSubclass = null;
                    }
                    medicationClasses.put(medName, new MedicationClassInfo(drugClass, drugSubclass));
                }
            }
            System.out.println("Loaded " + medicationClasses.size() + " medication class mappings");
            if (medicationClasses.containsKey("clozapine")) {
                MedicationClassInfo info = medicationClasses.get("clozapine");
                System.out.println("Clozapine found in database: " + info.drugClass + " / " + info.drugSubclass);
            } else {
                System.out.println("WARNING: Clozapine NOT found in medication classes!");
            }
        } catch (IOException e) {
            System.err.println("Error loading medication classes: " + e.getMessage());
        }
    }

    // In MedicationLookupService.java, update the getClassInfo method:

    public MedicationClassInfo getClassInfo(String medicationName) {
        System.out.println("MedicationLookupService: Looking up class for '" + medicationName + "'");
        MedicationClassInfo info = medicationClasses.get(medicationName.toLowerCase());
        if (info != null) {
            System.out.println("  Found: " + info.drugClass + " / " + info.drugSubclass);
        } else {
            System.out.println("  Not found in medication classes");
            // Let's see what we do have
            System.out.println("  Available medications starting with '" + 
                medicationName.substring(0, Math.min(3, medicationName.length())) + "':");
            for (String key : medicationClasses.keySet()) {
                if (key.startsWith(medicationName.substring(0, Math.min(3, medicationName.length())).toLowerCase())) {
                    System.out.println("    - " + key);
                }
            }
        }
        return info;
    }
}

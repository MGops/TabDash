package src.data_managers;

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
        } catch (IOException e) {
            System.err.println("Error loading medication classes: " + e.getMessage());
        }
    }

    // In MedicationLookupService.java, update the getClassInfo method:
    public MedicationClassInfo getClassInfo(String medicationName) {
        MedicationClassInfo info = medicationClasses.get(medicationName.toLowerCase());
        return info;
    }


    public boolean isPsychotropicMedication(String medName) {
        MedicationClassInfo classInfo = getClassInfo(medName);
        if (classInfo == null) {
            return false;
        }

        String drugClass = classInfo.drugClass != null ? classInfo.drugClass.toLowerCase(): "";
        String drugSubClass = classInfo.drugSubclass != null ? classInfo.drugSubclass.toLowerCase(): "";

        if (drugClass.equals("antidepressant") ||
            drugClass.equals("antipsychotic") ||
            drugClass.equals("anxiolytic") ||
            drugClass.equals("mood_stabiliser")) {
            return true;
        }

        if (drugSubClass.equals("benzodiazepine") ||
            drugSubClass.equals("ssri") ||
            drugSubClass.equals("tca") ||
            drugSubClass.equals("snri") || 
            drugSubClass.equals("maoi")) {
            return true;
        }

        String medNameLower = medName.toLowerCase();
        if (medNameLower.contains("zopiclone") || 
            medNameLower.contains("zopiclone") || 
            medNameLower.contains("zopiclone")) {
            return true;
        }
        
        return false;
    }
}

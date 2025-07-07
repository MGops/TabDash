package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeprescribingService {
    public enum DeprescribingCategory {
        DISCUSS_BEFORE_STOPPING("Discuss before stopping", "#000000", "#FFFFFF"),
        DISCUSS_BEFORE_ALTERING("Discuss before altering", "#FF0000", "#FFFFFF"),
        CHECK_EXPIRED_INDICATION("Check expired indication", "#FFA500", "#000000"),
        CHECK_VALID_INDICATION("Check valid indication", "#FFFF00", "#000000"),
        BENEFIT_VS_RISK("Benefit vs Risk", "#00FF00", "#000000");

        public final String displayName;
        public final String backgroundColour;
        public final String textColour;

        DeprescribingCategory(String displayName, String backgroundColour, String textColour) {
            this.displayName = displayName;
            this.backgroundColour = backgroundColour;
            this.textColour = textColour;
        }
    }

    private Map<String, List<DeprescribingCategory>> medicationCategories;
    private Map<String, List<DeprescribingCategory>> classCategories;

    public DeprescribingService() {
        medicationCategories = new HashMap<>();
        classCategories = new HashMap<>();
        initialiseDeprescribingRules();
    }


    private void initialiseDeprescribingRules() {
        // DISCUSS BEFORE STOPPING
        addClassRule("diuretic", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addSubclassRule("acei", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addClassRule("oral_steroid", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addSubclassRule("beta_blocker", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        
        // DISCUSS BEFORE ALTERING  
        addMedicationRule("amiodarone", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addClassRule("antidiabetic", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addClassRule("antiepileptic", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addClassRule("DMARD", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("levothyroxine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("insulin", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        
        // CHECK EXPIRED INDICATION
        addMedicationRule("omeprazole", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("lansoprazole", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addClassRule("laxative", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addClassRule("urinary_antispasmodic", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addClassRule("oral_steroid", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addSubclassRule("h1_antagonist", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addClassRule("antibiotic", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addSubclassRule("nsaid", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // CHECK VALID INDICATION
        addSubclassRule("ssri", DeprescribingCategory.CHECK_VALID_INDICATION);
        addSubclassRule("tca", DeprescribingCategory.CHECK_VALID_INDICATION);
        addClassRule("antipsychotic", DeprescribingCategory.CHECK_VALID_INDICATION);
        addMedicationRule("levodopa", DeprescribingCategory.CHECK_VALID_INDICATION);
        addSubclassRule("alpha_blocker", DeprescribingCategory.CHECK_VALID_INDICATION);
        
        // BENEFIT VS RISK
        addClassRule("statin", DeprescribingCategory.BENEFIT_VS_RISK);
        addClassRule("antihypertensive", DeprescribingCategory.BENEFIT_VS_RISK);
        
        System.out.println("Loaded deprescribing rules for " + 
            (medicationCategories.size() + classCategories.size()) + " medications/classes");
    }

    private void addMedicationRule(String medication, DeprescribingCategory category) {
        medicationCategories.computeIfAbsent(medication.toLowerCase(), k -> new ArrayList<>()).add(category);
    }


    private void addClassRule(String drugClass, DeprescribingCategory category) {
        medicationCategories.computeIfAbsent(drugClass.toLowerCase(), k -> new ArrayList<>()).add(category);
    }

    private void addSubclassRule(String drugSubClass, DeprescribingCategory category) {
        medicationCategories.computeIfAbsent(drugSubClass.toLowerCase(), k -> new ArrayList<>()).add(category);
    }

    public List<DeprescribingCategory> getCategoriesForMedication(String medicationName, String drugClass, String drugSubclass) {
        Set<DeprescribingCategory> categories = new HashSet<>();


        List<DeprescribingCategory> medCategories = medicationCategories.get(medicationName.toLowerCase()); 
        // Check specific medication first
        if (medCategories != null) {
            categories.addAll(medCategories);
        }
        // Check class
        if (drugClass != null) {
            List<DeprescribingCategory> classCategories = this.classCategories.get(drugClass.toLowerCase());
            if (classCategories != null) {
                categories.addAll(classCategories);
            }
        }
        //Check subclass
        if (drugSubclass != null) {
            List<DeprescribingCategory> subclassCategories = classCategories.get(drugSubclass.toLowerCase());
            if (subclassCategories != null) {
                categories.addAll(subclassCategories);
            }
        }
        return new ArrayList<>(categories);
    }
}

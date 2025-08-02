package src.service.StoppStartService;

import java.util.ArrayList;
import java.util.List;

import src.data_managers.MedicationLookupService;
import src.model.Patient;
import src.service.StoppStartService.RuleDataLoader.StartRule;
import src.service.StoppStartService.RuleDataLoader.StoppRule;

public class StoppStartService {
    private List<StoppRule> stoppRules;
    private List<StartRule> startRules;
    private RuleDataLoader dataLoader;
    private MedicationLookupService medicationLookupService;

    public StoppStartService() {
        this.dataLoader = new RuleDataLoader();
        this.medicationLookupService = new MedicationLookupService();
        loadRules();
    }

    private void loadRules() {
        stoppRules = dataLoader.loadStoppRules();
        startRules = dataLoader.loadStartRules();
    }

    public List<StoppRecommendation> getStoppRecommendations(Patient patient) {
        List<StoppRecommendation> recommendations = new ArrayList<>();

        if (patient == null) {
            return recommendations;
        }

        // Get patient's medications and conditions
        List<String> patientMedications = getPatientMedicationNames(patient);
        List<String> patientConditions = getPatientConditions(patient);

        // Debug logging
        System.out.println("=== STOPP ANALYSIS DEBUG ===");
        System.out.println("Patient medications: " + patientMedications);
        System.out.println("Patient conditions: " + patientConditions);
        System.out.println("Number of STOPP rules: " + stoppRules.size());

        // Check each STOPP rule
        for (StoppRule rule : stoppRules) {
            System.out.println("Checking rule: " + rule.medication + " + " + rule.condition);
            
            boolean medMatch = hasMatchingMedication(patientMedications, rule.medication);
            boolean condMatch = hasMatchingCondition(patientConditions, rule.condition);
            
            System.out.println("  Medication match: " + medMatch);
            System.out.println("  Condition match: " + condMatch);
            
            if (medMatch && condMatch) {
                System.out.println("  >>> MATCH FOUND! Adding recommendation");

                String actualMedication = findMatchingPatientMedication(patientMedications, rule.medication);

                recommendations.add(new StoppRecommendation(
                    capitaliseFirst(actualMedication),
                    rule.condition,
                    rule.reason
                ));
            }
        }

        System.out.println("Total recommendations: " + recommendations.size());
        return recommendations;
    }

    public List<StartRecommendation> getStartRecommendations(Patient patient) {
        List<StartRecommendation> recommendations = new ArrayList<>();
        if (patient == null) {
            return recommendations;
        }

        List<String> patientMedications = getPatientMedicationNames(patient);
        List<String> patientConditions = getPatientConditions(patient);

        System.out.println("=== START ANALYSIS DEBUG ===");
        System.out.println("Patient medications: " + patientMedications);
        System.out.println("Patient conditions: " + patientConditions);
        System.out.println("Number of START rules: " + startRules.size());

        for (StartRule rule : startRules) {
            System.out.println("Checking START rule: " + rule.condition + " -> " + rule.medication);

            boolean conditionMatch = patientConditions.contains(rule.condition);
            boolean alreadyHasMed = hasMatchingMedication(patientMedications, rule.medication);

            System.out.println(" Condition match: " + conditionMatch);
            System.out.println(" Already has medication: " + alreadyHasMed);

            if (conditionMatch && !alreadyHasMed) {
                System.out.println(" START MATCH FOUND. Adding medication");

                recommendations.add(new StartRecommendation(
                    capitaliseFirst(rule.medication.replace("_", " ")),
                    rule.condition,
                    rule.reason
                ));
            }
        }
        System.out.println("Total START recommendations: " + recommendations.size());
        return recommendations;
    }


    private List<String> getPatientMedicationNames(Patient patient) {
        List<String> medications = new ArrayList<>();
        for (String medName : patient.getMedications().keySet()) {
            medications.add(medName.toLowerCase());
        }
        return medications;
    }

    private List<String> getPatientConditions(Patient patient) {
        List<String> conditions = new ArrayList<>();
        for (String condition : patient.getPhysicalHealthConditions()) {
            conditions.add(condition);
        }
        return conditions;
    }


    private boolean hasMatchingMedication(List<String> patientMedications, String ruleMedication) {
        if (ruleMedication == null) {
            return false;
        }

        if (patientMedications.contains(ruleMedication.toLowerCase())) {
            return true;
        }

        for (String patientMed : patientMedications) {
            if (isMedicationInClass(patientMed, ruleMedication)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMedicationInClass(String patientMedication, String ruleClass) {
        if (ruleClass == null) {
            return false;
        }
        // Use existing MedicationLookupService to get class info
        MedicationLookupService.MedicationClassInfo classInfo = 
            medicationLookupService.getClassInfo(patientMedication);
            
        if (classInfo == null) {
            return false;
        }
        
        String ruleClassLower = ruleClass.toLowerCase();
        
        // Check against drug class
        if (classInfo.drugClass != null && 
            classInfo.drugClass.toLowerCase().equals(ruleClassLower)) {
            return true;
        }
        
        // Check against drug subclass  
        if (classInfo.drugSubclass != null && 
            classInfo.drugSubclass.toLowerCase().equals(ruleClassLower)) {
            return true;
        }
        
        return false;
    }

    private String findMatchingPatientMedication(List<String> patientMedications, String ruleMedication) {
        //Find which specific patient medication matched the rule
        //First check for exact match
        for (String patientMed : patientMedications) {
            if (patientMed.equals(ruleMedication.toLowerCase())) {
                return patientMed;
            }
        }
        // Then check for class matches
        for (String patientMed : patientMedications) {
            if (isMedicationInClass(patientMed, ruleMedication)) {
                return patientMed;
            }
        }

        return ruleMedication;
    }


    private boolean hasMatchingCondition(List<String> patientConditions, String ruleCondition) {
        // Special case: if rule condition is "any condition", always match
        if (ruleCondition.equals("Any condition")) {
            return true;
        }
        return patientConditions.contains(ruleCondition);
    }


    private String capitaliseFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

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

                String displayCondition = formatConditionForDisplay(rule.condition);
                recommendations.add(new StoppRecommendation(
                    capitaliseFirst(actualMedication),
                    displayCondition,
                    rule.reason
                ));
            }
        }

        System.out.println("Total recommendations: " + recommendations.size());
        return recommendations;
    }

    public List<StartRecommendation> getStartRecommendations(Patient patient) {
        List<StartRecommendation> recommendations = new ArrayList<>();

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
            conditions.add(condition.toLowerCase().replace(" ", "_"));
        }
        return conditions;
    }


    private boolean hasMatchingMedication(List<String> patientMedications, String ruleMedication) {
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
        if (ruleCondition.equals("any_condition")) {
            return true;
        }

        // Enhanced matching to handle synonyms and variations
        String normalisedRuleCondition = ruleCondition.toLowerCase().replace("_", " ");
        
        for (String condition : patientConditions) {
            String normalisedCondition = condition.toLowerCase();
            
            // Direct match
            if (normalisedCondition.equals(normalisedRuleCondition)) {
                return true;
            }
            
            // Check if rule condition contains patient condition or vice versa
            if (normalisedCondition.contains(normalisedRuleCondition) || 
                normalisedRuleCondition.contains(normalisedCondition)) {
                return true;
            }
            
            // Handle specific condition mappings
            if (isConditionMatch(normalisedCondition, normalisedRuleCondition)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isConditionMatch(String patientCondition, String ruleCondition) {
        // Handle specific condition synonyms
        switch (ruleCondition) {
            case "heart failure":
                return patientCondition.equals("chf") || 
                       patientCondition.contains("heart failure") || 
                       patientCondition.contains("congestive heart failure") ||
                       patientCondition.contains("hf");
            
            case "peptic ulcer disease":
                return patientCondition.contains("peptic ulcer") || 
                       patientCondition.contains("gastric ulcer") ||
                       patientCondition.equals("gord");
                       
            case "dementia":
                return patientCondition.contains("dementia") || 
                       patientCondition.contains("alzheimer");
                       
            case "falls history":
                // This might need to be tracked differently - for now just check for falls-related conditions
                return patientCondition.contains("falls") || 
                       patientCondition.contains("mobility");
                       
            default:
                return false;
        }
    }


    private String formatConditionForDisplay(String condition) {
        // Convert underscores to spaces and capitalize
        return capitaliseFirst(condition.replace("_", " "));
    }

    private String capitaliseFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

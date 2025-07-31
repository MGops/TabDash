package src.service.StoppStartService;

import java.util.ArrayList;
import java.util.List;

import src.model.Patient;
import src.service.StoppStartService.RuleDataLoader.StartRule;
import src.service.StoppStartService.RuleDataLoader.StoppRule;

public class StoppStartService {
    private List<StoppRule> stoppRules;
    private List<StartRule> startRules;
    private RuleDataLoader dataLoader;

    public StoppStartService() {
        this.dataLoader = new RuleDataLoader();
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

        // Check each STOPP rule
        for (StoppRule rule : stoppRules) {
            if (hasMatchingMedication(patientMedications , rule.medication) &&
                hasMatchingCondition(patientConditions, rule.condition)) {

                String displayCondition = formatConditionForDisplay(rule.condition);
                recommendations.add(new StoppRecommendation(
                    capitaliseFirst(rule.medication),
                    displayCondition,
                    rule.reason
                ));
            }
        }
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
        return patientMedications.contains(ruleMedication.toLowerCase());
    }


    private boolean hasMatchingCondition(List<String> patientConditions, String ruleCondition) {
        // Enhanced matching to handle synonyms and variations
        String normalizedRuleCondition = ruleCondition.toLowerCase().replace("_", " ");
        
        for (String condition : patientConditions) {
            String normalizedCondition = condition.toLowerCase();
            
            // Direct match
            if (normalizedCondition.equals(normalizedRuleCondition)) {
                return true;
            }
            
            // Check if rule condition contains patient condition or vice versa
            if (normalizedCondition.contains(normalizedRuleCondition) || 
                normalizedRuleCondition.contains(normalizedCondition)) {
                return true;
            }
            
            // Handle specific condition mappings
            if (isConditionMatch(normalizedCondition, normalizedRuleCondition)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isConditionMatch(String patientCondition, String ruleCondition) {
        // Handle specific condition synonyms
        switch (ruleCondition) {
            case "chf":
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

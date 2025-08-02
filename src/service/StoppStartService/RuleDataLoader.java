package src.service.StoppStartService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RuleDataLoader {
    private static final String STOPP_RULES_FILE = "data/stopp_rules.csv";
    private static final String START_RULES_FILE = "data/start_rules.csv";

    public static class StoppRule {
        public final String medication;
        public final String condition;
        public final String reason;

        public StoppRule(String medication, String condition, String reason) {
            this.medication = medication.toLowerCase().trim();
            this.condition = condition.trim();
            this.reason = reason.trim();
        }
    }
    
    public static class StartRule {
        public final String condition;
        public final String medication;
        public final String reason;

        public StartRule(String condition, String medication, String reason) {
            this.condition = condition.trim();
            this.medication = medication.toLowerCase().trim();
            this.reason = reason.trim();
        }
    }


    public List<StoppRule> loadStoppRules() {
        List<StoppRule> rules = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(STOPP_RULES_FILE))){
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    String medication = parts[0].trim();
                    String condition = parts[1].trim();
                    String reason = parts[2].trim();
                    
                    rules.add(new StoppRule(medication, condition, reason));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading STOPP rules: " + e.getMessage());
        }
        System.out.println("Loaded " + rules.size() + " STOPP rules");
        return rules;
    }
    

    public List<StartRule> loadStartRules() {
        List<StartRule> rules = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(START_RULES_FILE))){
            String line = reader.readLine();
            System.out.println("START rules file header: " + line);
            while ((line = reader.readLine()) != null) {
                System.out.println("Reading START rule line: " + line);
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    String condition = parts[0].trim();
                    String medication = parts[1].trim();
                    String reason = parts[2].trim();
                    
                    System.out.println("Creating START rule: condition='" + condition + "', medication='" + medication + "'");
                    rules.add(new StartRule(condition, medication, reason));
                } else {
                    System.out.println("Skipping invalid START rule line (not enough parts): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading START rules: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Loaded " + rules.size() + " START rules");
        return rules;
        
    }
}

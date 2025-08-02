package src.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PhysicalConditionService {
    private static final String CONDITIONS_FILE = "data/physical_health_conditions.csv";
    private Set<String> allConditions;
    private Map<String, ConditionInfo> conditionDetails;

    // Data container for all information about a condition
    // Gets returned when UI needs full details about a condition
    public static class ConditionInfo {
        public final String name;
        public final String category;

        public ConditionInfo(String name, String category) {
            this.name = name;
            this.category = category;
        }
    }

    // Initialises data structures and loads CSV data
    // Created by TabDash once, then used by IllnessListSection for all searches
    public PhysicalConditionService() {
        allConditions = new HashSet<>();
        conditionDetails = new HashMap<>();
        loadConditionsFromCSV();
    }


    // Extract data from CSV
    private void loadConditionsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONDITIONS_FILE))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length >= 2) {
                    String conditionName = parts[0].trim();
                    String category = parts[1].trim();

                    allConditions.add(conditionName); // Store condition name in main set
                    conditionDetails.put(conditionName.toLowerCase(), 
                        new ConditionInfo(conditionName, category));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading physical health conditions: " + e.getMessage());
            loadFallbackConditions();
        }
    }


    // Simple CSV parser to handle quoted fields
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes){
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    public List<String> searchConditions(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerSearch = searchText.toLowerCase();
        List<String> results = new ArrayList<>();

        // Search main condition names
        for (String condition : allConditions) {
            if (condition.toLowerCase().contains(lowerSearch)) {
                results.add(condition);
            }
        }
        // Sort results alphabetically
        results.sort((a, b) -> {
            // Prioritize results that start with the search term
            boolean aStarts = a.toLowerCase().startsWith(lowerSearch);
            boolean bStarts = b.toLowerCase().startsWith(lowerSearch);
            if (aStarts && !bStarts) return -1;
            if (!aStarts && bStarts) return 1;
            return a.compareToIgnoreCase(b);
        });

        // Limit results to prevent overwhelming the UI
        int maxResults = searchText.length() <= 2 ? 20 : 15;
        return results.size() > maxResults ? 
               results.subList(0, maxResults) : results;
    }


    public List<String> getAllConditions() {
        List<String> sortedConditions = new ArrayList<>(allConditions);
        Collections.sort(sortedConditions);
        return sortedConditions;
    }


    public ConditionInfo getConditionInfo(String conditionName) {
        return conditionDetails.get(conditionName.toLowerCase());
    }

    // Fallback conditions if CSV fails to load
    private void loadFallbackConditions() {
        String[] fallback = {
            "Type 2 diabetes", "Hypertension", "Heart failure", "Atrial fibrillation",
            "Asthma", "COPD", "MI", "CVA", "Hyponatraemia", "Type 1 diabetes"
        };
        allConditions.addAll(Arrays.asList(fallback));
        System.out.println("Using fallback conditions: " + allConditions.size());
    }
}
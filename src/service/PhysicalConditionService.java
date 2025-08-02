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
    private Map<String, String> synonymToCondition;

    // Data container for all information about a condition
    // Gets returned when UI needs full details about a condition
    public static class ConditionInfo {
        public final String name;
        public final String category;
        public final List<String> synonyms;

        public ConditionInfo(String name, String category, List<String> synonyms) {
            this.name = name;
            this.category = category;
            this.synonyms = synonyms;
        }
    }

    // Initialises data structures and loads CSV data
    // Created by TabDash once, then used by IllnessListSection for all searches
    public PhysicalConditionService() {
        allConditions = new HashSet<>();
        conditionDetails = new HashMap<>();
        synonymToCondition = new HashMap<>();
        loadConditionsFromCSV();
    }


    // Extract data from CSV
    private void loadConditionsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONDITIONS_FILE))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length >= 3) {
                    String conditionName = parts[0].trim();
                    String category = parts[1].trim();
                    String synonymStr = parts[2].trim();

                    List<String> synonyms = new ArrayList<>();
                    if (!synonymStr.isEmpty() && !synonymStr.equals("\"\"")) {
                        synonymStr = synonymStr.replaceAll("^\"|\"$", "");
                        for (String synonym : synonymStr.split(",")) {
                            String cleanedSynonym = synonym.trim();
                            if (!cleanedSynonym.isEmpty()) {
                                synonyms.add(cleanedSynonym);
                                // Map synonym to main condition; lowecase so search is case insensitive
                                synonymToCondition.put(cleanedSynonym.toLowerCase(), conditionName);
                            }
                        }
                    }
                    allConditions.add(conditionName); // Store condition name in main set
                    conditionDetails.put(conditionName.toLowerCase(), 
                        new ConditionInfo(conditionName, category, synonyms));
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

    public List<SearchResult> searchConditions(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerSearch = searchText.toLowerCase();
        List<SearchResult> results = new ArrayList<>();

        // Search main condition names
        for (String condition : allConditions) {
            if (condition.toLowerCase().contains(lowerSearch)) {
                results.add(new SearchResult(condition, condition, false));
            }
        }

        // Search synonyms
        for (Map.Entry<String, String> entry : synonymToCondition.entrySet()) {
            String synonym = entry.getKey();
            String mainCondition = entry.getValue();
            if (synonym.contains(lowerSearch)) {
                results.add(new SearchResult(mainCondition, synonym, true));
            }
        }

        // Sort results alphabetically
        results.sort((a, b) -> {
            // Prioritize results that start with the search term
            boolean aStarts = a.conditionName.toLowerCase().startsWith(lowerSearch);
            boolean bStarts = b.conditionName.toLowerCase().startsWith(lowerSearch);
            if (aStarts && !bStarts) return -1;
            if (!aStarts && bStarts) return 1;
            
            // Then sort alphabetically
            return a.conditionName.compareToIgnoreCase(b.conditionName);
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


    public static class SearchResult {
        public final String conditionName;
        public final String matchedText;
        public final boolean isSynonymMatch;

        public SearchResult(String conditionName, String matchedText, boolean isSynonymMatch) {
            this.conditionName = conditionName;
            this.matchedText = matchedText;
            this.isSynonymMatch = isSynonymMatch;
        }

        public String getDisplayText() {
            return conditionName;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof SearchResult)) return false;
            SearchResult other = (SearchResult) obj;
            return conditionName.equals(other.conditionName);
        }

        @Override
        public int hashCode() {
            return conditionName.hashCode();
        }
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
package src.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ADRService {
    private static final String ADR_FILE = "data/adr_data.csv";
    private Map<String, List<String>> medicationADRs;

    // ADR column names
    private static final String[] ADR_NAMES = {
        "falls_fractures", "constipation", "urinary_retention", "cns_depression",
        "bleeding", "heart_failure", "bradycardia", "cv_events", "respiratory",
        "hypoglycaemia", "renal_injury", "hypokalaemia", "hyperkalaemia", 
        "serotonin_syndrome", "angle_closure_glaucoma", "urinary_incontinence"
    };

    public ADRService() {
        medicationADRs = new HashMap<>();
        loadADRData();
    }

    private void loadADRData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADR_FILE))){
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 17) { // medication_identifier + type + 15 ADRs
                    String medicationID = parts[0].trim().toLowerCase();
                    List<String> adrs = new ArrayList<>();
                    // Check each ADR column starting from index 2
                    for (int i = 0; i < ADR_NAMES.length; i++) {
                        if (parts[i + 2].trim().equals("1")) {
                            adrs.add(ADR_NAMES[i]);
                        }
                    }
                    medicationADRs.put(medicationID, adrs);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading ADR data: " + e.getMessage());
        }
    }

    public List<String> getADRsForMed(String medicationID) {
        List<String> result = medicationADRs.getOrDefault(medicationID.toLowerCase(), new ArrayList<>());
        return medicationADRs.getOrDefault(medicationID.toLowerCase(), new ArrayList<>());
    }
}

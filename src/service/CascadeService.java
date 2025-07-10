package src.service;

import java.util.*;

public class CascadeService {
    private Map<String, CascadeInfo> cascades;

    public static class CascadeInfo { // Stores details of one cascade
        public final String cascadeMed; // Medication that gets added because of side effect
        public final String sideEffect;

        public CascadeInfo(String cascadeMed, String sideEffect) {
            this.cascadeMed = cascadeMed;
            this.sideEffect = sideEffect;
        }
    }

    // Create new CascadeService instance when newCascadeService() is called
    public CascadeService() {  
        cascades = new HashMap<>();
        intialisePrescribingCascadeRules();
    }

    // Load all cascade rules into the Hashmap
    private void intialisePrescribingCascadeRules() {
        cascades.put("amlodipine", new CascadeInfo("furosemide", "oedema"));
        cascades.put("nifedipine", new CascadeInfo("furosemide", "oedema"));
        cascades.put("diuretic", new CascadeInfo("solifenacin", "incontinence"));
        cascades.put("diazepam", new CascadeInfo("donepezil", "cognitive impairment"));
        cascades.put("lorazepam", new CascadeInfo("donepezil", "cognitive impairment"));
        cascades.put("sertraline", new CascadeInfo("zopiclone", "insomnia"));
        cascades.put("fluoxetine", new CascadeInfo("zopiclone", "insomnia"));
        cascades.put("citalopram", new CascadeInfo("zopiclone", "insomnia"));
        cascades.put("escitalopram", new CascadeInfo("zopiclone", "insomnia"));
        cascades.put("venlafaxine", new CascadeInfo("zopiclone", "insomnia"));
        cascades.put("duloxetine", new CascadeInfo("zopiclone", "insomnia"));
        cascades.put("solifenacin", new CascadeInfo("donepezil", "confusion/ cognitive impairment"));
        cascades.put("doxazosin", new CascadeInfo("prochlorperazine", "orthostatic hypotension/ dizziness"));
        cascades.put("donepezil", new CascadeInfo("solifenacin", "urinary incontinence"));
    }

    // Look up if specific medication can start a prescribing cascade
    public CascadeInfo findCascadeForMedication(String medication) {
        return cascades.get(medication.toLowerCase());
    }


    // Returns a hashmap of all cascade rules
    public Map<String, CascadeInfo> getAllCascadeRules() {
        return new HashMap<>(cascades);
    }
}

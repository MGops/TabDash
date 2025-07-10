package src.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeprescribingService {
    public enum DeprescribingCategory {
        DISCUSS_BEFORE_STOPPING("Discuss before stopping", "#000000", "#FF4438"),
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

    public DeprescribingService() {
        medicationCategories = new HashMap<>();
        initialiseDeprescribingRules();
    }


    private void initialiseDeprescribingRules() {
        // DISCUSS BEFORE STOPPING
        
        // Diuretics
        addMedicationRule("furosemide", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("bumetanide", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("chlorothiazide", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("indapamide", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("metolazone", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("spironolactone", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("amiloride", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        
        // ACE inhibitors  
        addMedicationRule("lisinopril", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("ramipril", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("enalapril", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("perindopril", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("captopril", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        
        // Steroids
        addMedicationRule("prednisolone", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("dexamethasone", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("beclometasone", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("budesonide", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        
        // Heart rate controlling drugs
        addMedicationRule("atenolol", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("bisoprolol", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("metoprolol", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("propranolol", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("timolol", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("carvedilol", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("labetalol", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);
        addMedicationRule("digoxin", DeprescribingCategory.DISCUSS_BEFORE_STOPPING);

        // DISCUSS BEFORE ALTERING
        
        // Anti-epileptics
        addMedicationRule("phenytoin", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("carbamazepine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("sodium_valproate", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("levetiracetam", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("lamotrigine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        
        // Mood stabilisers
        addMedicationRule("lithium", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("sodium_valproate", DeprescribingCategory.DISCUSS_BEFORE_ALTERING); // Also mood stabilizer
        
        // Antidepressants  
        addMedicationRule("sertraline", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("escitalopram", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("fluoxetine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("fluvoxamine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("paroxetine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("citalopram", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("amitriptyline", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("clomipramine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("dosulepin", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("imipramine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("lofepramine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("nortriptyline", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        
        // DMARDs
        addMedicationRule("methotrexate", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("sulfasalazine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("leflunomide", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("hydroxychloroquine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("adalimumab", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("infliximab", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        
        // Other specific medications
        addMedicationRule("levothyroxine", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("amiodarone", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("insulin", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        
        // Antidiabetics
        addMedicationRule("gliclazide", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("glimepiride", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("tolbutamide", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("glibenclamide", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("glipizide", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("sitagliptin", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("linagliptin", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);
        addMedicationRule("metformin", DeprescribingCategory.DISCUSS_BEFORE_ALTERING);

        // CHECK EXPIRED INDICATION
        
        // PPI/H2 blockers
        addMedicationRule("omeprazole", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("lansoprazole", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("esomeprazole", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("pantoprazole", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("ranitidine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("famotidine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("cimetidine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("nizatidine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // Laxatives
        addMedicationRule("movicol", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("senna", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("lactulose", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("docusate", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("sodium_picosulfate", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // Antispasmodics  
        addMedicationRule("oxybutynin", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("mirabegron", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("solifenacin", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("tolterodine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("flavoxate", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // Oral steroids
        addMedicationRule("prednisolone", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("dexamethasone", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("beclometasone", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("budesonide", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // Hypnotics/anxiolytics
        addMedicationRule("lorazepam", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("diazepam", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("clonazepam", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("temazepam", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("nitrazepam", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("alprazolam", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("chlordiazepoxide", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("clobazam", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("zopiclone", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("zolpidem", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("melatonin", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // H1 blockers
        addMedicationRule("chlorphenamine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("cyclizine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("promethazine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("cyproheptadine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("diphenhydramine", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // Antibacterials/antifungals 
        addMedicationRule("trimethoprim", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("ketoconazole", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        
        // NSAIDs
        addMedicationRule("aspirin", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("ibuprofen", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("naproxen", DeprescribingCategory.CHECK_EXPIRED_INDICATION);
        addMedicationRule("diclofenac", DeprescribingCategory.CHECK_EXPIRED_INDICATION);

        // CHECK VALID INDICATION
        
        addMedicationRule("aspirin", DeprescribingCategory.CHECK_VALID_INDICATION);
        addMedicationRule("clopidogrel", DeprescribingCategory.CHECK_VALID_INDICATION);
        addMedicationRule("apixaban", DeprescribingCategory.CHECK_VALID_INDICATION);
        addMedicationRule("rivaroxaban", DeprescribingCategory.CHECK_VALID_INDICATION);
        addMedicationRule("dalteparin", DeprescribingCategory.CHECK_VALID_INDICATION);
        
        
        // Specific medications
        addMedicationRule("levodopa", DeprescribingCategory.CHECK_VALID_INDICATION);
        
        // Alpha blockers
        addMedicationRule("doxazosin", DeprescribingCategory.CHECK_VALID_INDICATION);
        addMedicationRule("prazosin", DeprescribingCategory.CHECK_VALID_INDICATION);

        // BENEFIT VS RISK
        
        // Antianginals 
        addMedicationRule("gtn", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("isosorbide_mononitrate", DeprescribingCategory.BENEFIT_VS_RISK);

        // Antihypertensives 
        addMedicationRule("amlodipine", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("nifedipine", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("felodipine", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("verapamil", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("diltiazem", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("candesartan", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("losartan", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("irbesartan", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("olmesartan", DeprescribingCategory.BENEFIT_VS_RISK);
        
        // Statins

        addMedicationRule("simvastatin", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("atorvastatin", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("pravastatin", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("rosuvastatin", DeprescribingCategory.BENEFIT_VS_RISK);

        // Corticosteroids 
        addMedicationRule("prednisolone", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("dexamethasone", DeprescribingCategory.BENEFIT_VS_RISK);
        
        // Bisphosphonates  
        addMedicationRule("alendronate", DeprescribingCategory.BENEFIT_VS_RISK);

        // HbA1c control
        addMedicationRule("gliclazide", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("glimepiride", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("tolbutamide", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("glibenclamide", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("glipizide", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("sitagliptin", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("linagliptin", DeprescribingCategory.BENEFIT_VS_RISK);
        addMedicationRule("metformin", DeprescribingCategory.BENEFIT_VS_RISK);

        System.out.println("Loaded deprescribing rules for " + medicationCategories.size() + " medications.");
    }

    private void addMedicationRule(String medication, DeprescribingCategory category) {
        medicationCategories.computeIfAbsent(medication.toLowerCase(), k -> new ArrayList<>()).add(category);
    }


    public List<DeprescribingCategory> getCategoriesForMedication(String medicationName) {
        List<DeprescribingCategory> categories = medicationCategories.get(medicationName.toLowerCase()); 
        return categories != null ? categories : new ArrayList<>();
    }
}

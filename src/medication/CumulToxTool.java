package src.medication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

import src.MedicationDatabase;
import src.TabDash;

public class CumulToxTool extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    private static final Map<String, Color> ADR_COLORS;
    static {
        Map<String, Color> tempMap = new HashMap<>();
        tempMap.put("falls_fractures", new Color(255, 182, 193));// Light pink
        tempMap.put("constipation", new Color(222, 184, 135));// Burlywood
        tempMap.put("urinary_retention", new Color(173, 216, 230));// Light blue
        tempMap.put("cns_depression", new Color(255, 160, 122));// Light salmon
        tempMap.put("bleeding", new Color(255, 99, 71));// Tomato
        tempMap.put("heart_failure", new Color(240, 128, 128));// Light coral
        tempMap.put("bradycardia", new Color(176, 196, 222));// Light steel blue
        tempMap.put("cv_events", new Color(205, 92, 92));// Indian red
        tempMap.put("respiratory", new Color(135, 206, 250));// Sky blue
        tempMap.put("hypoglycaemia", new Color(144, 238, 144));// Light green
        tempMap.put("renal_injury", new Color(255, 218, 185));// Peach puff
        tempMap.put("hypokalaemia", new Color(221, 160, 221));// Plum
        tempMap.put("hyperkalaemia", new Color(255, 20, 147));// Deep pink
        tempMap.put("serotonin_syndrome", new Color(255, 215, 0));// Gold
        tempMap.put("angle_closure_glaucoma", new Color(255, 165, 0));// Orange
        ADR_COLORS = Collections.unmodifiableMap(tempMap);
    }

    public CumulToxTool(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Cumulative Toxicity Tool"));
        add(new JLabel("Toxicity tool"));
    }
}

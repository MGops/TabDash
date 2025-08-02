package src.ui.medication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.*;
import java.awt.*;

import src.data_managers.MedicationDatabase;
import src.model.Medication;
import src.model.Patient;
import src.service.ADRService;
import src.ui.TabDash;
import src.utils.UIUtils;

public class CumulToxTool extends JPanel {
    private MedicationDatabase medDatabase;
    private TabDash tabDash;
    private ADRService adrService;
    private JPanel adrDisplayPanel;

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
        this.adrService = new ADRService();

        setBorder(BorderFactory.createTitledBorder("Cumulative Toxicity Tool"));
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
        setLayout(new BorderLayout());

        // Scrollable panel for ADR labels
        adrDisplayPanel = new JPanel();
        adrDisplayPanel.setLayout(new BoxLayout(adrDisplayPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(adrDisplayPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        analyseCurrentPatientADRs();
    }


    private void analyseCurrentPatientADRs() {
        adrDisplayPanel.removeAll(); // Clear prev results
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            JLabel noPatientLabel = UIUtils.createInfoLabel("No patient selected", UIUtils.INFO_GRAY);
            adrDisplayPanel.add(noPatientLabel);
            adrDisplayPanel.revalidate();
            adrDisplayPanel.repaint();
            return;
        }
        Map<String, Medication> medications = currentPatient.getMedications();

        //Count meds causing each ADR
        Map<String, Set<String>> adrToMed = new HashMap<>();
        for (Medication med : medications.values()) {
            List<String> adrs = getADRsForMed(med);
            for (String adr : adrs) {
                adrToMed.computeIfAbsent(adr, k -> new HashSet<>()).add(med.getName());
            }
        }
            
        // Sort ADRs by count in descending order, then alphabetically
        List<Map.Entry<String, Set<String>>> sortedADRs = new ArrayList<>(adrToMed.entrySet());
        sortedADRs.sort((a, b) -> {
            int countCompare = Integer.compare(b.getValue().size(), a.getValue().size());
            if (countCompare != 0) return countCompare;
            return a.getKey().compareTo(b.getKey());
        });

        // Create coloured labels for each ADR
        for (Map.Entry<String, Set<String>> entry : sortedADRs) {
            String adr = entry.getKey();
            Set<String> causativeMeds = entry.getValue();

            JLabel adrLabel = createADRLabel(adr, causativeMeds);
            adrDisplayPanel.add(adrLabel);
            adrDisplayPanel.add(Box.createVerticalStrut(1));
        }
        
        if (sortedADRs.isEmpty()) {
            JLabel noADRLabel = UIUtils.createInfoLabel("No ADRs detected for current medications", UIUtils.INFO_GRAY);
            adrDisplayPanel.add(noADRLabel);
        }

        adrDisplayPanel.revalidate();
        adrDisplayPanel.repaint();
    }


    private List<String> getADRsForMed(Medication med) {
        List<String> adrs = adrService.getADRsForMed(med.getName());

        if (adrs.isEmpty() && med.getDrugSubclass() != null) {
            adrs = adrService.getADRsForMed(med.getDrugSubclass());
        }
        
        if (adrs.isEmpty() && med.getDrugClass() != null) {
            adrs = adrService.getADRsForMed(med.getDrugClass());
        }

        return adrs;
    }


    private JLabel createADRLabel(String adr, Set<String> causativeMeds) {
        // Convert ADR name to display format
        String displayName = adr.replace("_", " ");
        displayName = displayName.substring(0, 1).toUpperCase() + displayName.substring(1);

        JLabel label = new JLabel(displayName + " (" + causativeMeds.size() + ")");
        label.setOpaque(true);
        label.setBackground(ADR_COLORS.getOrDefault(adr, Color.LIGHT_GRAY));
        label.setBorder(BorderFactory.createEmptyBorder(2,4,2,4));

        // Create tooltip with causative medications
        StringBuilder tooltip = new StringBuilder("<html>Caused by:<br>");
        for (String med : causativeMeds) {
            tooltip.append("• ").append(med).append("<br>");
        }
        tooltip.append("</html>");
        label.setToolTipText(tooltip.toString());

        return label;
    }

    public void refreshForNewPatient() {
        analyseCurrentPatientADRs();
    }
}

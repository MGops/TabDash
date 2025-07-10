package src.ui.medication;

import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.util.*;
import java.util.List;

import src.data_managers.MedicationDatabase;
import src.model.Medication;
import src.model.Patient;
import src.service.CascadeService;
import src.ui.TabDash;

public class CascadeSection extends JPanel {
    private MedicationDatabase medDatabase;
    private TabDash tabDash;
    private CascadeService cascadeService;
    private JPanel cascadeDisplayPanel;

    public CascadeSection(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        this.cascadeService = new CascadeService();

        setBorder(BorderFactory.createTitledBorder("Prescribing Cascades"));
        setLayout(new BorderLayout());
    
        // Create scrollable panel for cascade display
        cascadeDisplayPanel = new JPanel();
        cascadeDisplayPanel.setLayout(new BoxLayout(cascadeDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cascadeDisplayPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        analyseCurrentCascades();
    }


    private void analyseCurrentCascades() {
        cascadeDisplayPanel.removeAll();

        Patient currentPatient = tabDash.getCurrentPatient();
        Map<String, Medication> medications = currentPatient.getMedications();

        // Get list of medications for checking
        Set<String> medicationNames = new HashSet<>();
        for(String medName : medications.keySet()) {
            medicationNames.add(medName.toLowerCase());
        }

        // Find potential cascades
        List<CascadeDetection> foundCascades = new ArrayList<>();

        for (String medName : medicationNames) {
            CascadeService.CascadeInfo cascadeInfo = cascadeService.findCascadeForMedication(medName);

            if (cascadeInfo != null) {
                // Check if cascade medication also present
                if (medicationNames.contains(cascadeInfo.cascadeMed.toLowerCase())) {
                    foundCascades.add(new CascadeDetection(medName, cascadeInfo.cascadeMed, cascadeInfo.sideEffect));
                }
            }
        }

        // Display results
        if (foundCascades.isEmpty()) {
            JLabel noCascadeLabel = new JLabel("No prescribing cascades detected");
            noCascadeLabel.setForeground(Color.GRAY);
            cascadeDisplayPanel.add(noCascadeLabel);
        } else {
            for (CascadeDetection cascade : foundCascades) {
                JPanel cascadePanel = createCascadePanel(cascade);
                cascadeDisplayPanel.add(cascadePanel);
                cascadeDisplayPanel.add(Box.createVerticalStrut(5));
            }
        }
        cascadeDisplayPanel.revalidate();
        cascadeDisplayPanel.repaint();
    }


    private JPanel createCascadePanel(CascadeDetection cascade) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED, 2),
            BorderFactory.createEmptyBorder(5,5,5,5)
            ));
            panel.setBackground(new Color(255, 240, 240));

        // Main cascade description
        JLabel mainLabel = new JLabel("<html><b>" + cascade.causativeMed.toUpperCase() +
            "</b> --> ?<b>" + cascade.sideEffect +
            "</b> --> ?<b>" + cascade.cascadeMed.toUpperCase() + "</html>");
        mainLabel.setFont(mainLabel.getFont().deriveFont(Font.PLAIN, 12f));
        panel.add(mainLabel);
        return panel;
    }

    public void refreshForNewPatient() {
        analyseCurrentCascades();
    }

    // Helper class to store cascade detection results
    private static class CascadeDetection {
        public final String causativeMed;
        public final String cascadeMed;
        public final String sideEffect;

        public CascadeDetection (String causativeMed, String cascadeMed, String sideEffect) {
            this.causativeMed = causativeMed;
            this.cascadeMed = cascadeMed;
            this.sideEffect = sideEffect;
        }
    }
}

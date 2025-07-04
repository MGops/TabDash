package src.medication;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.MedicationDatabase;
import src.TabDash;

public class SafetySection extends JPanel {
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    public SafetySection(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Safe deprescribing"));
        add(new JLabel("Medication safety review and rationalisation"));
    }
}

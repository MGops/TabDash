package src.medication;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.MedicationDatabase;
import src.TabDash;

public class CascadeSection extends JPanel {
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    public CascadeSection(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Prescribing Cascades"));
        add(new JLabel("Prescribing cascades"));
    }
}

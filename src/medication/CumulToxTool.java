package src.medication;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.MedicationDatabase;
import src.TabDash;

public class CumulToxTool extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    public CumulToxTool(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Cumulative Toxicity Tool"));
        add(new JLabel("Toxicity tool"));
    }
}

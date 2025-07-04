package src.medication;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.MedicationDatabase;
import src.TabDash;

public class StoppStart extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    public StoppStart(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("STOPP/START"));
        add(new JLabel("STOPP-START tool"));
    }
}

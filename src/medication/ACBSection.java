package src.medication;

import src.MedicationDatabase;
import src.TabDash;
import javax.swing.*;

public class ACBSection extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    public ACBSection(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("ACB Score"));
        add(new JLabel("ACB Section"));

    }
    
}

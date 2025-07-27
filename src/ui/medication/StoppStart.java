package src.ui.medication;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Font;

import java.awt.Font;
import src.data_managers.MedicationDatabase;
import src.ui.TabDash;

public class StoppStart extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    public StoppStart(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("STOPP/START"));
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(getFont().deriveFont(Font.BOLD));
        add(new JLabel("STOPP-START tool"));
    }
}

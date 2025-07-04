package src;

import javax.swing.*;

import src.medication.ACBSection;
import src.medication.CascadeSection;
import src.medication.CumulToxTool;
import src.medication.SafetySection;
import src.medication.StoppStart;

import java.awt.*;

public class MedicationPanel extends JPanel {
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    public MedicationPanel(MedicationDatabase medicationDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setLayout(new GridBagLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(new ACBSection(medDatabase, tabDash), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(new CumulToxTool(medDatabase, tabDash), gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(new StoppStart(medDatabase, tabDash), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.gridheight = 1;
        add(new SafetySection(medDatabase, tabDash), gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(new CascadeSection(medDatabase, tabDash), gbc);
    }

    public void refreshForNewPatient() {
        System.out.println("Refreshing medication panel for new patient");
    }
}
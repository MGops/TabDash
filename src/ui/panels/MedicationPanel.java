package src.ui.panels;

import src.data_managers.MedicationDatabase;
import src.ui.TabDash;
import src.ui.medication.*;

import javax.swing.*;
import java.awt.*;

public class MedicationPanel extends JPanel {
    private MedicationDatabase medDatabase;
    private TabDash tabDash;

    private ACBSection acbSection;
    private CumulToxTool cumulToxSection;
    private StoppStart stoppStartSection;
    private DeprescribingSection deprescribingSection;
    private CascadeSection cascadeSection;

    public MedicationPanel(MedicationDatabase medDatabase, TabDash tabDash) {
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

        acbSection = new ACBSection(medDatabase, tabDash);
        cumulToxSection = new CumulToxTool(medDatabase, tabDash);
        stoppStartSection = new StoppStart(medDatabase, tabDash);
        deprescribingSection = new DeprescribingSection(medDatabase, tabDash);
        cascadeSection = new CascadeSection(medDatabase, tabDash);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(acbSection, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(cumulToxSection, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(stoppStartSection, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.gridheight = 1;
        add(deprescribingSection, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(cascadeSection, gbc);
    }


    public void refreshForNewPatient() {
        acbSection.refreshForNewPatient();
        cumulToxSection.refreshForNewPatient();
        cascadeSection.refreshForNewPatient();
        deprescribingSection.refreshForNewPatient();
        stoppStartSection.refreshForNewPatient();
    }

    public void refreshMedicationData() {
        acbSection.refreshForNewPatient();
        cumulToxSection.refreshForNewPatient();
        cascadeSection.refreshForNewPatient();
        deprescribingSection.refreshForNewPatient();
        stoppStartSection.refreshForNewPatient();
    }
}
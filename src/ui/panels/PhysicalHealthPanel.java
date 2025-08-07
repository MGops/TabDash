package src.ui.panels;

import javax.swing.*;

import src.ui.TabDash;
import src.ui.physical.AppointmentsSection;
import src.ui.physical.FrailtySection;
import src.ui.physical.IllnessListSection;
import src.ui.physical.InvestigationsSection;
import src.ui.physical.PlaceholderSection;

import java.awt.*;

public class PhysicalHealthPanel extends JPanel {
    private TabDash tabDash;

    private IllnessListSection illnessListSection;
    private InvestigationsSection investigationsSection;
    private FrailtySection frailtySection;
    private AppointmentsSection appointmentsSection;
    private PlaceholderSection placeholderSection;

    public PhysicalHealthPanel(TabDash tabDash) {
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

        illnessListSection = new IllnessListSection(tabDash);
        investigationsSection = new InvestigationsSection(tabDash);
        frailtySection = new FrailtySection(tabDash);
        appointmentsSection = new AppointmentsSection(tabDash);
        placeholderSection = new PlaceholderSection();


        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(illnessListSection, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(frailtySection, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 1;
        gbc.weightx = 0.0; // No horizontal growth or resize
        gbc.weighty = 1.0;
        add(investigationsSection, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(appointmentsSection, gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(placeholderSection, gbc);
        
    }

    public void refreshForNewPatient() {
        illnessListSection.refreshForNewPatient();
        appointmentsSection.refreshForNewPatient();
        investigationsSection.refreshForNewPatient();
        frailtySection.refreshForNewPatient();
    }
} 
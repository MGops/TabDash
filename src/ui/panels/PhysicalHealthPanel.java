package src.ui.panels;

import javax.swing.*;

import src.ui.TabDash;
import src.ui.physical.AppointmentsSection;
import src.ui.physical.IllnessListSection;
import src.ui.physical.InvestigationsSection;
import src.ui.physical.OptimisationSection;
import src.ui.physical.PlaceholderSection;

import java.awt.*;

public class PhysicalHealthPanel extends JPanel {
    private TabDash tabDash;

    private IllnessListSection illnessListSection;
    private InvestigationsSection investigationsSection;
    private OptimisationSection optimisationSection;
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
        optimisationSection = new OptimisationSection(tabDash);
        appointmentsSection = new AppointmentsSection(tabDash);
        placeholderSection = new PlaceholderSection();


        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(illnessListSection, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.gridheight = 1;
        add(optimisationSection, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridheight = 1;
        add(investigationsSection, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.gridheight = 1;
        add(appointmentsSection, gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.gridwidth = 1; gbc.gridheight = 2;
        add(placeholderSection, gbc);
        
    }

    public void refreshForNewPatient() {
        illnessListSection.refreshForNewPatient();
        appointmentsSection.refreshForNewPatient();
    }
} 
package src.ui.panels;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import src.ui.TabDash;

public class HeaderPanel extends JPanel{
    private TabDash tabDash;
    private JLabel initialsLabel;

    public HeaderPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        setPreferredSize(new Dimension(800, 80));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        GridBagConstraints coordinates = new GridBagConstraints();
        coordinates.fill = GridBagConstraints.BOTH;
        coordinates.weighty = 1;

        initialsLabel = new JLabel("Placeholder");
        coordinates.gridx = 0;
        coordinates.gridy = 0;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        initialsLabel.setForeground(Color.WHITE);
        add(initialsLabel, coordinates);

        JLabel careCoLabel = new JLabel("Care coordinator");
        coordinates.gridx = 4;
        coordinates.gridy = 0;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        careCoLabel.setForeground(Color.WHITE);
        add(careCoLabel, coordinates);
                
        JLabel resusLabel = new JLabel("Resus status");
        coordinates.gridx = 8;
        coordinates.gridy = 0;
        coordinates.gridwidth = 1;
        coordinates.weightx = 0;
        resusLabel.setForeground(Color.WHITE);
        add(resusLabel, coordinates);

        JLabel namedNurseLabel = new JLabel("Named nurse:");
        coordinates.gridx = 0;
        coordinates.gridy = 1;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        namedNurseLabel.setForeground(Color.WHITE);
        add(namedNurseLabel, coordinates);

        JLabel cmhtLabel = new JLabel("CMHT: ");
        coordinates.gridx = 4;
        coordinates.gridy = 1;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        cmhtLabel.setForeground(Color.WHITE);
        add(cmhtLabel, coordinates);
        
        JButton editButton = new JButton("Edit");
        coordinates.gridx = 8; 
        coordinates.gridy = 1;
        coordinates.gridwidth = 1;
        coordinates.weightx = 0;
        add(editButton, coordinates);
    }


    public void updatePatientInfo(String patientId) {
        if (patientId != null && !patientId.trim().isEmpty()) {
            initialsLabel.setText(patientId);
            initialsLabel.setFont(initialsLabel.getFont().deriveFont(Font.BOLD, 20));
        } else {
            initialsLabel.setText("No patient selected");
        }
        repaint();
    }
}

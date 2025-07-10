package src.ui.panels;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeaderPanel extends JPanel{
    public HeaderPanel() {
        setPreferredSize(new Dimension(800, 80));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        GridBagConstraints coordinates = new GridBagConstraints();
        coordinates.fill = GridBagConstraints.BOTH;
        coordinates.weighty = 1;

        JLabel initialsLabel = new JLabel("Placeholder");
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
}

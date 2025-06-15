import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeaderPanel extends JPanel{
    public HeaderPanel() {
        setPreferredSize(new Dimension(800, 80));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new GridLayout(2,4, 10, 0));
        setBackground(Color.DARK_GRAY);

        JLabel initialsLabel = new JLabel("Placeholder");
        initialsLabel.setForeground(Color.WHITE);
        add(initialsLabel);

        JLabel careCoLabel = new JLabel("Care coordinator");
        careCoLabel.setForeground(Color.WHITE);
        add(careCoLabel);

        JLabel blankLabel1 = new JLabel("");
        add(blankLabel1);

        JLabel resusLabel = new JLabel("Resus status");
        resusLabel.setForeground(Color.WHITE);
        add(resusLabel);

        JLabel namedNurseLabel = new JLabel("Named nurse:");
        namedNurseLabel.setForeground(Color.WHITE);
        add(namedNurseLabel);

        JLabel cmhtLabel = new JLabel("CMHT: ");
        cmhtLabel.setForeground(Color.WHITE);
        add(cmhtLabel);

        JLabel blankLabel2 = new JLabel("");
        add(blankLabel2);

        JButton editButton = new JButton("Edit");
        add(editButton);
    }
}

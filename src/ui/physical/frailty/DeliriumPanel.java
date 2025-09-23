package src.ui.physical.frailty;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import src.ui.TabDash;

public class DeliriumPanel extends JPanel {
    private TabDash tabDash;
    private JButton fourATButton;
    private JButton timeBundleButton;

    public DeliriumPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        Border empty = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createTitledBorder(empty, "Delirium"));
        setBackground(new Color(255, 220, 220));
        setLayout(new BorderLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create buttons
        fourATButton = new JButton("4AT");
        timeBundleButton = new JButton("TIME Bundle");
        
        // Set button properties
        fourATButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeBundleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add some spacing between buttons
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(fourATButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(timeBundleButton);
        buttonPanel.add(Box.createVerticalGlue());
        
        add(buttonPanel, BorderLayout.CENTER);
        
        // Add button actions
        fourATButton.addActionListener(e -> FourATDialog.showDialog(this, tabDash));
        
        timeBundleButton.addActionListener(e -> {
            // TODO: Open TIME Bundle dialog or panel
            JOptionPane.showMessageDialog(this, "TIME Bundle - To be implemented", 
                "TIME Bundle", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    

    public void refreshForNewPatient() {
        // No persistent data to refresh
    }

    // Getter methods for future implementation
    public JButton getFourATButton() {
        return fourATButton;
    }

    public JButton getTimeBundleButton() {
        return timeBundleButton;
    }
}
package src.ui.physical.frailty;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import src.ui.TabDash;

public class DeliriumPanel extends JPanel {
    private TabDash tabDash;
    private JTabbedPane tabbedPane;
    private JPanel fourATPanel;
    private JPanel timeBundlePanel;

    public DeliriumPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        Border empty = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createTitledBorder(empty, "Delirium"));
        setBackground(new Color(255, 220, 220));
        setLayout(new BorderLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        // Create tabbed pane with custom styling
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setOpaque(false);
        tabbedPane.setBackground(new Color(255, 220, 220));
        
        // Set smaller font for tab titles to fit better in the panel
        Font tabFont = tabbedPane.getFont().deriveFont(Font.PLAIN, 11f);
        tabbedPane.setFont(tabFont);
        
        // Create placeholder panels for now
        fourATPanel = createTabPanel("4AT Assessment");
        timeBundlePanel = createTabPanel("TIME Bundle");
        
        // Add tabs
        tabbedPane.addTab("4AT", fourATPanel);
        tabbedPane.addTab("TIME", timeBundlePanel);
        
        // Ensure the tabbed pane fills the available space but doesn't expand beyond it
        add(tabbedPane, BorderLayout.CENTER);
        
        // Set preferred size to match the original delirium panel dimensions
        // This prevents the frailty section from growing
        setPreferredSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
    }

    private JPanel createTabPanel(String contentText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBackground(new Color(255, 220, 220));
        
        // Add a placeholder label for now (will be replaced with actual content later)
        JLabel placeholderLabel = new JLabel("<html><center><i>" + contentText + " content<br>will be added here</i></center></html>");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setVerticalAlignment(SwingConstants.CENTER);
        placeholderLabel.setForeground(Color.GRAY);
        
        panel.add(placeholderLabel, BorderLayout.CENTER);
        
        return panel;
    }

    public void refreshForNewPatient() {
        // This method will be called when switching patients
        // Content refresh logic will be added when actual content is implemented
    }

    // Getter methods for future content implementation
    public JPanel getFourATPanel() {
        return fourATPanel;
    }

    public JPanel getTimeBundlePanel() {
        return timeBundlePanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}

package src.ui.physical;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

import src.ui.TabDash;

public class PlaceholderSection extends JPanel {

    private JPanel phitToolSection;
    private LogSection logSection;
    private TabDash tabDash;

    public PlaceholderSection(TabDash tabDash) {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(150,400));

        initialiseComponents();
    }


    private void initialiseComponents() {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));

        phitToolSection = createPhitToolSection();

        if (tabDash != null) {
            logSection = new LogSection(tabDash);
        } else {
            logSection = createPlaceholderLogSection();
        }

        mainContainer.add(phitToolSection);
        mainContainer.add(Box.createVerticalStrut(5));
        mainContainer.add(logSection);

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createPhitToolSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder("PHIT Tool"));

        section.setMinimumSize(new Dimension(150,150));

        JLabel phitLabel = new JLabel("<html><center>PHIT Tool<br>Content</center></html>");
        phitLabel.setFont(phitLabel.getFont().deriveFont(Font.PLAIN, 11f));
        phitLabel.setForeground(Color.GRAY);
        phitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        section.add(phitLabel, BorderLayout.CENTER);
        
        return section;
    }
    

    private LogSection createPlaceholderLogSection() {
        return new LogSection(null) {
            @Override
            public void refreshForNewPatient() {
                // Override to show placeholder content when TabDash is null
                logContentPanel.removeAll();
                JLabel placeholderLabel = new JLabel("<html><center>Monitoring<br>Log</center></html>");
                placeholderLabel.setFont(placeholderLabel.getFont().deriveFont(Font.ITALIC, 11f));
                placeholderLabel.setForeground(Color.GRAY);
                placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                placeholderLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                logContentPanel.add(placeholderLabel);
                logContentPanel.revalidate();
                logContentPanel.repaint();
            }
        };
    }


    public JPanel getPhitToolSection() {
        return phitToolSection;
    }
    

    public LogSection getLogSection() {
        return logSection;
    }

    public void refreshForNewPatient() {
        if (logSection != null) {
            logSection.refreshForNewPatient();
        }
    }
}

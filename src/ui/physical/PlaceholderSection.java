package src.ui.physical;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

public class PlaceholderSection extends JPanel {

    private JPanel phitToolSection;
    private JPanel logSection;

    public PlaceholderSection() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(150,400));

        initialiseComponents();
    }


    private void initialiseComponents() {
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));

        phitToolSection = createPhitToolSection();

        logSection = createLogSection();

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
    

    private JPanel createLogSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder("Log"));

        section.setMinimumSize(new Dimension(150,150));

        JPanel logContent = new JPanel();
        logContent.setLayout(new BoxLayout(logContent, BoxLayout.Y_AXIS));

        JLabel logLabel = new JLabel("<html><center>Monitoring<br>Log</center></html>");
        logLabel.setFont(logLabel.getFont().deriveFont(Font.PLAIN, 11f));
        logLabel.setForeground(Color.GRAY);
        logLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        logContent.add(logLabel);
        
        JScrollPane scrollPane = new JScrollPane(logContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        section.add(scrollPane, BorderLayout.CENTER);
        
        return section;
    }


    public JPanel getPhitToolSection() {
        return phitToolSection;
    }
    

    public JPanel getLogSection() {
        return logSection;
    }
}

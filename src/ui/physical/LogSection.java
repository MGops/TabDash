package src.ui.physical;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import src.model.LogEntry;
import src.model.Patient;
import src.ui.TabDash;

public class LogSection extends JPanel {
    private TabDash tabDash;
    protected JPanel logContentPanel;

    public LogSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Log"));
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
        setMinimumSize(new Dimension(150,150));

        initialiseComponents();
        loadPatientLogEntries();
    }

    private void initialiseComponents() {
        logContentPanel = new JPanel();
        logContentPanel.setLayout(new BoxLayout(logContentPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(logContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPatientLogEntries() {
        logContentPanel.removeAll();

        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            JLabel noPatientLabel = new JLabel("No patient selected");
            noPatientLabel.setFont(noPatientLabel.getFont().deriveFont(Font.ITALIC, 11f));
            noPatientLabel.setForeground(Color.GRAY);
            noPatientLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noPatientLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            logContentPanel.add(noPatientLabel);
        } else {
            List<LogEntry> sortedEntries = currentPatient.getSortedLogEntries();

            if (sortedEntries.isEmpty()) {
                JLabel noEntriesLabel = new JLabel("No log entries");
                noEntriesLabel.setFont(noEntriesLabel.getFont().deriveFont(Font.ITALIC, 11f));
                noEntriesLabel.setForeground(Color.GRAY);
                noEntriesLabel.setHorizontalAlignment(SwingConstants.CENTER);
                noEntriesLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                logContentPanel.add(noEntriesLabel);
            } else {
                for (LogEntry entry : sortedEntries) {
                    JLabel entryLabel = createLogEntryLabel(entry);
                    logContentPanel.add(entryLabel);
                    logContentPanel.add(Box.createVerticalStrut(2));
                }
            }
        }
        logContentPanel.revalidate();
        logContentPanel.repaint();
    }

    
    private JLabel createLogEntryLabel(LogEntry entry) {
        JLabel label = new JLabel(entry.getDisplayText());

        // Set background colour based on chart type
        Color backgroundColour = Color.decode(entry.getChartType().colorHex);
        label.setOpaque(true);
        label.setBackground(backgroundColour);

        label.setForeground(Color.BLACK);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
        
        return label;

    }
    
    public void refreshForNewPatient() {
        loadPatientLogEntries();
    }
    
    public void refreshLogEntries() {
        loadPatientLogEntries();
    }
}

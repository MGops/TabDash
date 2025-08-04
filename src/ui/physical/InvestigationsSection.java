package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import src.data_managers.MonitoringDataManager;
import src.model.MonitoringIssue;
import src.model.Patient;
import src.ui.TabDash;

public class InvestigationsSection extends JPanel {
    private TabDash tabDash;
    private JPanel activeIssuesPanel;

    // Define available monitoring issue types
    private static final String[] ISSUE_TYPES = {
        "Physical obs", "Bloods", "ECG", "BM", "Diet", "Fluids", "Stool chart", "Bodymap"
    };

    public InvestigationsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Monitoring"));
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
        setLayout(new BorderLayout());

        // Set fixed size constraints to prevent resizing
        setPreferredSize(new Dimension(200,250));
        setMinimumSize(new Dimension(200,200));
        setMaximumSize(new Dimension(200,300));

        // Panel to show currently monitored issues
        activeIssuesPanel = new JPanel();
        activeIssuesPanel.setLayout(new BoxLayout(activeIssuesPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(activeIssuesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Edit issue to monitor");
        addBtn.addActionListener(e -> {
            showEditIssueDialog();
        });
        buttonPanel.add(addBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadActiveIssues();
    }

    
    private void showEditIssueDialog() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            JOptionPane.showMessageDialog(this, 
                "No patient selected. Please selected a patient first.",
                "No patient selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            "Add Monitoring Issues", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);

        // Main panel of dialog
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Maps to track components
        Map<String, JCheckBox> checkBoxes = new HashMap<>();
        Map<String, JTextField> textFields = new HashMap<>();

        // Create UI for each issue type
        for (int i = 0; i < ISSUE_TYPES.length; i++) {
            String issueType = ISSUE_TYPES[i];
            
            // Checkbox in first column
            JCheckBox checkBox = new JCheckBox(issueType);
            checkBox.setFont(checkBox.getFont().deriveFont(12f));
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.NONE;
            mainPanel.add(checkBox, gbc);

            JTextField textField = new JTextField();
            textField.setPreferredSize(new Dimension(250, 22));
            textField.setFont(textField.getFont().deriveFont(11f));
            textField.setBorder(BorderFactory.createLoweredBevelBorder());
            textField.setEnabled(false); // Start disabled
            textField.setBackground(Color.LIGHT_GRAY);
            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(textField, gbc);

            // Store references
            checkBoxes.put(issueType, checkBox);
            textFields.put(issueType, textField);

            // Add listener to enable/disable text field
            checkBox.addActionListener(e -> {
                boolean selected = checkBox.isSelected();
                textField.setEnabled(selected);
                textField.setBackground(selected ? Color.WHITE : Color.LIGHT_GRAY);
                if (selected) {
                    textField.requestFocus();
                } else {
                    textField.setText("");
                }
            });
        }

        populateExistingIssues(currentPatient, checkBoxes, textFields);

        // Scroll pane for main content
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Button actions
        okBtn.addActionListener(e -> {
            saveSelectedIssues(currentPatient, checkBoxes, textFields);
            dialog.dispose();
            loadActiveIssues();
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }


    private void populateExistingIssues(Patient patient, 
                                       Map<String, JCheckBox> checkBoxes, 
                                       Map<String, JTextField> textFields) {
        // Load current monitoring issues
        MonitoringDataManager.loadPatientMonitoring(patient);
        
        // Pre-populate dialog with existing active issues
        for (MonitoringIssue issue : patient.getMonitoringIssues()) {
            if (issue.isActive()) {
                JCheckBox checkBox = checkBoxes.get(issue.getIssueType());
                JTextField textField = textFields.get(issue.getIssueType());
                
                if (checkBox != null && textField != null) {
                    checkBox.setSelected(true);
                    textField.setText(issue.getNotes() != null ? issue.getNotes() : "");
                    textField.setEnabled(true); 
                    textField.setBackground(Color.WHITE);
                }
            }
        }
    }


    private void saveSelectedIssues(Patient patient,
                                    Map<String, JCheckBox> checkBoxes,
                                    Map<String, JTextField> textFields) {
        // Clear existing monitoring issues 
        patient.getMonitoringIssues().clear();
        
        // Process each issue type
        for (String issueType : ISSUE_TYPES) {
            JCheckBox checkBox = checkBoxes.get(issueType);
            JTextField textField = textFields.get(issueType);
            
            if (checkBox.isSelected()) {
                // Create new active monitoring issue
                String notes = textField.getText().trim();
                MonitoringIssue issue = new MonitoringIssue(issueType, 
                    notes.isEmpty() ? null : notes);
                issue.setActive(true);
                patient.addMonitoringIssue(issue);
            }
        }
        
        // Save to file
        MonitoringDataManager.savePatientMonitoring(patient);
        tabDash.onPatientDataChanged();
    }


    private void loadActiveIssues() {
        activeIssuesPanel.removeAll();

        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            JLabel noPatientLabel = new JLabel("No patient selected");
            noPatientLabel.setForeground(Color.GRAY);
            activeIssuesPanel.add(noPatientLabel);
        } else {
            // Load monitoring data from file
            MonitoringDataManager.loadPatientMonitoring(currentPatient);

            List<MonitoringIssue> activeIssues = currentPatient.getMonitoringIssues().stream()
                .filter(MonitoringIssue::isActive)
                .toList();
            
            if (activeIssues.isEmpty()) {
                JLabel noIssuesLabel = new JLabel("No monitoring issues active");
                noIssuesLabel.setForeground(Color.GRAY);
                activeIssuesPanel.add(noIssuesLabel);
            } else {
                for (MonitoringIssue issue : activeIssues) {
                    JPanel issuePanel = createActiveIssuePanel(issue);
                    activeIssuesPanel.add(issuePanel);
                    activeIssuesPanel.add(Box.createVerticalStrut(5));
                }
            }
        }
        activeIssuesPanel.revalidate();
        activeIssuesPanel.repaint();
    }


    private JPanel createActiveIssuePanel(MonitoringIssue issue) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLUE, 1),
            BorderFactory.createEmptyBorder(5,5,5,5)
        ));
        panel.setBackground(new Color(240,248,255));

        
        // Create display text
        String displayText = issue.getIssueType();
        if (issue.getNotes() != null && !issue.getNotes().trim().isEmpty()) {
            displayText += ": " + issue.getNotes();
        }
        JTextArea textArea = new JTextArea(displayText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setFont(textArea.getFont().deriveFont(11));
        textArea.setBorder(null);
        panel.add(textArea, BorderLayout.CENTER);

        // Remove button (same as before)
        JButton removeBtn = new JButton("X");
        removeBtn.setPreferredSize(new Dimension(20,20));
        removeBtn.setFont(removeBtn.getFont().deriveFont(10f));
        removeBtn.setToolTipText("Remove monitoring issue");
        removeBtn.setMargin(new Insets(0, 0, 0, 0));

        removeBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Remove monitoring for " + issue.getIssueType() + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                issue.setActive(false);
                MonitoringDataManager.savePatientMonitoring(tabDash.getCurrentPatient());
                tabDash.onPatientDataChanged();
                loadActiveIssues(); // refresh display
            }
        });

        panel.add(removeBtn, BorderLayout.EAST);
        return panel;
    }


    public void refreshForNewPatient() {
        loadActiveIssues();
    }
}
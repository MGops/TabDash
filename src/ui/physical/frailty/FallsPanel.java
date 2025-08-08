package src.ui.physical.frailty;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.Date;
import src.model.Patient;
import src.ui.TabDash;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;

public class FallsPanel extends JPanel {

    private JPanel fallsActionSection;
    private JPanel fallsNumberSection;

    private int fallsCount = 0;
    private JButton fallsCountBtn;
    private JLabel lastFallLabel;
    private TabDash tabDash;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public FallsPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Border empty = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createTitledBorder(empty,"Falls"));
        //setBackground(Color.LIGHT_GRAY);

        createSections();
        initialiseComponents();

    }


    private JPanel createActionSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        lastFallLabel = new JLabel();
        lastFallLabel.setFont(lastFallLabel.getFont().deriveFont(Font.PLAIN, 11f));
        lastFallLabel.setForeground(Color.DARK_GRAY);
        lastFallLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lastFallLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        updateLastFallDisplay();
        section.add(lastFallLabel, BorderLayout.NORTH);

        return section;
    }

    private JPanel createNumberSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        fallsCountBtn = new JButton(String.valueOf(fallsCount));
        fallsCountBtn.setFont(getFont().deriveFont(Font.BOLD, 36));
        fallsCountBtn.setForeground(new Color(40,190,40));
        //fallsCountBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        fallsCountBtn.setFocusPainted(false);

        fallsCountBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                    // Show dialog to set fall date
                    Date fallDate = showFallDateDialog();
                        if (fallDate != null) {
                        fallsCount ++;
                        fallsCountBtn.setText(String.valueOf(fallsCount));
                        fallsCountBtn.setForeground(Color.RED);   
                        
                        // Set the selected fall date
                        Patient currentPatient = tabDash.getCurrentPatient();
                        if (currentPatient != null) {
                            currentPatient.setLastFallDate(fallDate);
                        }
                        saveFallsCount();
                        saveButtonColour("RED");
                        updateLastFallDisplay();
                    }

                } else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON3) {
                    fallsCount = 0;
                    fallsCountBtn.setText(String.valueOf(fallsCount));

                    saveFallsCount();
                    updateLastFallDisplay();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (fallsCountBtn.getForeground().equals(Color.RED)) {
                        fallsCountBtn.setForeground(new Color(40,190,40));
                        saveButtonColour("GREEN");
                    }
                }
            } 
        });

        section.add(fallsCountBtn, BorderLayout.CENTER);
        return section;
    }


    private void createSections() {
        fallsActionSection = createActionSection();
        fallsNumberSection = createNumberSection();
    }


    private void initialiseComponents() {
        add(fallsNumberSection);
        add(createVerticalSeparator());
        add(fallsActionSection);
    }


    private JSeparator createVerticalSeparator() {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setMaximumSize(new Dimension(2,Integer.MAX_VALUE));
        separator.setPreferredSize(new Dimension(2,0));
        separator.setForeground(Color.BLACK);
        return separator;
    }


    private void saveFallsCount() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null) {
            currentPatient.setFallsCount(fallsCount);
            tabDash.onPatientDataChanged();
        }
    }


    private void saveButtonColour(String color) {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null) {
            currentPatient.setFallsButtonColour(color);
            tabDash.onPatientDataChanged();
        }
    }


    private void updateLastFallDisplay() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null && currentPatient.getLastFallDate() != null) {
            String formattedDate = dateFormat.format(currentPatient.getLastFallDate());
            lastFallLabel.setText("<html><center>Last fall:" + formattedDate + "</center></html>");
        } else {
            lastFallLabel.setText("<html><center>Last fall: None recorded</center></html>");
        }
    }

    private Date showFallDateDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "When was the fall?", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.insets = new Insets(10, 10, 10, 10);

        // Container to hold the selected date
        final Date[] selectedDate = {null};
        
        // Today button
        JButton todayBtn = new JButton("Today");
        todayBtn.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(todayBtn, gbc);
        
        // Yesterday button
        JButton yesterdayBtn = new JButton("Yesterday");
        yesterdayBtn.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(yesterdayBtn, gbc);
        
        // Other label and date field     
        JTextField dateField = new JTextField(10);
        dateField.setText("Other: dd/MM/yyyy");
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(dateField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);
        
        todayBtn.addActionListener(e -> {
            selectedDate[0] = new Date();
            dialog.dispose();
        });
        
        yesterdayBtn.addActionListener(e -> {
            // Calculate yesterday's date
            long yesterday = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
            selectedDate[0] = new Date(yesterday);
            dialog.dispose();
        });
        
        okBtn.addActionListener(e -> {
            // Use date from the text field
            try {
                selectedDate[0] = dateFormat.parse(dateField.getText().trim());
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Invalid date format. Please use dd/MM/yyyy (e.g., 15/01/2025)", 
                    "Invalid Date", 
                    JOptionPane.ERROR_MESSAGE);
                dateField.requestFocus();
            }
        });
        
        cancelBtn.addActionListener(e -> {
            selectedDate[0] = null;
            dialog.dispose();
        });
        
        // Allow Enter key in date field to submit
        dateField.addActionListener(e -> okBtn.doClick());
        
        // Focus on date field initially
        SwingUtilities.invokeLater(() -> dateField.requestFocus());
        
        dialog.setVisible(true);
        return selectedDate[0];
    }



    public void refreshForNewPatient() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null && fallsCountBtn != null) {
            fallsCount = currentPatient.getFallsCount();
            fallsCountBtn.setText(String.valueOf(fallsCount));

            String savedColour = currentPatient.getFallsButtonColour();
            if ("GREEN".equals(savedColour)) {
                fallsCountBtn.setForeground(new Color(40,190,40));
            } else {
                fallsCountBtn.setForeground(Color.RED);    
            }
            updateLastFallDisplay();
        } else if (fallsCountBtn != null) {
            fallsCount = 0;
            fallsCountBtn.setText("0");
            fallsCountBtn.setForeground(new Color(40,190,40));
            updateLastFallDisplay();
        }
    }
}

package src.ui.physical.frailty;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import src.model.Patient;
import src.ui.TabDash;

public class ImmobilityPanel extends JPanel {
    private TabDash tabDash;
    private JFormattedTextField vteAssessmentDateField;
    private JLabel nextAssessmentLabel;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ImmobilityPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        Border empty = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createTitledBorder(empty, "Immobility"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(220, 255,220));
        initialiseComponents();
    }

    private void initialiseComponents() {
        JPanel vtePanel = new JPanel();
        vtePanel.setLayout(new BoxLayout(vtePanel, BoxLayout.X_AXIS));
        vtePanel.setOpaque(false);
        vtePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel vteLabel = new JLabel("VTE risk done:");

        vteAssessmentDateField = new JFormattedTextField(dateFormat);
        vteAssessmentDateField.setColumns(8);
        vteAssessmentDateField.setMaximumSize(vteAssessmentDateField.getPreferredSize());
        vteAssessmentDateField.setBorder(BorderFactory.createLoweredBevelBorder());

        vtePanel.add(vteLabel);
        vtePanel.add(vteAssessmentDateField);

        nextAssessmentLabel = new JLabel();
        nextAssessmentLabel.setOpaque(true);
        nextAssessmentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nextAssessmentLabel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        add(vtePanel);
        add(Box.createVerticalStrut(5));
        add(nextAssessmentLabel);
        add(Box.createVerticalGlue());

        setupEventListeners();
        updateNextAssessmentDisplay();
    }


    private void setupEventListeners() {
        // Date field change listener
        vteAssessmentDateField.addPropertyChangeListener("value", e -> {
            updatePatientAndSave();
            updateNextAssessmentDisplay();
        });

        vteAssessmentDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (vteAssessmentDateField.getText().trim().isEmpty()) {
                    vteAssessmentDateField.setValue(null);
                    updatePatientAndSave();
                    updateNextAssessmentDisplay();
                }
            }
        });
    }

    private void updateNextAssessmentDisplay() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            nextAssessmentLabel.setText("");
            nextAssessmentLabel.setBackground(Color.WHITE);
            return;
        }

        Date vteDate = currentPatient.getVteAssessmentDate();
        if (vteDate == null) {
            nextAssessmentLabel.setText("");
            nextAssessmentLabel.setBackground(Color.WHITE);
            nextAssessmentLabel.setForeground(Color.GRAY);
            return;
        }

        // Calculate next assessment due date (12 weeks from assessment date)
        Calendar cal = Calendar.getInstance();
        cal.setTime(vteDate);
        cal.add(Calendar.WEEK_OF_YEAR, 12);
        Date nextDueDate = cal.getTime();

        // Format the next due date
        String formattedNextDate = dateFormat.format(nextDueDate);
        
        // Check if overdue
        Date today = new Date();
        boolean isOverdue = today.after(nextDueDate);
        
        // Calculate days until/since due
        long daysDifference = (nextDueDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
        
        String statusText;
        if (isOverdue) {
            long daysOverdue = Math.abs(daysDifference);
            statusText = "<html><center>VTE assessment OVERDUE<br>" +
                        "Due: " + formattedNextDate + 
                        " (" + daysOverdue + " days overdue)<br>" +
                        "<i>Unless change in mobility</i></center></html>";
            nextAssessmentLabel.setBackground(Color.RED);
            nextAssessmentLabel.setForeground(Color.WHITE);
        } else {
            statusText = "<html><center>Next VTE assessment due: " +
                        formattedNextDate + 
                        " (" + daysDifference + " days)<br>" +
                        "<i>Unless change in mobility</i></center></html>";
            
            if (daysDifference <= 7) {
                nextAssessmentLabel.setBackground(Color.ORANGE);
                nextAssessmentLabel.setForeground(Color.BLACK);
            } else if (daysDifference <= 14) {
                nextAssessmentLabel.setBackground(Color.YELLOW);
                nextAssessmentLabel.setForeground(Color.BLACK);
            } else {
                nextAssessmentLabel.setBackground(new Color(144, 238, 144)); // Light green
                nextAssessmentLabel.setForeground(Color.BLACK);
            }
        }
        
        nextAssessmentLabel.setText(statusText);
    }

    private void updatePatientAndSave() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) return;
        
        // Update patient with current field value
        if (vteAssessmentDateField.getValue() instanceof Date) {
            currentPatient.setVteAssessmentDate((Date) vteAssessmentDateField.getValue());
        } else if (vteAssessmentDateField.getText().trim().isEmpty()) {
            currentPatient.setVteAssessmentDate(null);
        }
        
        // Save data
        tabDash.onPatientDataChanged();
    }

    public void refreshForNewPatient() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null) {
            // Load VTE assessment date from patient
            Date vteDate = currentPatient.getVteAssessmentDate();
            if (vteDate != null) {
                vteAssessmentDateField.setValue(vteDate);
            } else {
                vteAssessmentDateField.setValue(null);
                vteAssessmentDateField.setText("");
            }
        } else {
            vteAssessmentDateField.setValue(null);
            vteAssessmentDateField.setText("");
        }
        
        updateNextAssessmentDisplay();
    }

     
}

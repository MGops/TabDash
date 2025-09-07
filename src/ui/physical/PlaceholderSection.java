package src.ui.physical;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import src.data_managers.PhitDataManager;
import src.model.Patient;
import src.ui.TabDash;

public class PlaceholderSection extends JPanel {

    private JPanel phitToolSection;
    private JLabel lipidProfileLabel;
    private JLabel hba1cLabel;
    private JLabel tftLabel;
    private JLabel prolactinLabel;
    private JLabel b12Label;
    private JLabel folateLabel;

    private JFormattedTextField lipidProfileField;
    private JFormattedTextField hba1cField;
    private JFormattedTextField tftField;
    private JFormattedTextField prolactinField;
    private JFormattedTextField b12Field;
    private JFormattedTextField folateField;
    private LogSection logSection;
    private TabDash tabDash;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public PlaceholderSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(180,400));

        initialiseComponents();
    }


    private void initialiseComponents() {
        JPanel mainContainer = new JPanel(new BorderLayout());

        phitToolSection = createPhitToolSection();

        if (tabDash != null) {
            logSection = new LogSection(tabDash);
        } else {
            logSection = createPlaceholderLogSection();
        }

        mainContainer.add(phitToolSection, BorderLayout.NORTH);
        mainContainer.add(logSection, BorderLayout.CENTER);

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createPhitToolSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder("PHIT Tool"));
        TitledBorder border = (TitledBorder) section.getBorder();
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));

        section.setPreferredSize(new Dimension(180, 200));
        section.setMinimumSize(new Dimension(180, 200));

        section.add(Box.createVerticalStrut(5));

        addInvestigationField(section, "Lipid profile:", createLipidProfileComponents());
        addInvestigationField(section, "HbA1c:", createHba1cComponents());
        addInvestigationField(section, "TFT:", createTftComponents());
        addInvestigationField(section, "Prolactin:", createProlactinComponents());
        addInvestigationField(section, "B12:", createB12Components());
        addInvestigationField(section, "Folate:", createFolateComponents());

        section.add(Box.createVerticalStrut(5));

        return section;
    }
    
    private void addInvestigationField(JPanel parent, String labelText, JComponent[] components) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = (JLabel) components[0];
        JFormattedTextField field = (JFormattedTextField) components[1];
        
        label.setText(labelText);
        label.setPreferredSize(new Dimension(90, 20));
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 12f));
        
        rowPanel.add(label);
        rowPanel.add(field);
        parent.add(rowPanel);
        parent.add(Box.createVerticalStrut(2));
    }

    private JComponent[] createLipidProfileComponents() {
        lipidProfileLabel = new JLabel();
        lipidProfileField = createDateField();
        return new JComponent[]{lipidProfileLabel, lipidProfileField};
    }

    private JComponent[] createHba1cComponents() {
        hba1cLabel = new JLabel();
        hba1cField = createDateField();
        return new JComponent[]{hba1cLabel, hba1cField};
    }

    private JComponent[] createTftComponents() {
        tftLabel = new JLabel();
        tftField = createDateField();
        return new JComponent[]{tftLabel, tftField};
    }

    private JComponent[] createProlactinComponents() {
        prolactinLabel = new JLabel();
        prolactinField = createDateField();
        return new JComponent[]{prolactinLabel, prolactinField};
    }

    private JComponent[] createB12Components() {
        b12Label = new JLabel();
        b12Field = createDateField();
        return new JComponent[]{b12Label, b12Field};
    }

    private JComponent[] createFolateComponents() {
        folateLabel = new JLabel();
        folateField = createDateField();
        return new JComponent[]{folateLabel, folateField};
    }

    private JFormattedTextField createDateField() {
        JFormattedTextField field = new JFormattedTextField(dateFormat);
        field.setColumns(8);
        field.setPreferredSize(new Dimension(85, 22));
        field.setMaximumSize(new Dimension(85, 22));
        field.setFont(field.getFont().deriveFont(Font.PLAIN, 11f));
        field.setBorder(BorderFactory.createLoweredBevelBorder());
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setValue(null);
                    updatePatientAndSave();
                }
            }
        });
        
        field.addPropertyChangeListener("value", e -> {
            SwingUtilities.invokeLater(() -> updatePatientAndSave());
        });
        
        return field;
    }


    private int calculateDaysBetween(Date investigationDate, Date currentDate) {
        if (investigationDate == null || currentDate == null) {
            return 0;
        }

        long diffInMillis = currentDate.getTime() - investigationDate.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }


    private void updateLabelColours() {
        if (tabDash == null || tabDash.getCurrentPatient() == null) {
            return;
        }

        Patient currentPatient = tabDash.getCurrentPatient();
        Date today = new Date();

        updateInvestigationLabelColour(lipidProfileLabel, currentPatient.getLipidProfileDate(), today, true);

        updateInvestigationLabelColour(hba1cLabel, currentPatient.getHba1cDate(), today, true);

        updateInvestigationLabelColour(tftLabel, currentPatient.getTftDate(), today, false);
        updateInvestigationLabelColour(prolactinLabel, currentPatient.getProlactinDate(), today, false);
        updateInvestigationLabelColour(b12Label, currentPatient.getB12Date(), today, false);
        updateInvestigationLabelColour(folateLabel, currentPatient.getFolateDate(), today, false);
    }


    private void updateInvestigationLabelColour(JLabel label, Date investigationDate, Date today, boolean alertRequired) {
        if (label == null) return;        

        if (!alertRequired || investigationDate == null) {
            label.setOpaque(false);
            label.setForeground(Color.BLACK);
            return;
        }

        int daysSinceInvestigation = calculateDaysBetween(investigationDate, today);

        if (daysSinceInvestigation > 90) {
            label.setOpaque(true);
            label.setBackground(Color.RED);
            label.setForeground(Color.WHITE);
        } else {
            label.setOpaque(false);
            label.setForeground(Color.BLACK);
        }
    }

    private void updatePatientAndSave() {
        if (tabDash == null || tabDash.getCurrentPatient() == null) return;
        
        Patient currentPatient = tabDash.getCurrentPatient();
        
        updatePatientFromFields(currentPatient);

        updateLabelColours();
        
        PhitDataManager.savePatientPhitData(currentPatient);
    }

    private void updatePatientFromFields(Patient patient) {
        patient.setLipidProfileDate(getDateFromField(lipidProfileField));
        patient.setHba1cDate(getDateFromField(hba1cField));
        patient.setTftDate(getDateFromField(tftField));
        patient.setProlactinDate(getDateFromField(prolactinField));
        patient.setB12Date(getDateFromField(b12Field));
        patient.setFolateDate(getDateFromField(folateField));
    }

    private Date getDateFromField(JFormattedTextField field) {
        if (field.getValue() instanceof Date) {
            return (Date) field.getValue();
        } else if (field.getText().trim().isEmpty()) {
            return null;
        }
        return null;
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

        loadPhitDataIntoFields();
    }

    private void loadPhitDataIntoFields() {
        if (tabDash == null || tabDash.getCurrentPatient() == null) {
            clearAllFields();
            return;
        }
        
        Patient currentPatient = tabDash.getCurrentPatient();
        
        // Load PHIT data from file
        PhitDataManager.loadPatientPhitData(currentPatient);
        
        // Populate fields
        setFieldValue(lipidProfileField, currentPatient.getLipidProfileDate());
        setFieldValue(hba1cField, currentPatient.getHba1cDate());
        setFieldValue(tftField, currentPatient.getTftDate());
        setFieldValue(prolactinField, currentPatient.getProlactinDate());
        setFieldValue(b12Field, currentPatient.getB12Date());
        setFieldValue(folateField, currentPatient.getFolateDate());

        updateLabelColours();
    }

    private void setFieldValue(JFormattedTextField field, Date date) {
        if (field != null) {
            if (date != null) {
                field.setValue(date);
            } else {
                field.setValue(null);
                field.setText("");
            }
        }
    }

    private void clearAllFields() {
        if (lipidProfileField != null) setFieldValue(lipidProfileField, null);
        if (hba1cField != null) setFieldValue(hba1cField, null);
        if (tftField != null) setFieldValue(tftField, null);
        if (prolactinField != null) setFieldValue(prolactinField, null);
        if (b12Field != null) setFieldValue(b12Field, null);
        if (folateField != null) setFieldValue(folateField, null);

        resetLabelColour(lipidProfileLabel);
        resetLabelColour(hba1cLabel);
        resetLabelColour(tftLabel);
        resetLabelColour(prolactinLabel);
        resetLabelColour(b12Label);
        resetLabelColour(folateLabel);
    }

    private void resetLabelColour(JLabel label) {
        if (label != null) {
            label.setOpaque(false);
            label.setForeground(Color.BLACK);
        }
    }
}

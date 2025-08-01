package src.ui.panels;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.SwingPropertyChangeSupport;

import src.data_managers.MHADataManager;
import src.data_managers.MedicationLookupService;
import src.model.Patient;
import src.ui.TabDash;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MHAPanel extends JPanel{
    private TabDash tabDash;
    private MedicationLookupService medicationLookupService;
    private JRadioButton noCapacityBtn;
    private JRadioButton yesCapacityBtn;
    private JPanel pathwayPanel;
    private JCheckBox t3CheckBox;
    private JPanel s62Panel;
    private JLabel s62AlertLabel;
    private Date threeMthExpiry;
    private JPanel soadPanel;
    private JCheckBox mh03CheckBox;
    private JPanel middleSection;
    private JPanel bottomSection;
    private JFormattedTextField detentionDateField;
    private JLabel sectionExpiryLabel;
    private JLabel threeMonthLabel;
    private JRadioButton section2Btn;
    private JRadioButton section3Btn;
    private Date originalDetentionDate;
    private JFormattedTextField admissionDateField;
    private JRadioButton informalBtn;
    private JRadioButton dolsBtn;
    private JCheckBox soadRequestedChk;
    private JFormattedTextField soadDateField;
    private JTextField soadRefField;
    private JCheckBox s62CompletedChk;
    private JFormattedTextField s62DateField;
    private JFormattedTextField t3DateField;
    private JFormattedTextField t3ReviewDateField;
    private JCheckBox t2CheckBox;
    private JFormattedTextField t2DateField;
    private JFormattedTextField t2ReviewDateField;
    private boolean autoSaveEnabled = true;
    private JLabel tribunalDateLabel;
    private JLabel tribunalTypeLabel;
    private JLabel reportDueLabel;
    private JPanel tribunalDisplayPanel;
    private JCheckBox emergencyLeaveCheckBox;
    private JTextArea otherLeaveTextArea;
    private JLabel detentionDateLbl;
    private JPanel topPanel;
    private JPanel emergencyPanel;
    private JLabel emergencyLabel;
    private JLabel alertIcon;
    private JLabel alertMessage;
    private JButton alertYesBtn;
    private JButton alertNoBtn;

    public MHAPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        this.medicationLookupService = new MedicationLookupService();
        setLayout(new BorderLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        JPanel topSection = createTopSection();
        middleSection = createMiddleSection();
        bottomSection = createBottomSection();

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));

        mainContainer.add(topSection);
        mainContainer.add(createSeparator());
        mainContainer.add(middleSection);
        mainContainer.add(createSeparator());
        mainContainer.add(bottomSection);
    
        add(mainContainer, BorderLayout.CENTER);
        
        enableMHAFunctionality(false);
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return separator;
    }

    // TOP SECTION

    private JPanel createTopSection() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        TitledBorder topBorder = BorderFactory.createTitledBorder("Patient Status");
        topBorder.setTitleFont(topBorder.getTitleFont().deriveFont(Font.BOLD));
        topBorder.setTitleJustification(TitledBorder.CENTER);
        topPanel.setBorder(topBorder);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        topPanel.add(new JLabel("Status:"));

        ButtonGroup statusGroup = new ButtonGroup();
        informalBtn = new JRadioButton("Informal");
        section2Btn = new JRadioButton("Section 2");
        section3Btn = new JRadioButton("Section 3");
        dolsBtn = new JRadioButton("DOLS");

        statusGroup.add(informalBtn);
        statusGroup.add(section2Btn);
        statusGroup.add(section3Btn);
        statusGroup.add(dolsBtn);

        topPanel.add(informalBtn);
        topPanel.add(section2Btn);
        topPanel.add(section3Btn);
        topPanel.add(dolsBtn);

        topPanel.add(Box.createHorizontalStrut(20));

        detentionDateLbl = new JLabel("Date detained");
        topPanel.add(detentionDateLbl);
        detentionDateLbl.setVisible(false);
        detentionDateField = new JFormattedTextField(dateFormat);
        detentionDateField.setColumns(8);
        addDateFieldClearingListener(detentionDateField);
        detentionDateField.setValue(new java.util.Date());
        detentionDateField.setVisible(false);
        topPanel.add(detentionDateField);

        topPanel.add(Box.createHorizontalStrut(20));

        topPanel.add(new JLabel("Admission date: "));
        admissionDateField = new JFormattedTextField(dateFormat);
        admissionDateField.setColumns(8);
        addDateFieldClearingListener(admissionDateField);
        admissionDateField.setValue(new java.util.Date());
        topPanel.add(admissionDateField);
    
        topPanel.add(Box.createHorizontalStrut(20));

        mh03CheckBox = new JCheckBox("MH03 completed");
        topPanel.add(mh03CheckBox);

        //Listener for automatic saving
        mh03CheckBox.addActionListener(e -> updatePatientAndSave());
        
        // Add to admission date field
        admissionDateField.addPropertyChangeListener("value", e -> updatePatientAndSave());

        section2Btn.addActionListener(e -> {
            updateDetentionFieldVisibility();
            updatePatientAndSave();
            SwingUtilities.invokeLater(() -> updateS62AlertVisibility());
        });
        section3Btn.addActionListener(e -> {
            updateDetentionFieldVisibility();
            updatePatientAndSave();
            SwingUtilities.invokeLater(() -> updateS62AlertVisibility());
        });
        informalBtn.addActionListener(e -> {
            updateDetentionFieldVisibility();
            updatePatientAndSave();
        });
        dolsBtn.addActionListener(e -> { 
            updateDetentionFieldVisibility();
            updatePatientAndSave();
        });

        detentionDateField.addPropertyChangeListener("value", e -> { // update calculations when detention date changes
            if (detentionDateField.getValue() != null) {
                updateDateCalculations();
                updatePatientAndSave();
            }
        });

        // Realtime update as date is typed
        detentionDateField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                delayUpdateDateCalc();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                delayUpdateDateCalc();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                delayUpdateDateCalc();
            }
        });


        informalBtn.setSelected(true);

        mh03CheckBox.addActionListener(e -> {
            boolean mh03Completed = mh03CheckBox.isSelected();
            enableMHAFunctionality(mh03Completed);
            if (mh03Completed) {
                SwingUtilities.invokeLater(() -> updateS62AlertVisibility());
            }
        });
        
        return topPanel;
    }

    // MIDDLE SECTION

    private JPanel createMiddleSection() {
        JPanel middlePanel = new JPanel();
        TitledBorder middleBorder = BorderFactory.createTitledBorder("MHA Management");
        middleBorder.setTitleFont(middleBorder.getTitleFont().deriveFont(Font.BOLD));
        middleBorder.setTitleJustification(TitledBorder.CENTER);
        middlePanel.setBorder(middleBorder);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));

        // Section 1: Expiry dates with traffic lights
        JPanel expiryPanel = createExpiryPanel();
        middlePanel.add(expiryPanel);

        // Section 2: Capacity assessment
        JPanel capacityPanel = createCapacityPanel();
        middlePanel.add(capacityPanel);

        //Section 3: Capacity-dependent content
        JPanel pathwayPanel = createPathwayPanel();
        middlePanel.add(pathwayPanel);

        // Section 4: Medication alert
        JPanel alertPanel = createAlertPanel();
        middlePanel.add(alertPanel);

        return middlePanel;
    }

    private JPanel createExpiryPanel() {
        JPanel expiryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        expiryPanel.setBorder(BorderFactory.createTitledBorder("Section Status"));
        
        // Section Expiry
        expiryPanel.add(new JLabel("Section Expires: "));
        sectionExpiryLabel = new JLabel("");
        sectionExpiryLabel.setOpaque(true);
        sectionExpiryLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        expiryPanel.add(sectionExpiryLabel);
        
        expiryPanel.add(Box.createHorizontalStrut(30));
        // 3 month rule with traffic light
        expiryPanel.add(new JLabel("Consent to treatment (3 month rule)"));
        threeMonthLabel = new JLabel("");
        threeMonthLabel.setOpaque(true);
        threeMonthLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        expiryPanel.add(threeMonthLabel);
        return expiryPanel;
    }

    private JPanel createCapacityPanel() {
        JPanel capacityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        capacityPanel.setBorder(BorderFactory.createTitledBorder("Capacity to CTT"));
        capacityPanel.add(new JLabel("Has capacity: "));
        ButtonGroup capacityGroup = new ButtonGroup();
        noCapacityBtn = new JRadioButton("No");
        yesCapacityBtn = new JRadioButton("Yes");
        capacityGroup.add(noCapacityBtn);
        capacityGroup.add(yesCapacityBtn);
        capacityPanel.add(noCapacityBtn);
        capacityPanel.add(yesCapacityBtn);
        noCapacityBtn.setSelected(true);
        
        noCapacityBtn.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) pathwayPanel.getLayout();
            cardLayout.show(pathwayPanel, "NO_CAPACITY");
            showCapacityChangeMh03Alert();
            updatePatientAndSave();
            updateS62AlertVisibility();
        });

        yesCapacityBtn.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) pathwayPanel.getLayout();
            cardLayout.show(pathwayPanel, "HAS_CAPACITY");
            showCapacityChangeMh03Alert();
            updatePatientAndSave();
        });

        return capacityPanel;
    }

    private JPanel createPathwayPanel() {
        pathwayPanel = new JPanel();
        pathwayPanel.setLayout(new CardLayout());
        // Create 2 different pathway panels
        JPanel noCapacityPanel = createNoCapacityPathway();
        JPanel hasCapacityPanel = createHasCapacityPathway();
        // Add both panels to CardLayout
        pathwayPanel.add(noCapacityPanel, "NO_CAPACITY");
        pathwayPanel.add(hasCapacityPanel, "HAS_CAPACITY");
        return pathwayPanel;
    }


    private JPanel createNoCapacityPathway() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("SOAD Pathway (no capacity)"));
        
        // SOAD Request section
        soadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        soadRequestedChk = new JCheckBox("SOAD requested");
        soadPanel.add(soadRequestedChk);
        soadPanel.add(new JLabel("Date sent"));
        soadDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        soadDateField.setColumns(8);
        addDateFieldClearingListener(soadDateField);
        soadPanel.add(soadDateField);
        soadPanel.add(new JLabel("Reference: "));
        soadRefField = new JTextField(10);
        soadPanel.add(soadRefField);
        panel.add(soadPanel);


        // S62 Alert label 
        s62AlertLabel = new JLabel("⚠ Detained patient. 3 month rule alert! Please complete S62");
        s62AlertLabel.setOpaque(true);
        s62AlertLabel.setBackground(Color.RED);
        s62AlertLabel.setForeground(Color.WHITE);
        s62AlertLabel.setFont(s62AlertLabel.getFont().deriveFont(Font.BOLD));
        s62AlertLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK,2),
            BorderFactory.createEmptyBorder(8,10,8,10)
        ));
        s62AlertLabel.setHorizontalAlignment(SwingConstants.CENTER);
        s62AlertLabel.setVisible(false);
        panel.add(s62AlertLabel);

        // S62 section(appears after 3 months)
        s62Panel = new JPanel((new FlowLayout(FlowLayout.LEFT)));
        s62CompletedChk = new JCheckBox("S62 completed");
        s62Panel.add(s62CompletedChk);
        s62Panel.add(new JLabel("Date: "));
        s62DateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        s62DateField.setColumns(8);
        addDateFieldClearingListener(s62DateField);
        s62Panel.add(s62DateField);
        s62Panel.setVisible(false);
        panel.add(s62Panel);

        // T3 section
        JPanel t3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        t3CheckBox = new JCheckBox("T3 Provided");
        t3Panel.add(t3CheckBox);
        t3Panel.add(new JLabel("Date: "));
        t3DateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t3DateField.setColumns(8);
        addDateFieldClearingListener(t3DateField);
        t3Panel.add(t3DateField);
        t3Panel.add(new JLabel("Review due: "));
        t3ReviewDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t3ReviewDateField.setColumns(8);
        t3Panel.add(t3ReviewDateField);
        panel.add(t3Panel);


        soadRequestedChk.addActionListener(e -> updatePatientAndSave());
        soadDateField.addPropertyChangeListener("value", e -> updatePatientAndSave());
        soadRefField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {updatePatientAndSave();}
            public void removeUpdate(DocumentEvent e) {updatePatientAndSave();}
            public void changedUpdate(DocumentEvent e) {updatePatientAndSave();}        
        });

        s62CompletedChk.addActionListener(e -> {
            updateS62AlertVisibility();
            updatePatientAndSave();
        });
        s62DateField.addPropertyChangeListener("value", e -> updatePatientAndSave());

        //Modify existing T3 ActionListener
        t3CheckBox.addActionListener(e -> {
            boolean t3Selected = t3CheckBox.isSelected();
            if (t3Selected) {
                soadPanel.setVisible(false);
                s62Panel.setVisible(false);
                s62AlertLabel.setVisible(false);
            } else {
                soadPanel.setVisible(true);
                s62Panel.setVisible(true);
                updateS62AlertVisibility();
            }
            panel.revalidate();
            panel.repaint();
            updatePatientAndSave();
        });

        t3DateField.addPropertyChangeListener("value", e -> updatePatientAndSave());
        t3ReviewDateField.addPropertyChangeListener("value", e -> updatePatientAndSave());
        
        return panel;
    }


    private void updateS62AlertVisibility() {
        if (!mh03CheckBox.isSelected()) {
            s62AlertLabel.setVisible(false);
            return;
        }

        if (!section2Btn.isSelected() && !section3Btn.isSelected()) {
            s62AlertLabel.setVisible(false);
            return;
        }

        if (s62CompletedChk.isSelected()) {
            s62AlertLabel.setVisible(false);
            return;
        }

        if (t3CheckBox.isSelected()) {
            s62AlertLabel.setVisible(false);
            return;
        }

        if (!noCapacityBtn.isSelected()) {
            s62AlertLabel.setVisible(false);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(threeMthExpiry);
        cal.add(Calendar.DAY_OF_MONTH, -3);
        Date s62alertDate = cal.getTime();

        Date today = new Date();

        boolean shouldShowS62Alert = today.compareTo(s62alertDate) >= 0;
        s62AlertLabel.setVisible(shouldShowS62Alert);
    }


    private JPanel createHasCapacityPathway() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("T2 Pathway (has capacity)"));
        JPanel t2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        t2CheckBox = new JCheckBox("T2 completed");
        t2Panel.add(t2CheckBox);
        t2Panel.add(new JLabel("Date: "));
        t2DateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t2DateField.setColumns(8);
        addDateFieldClearingListener(t2DateField);
        t2Panel.add(t2DateField);
        t2Panel.add(new JLabel("Review date: "));
        t2ReviewDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t2ReviewDateField.setColumns(8);
        addDateFieldClearingListener(t2ReviewDateField);
        t2Panel.add(t2ReviewDateField);
        panel.add(t2Panel);
        
        t2CheckBox.addActionListener(e -> updatePatientAndSave());
        t2DateField.addPropertyChangeListener("value", e -> updatePatientAndSave());
        t2ReviewDateField.addPropertyChangeListener("value", e -> updatePatientAndSave());

        return panel;
    }

    private JPanel createAlertPanel() {
        JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alertPanel.setBorder(BorderFactory.createTitledBorder("Alerts"));

        //Medication change alert - intitially hidden
        alertIcon = new JLabel("⚠");
        alertMessage = new JLabel("Medication changed- CTT review required");
        alertYesBtn = new JButton("Yes");
        alertNoBtn = new JButton("No");

        // Set small button size
        alertYesBtn.setPreferredSize(new Dimension(75, 25));
        alertNoBtn.setPreferredSize(new Dimension(75,25));;
        alertPanel.add(alertIcon);
        alertPanel.add(alertMessage);
        alertPanel.add(Box.createHorizontalStrut(10));
        alertPanel.add(alertYesBtn);
        alertPanel.add(alertNoBtn);

        // Initially hidden
        hideAlert();

        alertYesBtn.addActionListener(e -> {
            showMedicationReviewDialog();
            hideAlert();
        });

        alertNoBtn.addActionListener(e -> {
            hideAlert();
        });

        return alertPanel;
    }

    private JPanel createBottomSection() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        TitledBorder bottomBorder = BorderFactory.createTitledBorder("Leave & Tribunal Management");
        bottomBorder.setTitleFont(bottomBorder.getTitleFont().deriveFont(Font.BOLD));
        bottomBorder.setTitleJustification(TitledBorder.CENTER);
        bottomPanel.setBorder(bottomBorder);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        JPanel leavePanel = createLeavePanel();
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        bottomPanel.add(leavePanel, gbc);

        JPanel tribunalPanel = createTribunalPanel();
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        bottomPanel.add(tribunalPanel, gbc);

        return bottomPanel;
    }

    private JPanel createLeavePanel() {
        JPanel leavePanel = new JPanel();
        leavePanel.setLayout(new BoxLayout(leavePanel, BoxLayout.Y_AXIS));
        leavePanel.setBorder(BorderFactory.createTitledBorder("Leave Records"));
        emergencyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emergencyLabel = new JLabel("Emergency medical leave: ");
        emergencyLeaveCheckBox = new JCheckBox();
        emergencyPanel.add(emergencyLabel);
        emergencyPanel.add(emergencyLeaveCheckBox);
        emergencyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, emergencyPanel.getPreferredSize().height));
        emergencyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leavePanel.add(emergencyPanel);

        emergencyLeaveCheckBox.addActionListener(e -> {
            updateEmergencyLeaveBorder();
            updatePatientAndSave();
        });

        updateEmergencyLeaveBorder();

        leavePanel.add(Box.createVerticalStrut(5));

        JPanel otherLeavePanel = new JPanel(new BorderLayout());
        otherLeavePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel otherLeaveLabel = new JLabel("Other leave:");
        otherLeavePanel.add(otherLeaveLabel, BorderLayout.NORTH);
        
        otherLeaveTextArea = new JTextArea();
        otherLeaveTextArea.setLineWrap(true);
        otherLeaveTextArea.setWrapStyleWord(true);
        otherLeaveTextArea.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane scrollPane = new JScrollPane(otherLeaveTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        otherLeavePanel.add(scrollPane, BorderLayout.CENTER);
        leavePanel.add(otherLeavePanel);

        otherLeaveTextArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {updatePatientAndSave();}
            public void removeUpdate(DocumentEvent e) {updatePatientAndSave();}
            public void changedUpdate(DocumentEvent e) {updatePatientAndSave();}
        });

        return leavePanel;
    }

    private void updateEmergencyLeaveBorder() {
        if (emergencyLeaveCheckBox.isSelected()) {
            // Remove border when checked
            emergencyLabel.setBorder(null);
        } else {
            // Add red border when not checked
            emergencyLabel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.RED, 2),
                    BorderFactory.createEmptyBorder(1, 1, 1, 1)
                ));
        }
        emergencyPanel.revalidate();
        emergencyPanel.repaint();
    }
    
    private JPanel createTribunalPanel() {
        JPanel tribunalPanel = new JPanel();
        tribunalPanel.setLayout(new BoxLayout(tribunalPanel, BoxLayout.Y_AXIS));
        tribunalPanel.setBorder(BorderFactory.createTitledBorder("Tribunal information"));
        tribunalPanel.setPreferredSize(new Dimension(350, 120));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addTribunalBtn = new JButton("Add tribunal");
        JButton clearBtn = new JButton("Clear");
        buttonPanel.add(addTribunalBtn);
        buttonPanel.add(clearBtn);

        tribunalDisplayPanel = new JPanel();
        tribunalDisplayPanel.setLayout(new BoxLayout(tribunalDisplayPanel, BoxLayout.Y_AXIS));

        tribunalDateLabel = new JLabel("");
        tribunalTypeLabel = new JLabel("");
        reportDueLabel = new JLabel("");

        tribunalDisplayPanel.add(tribunalDateLabel);
        tribunalDisplayPanel.add(tribunalTypeLabel);
        tribunalDisplayPanel.add(reportDueLabel);
        
        addTribunalBtn.addActionListener(e -> showAddTribunalDialog());

        clearBtn.addActionListener(e -> {
            Patient currentPatient = tabDash.getCurrentPatient();
            currentPatient.setTribunalDate(null);
            currentPatient.setTribunalType(null);
            currentPatient.setReportDueDate(null);
            updateTribunalDisplay();
            updatePatientAndSave();
        });

        tribunalPanel.add(buttonPanel);
        tribunalPanel.add(tribunalDisplayPanel);
        return tribunalPanel;
    } 

    // HELPER METHODS
    
    private void enableMHAFunctionality(boolean enabled) {
        enableComponentsRecursively(middleSection, enabled);
        enableComponentsRecursively(bottomSection, enabled);
        if (enabled) {
            middleSection.setBackground(null);
            bottomSection.setBackground(null);
        } else {
            middleSection.setBackground(Color.LIGHT_GRAY);
            bottomSection.setBackground(Color.LIGHT_GRAY);
        }
        middleSection.repaint();
        bottomSection.repaint();
    }

    private void enableComponentsRecursively(Container container, boolean enabled) {
        container.setEnabled(enabled);
        for (Component component :container.getComponents()) {
            component.setEnabled(enabled);
            if (component instanceof Container) {
                enableComponentsRecursively((Container) component, enabled);
            }
        }
    }

    private void showMedicationReviewDialog() {
        String[] options = {"Review S62", "Review T2", "Review T3", "No action needed"};
        int choice = JOptionPane.showOptionDialog(
            this, 
            "Medication has changed. What needs to be reviewed?",
            "Medication Review",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, 
            options,
            options[0]
        );

        if (choice >= 0) {
            JOptionPane.showMessageDialog(this, "To do: " + options[choice]);
        }
    }


    private void hideAlert(JLabel icon, JLabel message, JButton yes, JButton no) {
        icon.setVisible(false);
        message.setVisible(false);
        yes.setVisible(false);
        no.setVisible(false);
    }


    private void showCapacityChangeMh03Alert() { // shows alert to redo MH03 when capacity changed
        JOptionPane.showConfirmDialog(
            this,
            "Capacity status changed. Please redo MH03 form.", 
            "Capacity changed",
            JOptionPane.OK_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
    }


    private void updateDateCalculations() {
        // Do not update if MHO3 is not completed
        if (!mh03CheckBox.isSelected()) {
            return;
        }

        if (detentionDateField.getValue() == null) {
            threeMthExpiry = null;
            return;
        }
        
        // Check if patient is currently detained
        if (!section2Btn.isSelected() && !section3Btn.isSelected()) {
            clearExpiryDisplays();
            threeMthExpiry = null;
            return;
        }
        Date detentionDate = (Date) detentionDateField.getValue();

        //Store original detention date (first time detention date is set)
        if (originalDetentionDate == null) {
            originalDetentionDate = detentionDate;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(detentionDate);

        Date sectionExpiry; // Calculate seciton expiry based on section type
        if (section2Btn.isSelected()) {
            cal.add(Calendar.DAY_OF_MONTH, 28);
            sectionExpiry = cal.getTime();
        } else if (section3Btn.isSelected()) {
            cal.add(Calendar.MONTH, 6);
            sectionExpiry = cal.getTime();
        } else {
            return;
        }

        cal.setTime(originalDetentionDate != null ? originalDetentionDate : detentionDate); // Calculate 3 month-rule expiries
        cal.add(Calendar.MONTH, 3);
        threeMthExpiry = cal.getTime();

        updateExpiryDisplays(sectionExpiry, threeMthExpiry);
        updateS62AlertVisibility();
    }


    private void updateExpiryDisplays(Date sectionExpiry, Date threeMthExpiry) {
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();

        //Calculate days remaining for each deadline
        long sectionDaysRemaining = (sectionExpiry.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
        long threeMthDaysRemaining = (threeMthExpiry.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);

        //Update section expiry display
        sectionExpiryLabel.setText(" " + displayFormat.format(sectionExpiry) + " (in " + sectionDaysRemaining + " days)");
        setSectionExpiryTrafficLight((int)sectionDaysRemaining);

        // Update 3-mth rule display 
        threeMonthLabel.setText(" " + displayFormat.format(threeMthExpiry) + " (in " + threeMthDaysRemaining + " days)");
        setThreeMthTrafficLight((int)threeMthDaysRemaining);
    
        // Show/hide S62 panel based on 3 mth rule
        if (threeMthDaysRemaining <= 0 && s62Panel != null) {
            s62Panel.setVisible(true);
        }
    }

    private void setThreeMthTrafficLight(int daysRemaining) {
        if (daysRemaining > 42) {
            threeMonthLabel.setBackground(new Color(144, 238, 144));
            threeMonthLabel.setForeground(Color.BLACK);
        } else if (daysRemaining >= 30) {
            threeMonthLabel.setBackground(Color.YELLOW);
            threeMonthLabel.setForeground(Color.BLACK);
        } else {
            threeMonthLabel.setBackground(Color.RED);
            threeMonthLabel.setForeground(Color.WHITE);
        }
    }
    
    private void setSectionExpiryTrafficLight(int daysRemaining) {
        if (daysRemaining > 14) {
            sectionExpiryLabel.setBackground(new Color(144, 238, 144));
            sectionExpiryLabel.setForeground(Color.BLACK);
        } else if (daysRemaining >= 7) {
            sectionExpiryLabel.setBackground(Color.YELLOW);
            sectionExpiryLabel.setForeground(Color.BLACK);
        } else {
            sectionExpiryLabel.setBackground(Color.RED);
            sectionExpiryLabel.setForeground(Color.WHITE);
        }
    }


    private void delayUpdateDateCalc() {
        SwingUtilities.invokeLater(() -> {
            try { // try to parse the current text as a date
                if (detentionDateField.getText() != null && !detentionDateField.getText().trim().isEmpty()) {
                    // Force the field to commit its current value
                    detentionDateField.commitEdit();
                    updateDateCalculations();
                }
            } catch (Exception e) {
                // Ignore parsing errors while date still being typed
            }
        });
    }

    
    private void clearExpiryDisplays() {
        sectionExpiryLabel.setText("");
        sectionExpiryLabel.setBackground(null);
        threeMonthLabel.setText("");
        threeMonthLabel.setBackground(null);

        if (s62Panel != null) {
            s62Panel.setVisible(false);
        }
    }


    public void refreshForNewPatient() {
        clearAllFields();

        // Load the new patient's MHA data from file
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            enableMHAFunctionality(false);
            return;
        }
        MHADataManager.loadPatientMHAdata(currentPatient);
        
        // Populate GUI fields with loaded data
        populateFieldsFromPatient(currentPatient);
    }


    private void clearAllFields() {
        autoSaveEnabled = false;
        // Reset top section
        mh03CheckBox.setSelected(false);

        // Reset status to informal(default)
        informalBtn.setSelected(true);
        section2Btn.setSelected(false);
        section3Btn.setSelected(false);
        dolsBtn.setSelected(false);
        
        // Reset and hide detention date
        detentionDateField.setValue(new Date());
        detentionDateField.setVisible(false);
        originalDetentionDate = null;

        // Clear expiry displays
        clearExpiryDisplays();

        // Reset capacity to "No" (default)
        noCapacityBtn.setSelected(true);
        yesCapacityBtn.setSelected(false);
        CardLayout cardLayout = (CardLayout) pathwayPanel.getLayout();
        cardLayout.show(pathwayPanel, "NO_CAPACITY");

        // Clear SOAD pathway fields
        soadRequestedChk.setSelected(false);
        soadDateField.setValue(null);
        soadRefField.setText("");

        // Clear S62 fields
        s62CompletedChk.setSelected(false);
        s62DateField.setValue(null);
        s62Panel.setVisible(false);
        if (s62AlertLabel != null) {
            s62AlertLabel.setVisible(false);
        }

        // Clear T3 fields
        t3CheckBox.setSelected(false);
        t3DateField.setValue(null);
        t3ReviewDateField.setValue(null);

        // Clear T2 fields
        t2CheckBox.setSelected(false);
        t2DateField.setValue(null);
        t2ReviewDateField.setValue(null);

        // Show SOAD and hide S62 (default state)
        soadPanel.setVisible(true);

        emergencyLeaveCheckBox.setSelected(false);
        updateEmergencyLeaveBorder();
        otherLeaveTextArea.setText("");

        enableMHAFunctionality(false);

        autoSaveEnabled = true;
    }


    // Method to read from Patient and populate GUI components
    private void populateFieldsFromPatient(Patient patient) {
        autoSaveEnabled = false;
        // Set MH03 checkbox
        mh03CheckBox.setSelected(patient.isMh03Completed());

        // Set admission date
        if (patient.getAdmissionDate() != null) {
            admissionDateField.setValue(patient.getAdmissionDate());
        } else {
            admissionDateField.setValue(new Date());
        }
                
        // Set section status radio buttons
        String sectionStatus = patient.getSectionStatus();
        informalBtn.setSelected(sectionStatus.equals("Informal"));
        section2Btn.setSelected(sectionStatus.equals("Section2"));
        section3Btn.setSelected(sectionStatus.equals("Section3"));
        dolsBtn.setSelected(sectionStatus.equals("DOLS"));

        // Set detention date if exists
        if (patient.getDetentionDate() != null) {
            detentionDateField.setValue(patient.getDetentionDate());
            // Show detention field if patient is detained
            if (sectionStatus.equals("Section2") || sectionStatus.equals("Section3")) {
                detentionDateField.setVisible(true);
            }
        }
        originalDetentionDate = patient.getOriginalDetentionDate();
        
        // Set capacity radio buttons
        String capacity = patient.getCapacity();
        noCapacityBtn.setSelected(capacity.equals("No"));
        yesCapacityBtn.setSelected(capacity.equals("Yes"));

            
        CardLayout cardLayout = (CardLayout) pathwayPanel.getLayout();
        if (capacity.equals("No")) {
            cardLayout.show(pathwayPanel, "NO_CAPACITY");

            // Populate SOAD pathway fields
            soadRequestedChk.setSelected((patient.isSoadRequested()));
            if(patient.getSoadDate() != null) {
                soadDateField.setValue(patient.getSoadDate());
            }
            if(patient.getSoadReference() != null) {
                soadRefField.setText(patient.getSoadReference());
            } 
            
            
            // Populate S62 fields
            s62CompletedChk.setSelected(patient.isS62Completed());
            if (patient.getS62Date() != null) {
                s62DateField.setValue(patient.getS62Date());
            }

            // Populate T3 fields
            t3CheckBox.setSelected(patient.isT3Provided());
            if (patient.getT3Date() != null) {
                t3DateField.setValue(patient.getT3Date());
            }
            if (patient.getT3ReviewDate() != null) {
                t3ReviewDateField.setValue(patient.getT3ReviewDate());
            }

            //  Handle visibility base on T3 checkbox state
            if (t3CheckBox.isSelected()) {
                soadPanel.setVisible(false);
                s62Panel.setVisible(false);
            } else {
                soadPanel.setVisible(true);
            }

        } else {
            cardLayout.show(pathwayPanel, "HAS_CAPACITY");

            // Populate T2 pathway fields
            t2CheckBox.setSelected(patient.isT2Completed());
            if (patient.getT2Date() != null) {
                t2DateField.setValue(patient.getT2Date());
            }
            if (patient.getT2ReviewDate() != null) {
                t2ReviewDateField.setValue(patient.getT2ReviewDate());
            }
        }

        // Update displays and calculations
        updateDateCalculations();
        enableMHAFunctionality(patient.isMh03Completed());
        updateDetentionFieldVisibility();
        updateTribunalDisplay();

        emergencyLeaveCheckBox.setSelected(patient.isEmergencyMedicalLeave());
        SwingUtilities.invokeLater(() -> {
            updateEmergencyLeaveBorder();
        });
        if (patient.getOtherLeave() != null) {
            otherLeaveTextArea.setText(patient.getOtherLeave());
        }

        SwingUtilities.invokeLater(() -> {
            updateS62AlertVisibility();
        });

        autoSaveEnabled = true;
    }


    private void saveCurrentPatientMHA() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null) {
            MHADataManager.savePatientMHA(currentPatient);
        }
    }


    private void updatePatientAndSave() {
        if (!autoSaveEnabled) return;
            
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) return;
            
        // Update the Patient object with current GUI values, then save
        updatePatientFromFields();
        saveCurrentPatientMHA();
    }


    private void updatePatientFromFields() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) return;

        // Update Patient object with current GUI values
        currentPatient.setMh03Completed(mh03CheckBox.isSelected());
        currentPatient.setSectionStatus(getCurrentSectionStatus());

        // Handle admission date
        if (admissionDateField.getValue() instanceof Date) {
            currentPatient.setAdmissionDate((Date) admissionDateField.getValue());
        } else if (admissionDateField.getText().trim().isEmpty()) {
            currentPatient.setAdmissionDate(null);
        }

        // Handle detention date
        if (detentionDateField.getValue() instanceof Date) {
            currentPatient.setDetentionDate((Date) detentionDateField.getValue());
        } else if (detentionDateField.getText().trim().isEmpty()) {
            currentPatient.setDetentionDate(null);
        }

        currentPatient.setCapacity(noCapacityBtn.isSelected() ? "No" : "Yes");

        // Update pathway-specific fields
        currentPatient.setSoadRequested(soadRequestedChk.isSelected());

        // Handle SOAD date
        if (soadDateField.getValue() instanceof Date) {
            currentPatient.setSoadDate((Date) soadDateField.getValue());
        } else if (soadDateField.getText().trim().isEmpty()){
            currentPatient.setSoadDate(null);
        }
        
        currentPatient.setSoadReference(soadRefField.getText().isEmpty() ? null :soadRefField.getText());

        currentPatient.sets62Completed(s62CompletedChk.isSelected());

        //Handle S62 date
        if (s62DateField.getValue() instanceof Date) {
            currentPatient.setS62Date((Date) s62DateField.getValue());
        } else if (s62DateField.getText().trim().isEmpty()) {
            currentPatient.setS62Date(null);
        }

        currentPatient.setT3Provided(t3CheckBox.isSelected());
        
        // Handle T3 date - check if field is empty
        if (t3DateField.getValue() instanceof Date) {
            currentPatient.setT3Date((Date) t3DateField.getValue());
        } else if (t3DateField.getText().trim().isEmpty()) {
            currentPatient.setT3Date(null);
        }

        //Handle T3 review date
        if (t3ReviewDateField.getValue() instanceof Date) {
            currentPatient.setT3ReviewDate((Date) t3ReviewDateField.getValue());
        } else if (t3ReviewDateField.getText().trim().isEmpty()) {
            currentPatient.setT3ReviewDate(null);
        }

        currentPatient.setT2Completed(t2CheckBox.isSelected());

        // Handle T2 date
        if (t2DateField.getValue() instanceof Date) {
            currentPatient.setT2Date((Date) t2DateField.getValue());
        } else if (t2DateField.getText().trim().isEmpty()) {
            currentPatient.setT2Date(null);
        }

        //Handle T2 review date
        if (t2ReviewDateField.getValue() instanceof Date) {
            currentPatient.setT2ReviewDate((Date) t2ReviewDateField.getValue());
        } else if (t2ReviewDateField.getText().trim().isEmpty()) {
            currentPatient.setT2ReviewDate(null);
        }

        currentPatient.setEmergencyMedicalLeave(emergencyLeaveCheckBox.isSelected());
        currentPatient.setOtherLeave(otherLeaveTextArea.getText().trim().isEmpty() ? null : otherLeaveTextArea.getText());
    }

    
    private void addDateFieldClearingListener(JFormattedTextField dateField) {
        dateField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (dateField.getText().trim().isEmpty()) {
                    dateField.setValue(null);
                    updatePatientAndSave();
                }
            }
        });
    }


    private String getCurrentSectionStatus() {
        if (informalBtn.isSelected()) return "Informal";
        if (section2Btn.isSelected()) return "Section2";
        if (section3Btn.isSelected()) return "Section3";
        if (dolsBtn.isSelected()) return "DOLS";
        return "Informal";
    }


    public void checkMedicationChangeAlert(String medicationName) {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null || !mh03CheckBox.isSelected()) {
            return;
        }

        // Only trigger if patient is on Section 2 or 3
        if (!section2Btn.isSelected() && !section3Btn.isSelected()) {
            return;
        }


        // Only trigger for specific medication classes
        if (isControlledMedicationClass(medicationName)) {
            showAlert();
        }
    }

    private boolean isControlledMedicationClass(String medicationName) {
        return medicationLookupService.isPsychotropicMedication(medicationName);
    }

    private void showAlert() {
        alertIcon.setVisible(true);
        alertMessage.setVisible(true);
        alertYesBtn.setVisible(true);
        alertNoBtn.setVisible(true);
        
        // Make the panel repaint to show the alert
        this.revalidate();
        this.repaint();
    }

    private void hideAlert() {
        alertIcon.setVisible(false);
        alertMessage.setVisible(false);
        alertYesBtn.setVisible(false);
        alertNoBtn.setVisible(false);
        this.revalidate();
        this.repaint();
    }


    private void showAddTribunalDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Tribunal", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Tribunal date
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        dialog.add(new JLabel("Tribunal Date:"), gbc);

        JFormattedTextField tribunalDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        tribunalDateField.setColumns(10);
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(tribunalDateField, gbc);

        // Tribunal type
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Tribunal Type:"), gbc);
        
        String[] tribunalTypes = {"S3", "S2", "Hospital Managers hearing"};
        JComboBox<String> tribunalTypeComboBox = new JComboBox<>(tribunalTypes);
        gbc.gridx = 1; gbc.gridy = 1;
        dialog.add(tribunalTypeComboBox, gbc);

        // Report due date 
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Report Due date:"), gbc);

        JFormattedTextField reportDueDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        reportDueDateField.setColumns(10);
        gbc.gridx = 1; gbc.gridy = 2;
        dialog.add(reportDueDateField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        okBtn.addActionListener(e -> {
            try {
                Date tribunalDate = (Date) tribunalDateField.getValue();
                String tribunalType = (String) tribunalTypeComboBox.getSelectedItem();
                Date reportDueDate = (Date) reportDueDateField.getValue();

                System.out.println("=== DIALOG VALUES ===");
                System.out.println("Tribunal date field value: " + tribunalDate);
                System.out.println("Report due date field value: " + reportDueDate);
                System.out.println("Are they the same object? " + (tribunalDate == reportDueDate));

                if (tribunalDate == null || tribunalType == null || reportDueDate == null) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields", "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return;               
                }
                Patient currentPatient = tabDash.getCurrentPatient();
                currentPatient.setTribunalDate(tribunalDate);
                currentPatient.setTribunalType(tribunalType);
                currentPatient.setReportDueDate(reportDueDate);

                
                updateTribunalDisplay();
                updatePatientAndSave();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid dates", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private void updateTribunalDisplay() {
        Patient currentPatient = tabDash.getCurrentPatient();
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (currentPatient.getTribunalDate() != null) {
            tribunalDateLabel.setText("Tribunal Date: " + displayDateFormat.format(currentPatient.getTribunalDate()));
            tribunalTypeLabel.setText("Type: " + currentPatient.getTribunalType());
            reportDueLabel.setText("Report due: " + displayDateFormat.format(currentPatient.getReportDueDate()));
        } else {
            tribunalDateLabel.setText("");
            tribunalTypeLabel.setText("");
            reportDueLabel.setText("");
        }
    }


    private void updateDetentionFieldVisibility() {
        boolean shouldShowDetentionFields = section2Btn.isSelected() || section3Btn.isSelected();

        detentionDateLbl.setVisible(shouldShowDetentionFields);
        detentionDateField.setVisible(shouldShowDetentionFields);

        if (!shouldShowDetentionFields) {
            clearExpiryDisplays();
            originalDetentionDate = null;
        }

        topPanel.revalidate();
        topPanel.repaint();
    }
}
package src;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.text.SimpleDateFormat;

public class MHAPanel extends JPanel{
    private TabDash tabDash;
    private JRadioButton noCapacityBtn;
    private JRadioButton yesCapacityBtn;
    private JPanel pathwayPanel;
    private JCheckBox t3CheckBox;
    private JPanel s62Panel;
    private JPanel soadPanel;
    private JCheckBox mh03CheckBox;
    private JPanel middleSection;
    private JPanel bottomSection;

    public MHAPanel(TabDash tabDash) {
        this.tabDash = tabDash;
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
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        TitledBorder topBorder = BorderFactory.createTitledBorder("Patient Status");
        topBorder.setTitleJustification(TitledBorder.CENTER);
        topPanel.setBorder(topBorder);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        topPanel.add(new JLabel("Admission date: "));
        JFormattedTextField admissionDateField = new JFormattedTextField(dateFormat);
        admissionDateField.setColumns(8);
        admissionDateField.setValue(new java.util.Date());
        topPanel.add(admissionDateField);

        topPanel.add(Box.createHorizontalStrut(20));

        mh03CheckBox = new JCheckBox("MH03 completed");
        topPanel.add(mh03CheckBox);

        topPanel.add(Box.createHorizontalStrut(20));

        topPanel.add(new JLabel("Status:"));

        ButtonGroup statusGroup = new ButtonGroup();
        JRadioButton informalBtn = new JRadioButton("Informal");
        JRadioButton section2Btn = new JRadioButton("Section 2");
        JRadioButton section3Btn = new JRadioButton("Section 3");
        JRadioButton dolsBtn = new JRadioButton("DOLS");

        statusGroup.add(informalBtn);
        statusGroup.add(section2Btn);
        statusGroup.add(section3Btn);
        statusGroup.add(dolsBtn);

        topPanel.add(informalBtn);
        topPanel.add(section2Btn);
        topPanel.add(section3Btn);
        topPanel.add(dolsBtn);

        topPanel.add(Box.createHorizontalStrut(20));

        JFormattedTextField detentionDateField = new JFormattedTextField(dateFormat);
        detentionDateField.setColumns(8);
        detentionDateField.setValue(new java.util.Date());
        detentionDateField.setVisible(false);
        topPanel.add(detentionDateField);

        section2Btn.addActionListener(e -> {
            detentionDateField.setVisible(true);
            topPanel.revalidate();
            topPanel.repaint();
        });
        section3Btn.addActionListener(e -> {
            detentionDateField.setVisible(true);
            topPanel.revalidate();
            topPanel.repaint();
        });
        informalBtn.addActionListener(e -> {
            detentionDateField.setVisible(false);
            topPanel.revalidate();
            topPanel.repaint();
        });
        dolsBtn.addActionListener(e -> { 
            detentionDateField.setVisible(false);
            topPanel.revalidate();
            topPanel.repaint();
        });

        informalBtn.setSelected(true);

        mh03CheckBox.addActionListener(e -> {
            boolean mh03Completed = mh03CheckBox.isSelected();
            enableMHAFunctionality(mh03Completed);
        });
        
        return topPanel;
    }

    // MIDDLE SECTION

    private JPanel createMiddleSection() {
        JPanel middlePanel = new JPanel();
        TitledBorder middleBorder = BorderFactory.createTitledBorder("MHA Management");
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
        JLabel sectionTrafficLight = new JLabel("🟡");
        expiryPanel.add(sectionTrafficLight);
        JLabel sectionExpiryLabel = new JLabel("25/01/2025 (28 days)");
        expiryPanel.add(sectionExpiryLabel);
        expiryPanel.add(Box.createHorizontalStrut(30));
        // 3 month rule with traffic light
        expiryPanel.add(new JLabel("Consent to treatment (3 month rule): "));
        JLabel threeMthTrafficLight = new JLabel("🔴");
        expiryPanel.add(threeMthTrafficLight);
        JLabel threeMonthLabel = new JLabel("15 days remaining");
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
        });

        yesCapacityBtn.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) pathwayPanel.getLayout();
            cardLayout.show(pathwayPanel, "HAS_CAPACITY");
            showCapacityChangeMh03Alert();
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
        JCheckBox soadRequestedChk = new JCheckBox("SOAD requested");
        soadPanel.add(soadRequestedChk);
        soadPanel.add(new JLabel("Date sent"));
        JFormattedTextField soadDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        soadDateField.setColumns(8);
        soadPanel.add(soadDateField);
        soadPanel.add(new JLabel("Reference: "));
        JTextField soadRefField = new JTextField(10);
        soadPanel.add(soadRefField);
        panel.add(soadPanel);

        // S62 section(will appear after 3 months)
        s62Panel = new JPanel((new FlowLayout(FlowLayout.LEFT)));
        JCheckBox s62CompletedChk = new JCheckBox("S62 completed");
        s62Panel.add(s62CompletedChk);
        s62Panel.add(new JLabel("Date: "));
        JFormattedTextField s62DateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        s62DateField.setColumns(8);
        s62Panel.add(s62DateField);
        s62Panel.setVisible(false);
        panel.add(s62Panel);
        // T3 section
        JPanel t3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        t3CheckBox = new JCheckBox("T3 Provided");
        t3Panel.add(t3CheckBox);
        t3Panel.add(new JLabel("Date: "));
        JFormattedTextField t3DateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t3DateField.setColumns(8);
        t3Panel.add(t3DateField);
        t3Panel.add(new JLabel("Review due: "));
        JFormattedTextField t3ReviewDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t3ReviewDateField.setColumns(8);
        t3Panel.add(t3ReviewDateField);
        panel.add(t3Panel);

        t3CheckBox.addActionListener(e -> {
            boolean t3Selected = t3CheckBox.isSelected();

            if (t3Selected) {
                soadPanel.setVisible(false);
                s62Panel.setVisible(false);
            } else {
                soadPanel.setVisible(true);
                s62Panel.setVisible(false);
            }
            panel.revalidate();
            panel.repaint();
        });

        return panel;
    }

    private JPanel createHasCapacityPathway() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("T2 Pathway (has capacity)"));
        JPanel t2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox t2CheckBox = new JCheckBox("T2 completed");
        t2Panel.add(t2CheckBox);
        t2Panel.add(new JLabel("Date: "));
        JFormattedTextField t2DateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t2DateField.setColumns(8);
        t2Panel.add(t2DateField);
        t2Panel.add(new JLabel("Review date: "));
        JFormattedTextField t2ReviewDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        t2ReviewDateField.setColumns(8);
        t2Panel.add(t2ReviewDateField);
        panel.add(t2Panel);
        return panel;
    }

    private JPanel createAlertPanel() {
        JPanel alertPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alertPanel.setBorder(BorderFactory.createTitledBorder("Alerts"));
        //Medication change alert - intitially hidden
        JLabel alertIcon = new JLabel("❗");
        JLabel alertMessage = new JLabel("Medication changed- review required");
        JButton yesBtn = new JButton("Yes");
        JButton noBtn = new JButton("No");
        // Set small button size
        yesBtn.setPreferredSize(new Dimension(50, 25));
        noBtn.setPreferredSize(new Dimension(50,25));;
        alertPanel.add(alertIcon);
        alertPanel.add(alertMessage);
        alertPanel.add(Box.createHorizontalStrut(10));
        alertPanel.add(yesBtn);
        alertPanel.add(noBtn);

        alertIcon.setVisible(false);
        alertMessage.setVisible(false);
        yesBtn.setVisible(false);
        noBtn.setVisible(false);

        yesBtn.addActionListener(e -> {
            showMedicationReviewDialog();
            hideAlert(alertIcon, alertMessage, yesBtn, noBtn);
        });

        noBtn.addActionListener(e -> {
            hideAlert(alertIcon, alertMessage, yesBtn, noBtn);
        });

        return alertPanel;
    }

    private JPanel createBottomSection() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        TitledBorder bottomBorder = BorderFactory.createTitledBorder("Leave & Tribunal Management");
        bottomBorder.setTitleJustification(TitledBorder.CENTER);
        bottomPanel.setBorder(bottomBorder);
        JPanel leavePanel = createLeavePanel();
        JPanel tribunalPanel = createTribunalPanel();
        bottomPanel.add(leavePanel, BorderLayout.WEST);
        bottomPanel.add(tribunalPanel, BorderLayout.EAST);
        return bottomPanel;
    }

    private JPanel createLeavePanel() {
        JPanel leavePanel = new JPanel();
        leavePanel.setLayout(new BoxLayout(leavePanel, BoxLayout.Y_AXIS));
        leavePanel.setBorder(BorderFactory.createTitledBorder("Leave Records"));
        leavePanel.setPreferredSize(new Dimension(300, 120));

        JLabel placeholder = new JLabel("Leave records will go here");
        leavePanel.add(placeholder);
        return leavePanel;
    }

    private JPanel createTribunalPanel() {
        JPanel tribunalPanel = new JPanel();
        tribunalPanel.setLayout(new BoxLayout(tribunalPanel, BoxLayout.Y_AXIS));
        tribunalPanel.setBorder(BorderFactory.createTitledBorder("Tribunal information"));
        tribunalPanel.setPreferredSize(new Dimension(350, 120));
        JLabel placeholder = new JLabel("Tribunal information will go here");
        tribunalPanel.add(placeholder);
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
            JOptionPane.showMessageDialog(this, "You selected: " + options[choice]);
        }
    }


        private void hideAlert(JLabel icon, JLabel message, JButton yes, JButton no) {
        icon.setVisible(false);
        message.setVisible(false);
        yes.setVisible(false);
        no.setVisible(false);
    }


    private void showCapacityChangeMh03Alert() { // shows alert to redo MH03 when capacity changed
        int result = JOptionPane.showConfirmDialog(
            this,
            "Capacity status changed. Please redo MH03 form.\n\nDo you want to update MH03 now?", 
            "Capacity changed",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                this,
                "Please complete MH03 form again.",
                "MH03 Reset",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private String getThreeMthTrafficLightIcon(int daysRemaining) {
        if (daysRemaining > 42) {
            return "🟢";
        } else if (daysRemaining >= 30) {
            return "🟡";
        } else {
            return "🔴";
        }
    }
}


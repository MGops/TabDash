package src;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.Flow;

public class MHAPanel extends JPanel{
    private TabDash tabDash;

    public MHAPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BorderLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        JPanel topSection = createTopSection();
        JPanel middleSection = createMiddleSection();
        JPanel bottomSection = createBottomSection();

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));

        mainContainer.add(topSection);
        mainContainer.add(createSeparator());
        mainContainer.add(middleSection);
        mainContainer.add(createSeparator());
        mainContainer.add(bottomSection);
    
        add(mainContainer, BorderLayout.CENTER);
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
        topPanel.setBorder(BorderFactory.createTitledBorder("Patient Status"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        topPanel.add(new JLabel("Admission date: "));
        JFormattedTextField admissionDateField = new JFormattedTextField(dateFormat);
        admissionDateField.setColumns(8);
        admissionDateField.setValue(new java.util.Date());
        topPanel.add(admissionDateField);

        topPanel.add(Box.createHorizontalStrut(20));

        JCheckBox mh03CheckBox = new JCheckBox("MH03 completed");
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

        return topPanel;
    }

    // MIDDLE SECTION

    private JPanel createMiddleSection() {
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBorder(BorderFactory.createTitledBorder("MHA Management"));

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
        JRadioButton noCapacityBtn = new JRadioButton("No");
        JRadioButton yesCapacityBtn = new JRadioButton("Yes");
        capacityGroup.add(noCapacityBtn);
        capacityGroup.add(yesCapacityBtn);
        capacityPanel.add(noCapacityBtn);
        capacityPanel.add(yesCapacityBtn);
        noCapacityBtn.setSelected(true);
        return capacityPanel;
    }

    private JPanel createPathwayPanel() {
        JPanel pathwayPanel = new JPanel();
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
        return panel;
    }

    private JPanel createHasCapacityPathway() {
        JPanel panel = new JPanel();
        return panel;
    }

    private JPanel createAlertPanel() {
        return new JPanel();
    }

    private JPanel createBottomSection() {
        return new JPanel();
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


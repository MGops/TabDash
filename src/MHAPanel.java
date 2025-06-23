package src;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

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

    private JPanel createTopSection() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("Patient Status"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        topPanel.add(new JLabel("Admission date: "));
        JFormattedTextField admissionDateField = new JFormattedTextField(dateFormat);
        admissionDateField.setColumns(10);
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
        detentionDateField.setColumns(10);
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

    private JPanel createMiddleSection() {
            return new JPanel();
        }

    private JPanel createBottomSection() {
            return new JPanel();
        }
}


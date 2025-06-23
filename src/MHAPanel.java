package src;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

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

        topPanel.add(new JLabel("Admission date: "));
        JTextField admissionDateField = new JTextField(10);
        topPanel.add(admissionDateField);

        topPanel.add(Box.createHorizontalStrut(20));

        topPanel.add(new JLabel("Status"));

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

        informalBtn.setSelected(true);

        topPanel.add(Box.createHorizontalStrut(30));

        return topPanel;
    }

    private JPanel createMiddleSection() {
            return new JPanel();
        }

    private JPanel createBottomSection() {
            return new JPanel();
        }
}

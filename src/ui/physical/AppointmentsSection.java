package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTarget;

import javax.swing.*;

import src.ui.TabDash;

public class AppointmentsSection extends JPanel {
    private TabDash tabDash;
    private JPanel toReferPanel;
    private JPanel referredPanel;
    private JPanel scheduledPanel;
    private JPanel attendedPanel;
    private JPanel missedPanel;

    public AppointmentsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Appointments"));
        setLayout(new GridBagLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create columns
        toReferPanel = createColumn("To Refer");
        referredPanel = createColumn("Referred");
        scheduledPanel = createColumn("Scheduled");
        // Create split panels for attended & missed
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.fill = GridBagConstraints.BOTH;
        rightGbc.weightx = 1.0;
        rightGbc.insets = new Insets(2, 2, 2, 2);

        attendedPanel = createColumn("Attended");
        missedPanel = createColumn("Missed");

        rightGbc.gridy = 0; rightGbc.weighty = 0.5;
        rightPanel.add(attendedPanel, rightGbc);
        rightGbc.gridy = 1; rightGbc.weighty = 0.5;
        rightPanel.add(missedPanel, rightGbc);

        gbc.gridx = 0; gbc.weightx = 0.25;
        add(toReferPanel, gbc);
        gbc.gridx = 1; gbc.weightx = 0.25;
        add(referredPanel, gbc);
        gbc.gridx = 2; gbc.weightx = 0.25;
        add(scheduledPanel, gbc);
        gbc.gridx = 3; gbc.weightx = 0.25;
        add(rightPanel, gbc);
    }

    private JPanel createColumn(String title) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBorder(BorderFactory.createTitledBorder(title));
        column.setMinimumSize(new Dimension(150, 200));

        //new DropTarget(column, new AppointmentDropTargetListener(column));
        return column;
    }


    
}

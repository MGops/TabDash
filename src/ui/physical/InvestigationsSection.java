package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import src.ui.TabDash;

public class InvestigationsSection extends JPanel {
    private TabDash tabDash;
    private JPanel activeIssuesPanel;

    public InvestigationsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Monitoring"));
        setLayout(new BorderLayout());

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

    public void showEditIssueDialog() {
        JOptionPane.showMessageDialog(this, "Dialog next");
    }


    private void loadActiveIssues() {

    }


    private void refreshForNewPatient() {
        loadActiveIssues();
    }
}

package src.ui.physical;

import java.awt.BorderLayout;
import javax.swing.*;
import src.ui.TabDash;

public class InvestigationsSection extends JPanel {
    private TabDash tabDash;

    public InvestigationsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Monitoring"));

        JPanel issuePanel = new JPanel(new BoxLayout(getFocusCycleRootAncestor(), ABORT));
        this.add(issuePanel, BorderLayout.CENTER);
        JButton addBtn = new JButton("Add issue to monitor");
        this.add(addBtn, BorderLayout.SOUTH);
    }
}

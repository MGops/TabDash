package src.ui.physical;

import javax.swing.JLabel;
import javax.swing.JPanel;

import src.ui.TabDash;

public class InvestigationsSection extends JPanel {
    private TabDash tabDash;

    public InvestigationsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        add(new JLabel("Investigations"));
    }
}

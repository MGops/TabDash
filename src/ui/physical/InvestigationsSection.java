package src.ui.physical;

import javax.swing.*;
import src.ui.TabDash;

public class InvestigationsSection extends JPanel {
    private TabDash tabDash;

    public InvestigationsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Investigations"));
    }
}

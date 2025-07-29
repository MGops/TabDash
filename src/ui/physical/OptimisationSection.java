package src.ui.physical;

import javax.swing.JLabel;
import javax.swing.JPanel;

import src.ui.TabDash;

public class OptimisationSection extends JPanel {
    private TabDash tabDash;

    public OptimisationSection(TabDash tabDash) {
        this.tabDash = tabDash;
        add(new JLabel("Optimisation"));
    }
}
package src.ui.physical;

import javax.swing.JLabel;
import javax.swing.JPanel;

import src.ui.TabDash;

public class AppointmentsSection extends JPanel {
    private TabDash tabDash;

    public AppointmentsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        add(new JLabel("Appointments"));
    }
    
}

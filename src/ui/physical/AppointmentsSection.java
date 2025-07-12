package src.ui.physical;

import java.awt.BorderLayout;

import javax.swing.*;

import src.ui.TabDash;

public class AppointmentsSection extends JPanel {
    private TabDash tabDash;

    public AppointmentsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Appointments"));
        setLayout(new BorderLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        
    }


    
}

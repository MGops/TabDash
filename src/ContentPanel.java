package src;

import java.awt.*;
import javax.swing.*;

public class ContentPanel extends JPanel{
    
    public ContentPanel(MedicationDatabase medDatabase, TabDash tabDash, MedicationPanel medicationPanel) {
        setLayout(new BorderLayout());
        //setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,50,5));

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Physical health", new PhysicalHealthPanel());
        tabbedPane.add("Medication", medicationPanel);
        tabbedPane.add("MHA", new MHAPanel());

        add(tabbedPane);
    }
}

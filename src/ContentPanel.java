package src;

import java.awt.*;
import javax.swing.*;

public class ContentPanel extends JPanel{
    
    public ContentPanel(
        MedicationDatabase medDatabase, 
        TabDash tabDash, 
        MedicationPanel medicationPanel, 
        MHAPanel mhaPanel) {

        setLayout(new BorderLayout());
        //setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,50,5));

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("MHA", mhaPanel);
        tabbedPane.add("Physical health", new PhysicalHealthPanel());
        tabbedPane.add("Medication", medicationPanel);
        tabbedPane.add("Dementia", new DementiaTab());

        add(tabbedPane);
    }
}

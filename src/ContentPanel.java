package src;

import java.awt.*;
import javax.swing.*;

public class ContentPanel extends JPanel{
    private MHAPanel mhaPanel;

    public ContentPanel(
        MedicationDatabase medDatabase, 
        TabDash tabDash, 
        MedicationPanel medicationPanel) {

        setLayout(new BorderLayout());
        //setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,50,5));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        mhaPanel = new MHAPanel(tabDash);
        tabbedPane.add("MHA", mhaPanel);
        tabbedPane.add("Physical health", new PhysicalHealthPanel());
        tabbedPane.add("Medication", medicationPanel);
        tabbedPane.add("Dementia", new DementiaTab());

        add(tabbedPane);
    }

    public MHAPanel getMHAPanel() {
        return mhaPanel;
    }
}

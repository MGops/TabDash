package src.ui.panels;

import java.awt.*;
import javax.swing.*;

import src.data_managers.MedicationDatabase;
import src.ui.TabDash;

public class ContentPanel extends JPanel{
    private MHAPanel mhaPanel;
    private PhysicalHealthPanel physicalHealthPanel;

    public ContentPanel(
        MedicationDatabase medDatabase, 
        TabDash tabDash, 
        MedicationPanel medicationPanel) {

        setLayout(new BorderLayout());
        //setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,50,5));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        mhaPanel = new MHAPanel(tabDash);
        physicalHealthPanel = new PhysicalHealthPanel(tabDash);
        tabbedPane.add("MHA", mhaPanel);
        tabbedPane.add("Physical health", physicalHealthPanel);
        tabbedPane.add("Medication", medicationPanel);
        tabbedPane.add("Dementia", new DementiaTab());
        tabbedPane.add("Notes", new NotesPanel());

        add(tabbedPane);
    }

    public MHAPanel getMHAPanel() {
        return mhaPanel;
    }

    public PhysicalHealthPanel getPhysicalHealthPanel() {
        return physicalHealthPanel;
    }

}

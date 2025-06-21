package src;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ContentPanel extends JPanel{
    private MedicationDatabase medDatabase;
    private JLabel totalAcbScore;
    private JTable medicationTable;
    private ArrayList<String> currentMedications;
    
    public ContentPanel(MedicationDatabase medDatabase) {
        setLayout(new BorderLayout());
        //setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,50,5));

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Physical health"));
        tabbedPane.add("Physical health", panel1);

        MedicationPanel medicationPanel = new MedicationPanel(medDatabase);
        tabbedPane.add("Medication", medicationPanel);
        

        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("MHA"));
        tabbedPane.add("MHA", panel3);

        add(tabbedPane);
    }

    private void updateTotalACB() {
        DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
        int total = 0;

        for (int row = 0; row < model.getRowCount(); row++) {
            Integer acbScore = (Integer) model.getValueAt(row, 1);
            total += acbScore;
        }

        totalAcbScore.setText("Total ACB Score: " + total);
    }
}

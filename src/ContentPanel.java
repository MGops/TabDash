package src;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

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

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        tabbedPane.add("Medication", panel2);

        String[] columnNames = {"#", "Medication", "ACB Score"};
        Object[][] data = {};
        medicationTable = new JTable(data, columnNames);
        JScrollPane medScrollPane = new JScrollPane(medicationTable);
        panel2.add(medScrollPane, BorderLayout.CENTER);        

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        panel2.add(buttonPanel, BorderLayout.SOUTH);
        totalAcbScore = new JLabel();
        updateTotalACB();

        JButton addMedBtn = new JButton("Add medication");
        addMedBtn.addActionListener(e -> {
            String[] medicationNames = medDatabase.getAllMedications().keySet().toArray(new String[0]);
            JComboBox<String> medComboBox = new JComboBox<>(medicationNames);
            medComboBox.setEditable(true);

            int result = JOptionPane.showConfirmDialog(
                this,
                medComboBox,
                "Select or enter medication",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                String selectedMed = (String) medComboBox.getSelectedItem();
                System.out.println("Selected: " + selectedMed);
            }
        });
        buttonPanel.add(addMedBtn);

        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(totalAcbScore);

        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("MHA"));
        tabbedPane.add("MHA", panel3);

        add(tabbedPane);
    }

    private void updateTotalACB() {
        totalAcbScore.setText("Total ACB Score: " + 0);
    }
}

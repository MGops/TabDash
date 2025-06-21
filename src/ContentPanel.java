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

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        tabbedPane.add("Medication", panel2);

        String[] columnNames = {"Medication", "ACB Score"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        medicationTable = new JTable(tableModel);
        medicationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicationTable.setRowSelectionAllowed(true);
        medicationTable.setFillsViewportHeight(true);
        medicationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int row = medicationTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        medicationTable.setRowSelectionInterval(row, row);
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem removeItem = new JMenuItem("Remove");
                        removeItem.addActionListener(ae -> {
                            DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
                            model.removeRow(row);
                            updateTotalACB();
                        });
                        popup.add(removeItem);
                        popup.show(medicationTable, e.getX(), e.getY());
                    }
                }
            }
        });
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
                if (selectedMed != null && !selectedMed.trim().isEmpty()) {
                    Integer acbScore = medDatabase.getACBScore(selectedMed);
                    if (acbScore == null) {
                        acbScore = 0;
                    }
                    DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
                    int rowNumber = model.getRowCount() + 1;
                    model.addRow(new Object[] {selectedMed, acbScore});
                    updateTotalACB();
                }
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
        DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
        int total = 0;

        for (int row = 0; row < model.getRowCount(); row++) {
            Integer acbScore = (Integer) model.getValueAt(row, 1);
            total += acbScore;
        }

        totalAcbScore.setText("Total ACB Score: " + total);
    }
}

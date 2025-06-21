package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class MedicationPanel extends JPanel {
    private MedicationDatabase medDatabase;
    private JLabel totalAcbScore;
    private JTable medicationTable;

    public MedicationPanel(MedicationDatabase medDatabase) {
        this.medDatabase = medDatabase;
        setLayout(new BorderLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        String[] columnNames = {"Medication", "ACB Score"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
        medScrollPane.setPreferredSize((new Dimension(300,300)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        totalAcbScore = new JLabel();
        updateTotalACB();

        JButton addMedBtn = new JButton("Add medication");
        addMedBtn.addActionListener(e -> {
            String[] medicationNames = medDatabase.getAllMedications().keySet().toArray(new String[0]);
            java.util.Arrays.sort(medicationNames);
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
                    DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
                    boolean alreadyExists = false;
                    for (int row = 0; row < model.getRowCount(); row++) {
                        String existingMed = (String) model.getValueAt(row, 0);
                        if (existingMed.equalsIgnoreCase(selectedMed)) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (alreadyExists) {
                        JOptionPane.showMessageDialog(this, 
                        "'" + selectedMed + "'" + "is already in the medication list.",
                        "Duplicate Medication",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                    }

                    Integer acbScore = medDatabase.getACBScore(selectedMed);
                    if (acbScore == null) {
                        acbScore = 0;
                    }
                    
                    int rowNumber = model.getRowCount() + 1;
                    model.addRow(new Object[] {selectedMed, acbScore});
                    updateTotalACB();
                }
            }
        });
        buttonPanel.add(addMedBtn);

        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(totalAcbScore);

        add(medScrollPane, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);

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

package src.medication;

import src.Medication;
import src.MedicationDatabase;
import src.TabDash;
import src.Patient;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

public class ACBSection extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;
    private JTable medicationTable;
    private JLabel totalAcbScore;

    public ACBSection(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("ACB Score"));
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

        JScrollPane medScrollPane = new JScrollPane(medicationTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addMedBtn = new JButton("Add medication");
        JButton removeMedBtn = new JButton("Remove medication");
        buttonPanel.add(addMedBtn);
        buttonPanel.add(removeMedBtn);

        totalAcbScore = new JLabel("Total ACB: 0");

        add(buttonPanel, BorderLayout.NORTH);
        add(medScrollPane, BorderLayout.CENTER);
        add(totalAcbScore, BorderLayout.SOUTH);

        loadPatientMedications();
    

        addMedBtn.addActionListener(e -> {
            // Dialog code for selecting medications
            String[] medicationNames = medDatabase.getAllMedications().keySet().toArray(new String[0]);
            java.util.Arrays.sort(medicationNames);
            JComboBox<String> medComboBox = new JComboBox<>(medicationNames);
            medComboBox.setEditable(true);
            int result = JOptionPane.showConfirmDialog(
                medicationTable,
                medComboBox,
                "Select or enter medication",
                JOptionPane.OK_CANCEL_OPTION
            );
            // Logic to add to table
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

                    // Logic to update patient data
                    Integer acbScore = medDatabase.getACBScore(selectedMed);
                    // Dont change null to 0- keep as null for meds without ACB scores.
                    Object displayScore = (acbScore != null) ? acbScore : "-";
                    model.addRow(new Object[] {selectedMed, displayScore});
                    updateTotalACB();

                    Patient currentPatient = tabDash.getCurrentPatient();
                    currentPatient.addMedication(selectedMed, acbScore); // Store null if no ACB
                    tabDash.onPatientDataChanged();
                }
            } 
        });

        removeMedBtn.addActionListener(e -> {
            int selectedRow = medicationTable.getSelectedRow();
            if (selectedRow >= 0) {
                DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
                String medName = (String) model.getValueAt(selectedRow, 0);
                model.removeRow(selectedRow);
                updateTotalACB();

                Patient currentPatient = tabDash.getCurrentPatient();
                currentPatient.removeMedication(medName);
                tabDash.onPatientDataChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a medication to remove.");
            }
        });
    }

    private void updateTotalACB() {
        DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
        int total = 0;
        for (int row = 0; row < model.getRowCount(); row++) {
            Object acbValue = model.getValueAt(row, 1);
            if (acbValue instanceof Integer) {
                total += (Integer) acbValue;
            }
            // If acbValue is "-" (String), skip it and don't add to total
        }
        totalAcbScore.setText("Total ACB Score: " + total);
    }
    
    private void loadPatientMedications() {
        Patient currentPatient = tabDash.getCurrentPatient();
        HashMap<String, Medication> patientMeds = currentPatient.getMedications();
        DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
        model.setRowCount(0);
        for (String medName : patientMeds.keySet()) {
            Medication med = patientMeds.get(medName);
            Integer acbScore = med.getAcbScore();
            Object displayScore = (acbScore != null) ? acbScore : "-";
            model.addRow(new Object[]{medName, displayScore});
        }
        updateTotalACB();
    }    

    public void refreshForNewPatient() {
        loadPatientMedications();
    }
}
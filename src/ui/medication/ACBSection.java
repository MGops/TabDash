package src.ui.medication;

import src.data_managers.MedicationDatabase;
import src.data_managers.MedicationLookupService;
import src.model.Medication;
import src.model.Patient;
import src.ui.TabDash;
import src.utils.UIUtils;

import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

public class ACBSection extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;
    private JTable medicationTable;
    private JLabel totalAcbScore;
    private MedicationLookupService medicationLookupService;

    public ACBSection(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        this.medicationLookupService = new MedicationLookupService();
        setBorder(BorderFactory.createTitledBorder("ACB Score"));
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
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
            Patient currentPatient = tabDash.getCurrentPatient();
            if (currentPatient == null) {
                JOptionPane.showMessageDialog(this,
                "No patient selected. Please select a patient first.",
                "No Patient Selected",
                JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Dialog code for selecting medications
            String[] medicationNames = medDatabase.getAllMedications().keySet().toArray(new String[0]);
            java.util.Arrays.sort(medicationNames);

            // Create display names array with capitalised first letters.
            String[]displayNames = new String[medicationNames.length];
            for (int i = 0; i < medicationNames.length; i++) {
                displayNames[i] = UIUtils.capitaliseFirst(medicationNames[i]);
            }

            JComboBox<String> medComboBox = new JComboBox<>(displayNames);
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

                    // Convert back to lowercase to check against existing medications
                    // As data stored in lowercase
                    String medNameForCheck = selectedMed.trim().toLowerCase();
                    
                    for (int row = 0; row < model.getRowCount(); row++) {
                        String existingMed = (String) model.getValueAt(row, 0);
                        if (existingMed.toLowerCase().equals(medNameForCheck)) {
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
                    Integer acbScore = medDatabase.getACBScore(medNameForCheck);
                    
                    // Dont change null to 0- keep as null for meds without ACB scores.
                    Object displayScore = (acbScore != null) ? acbScore : "-";
                    model.addRow(new Object[] {selectedMed, displayScore});
                    updateTotalACB();

                    //Create medication object with class information
                    Medication medication = new Medication(medNameForCheck);
                    medication.setAcbScore(acbScore);

                    //Look up and set class/subclass information
                    MedicationLookupService.MedicationClassInfo classInfo = medicationLookupService.getClassInfo(medNameForCheck);
                    if (classInfo != null) {
                        medication.setDrugClass(classInfo.drugClass);
                        medication.setDrugSubclass(classInfo.drugSubclass);
                    } else {
                        System.out.println("No medication class information found for: " + medNameForCheck);
                    }
                    currentPatient.getMedications().put(medNameForCheck, medication);
                    tabDash.onPatientDataChanged();
                    tabDash.refreshMedicationPanel();
                    // Check for medication alert when adding medication
                    tabDash.checkMedicationAlert(medNameForCheck);
                }
            } 
        });

        removeMedBtn.addActionListener(e -> {
            Patient currentPatient = tabDash.getCurrentPatient();
            if (currentPatient == null) {
                JOptionPane.showMessageDialog(this, 
                    "No patient selected.",
                    "No Patient Selected", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            int selectedRow = medicationTable.getSelectedRow();
            if (selectedRow >= 0) {
                DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
                String medName = (String) model.getValueAt(selectedRow, 0);
                model.removeRow(selectedRow);
                updateTotalACB();

                currentPatient.removeMedication(medName);
                tabDash.onPatientDataChanged();
                tabDash.refreshMedicationPanel();
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
        if (currentPatient == null) {
            return;
        }
        HashMap<String, Medication> patientMeds = currentPatient.getMedications();
        DefaultTableModel model = (DefaultTableModel) medicationTable.getModel();
        model.setRowCount(0);
        for (String medName : patientMeds.keySet()) {
            Medication med = patientMeds.get(medName);
            if (med.getDrugClass() == null) {
                MedicationLookupService.MedicationClassInfo classInfo = medicationLookupService.getClassInfo(medName);
                if (classInfo != null) {
                    med.setDrugClass(classInfo.drugClass);
                    med.setDrugSubclass(classInfo.drugSubclass);
                }
            }
            Integer acbScore = med.getAcbScore();
            Object displayScore = (acbScore != null) ? acbScore : "-";
            String displayName = UIUtils.capitaliseFirst(medName);
            model.addRow(new Object[]{displayName, displayScore});
        }
        updateTotalACB();
    }    

    public void refreshForNewPatient() {
        if (tabDash.getCurrentPatient() == null) {
            DefaultTableModel model =  (DefaultTableModel) medicationTable.getModel();
            model.setRowCount(0);
            updateTotalACB();
            return;
        }
        loadPatientMedications();
    }
}
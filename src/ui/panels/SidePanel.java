package src.ui.panels;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import src.model.Patient;
import src.ui.TabDash;

import java.awt.*;
import java.io.File;
import java.util.List;

public class SidePanel extends JPanel{
    private TabDash tabDash;
    private JList<String> nameList;
    private DefaultListModel<String> nameListModel;
    private JButton addBtn;
    private JButton removeBtn;
    
    public SidePanel(TabDash tabDash) {
        this.tabDash = tabDash;

        setPreferredSize(new Dimension(200, 700));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
        JLabel listLabel = new JLabel("Names");
        listLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(listLabel);


        nameListModel = new DefaultListModel<>();
        List<String> patientNames = tabDash.getPatientNames();
        for (String name: patientNames) {
            nameListModel.addElement(name);
        }
        
        nameList = new JList<>(nameListModel);
        JScrollPane nameScrollPane = new JScrollPane(nameList);
        nameScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        nameScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(nameScrollPane);
        
        add(Box.createRigidArea(new Dimension(0,10)));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttonPanel.add(new JButton("Add"));
        buttonPanel.add(new JButton("Remove"));
        add(buttonPanel);

        setupButtonListeners();

        nameList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = nameList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        tabDash.setCurrentPatient(selectedIndex);
                        tabDash.refreshAllPanels();
                    }
                    updateButtonStates();
                }
            }
        });

        // Pass JList reference to TabDash
        tabDash.setPatientList(nameList); 

        // Set initial selection(visual only)
        // Actual data loading happens in Tabdash
        if(!nameListModel.isEmpty()) {
            nameList.setSelectedIndex(0);
            nameList.ensureIndexIsVisible(0);
        }
        updateButtonStates();
    }

    private void setupButtonListeners() {
        addBtn.addActionListener(e -> {
            showAddPatientDialog();
        });

        removeBtn.addActionListener(e -> {
            confirmAndRemovePatient();
        });
    }


    private void showAddPatientDialog() {
        String newPatientId = JOptionPane.showInputDialog(
            this,
            "Enter new patient ID (eg. AB 123456):",
            "Add New Patient",
            JOptionPane.PLAIN_MESSAGE
        );

        if (newPatientId != null && !newPatientId.trim().isEmpty()) {
            newPatientId = newPatientId.trim();

            // Check if new patient ID already exists
            if (doesPatientIdExist(newPatientId)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Patient ID '" + newPatientId + "' already exists.",
                    "Duplicate Patient ID",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            addNewPatient(newPatientId);
        }
    }


    private boolean doesPatientIdExist(String patientID) {
        for (int i = 0; i < nameListModel.getSize(); i++) {
            if (nameListModel.getElementAt(i).equals(patientID)) {
                return true;
            }
        }
        return false;
    }
    

    private void addNewPatient(String patientId) {
        try {
            // Create new patient object
            Patient newPatient = new Patient(patientId);
            
            // Add to TabDash's patient list
            tabDash.addPatient(newPatient);
            
            // Add to UI list model
            nameListModel.addElement(patientId);
            
            // Select the new patient
            int newIndex = nameListModel.getSize() - 1;
            nameList.setSelectedIndex(newIndex);
            nameList.ensureIndexIsVisible(newIndex);
            
            // Update button states
            updateButtonStates();
            
            // Refresh panels for the new patient
            tabDash.refreshAllPanels();
            
            JOptionPane.showMessageDialog(
                this,
                "Patient '" + patientId + "' added successfully.",
                "Patient Added",
                JOptionPane.INFORMATION_MESSAGE
            );
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error adding patient: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void confirmAndRemovePatient() {
        int selectedIndex = nameList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a patient to remove.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String selectedPatientId = nameListModel.getElementAt(selectedIndex);
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove patient '" + selectedPatientId + "'?\n" +
            "This will delete all associated data files and cannot be undone.",
            "Confirm Patient Removal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            removePatient(selectedIndex, selectedPatientId);
        }
    }

    private void removePatient(int index, String patientId) {
        try {
            // Remove patient from TabDash
            tabDash.removePatient(index);
            
            // Delete all associated data files
            deletePatientDataFiles(patientId);
            
            // Remove from UI list
            nameListModel.removeElementAt(index);
            
            // Handle selection after removal
            if (nameListModel.isEmpty()) {
                // No patients left - clear everything
                tabDash.clearCurrentPatient();
            } else {
                // Select appropriate patient after removal
                int newIndex = Math.min(index, nameListModel.getSize() - 1);
                nameList.setSelectedIndex(newIndex);
                nameList.ensureIndexIsVisible(newIndex);
            }
            
            // Update button states
            updateButtonStates();
            
            // Refresh panels
            tabDash.refreshAllPanels();
            
            JOptionPane.showMessageDialog(
                this,
                "Patient '" + patientId + "' and all associated data removed successfully.",
                "Patient Removed",
                JOptionPane.INFORMATION_MESSAGE
            );
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error removing patient: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deletePatientDataFiles(String patientId) {
        String safeName = patientId.replace(" ", "_");
        
        // List of directories and file patterns to clean up
        String[] directories = {
            "data/patients/",
            "data/physical_health/",
            "data/mha/",
            "data/appointments/"
        };
        
        for (String dir : directories) {
            File dataFile = new File(dir + safeName + ".txt");
            if (dataFile.exists()) {
                boolean deleted = dataFile.delete();
                if (deleted) {
                    System.out.println("Deleted: " + dataFile.getPath());
                } else {
                    System.err.println("Failed to delete: " + dataFile.getPath());
                }
            }
        }
    }

    private void updateButtonStates() {
        // Remove button should only be enabled if there's a selection
        removeBtn.setEnabled(nameList.getSelectedIndex() >= 0);
        
        // Add button is always enabled
        addBtn.setEnabled(true);
    }

    // Method to refresh the list when patients are added/removed externally
    public void refreshPatientList() {
        nameListModel.clear();
        List<String> patientNames = tabDash.getPatientNames();
        for (String name : patientNames) {
            nameListModel.addElement(name);
        }
        updateButtonStates();
    }
}
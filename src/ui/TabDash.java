package src.ui;
import javax.swing.*;

import src.data_managers.AppointmentDataManager;
import src.data_managers.FrailtyDataManager;
import src.data_managers.HeaderDataManager;
import src.data_managers.MHADataManager;
import src.data_managers.MedicationDatabase;
import src.data_managers.MonitoringDataManager;
import src.data_managers.PatientDataManager;
import src.data_managers.PhysicalHealthDataManager;
import src.data_managers.LogEntryDataManager;
import src.data_managers.PhitDataManager;
import src.model.Patient;
import src.ui.panels.ContentPanel;
import src.ui.panels.HeaderPanel;
import src.ui.panels.MHAPanel;
import src.ui.panels.MedicationPanel;
import src.ui.panels.NotesPanel;
import src.ui.panels.PhysicalHealthPanel;
import src.ui.panels.SidePanel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;



public class TabDash {
    private List<Patient> patients;
    private Patient currentPatient;
    private MedicationPanel medicationPanel;
    private MHAPanel mhaPanel;
    private JList<String> patientList;
    private PhysicalHealthPanel physicalHealthPanel;
    private NotesPanel notesPanel;
    private SidePanel sidePanel;
    private HeaderPanel headerPanel;

    public TabDash() {
        patients = new ArrayList<>();

        for (Patient patient : patients) {
            PatientDataManager.loadPatient(patient);
            PhysicalHealthDataManager.loadPatientPhysicalHealth(patient);
            MHADataManager.loadPatientMHAdata(patient);
            AppointmentDataManager.loadPatientAppointments(patient);
            MonitoringDataManager.loadPatientMonitoring(patient);
            HeaderDataManager.loadPatientHeaderData(patient);
            FrailtyDataManager.loadPatientFrailtyData(patient);
            PhitDataManager.loadPatientPhitData(patient);
            LogEntryDataManager.loadPatientLogEntries(patient);
        }
        
        currentPatient = patients.isEmpty() ? null : patients.get(0);

        JFrame frame = new JFrame("Dashboard");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(mainPanel);

        MedicationDatabase medDatabase = new MedicationDatabase();
        
        headerPanel = new HeaderPanel(this);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        sidePanel = new SidePanel(this);
        mainPanel.add(sidePanel, BorderLayout.WEST);

        medicationPanel = new MedicationPanel(medDatabase, this);
        ContentPanel panelCenter = new ContentPanel(medDatabase, this, medicationPanel);
        mhaPanel = panelCenter.getMHAPanel();
        physicalHealthPanel = panelCenter.getPhysicalHealthPanel();
        notesPanel = panelCenter.getNotesPanel();
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        frame.setVisible(true);

        // Initialise first patient's data after components set up
        SwingUtilities.invokeLater(() -> {
            initialiseFirstPatient();
        });
    }

    public List<String> getPatientNames() {
        List<String> names = new ArrayList<>();
        for (Patient patient : patients) {
            names.add(patient.getPatientId());
        }
        return names;
    }

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(int index) {
        if (index >=0 && index < patients.size()) {
            currentPatient = patients.get(index);
        }
    }


    // New method to add a patient
    public void addPatient(Patient newPatient) {
        if (newPatient != null) {
            patients.add(newPatient);
            // Initialise data for new patient
            PatientDataManager.loadPatient(newPatient);
            PhysicalHealthDataManager.loadPatientPhysicalHealth(newPatient);
            MHADataManager.loadPatientMHAdata(newPatient);
            AppointmentDataManager.loadPatientAppointments(newPatient);
            MonitoringDataManager.loadPatientMonitoring(newPatient);
            HeaderDataManager.loadPatientHeaderData(newPatient);
            PhitDataManager.loadPatientPhitData(newPatient);
            LogEntryDataManager.loadPatientLogEntries(newPatient);

            // Set as current patient
            currentPatient = newPatient;
        }
    }

    // New method to remove a patient
    public void removePatient(int index) {
        if (index >= 0 && index < patients.size()) {
            Patient patientToRemove = patients.get(index);

            // If removing current patient, handle current patient reference
            if (currentPatient == patientToRemove) {
                if (patients.size() == 1) {
                    // This was the last patient
                    currentPatient = null;
                } else if (index == patients.size() - 1) {
                    // Remove last patient, select previous one
                    currentPatient = patients.get(index - 1);
                } else {
                    // Select next patient (which will shift to current index)
                    currentPatient = patients.get(index + 1);
                }
            }
            // Remove from list
            patients.remove(index);

            // If selected a patient that shifted positions, update current patient
            if (currentPatient != null && !patients.contains(currentPatient)) {
                currentPatient = patients.isEmpty() ? null : patients.get(Math.min(index, patients.size() - 1));
            }
        }
    }


    // New method to clear current patient reference
    public void clearCurrentPatient() {
        currentPatient = null;
    }

    private void updateHeaderPanel() {
        if (headerPanel != null) {
            String patientID = (currentPatient != null) ? currentPatient.getPatientId() : null;
            headerPanel.updatePatientInfo(patientID);
        }
    }


    public void onPatientDataChanged() {
        if (currentPatient == null) {
            return;
        }
        
        PatientDataManager.savePatient(currentPatient);
        PhysicalHealthDataManager.savePatientPhysicalHealth(currentPatient);
        AppointmentDataManager.savePatientAppointments(currentPatient);
        MonitoringDataManager.savePatientMonitoring(currentPatient);
        HeaderDataManager.savePatientHeaderData(currentPatient);
        FrailtyDataManager.savePatientFrailtyData(currentPatient);
        PhitDataManager.savePatientPhitData(currentPatient);
        LogEntryDataManager.savePatientLogEntries(currentPatient);
    }

    public MHAPanel getMHAPanel() {
        return mhaPanel;
    }
    
    // Method to trigger medication alert
    public void checkMedicationAlert(String medicationName) {
        if (mhaPanel != null) {
            mhaPanel.checkMedicationChangeAlert(medicationName);
        }
    }


    public void refreshMedicationPanel() {
        if (medicationPanel != null) {
            medicationPanel.refreshMedicationData();
        }
    }

    public void refreshAllPanels() {
        if (medicationPanel != null) {
            medicationPanel.refreshForNewPatient();
        }
        if (mhaPanel != null) {
            mhaPanel.refreshForNewPatient();
        }
        if (physicalHealthPanel != null) {
            physicalHealthPanel.refreshForNewPatient();
        }
        if (notesPanel != null) {
            notesPanel.refreshForNewPatient();
        }
        updateHeaderPanel();
    }

    // Method to set the patient list reference
    public void setPatientList(JList<String> list) {
        this.patientList = list;
    }

    // Method to initialise first patient after GUI is ready
    private void initialiseFirstPatient() {
        if (patientList != null && patientList.getModel().getSize() > 0) {
            patientList.setSelectedIndex(0);
            patientList.ensureIndexIsVisible(0);
        }
        refreshAllPanels();
    }
}

package src.ui;
import javax.swing.*;

import src.data_managers.AppointmentDataManager;
import src.data_managers.HeaderDataManager;
import src.data_managers.MHADataManager;
import src.data_managers.MedicationDatabase;
import src.data_managers.MonitoringDataManager;
import src.data_managers.PatientDataManager;
import src.data_managers.PhysicalHealthDataManager;
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
        patients.add(new Patient("AB 123456"));
        patients.add(new Patient("CD 678907"));
        patients.add(new Patient("EF 234561"));
        patients.add(new Patient("GH 890123"));
        patients.add(new Patient("IJ 456789"));
        patients.add(new Patient("KL 012345"));
        patients.add(new Patient("MN 678901"));
        patients.add(new Patient("OP 234567"));
        patients.add(new Patient("QR 890123"));
        patients.add(new Patient("ST 456789"));
        patients.add(new Patient("UV 012345"));
        patients.add(new Patient("WX 678901"));
        patients.add(new Patient("YZ 234567"));
        patients.add(new Patient("AA 890123"));
        patients.add(new Patient("BB 456789"));
        patients.add(new Patient("CC 012345"));
        patients.add(new Patient("DD 678901"));
        patients.add(new Patient("EE 234567"));
        patients.add(new Patient("FF 890123"));
        patients.add(new Patient("GG 456789"));

        for (Patient patient : patients) {
            PatientDataManager.loadPatient(patient);
            PhysicalHealthDataManager.loadPatientPhysicalHealth(patient);
            MHADataManager.loadPatientMHAdata(patient);
            AppointmentDataManager.loadPatientAppointments(patient);
            MonitoringDataManager.loadPatientMonitoring(patient);
        }
        
        currentPatient = patients.isEmpty() ? null : patients.get(0);

        JFrame frame = new JFrame("Dashboard");
        // frame.setBounds(0, 0, 1000, 700);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);
        int height = (int) (screenSize.height * 0.8);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
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

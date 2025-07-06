package src;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class TabDash {
    private List<Patient> patients;
    private Patient currentPatient;
    private MedicationPanel medicationPanel;
    private MHAPanel mhaPanel;
    private JList<String> patientList;

    public TabDash() {
        patients = new ArrayList<>();
        patients.add(new Patient("AB 123456"));
        patients.add(new Patient("CD 678907"));
        patients.add(new Patient("EF 234561"));

        for (Patient patient : patients) {
            PatientDataManager.loadPatient(patient);
        }
        
        currentPatient = patients.get(0);

        JFrame frame = new JFrame("Dashboard");
        // frame.setBounds(0, 0, 1000, 700);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.7);
        int height = (int) (screenSize.height * 0.75);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(mainPanel);

        MedicationDatabase medDatabase = new MedicationDatabase();
        
        HeaderPanel panelNorth = new HeaderPanel();
        mainPanel.add(panelNorth, BorderLayout.NORTH);

        SidePanel panelWest = new SidePanel(this);
        mainPanel.add(panelWest, BorderLayout.WEST);

        
        medicationPanel = new MedicationPanel(medDatabase, this);
        ContentPanel panelCenter = new ContentPanel(medDatabase, this, medicationPanel);
        mhaPanel = panelCenter.getMHAPanel();
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

    public void onPatientDataChanged() {
        PatientDataManager.savePatient(currentPatient);
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

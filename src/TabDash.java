package src;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class TabDash {
    private List<Patient> patients;
    private Patient currentPatient;

    public TabDash() {
        patients = new ArrayList<>();

        patients.add(new Patient("AB 123456"));
        patients.add(new Patient("CD 678907"));
        patients.add(new Patient("EF 234561"));

        currentPatient = patients.get(0);

        JFrame frame = new JFrame("TabDash");
        // frame.setBounds(0, 0, 1000, 700);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.75);
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

        ContentPanel panelCenter = new ContentPanel(medDatabase, this);
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        frame.setVisible(true);
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
}

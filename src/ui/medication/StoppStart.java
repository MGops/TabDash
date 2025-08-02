package src.ui.medication;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import src.data_managers.MedicationDatabase;
import src.model.Patient;
import src.service.StoppStartService.StoppStartService;
import src.service.StoppStartService.StoppRecommendation;
import src.service.StoppStartService.StartRecommendation;
import src.ui.TabDash;

public class StoppStart extends JPanel{
    private MedicationDatabase medDatabase;
    private TabDash tabDash;
    private StoppStartService stoppStartService;
    private JPanel stoppPanel;
    private JPanel startPanel;

    public StoppStart(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        this.stoppStartService = new StoppStartService();

        setBorder(BorderFactory.createTitledBorder("STOPP/START"));
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(getFont().deriveFont(Font.BOLD));

        setLayout(new BorderLayout());
        initialiseComponents();
        analyseCurrentPatient();
    }


    private void initialiseComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        Border empty = BorderFactory.createEmptyBorder();

        // STOPP Panel (light red background)
        stoppPanel = new JPanel();
        stoppPanel.setLayout(new BoxLayout(stoppPanel, BoxLayout.Y_AXIS));
        stoppPanel.setBackground(new Color(255, 230, 230)); // Light red
        TitledBorder stoppTitledBorder = BorderFactory.createTitledBorder(empty,"STOPP");
        stoppTitledBorder.setTitleJustification(TitledBorder.CENTER);
        stoppPanel.setBorder(stoppTitledBorder);
        
        JScrollPane stoppScroll = new JScrollPane(stoppPanel);
        stoppScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stoppScroll.setPreferredSize(new Dimension(200, 100));

        // START Panel (light green background)
        startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setBackground(new Color(230, 255, 230)); // Light green
        TitledBorder startTitledBorder = BorderFactory.createTitledBorder(empty,"START");
        startTitledBorder.setTitleJustification(TitledBorder.CENTER);
        startPanel.setBorder(startTitledBorder);
        
        JScrollPane startScroll = new JScrollPane(startPanel);
        startScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        startScroll.setPreferredSize(new Dimension(200, 100));

        mainPanel.add(stoppScroll);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(startScroll);

        add(mainPanel, BorderLayout.CENTER);
    }


    private void analyseCurrentPatient() {
        stoppPanel.removeAll();
        startPanel.removeAll();

        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            addNoPatientMessage();
            return;
        }


        // Get STOPP recommendations
        List<StoppRecommendation> stoppRecommendations = stoppStartService.getStoppRecommendations(currentPatient);

        if (stoppRecommendations.isEmpty()) {
            JLabel noStoppLabel = new JLabel("No STOPP recommendations");
            noStoppLabel.setForeground(Color.GRAY);
            stoppPanel.add(noStoppLabel);
        } else {
            for (StoppRecommendation recommendation : stoppRecommendations) {
                JLabel medLabel = new JLabel(recommendation.getMedicationName());
                medLabel.setToolTipText(recommendation.getTooltipText());
                medLabel.setBorder(BorderFactory.createEmptyBorder(2,5,2,5));
                stoppPanel.add(medLabel);
            }
        }

        List<StartRecommendation> startRecommendations = stoppStartService.getStartRecommendations(currentPatient);

        if (startRecommendations.isEmpty()) {
            JLabel noStartLabel = new JLabel("No START recommendations");
            noStartLabel.setForeground(Color.GRAY);
            startPanel.add(noStartLabel);
        } else {
            for (StartRecommendation recommendation : startRecommendations) {
                JLabel medLabel = new JLabel(recommendation.getMedicationName());
                medLabel.setToolTipText(recommendation.getTooltipText());
                medLabel.setBorder(BorderFactory.createEmptyBorder(2,5,2,5));
                startPanel.add(medLabel);
            }
        }


        stoppPanel.revalidate();
        stoppPanel.repaint();
        startPanel.revalidate();
        startPanel.repaint();
    }

    private void addNoPatientMessage() {
        JLabel noPatientLabel = new JLabel("No patient selected");
        noPatientLabel.setForeground(Color.GRAY);
        stoppPanel.add(noPatientLabel);

        JLabel noPatientLabel2 = new JLabel("No patient selected");
        noPatientLabel2.setForeground(Color.GRAY);
        startPanel.add(noPatientLabel2);

        stoppPanel.revalidate();
        stoppPanel.repaint();
        startPanel.revalidate();
        startPanel.repaint();
    }

    public void refreshForNewPatient() {
        analyseCurrentPatient();
    }
}

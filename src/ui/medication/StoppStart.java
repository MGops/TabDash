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
import src.utils.UIUtils;

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

        // STOPP Panel
        stoppPanel = new JPanel();
        stoppPanel.setLayout(new BoxLayout(stoppPanel, BoxLayout.Y_AXIS));
        stoppPanel.setBackground(new Color(255, 235, 235)); 
        TitledBorder stoppTitledBorder = BorderFactory.createTitledBorder(empty,"STOPP");
        stoppTitledBorder.setTitleFont(getFont().deriveFont(Font.BOLD, 12f));
        stoppTitledBorder.setTitleJustification(TitledBorder.CENTER);
        stoppTitledBorder.setTitleColor(new Color(150,0,0));
        stoppPanel.setBorder(stoppTitledBorder);
        
        JScrollPane stoppScroll = new JScrollPane(stoppPanel);
        stoppScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        stoppScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        stoppScroll.setPreferredSize(new Dimension(200, 100));

        // START Panel
        startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setBackground(new Color(235, 255, 235)); // Light green
        TitledBorder startTitledBorder = BorderFactory.createTitledBorder(empty,"START");
        startTitledBorder.setTitleFont(getFont().deriveFont(Font.BOLD, 12f));
        startTitledBorder.setTitleJustification(TitledBorder.CENTER);
        startTitledBorder.setTitleColor(new Color(0,120,0));
        startPanel.setBorder(startTitledBorder);
        
        JScrollPane startScroll = new JScrollPane(startPanel);
        startScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        startScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
            JLabel noStoppLabel = UIUtils.createInfoLabel("No STOPP recommendations", UIUtils.INFO_GRAY);
            stoppPanel.add(noStoppLabel);
        } else {
            for (StoppRecommendation recommendation : stoppRecommendations) {
                JLabel medLabel = createRecommendationLabel(
                    recommendation.getMedicationName(), 
                    recommendation.getTooltipText(),
                    new Color(180, 0, 0) // Dark red text
                );
                stoppPanel.add(medLabel);
                stoppPanel.add(Box.createVerticalStrut(2));
            }
        }

        List<StartRecommendation> startRecommendations = stoppStartService.getStartRecommendations(currentPatient);

        if (startRecommendations.isEmpty()) {
            JLabel noStartLabel = UIUtils.createInfoLabel("No START recommendations", UIUtils.INFO_GRAY);
            startPanel.add(noStartLabel);
        } else {
            for (StartRecommendation recommendation : startRecommendations) {
                JLabel medLabel = createRecommendationLabel(
                    recommendation.getMedicationName(),
                    recommendation.getTooltipText(),
                    new Color(0, 120, 0) // Dark green text
                );
                startPanel.add(medLabel);
                startPanel.add(Box.createVerticalStrut(2));
            }
        }


        stoppPanel.revalidate();
        stoppPanel.repaint();
        startPanel.revalidate();
        startPanel.repaint();
    }


    private JLabel createRecommendationLabel(String text, String tooltip, Color textColor) {
        JLabel label = new JLabel("• " + text);
        //label.setFont(label.getFont().deriveFont(Font.PLAIN, 11f));
        label.setForeground(textColor);
        label.setToolTipText(tooltip);
        label.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        label.setOpaque(false);
        return label;
    }

    public JLabel createInfoLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.ITALIC, 11f));
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
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

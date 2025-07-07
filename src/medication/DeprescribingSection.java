package src.medication;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import src.DeprescribingService;
import src.DeprescribingService.DeprescribingCategory;
import src.Medication;
import src.MedicationDatabase;
import src.Patient;
import src.TabDash;

public class DeprescribingSection extends JPanel {
    private MedicationDatabase medDatabase;
    private TabDash tabDash;
    private DeprescribingService deprescribingService;
    private JPanel columnsPanel;

    public DeprescribingSection(MedicationDatabase medDatabase, TabDash tabDash) {
        this.medDatabase = medDatabase;
        this.tabDash = tabDash;
        this.deprescribingService = new DeprescribingService();

        setBorder(BorderFactory.createTitledBorder("Safe Deprescribing"));
        setLayout(new BorderLayout());

        // Create main panel for columns
        columnsPanel = new JPanel();
        columnsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5,0));

        JScrollPane scrollPane = new JScrollPane(columnsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        analyseCurrentPatientDeprescribing();
    }

    private void analyseCurrentPatientDeprescribing() {
        // Clear previous columns
        columnsPanel.removeAll();

        Patient currentPatient = tabDash.getCurrentPatient();
        Map<String, Medication> medications = currentPatient.getMedications();

        // Group medications by category
        Map<DeprescribingCategory, List<String>> categoryMedications = new EnumMap<>(DeprescribingCategory.class);

        for (Medication med : medications.values()) {
            List<DeprescribingCategory> categories = deprescribingService.getCategoriesForMedication(
                med.getName(), med.getDrugClass(), med.getDrugSubclass());

                for (DeprescribingCategory category : categories) {
                    categoryMedications.computeIfAbsent(category, k -> new ArrayList<>()).add(med.getName());
                }
        }

        // Create columns for categories that have medications
        boolean hasAnyMeds = false;
        for (DeprescribingCategory category : DeprescribingCategory.values()) {
            List<String> medsInCategory = categoryMedications.get(category);
            if(medsInCategory != null && !medsInCategory.isEmpty()) {
                JPanel columnPanel = createColumnPanel(category, medsInCategory);
                columnsPanel.add(columnPanel);
                hasAnyMeds = true;
            }
        }

        // Show message if no deprescribing considerations found
        if (!hasAnyMeds) {
            JLabel noDeprescribingLabel = new JLabel("No deprescribing considerations detected");
            noDeprescribingLabel.setForeground(Color.GRAY);
            columnsPanel.add(noDeprescribingLabel);
        }

        columnsPanel.revalidate();
        columnsPanel.repaint();
    }


    private JPanel createColumnPanel(DeprescribingCategory category, List<String> medications) {
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));

        // Create header with category colours
        JLabel headerLabel =  new JLabel(category.displayName);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(Color.decode(category.backgroundColour));
        headerLabel.setForeground(Color.decode(category.textColour));
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create content panel with light gray background
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add meds as bulletpoints
        for (String medication : medications) {
            JLabel medLabel = new JLabel("• " + medication);
            medLabel.setForeground(Color.BLACK);
            medLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(medLabel);
            contentPanel.add(Box.createVerticalStrut(2));
        }

        // Set consistent columnwidth based on content but with reasonable limits
        int preferredWidth = Math.max(120, Math.min(200, headerLabel.getPreferredSize().width + 20));
        columnPanel.setPreferredSize(new Dimension(preferredWidth,
            headerLabel.getPreferredSize().height + contentPanel.getPreferredSize().height + 20));
        columnPanel.add(headerLabel);
        columnPanel.add(contentPanel);
        return columnPanel;
    }


    public void refreshForNewPatient() {
        analyseCurrentPatientDeprescribing();
    }
}

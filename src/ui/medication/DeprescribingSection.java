package src.ui.medication;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.*;
import java.util.*;
import java.util.List;

import src.data_managers.MedicationDatabase;
import src.model.Medication;
import src.model.Patient;
import src.service.DeprescribingService;
import src.service.DeprescribingService.DeprescribingCategory;
import src.ui.TabDash;

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
        columnsPanel.setLayout(new BoxLayout(columnsPanel, BoxLayout.X_AXIS));

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
        if (currentPatient == null) {
            JLabel noPatientLabel = new JLabel("No patient selected");
            noPatientLabel.setForeground(Color.GRAY);
            columnsPanel.add(noPatientLabel);
            columnsPanel.revalidate();
            columnsPanel.repaint();
        }
        Map<String, Medication> medications = currentPatient.getMedications();

        // Group medications by category
        Map<DeprescribingCategory, List<String>> categoryMedications = new EnumMap<>(DeprescribingCategory.class);

        for (Medication med : medications.values()) {
            List<DeprescribingCategory> categories = deprescribingService.getCategoriesForMedication(med.getName());

                for (DeprescribingCategory category : categories) {
                    categoryMedications.computeIfAbsent(category, k -> new ArrayList<>()).add(med.getName());
                }
        }

        // Create columns for categories that have medications
        boolean hasAnyMeds = false;
        for (DeprescribingCategory category : DeprescribingCategory.values()) {
            List<String> medsInCategory = categoryMedications.get(category);
            if(medsInCategory != null && !medsInCategory.isEmpty()) {
                if (hasAnyMeds) {
                    columnsPanel.add(Box.createHorizontalStrut(5));
                }
                JPanel columnPanel = createColumnPanel(category, medsInCategory);
                columnPanel.setAlignmentY(Component.TOP_ALIGNMENT);
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
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        // Calculate minimum width needed but set a reasonable minimum
        int headerWidth = headerLabel.getPreferredSize().width;
        int contentWidth = contentPanel.getPreferredSize().width;
        int maxWidth = Math.max(headerWidth, contentWidth);
        int preferredWidth = Math.max(150, Math.min(250, maxWidth + 20));

        // Set column panel size first
        int headerHeight = headerLabel.getPreferredSize().height;
        int contentPanelHeight = contentPanel.getPreferredSize().height;
        columnPanel.setPreferredSize(new Dimension(preferredWidth, headerHeight + contentPanelHeight));

        // Force both header and content to match the column panel width exactly
        headerLabel.setPreferredSize(new Dimension(preferredWidth, headerHeight));
        headerLabel.setMinimumSize(new Dimension(preferredWidth, headerHeight));
        headerLabel.setMaximumSize(new Dimension(preferredWidth, headerHeight));

        contentPanel.setPreferredSize(new Dimension(preferredWidth, contentPanelHeight));
        contentPanel.setMinimumSize(new Dimension(preferredWidth, contentPanelHeight));
        contentPanel.setMaximumSize(new Dimension(preferredWidth, contentPanelHeight));

        columnPanel.add(headerLabel);
        columnPanel.add(contentPanel);
        return columnPanel;
    }


    public void refreshForNewPatient() {
        analyseCurrentPatientDeprescribing();
    }
}

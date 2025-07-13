package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.locks.Condition;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import src.model.Patient;
import src.service.PhysicalConditionService;
import src.ui.TabDash;

public class IllnessListSection extends JPanel{
    private TabDash tabDash;
    private JTable comorbidityTable;
    private PhysicalConditionService conditionService;

    public IllnessListSection(TabDash tabDash) {
        this.tabDash = tabDash;
        this.conditionService = new PhysicalConditionService();
        setBorder(BorderFactory.createTitledBorder("Illness List"));
        initialiseComponents();
    }

    private void initialiseComponents() {
        setLayout(new BorderLayout());

        String[] columnNames = {"Comorbidity"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        comorbidityTable = new JTable(tableModel);
        comorbidityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        comorbidityTable.setRowSelectionAllowed(true);
        comorbidityTable.setFillsViewportHeight(true);

        JScrollPane comorbidityScrollPane = new JScrollPane(comorbidityTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addComorbidityBtn = new JButton("Add");
        addComorbidityBtn.addActionListener(e -> showAddConditionDialog());
        buttonPanel.add(addComorbidityBtn);

        JButton removeComorbidityBtn = new JButton("Remove");
        removeComorbidityBtn.addActionListener(e -> removeSelectedCondition());
        buttonPanel.add(removeComorbidityBtn);

        add(buttonPanel, BorderLayout.NORTH);
        add(comorbidityScrollPane, BorderLayout.CENTER);

        loadPatientConditions();
    }


    private void loadPatientConditions() {
        if (tabDash.getCurrentPatient() == null) return; 
        DefaultTableModel model = (DefaultTableModel) comorbidityTable.getModel();
        model.setRowCount(0);

        List<String> conditions = tabDash.getCurrentPatient().getPhysicalHealthConditions();
        for (String condition : conditions) {
            model.addRow(new Object[]{condition});
        }
    }


    private void showAddConditionDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Medical Condition", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Condition:"), BorderLayout.WEST);
        JTextField conditionField = new JTextField(20);
        inputPanel.add(conditionField, BorderLayout.CENTER);

        // Suggestions list (initially hidden)
        DefaultListModel<String> suggestionModel = new DefaultListModel<>();
        JList<String> suggestionList = new JList<>(suggestionModel);
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane suggestionScroll = new JScrollPane(suggestionList);
        suggestionScroll.setPreferredSize(new Dimension(380, 100));
        suggestionScroll.setVisible(false);

        // Real-time search
        conditionField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {updateSuggestions();}
            public void removeUpdate(DocumentEvent e) {updateSuggestions();}
            public void changedUpdate(DocumentEvent e) {updateSuggestions();}
            
            private void updateSuggestions() {
                String searchText = conditionField.getText().trim();
                suggestionModel.clear();

                if (!searchText.isEmpty()) {
                    List<PhysicalConditionService.SearchResult> results = 
                        conditionService.searchConditions(searchText);
                    
                    if (!results.isEmpty()) {
                        for (PhysicalConditionService.SearchResult result : results) {
                            suggestionModel.addElement(result.getDisplayText());
                        }
                        suggestionScroll.setVisible(true);
                    } else {
                        suggestionScroll.setVisible(false);
                    }
                } else {
                    suggestionScroll.setVisible(false);
                }
                dialog.revalidate();
                dialog.repaint();
            }
        });


        // Click on suggestion to select it
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = suggestionList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        String searchText = conditionField.getText().trim();
                        List<PhysicalConditionService.SearchResult> results =
                            conditionService.searchConditions(searchText);
                        if (selectedIndex < results.size()) {
                            PhysicalConditionService.SearchResult selected = results.get(selectedIndex);
                            conditionField.setText(selected.conditionName);
                            suggestionScroll.setVisible(false);
                            dialog.revalidate();
                        }
                    }
                }
            }
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(suggestionScroll, BorderLayout.CENTER);

        dialog.add(topPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String condition = conditionField.getText().trim();
            if (!condition.isEmpty()) {
                Patient currentPatient = tabDash.getCurrentPatient();
                if (!currentPatient.getPhysicalHealthConditions().contains(condition)) {
                    currentPatient.addPhysicalHealthConditions(condition);
                    tabDash.onPatientDataChanged();
                    loadPatientConditions();
                    tabDash.refreshMedicationPanel();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "This condition is already in the list.",
                        "Duplicate Condition",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            dialog.dispose();
        });
        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void removeSelectedCondition() {
        int selectedRow = comorbidityTable.getSelectedRow();
        if (selectedRow >= 0) {
            String condition = (String) comorbidityTable.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                "Remove "+ condition + " ?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                tabDash.getCurrentPatient().removePhysicalHealthCondition(condition);
                tabDash.onPatientDataChanged();
                loadPatientConditions();
                tabDash.refreshMedicationPanel();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a condition to remove.");
        }
    }

    public void refreshForNewPatient() {
        loadPatientConditions();
    }
}

package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.border.TitledBorder;
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
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
        setPreferredSize(new Dimension(200, 250));  
        setMaximumSize(new Dimension(200, 300));
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

        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) return;
        
        List<String> conditions = tabDash.getCurrentPatient().getPhysicalHealthConditions();
        for (String condition : conditions) {
            model.addRow(new Object[]{condition});
        }
    }


    private void showAddConditionDialog() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            JOptionPane.showMessageDialog(this,
            "No patient selected. Please select a patient first.", 
            "No Patient Selected", 
            JOptionPane.WARNING_MESSAGE);
        return;
        }

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Medical Condition", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        JTextField conditionField = new JTextField(20);
        inputPanel.add(conditionField, BorderLayout.CENTER);

        // Suggestions list (initially hidden)
        DefaultListModel<String> suggestionModel = new DefaultListModel<>();
        JList<String> suggestionList = new JList<>(suggestionModel);
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane suggestionScroll = new JScrollPane(suggestionList);
        suggestionScroll.setPreferredSize(new Dimension(380, 100));

        populateAllConditions(suggestionModel);

        // Real-time search
        conditionField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateSuggestions());
            }
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateSuggestions());
            }
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> updateSuggestions());
            }
            
            private void updateSuggestions() {
                String searchText = conditionField.getText().trim();
                
                suggestionModel.clear();

                if (searchText.isEmpty()) {
                    // If search field is empty, show all conditions
                    populateAllConditions(suggestionModel);
                } else {
                    // Show filtered search results
                    List<String> results = conditionService.searchConditions(searchText);
                    
                    if (results.isEmpty()) {
                        // If no search results, still show all conditions
                        populateAllConditions(suggestionModel);
                    } else {
                        for (String condition : results) {
                            suggestionModel.addElement(condition);
                        }
                    }
                }
                
                // Force UI refresh
                suggestionList.revalidate();
                suggestionList.repaint();
            }
        });


        // Click on suggestion to select it
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = suggestionList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String selectedCondition = suggestionModel.getElementAt(selectedIndex);
                    conditionField.setText(selectedCondition);
                    
                    // If double-click, add immediately
                    if (e.getClickCount() == 2) {
                        addConditionToPatient(currentPatient, selectedCondition, dialog);
                    }
                }
            }
        });

        suggestionList.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    int selectedIndex = suggestionList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        String selectedCondition = suggestionModel.getElementAt(selectedIndex);
                        conditionField.setText(selectedCondition);
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
                addConditionToPatient(currentPatient, condition, dialog);
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter or select a condition.",
                    "No Condition Selected",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        SwingUtilities.invokeLater(() -> conditionField.requestFocusInWindow());
        dialog.setVisible(true);
    }


    private void addConditionToPatient(Patient patient, String condition, JDialog dialog) {
        if (!patient.getPhysicalHealthConditions().contains(condition)) {
            patient.addPhysicalHealthConditions(condition);
            tabDash.onPatientDataChanged();
            loadPatientConditions();
            tabDash.refreshMedicationPanel();
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog,
                "This condition is already in the list.",
                "Duplicate Condition",
                JOptionPane.WARNING_MESSAGE);
        }
    }


    private void populateAllConditions(DefaultListModel<String> model) {
        List<String> allConditions = conditionService.getAllConditions();
        
        for (String condition : allConditions) {
            model.addElement(condition);
        }
    }

    private void removeSelectedCondition() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            JOptionPane.showMessageDialog(this,
                "No patient selected",
                "No Patient Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

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
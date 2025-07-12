package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.concurrent.locks.Condition;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;

import src.ui.TabDash;

public class IllnessListSection extends JPanel{
    private TabDash tabDash;
    private JTable comorbidityTable;

    public IllnessListSection(TabDash tabDash) {
        this.tabDash = tabDash;
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

        add(buttonPanel, BorderLayout.NORTH);
        add(comorbidityScrollPane, BorderLayout.CENTER);

        loadMockData();
    }


    private void loadMockData() {
        DefaultTableModel model = (DefaultTableModel) comorbidityTable.getModel();
        model.addRow(new Object[]{"Diabetes mellitus"});
        model.addRow(new Object[]{"Hypertension"});
        model.addRow(new Object[]{"Heart failure"});
    }


    private void showAddConditionDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Medical Condition", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 150);
        dialog.setLocationRelativeTo(this);


        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Condition:"));
        JTextField conditionField = new JTextField(20);
        inputPanel.add(conditionField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String condition = conditionField.getText().trim();
            if (!condition.isEmpty()) {
                DefaultTableModel model = (DefaultTableModel) comorbidityTable.getModel();
                model.addRow(new Object[]{condition});
            }
            dialog.dispose();
        });
        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }
}

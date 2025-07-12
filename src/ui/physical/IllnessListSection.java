package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
        buttonPanel.add(addComorbidityBtn);

        add(buttonPanel, BorderLayout.NORTH);
    }
}

package src;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class SidePanel extends JPanel{
    private TabDash tabDash;

    public SidePanel(TabDash tabDash) {
        this.tabDash = tabDash;
        tabDash.getPatientNames();

        setPreferredSize(new Dimension(200, 700));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
        JLabel listLabel = new JLabel("Names");
        listLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(listLabel);

        DefaultListModel<String> nameListModel = new DefaultListModel<>();
        List<String> patientNames = tabDash.getPatientNames();
        for (String name: patientNames) {
            nameListModel.addElement(name);
        }
        JList<String> nameList = new JList<>(nameListModel);
        JScrollPane nameScrollPane = new JScrollPane(nameList);
        nameScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        nameScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(nameScrollPane);
        
        add(Box.createRigidArea(new Dimension(0,10)));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttonPanel.add(new JButton("Add"));
        buttonPanel.add(new JButton("Remove"));
        add(buttonPanel);


        nameList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = nameList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        tabDash.setCurrentPatient(selectedIndex);
                    }
                }
            }
        });
    }
}
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.*;

public class TabDash {
    public TabDash() {
        JFrame frame = new JFrame("TabDash");
        frame.setBounds(0, 0, 800, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5)); 
        frame.add(mainPanel);
        
        JPanel panelNorth = new JPanel();
        panelNorth.setBackground(Color.DARK_GRAY);
        panelNorth.setPreferredSize(new Dimension(800,80));
        mainPanel.add(panelNorth, BorderLayout.NORTH);

        JPanel panelWest = new JPanel();
        panelWest.setPreferredSize(new Dimension(200,500));
        panelWest.setLayout(new BoxLayout(panelWest, BoxLayout.Y_AXIS));
        panelWest.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(panelWest, BorderLayout.WEST);
        
        JLabel listLabel = new JLabel("Names");
        listLabel.setAlignmentX(Component.CENTER_ALIGNMENT);;
        panelWest.add(listLabel);
        
        DefaultListModel nameListModel = new DefaultListModel<>();
        JList nameList = new JList<>(nameListModel);
        JScrollPane nameScrollPane = new JScrollPane(nameList);
        nameListModel.addElement("Tom");
        nameScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        nameScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelWest.add(nameScrollPane);

        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        panelWest.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));;
        
        JButton addButton = new JButton("Add");
        buttonPanel.add(addButton);        

        JButton removeButton = new JButton("Remove");
        buttonPanel.add(removeButton);
        panelWest.add(buttonPanel);

        frame.setVisible(true);

    }
}

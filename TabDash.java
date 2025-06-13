import javax.swing.DefaultListModel;
import javax.swing.JFrame;
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
        panelWest.setBackground(Color.LIGHT_GRAY);
        panelWest.setPreferredSize(new Dimension(200,500));
        mainPanel.add(panelWest, BorderLayout.WEST);
        
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(Color.WHITE);
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        frame.setVisible(true);

        DefaultListModel nameListModel = new DefaultListModel<>();
        JList nameList = new JList<>(nameListModel);
        JScrollPane nameScrollPane = new JScrollPane(nameList);
        nameScrollPane.setBounds(5, 20, 190, 300);
        panelWest.add(nameScrollPane);
    }
}

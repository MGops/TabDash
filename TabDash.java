import javax.swing.*;
import java.awt.*;

public class TabDash {
    public TabDash() {
        JFrame frame = new JFrame("TabDash");
        frame.setBounds(0, 0, 800, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(mainPanel);
        
        JPanel panelNorth = new JPanel();
        panelNorth.setBackground(Color.DARK_GRAY);
        panelNorth.setPreferredSize(new Dimension(800,80));
        mainPanel.add(panelNorth, BorderLayout.NORTH);

        SidePanel panelWest = new SidePanel();
        mainPanel.add(panelWest, BorderLayout.WEST);

        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        frame.setVisible(true);

    }
}

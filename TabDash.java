import javax.swing.*;
import java.awt.*;

public class TabDash {
    public TabDash() {
        JFrame frame = new JFrame("TabDash");
        frame.setBounds(0, 0, 1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(mainPanel);
        
        HeaderPanel panelNorth = new HeaderPanel();
        mainPanel.add(panelNorth, BorderLayout.NORTH);

        SidePanel panelWest = new SidePanel();
        mainPanel.add(panelWest, BorderLayout.WEST);

        ContentPanel panelCenter = new ContentPanel();
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        frame.setVisible(true);

    }
}

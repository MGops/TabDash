package src;
import javax.swing.*;
import java.awt.*;

public class TabDash {
    public TabDash() {
        JFrame frame = new JFrame("TabDash");
        // frame.setBounds(0, 0, 1000, 700);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.75);
        int height = (int) (screenSize.height * 0.75);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(mainPanel);

        MedicationDatabase medDatabase = new MedicationDatabase();
        
        HeaderPanel panelNorth = new HeaderPanel();
        mainPanel.add(panelNorth, BorderLayout.NORTH);

        SidePanel panelWest = new SidePanel();
        mainPanel.add(panelWest, BorderLayout.WEST);

        ContentPanel panelCenter = new ContentPanel(medDatabase);
        mainPanel.add(panelCenter, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ContentPanel extends JPanel{
    public ContentPanel() {
        setLayout(new BorderLayout());
        //setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Panel 1"));
        tabbedPane.add("Tab 1", panel1);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Panel 2"));
        tabbedPane.add("Tab 2", panel2);

        add(tabbedPane);
    }
    
}

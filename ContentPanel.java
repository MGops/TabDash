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
        setBorder(BorderFactory.createEmptyBorder(5,5,50,5));

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Physical health"));
        tabbedPane.add("Physical health", panel1);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Medication"));
        tabbedPane.add("Medication", panel2);

        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("MHA"));
        tabbedPane.add("MHA", panel3);

        add(tabbedPane);
    }
    
}

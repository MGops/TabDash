import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel{

    public SidePanel() {
        setPreferredSize(new Dimension(200, 700));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
        JLabel listLabel = new JLabel("Names");
        listLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(listLabel);

        DefaultListModel<String> nameListModel = new DefaultListModel<>();
        nameListModel.addElement("Tom");

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
    }
}


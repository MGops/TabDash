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
            
        nameListModel.addElement("AB 123456");
        nameListModel.addElement("CD 678907");
        nameListModel.addElement("EF 234561");
        nameListModel.addElement("GH 789012");
        nameListModel.addElement("IJ 345678");
        nameListModel.addElement("KL 890123");
        nameListModel.addElement("MN 456789");
        nameListModel.addElement("OP 901234");
        nameListModel.addElement("QR 567890");
        nameListModel.addElement("ST 012345");
        nameListModel.addElement("UV 678901");
        nameListModel.addElement("WX 234567");
        nameListModel.addElement("YZ 789012");
        nameListModel.addElement("AC 345678");
        nameListModel.addElement("BD 890123");
        nameListModel.addElement("CE 456789");
        nameListModel.addElement("DF 901234");
        nameListModel.addElement("EG 567890");
        nameListModel.addElement("FH 012345");
        nameListModel.addElement("GI 678901");

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


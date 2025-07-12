package src.ui.physical;

import javax.swing.JLabel;
import javax.swing.JPanel;

import src.ui.TabDash;

public class IllnessListSection extends JPanel{
    private TabDash tabDash;

    public IllnessListSection(TabDash tabDash) {
        this.tabDash = tabDash;
        add(new JLabel("Illness List"));
    }
    
}

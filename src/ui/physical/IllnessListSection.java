package src.ui.physical;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import src.ui.TabDash;

public class IllnessListSection extends JPanel{
    private TabDash tabDash;

    public IllnessListSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Illness List"));
    }
    
}

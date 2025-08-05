package src.ui.physical;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.border.Border;

import src.ui.TabDash;

public class FrailtySection extends JPanel {
    private TabDash tabDash;

    public FrailtySection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Frailty"));
        TitledBorder border = (TitledBorder) getBorder();
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
        border.setTitleJustification(TitledBorder.CENTER);

        setLayout(new GridLayout(2,2,5,5));
        initialiseQuadrants();
    }

    private void initialiseQuadrants() {

        JPanel quadrant1 = createQuadrant("Falls", Color.LIGHT_GRAY);
        JPanel quadrant2 = createQuadrant("Incontinence", new Color(220, 220, 255));
        JPanel quadrant3 = createQuadrant("Immobility", new Color(220, 255, 220));
        JPanel quadrant4 = createQuadrant("Delirium", new Color(255, 220, 220));
        
        add(quadrant1);
        add(quadrant2);
        add(quadrant3);
        add(quadrant4);
    }

    private JPanel createQuadrant(String title, Color backgroundColour) {

        Border empty = BorderFactory.createEmptyBorder();
        JPanel quadrant = new JPanel();
        quadrant.setBorder(BorderFactory.createTitledBorder(empty, title));
        quadrant.setBackground(backgroundColour);
        return quadrant;
    }
}
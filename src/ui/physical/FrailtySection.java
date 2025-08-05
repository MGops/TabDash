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

        JPanel fallsPanel = createQuadrant("Falls", Color.LIGHT_GRAY);
        JPanel incontinencePanel = createQuadrant("Incontinence", new Color(220, 220, 255));
        JPanel immobilityPanel = createQuadrant("Immobility", new Color(220, 255, 220));
        JPanel deliriumPanel = createQuadrant("Delirium", new Color(255, 220, 220));
        
        add(fallsPanel);
        add(incontinencePanel);
        add(immobilityPanel);
        add(deliriumPanel);
    }

    private JPanel createQuadrant(String title, Color backgroundColour) {

        Border empty = BorderFactory.createEmptyBorder();
        JPanel quadrant = new JPanel();
        quadrant.setBorder(BorderFactory.createTitledBorder(empty, title));
        quadrant.setBackground(backgroundColour);
        return quadrant;
    }
}
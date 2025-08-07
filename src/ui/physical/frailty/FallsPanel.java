package src.ui.physical.frailty;

import javax.swing.*;
import javax.swing.border.Border;

import src.model.Patient;
import src.ui.TabDash;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class FallsPanel extends JPanel {

    private JPanel risksSection;
    private JPanel fallsNumberSection;

    private int fallsCount = 0;
    private JButton fallsCountBtn;
    private TabDash tabDash;
    
    public FallsPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Border empty = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createTitledBorder(empty,"Falls"));
        //setBackground(Color.LIGHT_GRAY);

        createSections();
        initialiseComponents();

    }


    private JPanel createRisksSection() {
        JPanel section = new JPanel();
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return section;
    }

    private JPanel createNumberSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        fallsCountBtn = new JButton(String.valueOf(fallsCount));
        fallsCountBtn.setFont(getFont().deriveFont(Font.BOLD, 36));
        fallsCountBtn.setForeground(new Color(40,190,40));
        //fallsCountBtn.setBorder(BorderFactory.createRaisedBevelBorder());
        fallsCountBtn.setFocusPainted(false);

        fallsCountBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                    fallsCount ++;
                    fallsCountBtn.setText(String.valueOf(fallsCount));
                    fallsCountBtn.setForeground(Color.RED);
                    saveFallsCount();
                    saveButtonColour("RED");
                } else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    fallsCount = 0;
                    fallsCountBtn.setText(String.valueOf(fallsCount));
                    saveFallsCount();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (fallsCountBtn.getForeground().equals(Color.RED)) {
                        fallsCountBtn.setForeground(new Color(40,190,40));
                        saveButtonColour("GREEN");
                    }
                }
            } 
        });

        section.add(fallsCountBtn, BorderLayout.CENTER);
        return section;
    }


    private void createSections() {
        risksSection = createRisksSection();
        fallsNumberSection = createNumberSection();
    }


    private void initialiseComponents() {
        add(risksSection);
        add(createVerticalSeparator());
        add(fallsNumberSection);
    }


    private JSeparator createVerticalSeparator() {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setMaximumSize(new Dimension(2,Integer.MAX_VALUE));
        separator.setPreferredSize(new Dimension(2,0));
        separator.setForeground(Color.BLACK);
        return separator;
    }


    private void saveFallsCount() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null) {
            currentPatient.setFallsCount(fallsCount);
            tabDash.onPatientDataChanged();
        }
    }


    private void saveButtonColour(String color) {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null) {
            currentPatient.setFallsButtonColour(color);
            tabDash.onPatientDataChanged();
        }
    }


    public void refreshForNewPatient() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient != null && fallsCountBtn != null) {
            fallsCount = currentPatient.getFallsCount();
            fallsCountBtn.setText(String.valueOf(fallsCount));

            String savedColour = currentPatient.getFallsButtonColour();
            if ("GREEN".equals(savedColour)) {
                fallsCountBtn.setForeground(new Color(40,190,40));
            } else {
                fallsCountBtn.setForeground(Color.RED);    
            }
            
        } else if (fallsCountBtn != null) {
            fallsCount = 0;
            fallsCountBtn.setText("0");
            fallsCountBtn.setForeground(new Color(40,190,40));
        }
    }
}

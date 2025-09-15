package src.ui.physical.frailty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.Border;

import src.data_managers.FrailtyDataManager;
import src.model.Patient;
import src.ui.TabDash;

public class IncontinencePanel extends JPanel {
    private TabDash tabDash;
    private JRadioButton continentBtn;
    private JRadioButton dependentContinenceBtn;
    private JRadioButton containedIncontinenceBtn;
    private JRadioButton incontinentBtn;
    private ButtonGroup incontinenceGroup;

    public IncontinencePanel(TabDash tabDash) {
        this.tabDash = tabDash;
        Border empty = BorderFactory.createEmptyBorder();
        setBorder(BorderFactory.createTitledBorder(empty, "Incontinence"));
        setLayout(new GridLayout(3,3,0,0));
        setBackground(new Color(220,220,255));
        initialiseComponents();
    }


    private void initialiseComponents() {
        continentBtn = new JRadioButton("Continent");
        dependentContinenceBtn = new JRadioButton("Dependent continence");
        containedIncontinenceBtn = new JRadioButton("Contained incontinence");
        incontinentBtn = new JRadioButton("Incontinent");

        incontinenceGroup = new ButtonGroup();
        incontinenceGroup.add(continentBtn);
        incontinenceGroup.add(dependentContinenceBtn);
        incontinenceGroup.add(containedIncontinenceBtn);
        incontinenceGroup.add(incontinentBtn);

        continentBtn.setOpaque(false);
        dependentContinenceBtn.setOpaque(false);
        containedIncontinenceBtn.setOpaque(false);
        incontinentBtn.setOpaque(false);

        ActionListener saveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveIncontinenceStatus();
            }
        };

        continentBtn.addActionListener(saveListener);
        dependentContinenceBtn.addActionListener(saveListener);
        containedIncontinenceBtn.addActionListener(saveListener);
        incontinentBtn.addActionListener(saveListener);

        Color borderColor = new Color(190, 190, 225);

        add(createBorderedPanel(new JLabel(""), borderColor));
        add(createBorderedPanel(continentBtn, borderColor));
        add(createBorderedPanel(new JLabel(""), borderColor));
        
        add(createBorderedPanel(dependentContinenceBtn, borderColor));
        add(createBorderedPanel(new JLabel(""), borderColor));
        add(createBorderedPanel(containedIncontinenceBtn, borderColor));

        add(createBorderedPanel(new JLabel(""), borderColor));
        add(createBorderedPanel(incontinentBtn, borderColor));
        add(createBorderedPanel(new JLabel(""), borderColor));
    }

    private JPanel createBorderedPanel(JComponent component, Color borderColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        panel.setBackground(getBackground());
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }


    private void saveIncontinenceStatus() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            return;
        }

        String incontinenceStatus = getSelectedIncontinenceStatus();
        currentPatient.setIncontinenceStatus(incontinenceStatus);

        FrailtyDataManager.savePatientFrailtyData(currentPatient);

        tabDash.onPatientDataChanged();
    }

    private String getSelectedIncontinenceStatus() {
        if (continentBtn.isSelected()) {
            return "Continent";
        } else if (dependentContinenceBtn.isSelected()) {
            return "Dependent continence";
        } else if (containedIncontinenceBtn.isSelected()) {
            return "Contained incontinence";
        } else if (incontinentBtn.isSelected()) {
            return "Incontinent";
        }
        return "";
    }


    private void setIncontinenceStatus(String status) {
        incontinenceGroup.clearSelection();
        switch (status) {
            case "Continent":
                continentBtn.setSelected(true);
                break;
            case "Dependent continence":
                dependentContinenceBtn.setSelected(true);
                break;
            case "Contained incontinence":
                containedIncontinenceBtn.setSelected(true);
                break;
            case "Incontinent":
                incontinentBtn.setSelected(true);
                break;
            default:
                // No selection for empty or unknown status
                break;
        }
    }

    public void refreshForNewPatient() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            incontinenceGroup.clearSelection();
            return;
        }

        FrailtyDataManager.loadPatientFrailtyData(currentPatient);

        String incontinenceStatus = currentPatient.getIncontinenceStatus();
        setIncontinenceStatus(incontinenceStatus != null ? incontinenceStatus : "");
    }
}

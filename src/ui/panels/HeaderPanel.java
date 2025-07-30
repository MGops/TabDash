package src.ui.panels;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import src.data_managers.HeaderDataManager;
import src.model.Patient;
import src.ui.TabDash;

public class HeaderPanel extends JPanel{
    private TabDash tabDash;
    private JLabel initialsLabel;
    private JLabel careCoLabel;
    private JLabel resusLabel;
    private JLabel namedNurseLabel;
    private JLabel cmhtLabel;
    

    public HeaderPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        setPreferredSize(new Dimension(800, 80));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        setBackground(Color.DARK_GRAY);

        GridBagConstraints coordinates = new GridBagConstraints();
        coordinates.fill = GridBagConstraints.BOTH;
        coordinates.weighty = 1;

        initialsLabel = new JLabel("Placeholder");
        coordinates.gridx = 0;
        coordinates.gridy = 0;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        initialsLabel.setForeground(Color.WHITE);
        add(initialsLabel, coordinates);

        careCoLabel = new JLabel("Care coordinator");
        coordinates.gridx = 4;
        coordinates.gridy = 0;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        careCoLabel.setForeground(Color.WHITE);
        add(careCoLabel, coordinates);
                
        resusLabel = new JLabel("Resus status");
        coordinates.gridx = 8;
        coordinates.gridy = 0;
        coordinates.gridwidth = 1;
        coordinates.weightx = 0;
        resusLabel.setForeground(Color.WHITE);
        add(resusLabel, coordinates);

        namedNurseLabel = new JLabel("Named nurse:");
        coordinates.gridx = 0;
        coordinates.gridy = 1;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        namedNurseLabel.setForeground(Color.WHITE);
        add(namedNurseLabel, coordinates);

        cmhtLabel = new JLabel("CMHT: ");
        coordinates.gridx = 4;
        coordinates.gridy = 1;
        coordinates.gridwidth = 4;
        coordinates.weightx = 1;
        cmhtLabel.setForeground(Color.WHITE);
        add(cmhtLabel, coordinates);
        
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> showEditDialog());
        coordinates.gridx = 8; 
        coordinates.gridy = 1;
        coordinates.gridwidth = 1;
        coordinates.weightx = 0;
        add(editButton, coordinates);
    }


    public void updatePatientInfo(String patientId) {
        if (patientId != null && !patientId.trim().isEmpty()) {
            initialsLabel.setText(patientId);
            initialsLabel.setFont(initialsLabel.getFont().deriveFont(Font.BOLD, 20));

            Patient currentPatient = tabDash.getCurrentPatient();
            if (currentPatient != null) {
                updateHeaderLabels(currentPatient);
            }
        } else {
            initialsLabel.setText("No patient selected");
            careCoLabel.setText("Care coordinator: ");
            resusLabel.setText("Resus status: ");
            namedNurseLabel.setText("Named nurse: ");
            cmhtLabel.setText("CMHT: ");
        }
        repaint();
    }

    private void updateHeaderLabels(Patient patient) {
        careCoLabel.setText("Care coordinator: " +
            (patient.getCareCoordinator() != null && !patient.getCareCoordinator().isEmpty()
                ? patient.getCareCoordinator() : ""));
        
        resusLabel.setText("Resus status: " +
            (patient.getResusStatus() != null ? patient.getResusStatus() : "For resus"));
    
        namedNurseLabel.setText("Named nurse: " +
            (patient.getNamedNurse() != null && !patient.getNamedNurse().isEmpty()
                ? patient.getNamedNurse() : ""));

        cmhtLabel.setText("CMHT: " +
            (patient.getCmht() != null && !patient.getCmht().isEmpty()
                ? patient.getCmht() : ""));
    }

    private void showEditDialog() {
        Patient currentPatient = tabDash.getCurrentPatient();
        if (currentPatient == null) {
            JOptionPane.showMessageDialog(this,
                "No patient selected. Please select a patient first.",
                "No Patient Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            "Edit Patient Header Information", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(450,250);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Care coordinator:"), gbc);

        JTextField careCoField = new JTextField(20);
        careCoField.setText(currentPatient.getCareCoordinator() != null ?
            currentPatient.getCareCoordinator() : "");
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dialog.add(careCoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        dialog.add(new JLabel("Resus status:"), gbc);

        String[] resusOptions = {"For resus", "DNACPR"};
        JComboBox<String> resusComboBox = new JComboBox<>(resusOptions);
        resusComboBox.setSelectedItem(currentPatient.getResusStatus() != null ?
            currentPatient.getResusStatus() : "For resus");
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dialog.add(resusComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        dialog.add(new JLabel("Named nurse:"), gbc);

        JTextField namedNurseField = new JTextField(20);
        namedNurseField.setText(currentPatient.getNamedNurse() != null ?
            currentPatient.getNamedNurse() : "");
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dialog.add(namedNurseField, gbc); 

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        dialog.add(new JLabel("CMHT:"), gbc);

        JTextField cmhtField = new JTextField(20);
        cmhtField.setText(currentPatient.getCmht() != null ?
            currentPatient.getCmht() : "");
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dialog.add(cmhtField, gbc);
    
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        dialog.add(buttonPanel, gbc);

        careCoField.requestFocusInWindow();

        saveBtn.addActionListener(e -> {
            currentPatient.setCareCoordinator(careCoField.getText().trim());
            currentPatient.setResusStatus((String) resusComboBox.getSelectedItem());
            currentPatient.setNamedNurse(namedNurseField.getText().trim());
            currentPatient.setCmht(cmhtField.getText().trim());

            HeaderDataManager.savePatientHeaderData(currentPatient);
            updateHeaderLabels(currentPatient);
            tabDash.onPatientDataChanged();
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        careCoField.addActionListener(e -> saveBtn.doClick());
        namedNurseField.addActionListener(e -> saveBtn.doClick());
        cmhtField.addActionListener(e -> saveBtn.doClick());

        dialog.setVisible(true);
    }
}

package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import src.model.Appointment;
import src.model.Patient;
import src.ui.TabDash;

public class AppointmentsSection extends JPanel {
    private TabDash tabDash;
    private JPanel toReferPanel;
    private JPanel referredPanel;
    private JPanel scheduledPanel;
    private JPanel attendedPanel;
    private JPanel missedPanel;

    public AppointmentsSection(TabDash tabDash) {
        this.tabDash = tabDash;
        setBorder(BorderFactory.createTitledBorder("Appointments"));
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(600,250));
        setPreferredSize(new Dimension(700, 300));
        initialiseComponents();
        loadPatientAppointments();
    }

    private void initialiseComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createAppointmentBtn = new JButton("Create Appointment");
        createAppointmentBtn.addActionListener(e -> showCreateAppointmentDialog());
        buttonPanel.add(createAppointmentBtn);

        // Add button panel to layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.weighty = 0.0;
        add(buttonPanel, gbc);

        // Reset constraints for columns (shift down by 1 row)
        gbc.gridy = 1; gbc.weighty = 1.0; gbc.gridwidth = 1;

        // Create columns
        toReferPanel = createColumn("To Refer");
        referredPanel = createColumn("Referred");
        scheduledPanel = createColumn("Scheduled");

        // Create split panels for attended & missed
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.fill = GridBagConstraints.BOTH;
        rightGbc.weightx = 1.0;
        rightGbc.insets = new Insets(2, 2, 2, 2);

        attendedPanel = createColumn("Attended");
        missedPanel = createColumn("Missed");

        rightGbc.gridy = 0; rightGbc.weighty = 0.5;
        rightPanel.add(attendedPanel, rightGbc);
        rightGbc.gridy = 1; rightGbc.weighty = 0.5;
        rightPanel.add(missedPanel, rightGbc);

        gbc.gridx = 0; gbc.weightx = 0.25;
        add(toReferPanel, gbc);
        gbc.gridx = 1; gbc.weightx = 0.25;
        add(referredPanel, gbc);
        gbc.gridx = 2; gbc.weightx = 0.25;
        add(scheduledPanel, gbc);
        gbc.gridx = 3; gbc.weightx = 0.25;
        add(rightPanel, gbc);
    }

    private JPanel createColumn(String title) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBorder(BorderFactory.createTitledBorder(title));
        column.setMinimumSize(new Dimension(150, 200));

        new DropTarget(column, new AppointmentDropTargetListener(column));
        return column;
    }


    private void loadPatientAppointments() {
        if (tabDash.getCurrentPatient() == null) return;
            
        //Clear all columns first
        clearAllColumns();

        //Load appointments from current patient
        List<Appointment> appointments = tabDash.getCurrentPatient().getAppointments();

        // If no appointments exist, add sample data (for testing)
        if (appointments.isEmpty()) {
            addSampleAppointments();
            return;
        }

        // Distribute appointments to appropriate columns based on status
        for (Appointment appointment : appointments) {
            JLabel appointmentLabel = createAppointmentLabel(appointment);

            switch (appointment.getStatus()) {
                case TO_REFER:
                    toReferPanel.add(appointmentLabel);
                    break;
                case REFERRED:
                    referredPanel.add(appointmentLabel);
                    break;
                case SCHEDULED:
                    scheduledPanel.add(appointmentLabel);
                    break;
                case ATTENDED:
                    attendedPanel.add(appointmentLabel);
                    break;
                case MISSED:
                    missedPanel.add(appointmentLabel);
                    break;
            }
        }
        revalidateAllColumns();
    }

    private void clearAllColumns() {
        toReferPanel.removeAll();
        referredPanel.removeAll();
        scheduledPanel.removeAll();
        attendedPanel.removeAll();
        missedPanel.removeAll();
    }

    private void revalidateAllColumns() {
        toReferPanel.revalidate();
        toReferPanel.repaint();
        referredPanel.revalidate();
        referredPanel.repaint();
        scheduledPanel.revalidate();
        scheduledPanel.repaint();
        attendedPanel.revalidate();
        attendedPanel.repaint();
        missedPanel.revalidate();
        missedPanel.repaint();
    }

    private void addSampleAppointments() {
        // Add some sample draggable appointments to add to pt date
        Appointment cardio = new Appointment("Cardiology", 
            LocalDateTime.now().plusDays(30), null);
        Appointment neuro = new Appointment("Neurology", 
            LocalDateTime.now().plusDays(30), null);
        Appointment ortho = new Appointment("Orthopaedics", 
            LocalDateTime.now().plusDays(30), null);

        // Add to pt data
        Patient currentPatient = tabDash.getCurrentPatient();
        currentPatient.addAppointment(cardio);
        currentPatient.addAppointment(neuro);
        currentPatient.addAppointment(ortho);
        
        // Save data
        tabDash.onPatientDataChanged();
        
        loadPatientAppointments();
    }


    private JLabel createAppointmentLabel(Appointment appointment) {
        JLabel label = new JLabel(appointment.getDisplayText());
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createRaisedBevelBorder());
        label.setPreferredSize(new Dimension(140,70));
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        label.setVerticalAlignment(SwingConstants.TOP);

        // Store appointment reference in label
        label.putClientProperty("appointment", appointment);
        
        setVenueColours(label, appointment.getLocation());

        //Make label draggable
        label.setTransferHandler(new AppointmentTransferHandler());
        label.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // right-click
                    showAppointmentContextMenu(e,appointment, label);
                } else { // left-click (drag)
                    JComponent component = (JComponent) e.getSource();
                    TransferHandler handler = component.getTransferHandler();
                    handler.exportAsDrag(component, e, TransferHandler.MOVE);
                }
            }
        });
        return label;
    }

    
    private void setVenueColours(JLabel label, String location) {
        // If no location (unscheduled), use default styling
        if (location == null || location.isEmpty()) {
            label.setBackground(Color.WHITE);
            label.setForeground(Color.BLACK);
            return;
        }

        switch (location.trim()) {
            case "NMGH": 
                label.setBackground(Color.RED);
                label.setForeground(Color.WHITE);
                break;
            case "MRI":
                label.setBackground(Color.ORANGE);
                label.setForeground(Color.BLACK);
                break;
            case "Wythenshawe":
                label.setBackground(Color.GREEN);
                label.setForeground(Color.BLACK);
                break;
            case "SRH":
                label.setBackground(Color.DARK_GRAY);
                label.setForeground(Color.WHITE);
                break;
            case "Other":
                label.setBackground(Color.BLUE);
                label.setForeground(Color.WHITE);
                break;
            default:
                label.setBackground(Color.BLACK);
                label.setForeground(Color.WHITE);
                break;
        }
    }


    // Custom TransferHandler for dragging labels
    private class AppointmentTransferHandler extends TransferHandler {
        @Override
        protected Transferable createTransferable(JComponent c) {
            if (c instanceof JLabel) {
                Appointment appointment = (Appointment) ((JLabel) c).getClientProperty("appointment");
                return new AppointmentTransferable(appointment);
            }
            return null;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected void exportDone(JComponent source, Transferable date, int action) {
            if (action == MOVE && source instanceof JLabel) {
                Container parent = source.getParent();
                if (parent != null) {
                    parent.remove(source);
                    parent.revalidate();
                    parent.repaint();
                }
            }
        }
    }


    // Custom Transferable for Appointment objects
    private class AppointmentTransferable implements Transferable {
        private Appointment appointment;
        public static final DataFlavor APPOINTMENT_FLAVOR = new DataFlavor(Appointment.class, "Appointment");

        public AppointmentTransferable(Appointment appointment) {
            this.appointment = appointment;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{APPOINTMENT_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return APPOINTMENT_FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (isDataFlavorSupported(flavor)) {
                return appointment;
            }
            throw new UnsupportedFlavorException(flavor);
        }
    }


    // Drop target listener for columns
    private class AppointmentDropTargetListener implements DropTargetListener {
        private JPanel targetPanel;

        public AppointmentDropTargetListener(JPanel panel) {
            this.targetPanel = panel;
        }
        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                Appointment appointment = (Appointment) dtde.getTransferable()
                    .getTransferData(AppointmentTransferable.APPOINTMENT_FLAVOR);
                
                boolean movingToScheduled = (targetPanel == scheduledPanel);
                boolean needsScheduling = (appointment.getLocation() == null || appointment.getLocation().isEmpty());

                if (movingToScheduled && needsScheduling) {
                    // Show venue/time assignment dialog
                    if (showScheduleAppointmentDialog(appointment)) {
                        // User assigned venue/time successfully
                        updateAppointmentStatus(appointment, targetPanel);
                        JLabel newLabel = createAppointmentLabel(appointment);
                        targetPanel.add(newLabel);
                        targetPanel.revalidate();
                        targetPanel.repaint();
                        tabDash.onPatientDataChanged();
                        dtde.dropComplete(true);
                    } else {
                        // User cancelled - don't move appointment
                        dtde.dropComplete(false);
                        return;
                    }
                } else {
                    updateAppointmentStatus(appointment, targetPanel);
                    JLabel newLabel = createAppointmentLabel(appointment);
                    targetPanel.add(newLabel);
                    targetPanel.revalidate();
                    targetPanel.repaint();
                    tabDash.onPatientDataChanged();
                    dtde.dropComplete(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                dtde.dropComplete(false);
            }
        }

        private void updateAppointmentStatus(Appointment appointment, JPanel targetPanel) {
            if (targetPanel == toReferPanel) appointment.setStatus(Appointment.Status.TO_REFER);
            else if (targetPanel == referredPanel) appointment.setStatus(Appointment.Status.REFERRED);
            else if (targetPanel == scheduledPanel) appointment.setStatus(Appointment.Status.SCHEDULED);
            else if (targetPanel == attendedPanel) appointment.setStatus(Appointment.Status.ATTENDED);
            else if (targetPanel == missedPanel) appointment.setStatus(Appointment.Status.MISSED);
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            targetPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE,2),
                ((TitledBorder) targetPanel.getBorder()).getTitle()));
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            targetPanel.setBorder(BorderFactory.createTitledBorder(
                ((TitledBorder) targetPanel.getBorder()).getTitle()));
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {}

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {}
    }

    private void showCreateAppointmentDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Create New Appointment", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450,150);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Specialty text field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Appointment:"), gbc);
        JTextField specialtyField = new JTextField(255555);
        specialtyField.setPreferredSize(new Dimension(250, 25));
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(specialtyField, gbc);

        // Info label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        JLabel infoLabel = new JLabel("<html><i>Venue and time to be assigned when confirmed and moved to Scheduled</i></html>");
        infoLabel.setForeground(Color.GRAY);
        formPanel.add(infoLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton createBtn = new JButton("Create");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(createBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        specialtyField.requestFocusInWindow();

        createBtn.addActionListener(e -> {
            String specialty = specialtyField.getText().trim();

            if (!specialty.isEmpty()) {
                createNewAppointment(specialty);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please enter a specialty.", "Missing information", JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        specialtyField.addActionListener(e -> createBtn.doClick());

        dialog.setVisible(true);
    }


    private void createNewAppointment(String specialty) {
        // Create appointment with placeholder date (will be set when scheduled)
        LocalDateTime placeholderDate = LocalDateTime.now().plusDays(30);
        Appointment newAppointment = new Appointment(specialty, placeholderDate, null);

        newAppointment.setPatientId(tabDash.getCurrentPatient().getPatientId());
        newAppointment.setStatus(Appointment.Status.TO_REFER);
        tabDash.getCurrentPatient().addAppointment(newAppointment);
        tabDash.onPatientDataChanged();

        JLabel appointmentLabel = createAppointmentLabel(newAppointment);
        toReferPanel.add(appointmentLabel);
        toReferPanel.revalidate();
        toReferPanel.repaint();
    }


    private void showAppointmentContextMenu(MouseEvent e, Appointment appointment, JLabel label) {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem deleteItem = new JMenuItem("Delete Appointment");
        deleteItem.addActionListener(event -> confirmDeleteAppointment(appointment, label));
        contextMenu.add(deleteItem);

        JMenuItem editItem = new JMenuItem("Edit Appointment");
        editItem.addActionListener(event -> editAppointment(appointment, label));
        contextMenu.add(editItem);

        contextMenu.show(label, e.getX(), e.getY());
    }


    private void confirmDeleteAppointment(Appointment appointment, JLabel label) {
        int result = JOptionPane.showConfirmDialog(
            this, 
            "Delete appointment: " + appointment.getSpecialty() + "?", 
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            // Remove from patient data
            tabDash.getCurrentPatient().removeAppointment(appointment);

            // Remove from UI
            Container parent = label.getParent();
            if (parent != null) {
                parent.remove(label);
                parent.revalidate();
                parent.repaint();
            }

            tabDash.onPatientDataChanged();
        }
    }


    private void editAppointment(Appointment appointment, JLabel label) {
        if (appointment.getLocation() == null || appointment.getLocation().isEmpty()) {
            // Unscheduled appointment - show schedule dialog
            if (showScheduleAppointmentDialog(appointment)) {
                // Update label display
                label.setText(appointment.getDisplayText());
                setVenueColours(label, appointment.getLocation());
                label.revalidate();
                label.repaint();
                tabDash.onPatientDataChanged();
            }
        } else {
            // Scheduled appointment - show schedule dialog to edit
            if (showScheduleAppointmentDialog(appointment)) {
                // Update label display
                label.setText(appointment.getDisplayText());
                setVenueColours(label, appointment.getLocation());
                label.revalidate();
                label.repaint();
                tabDash.onPatientDataChanged();
            }
        }
    }


    private boolean showScheduleAppointmentDialog(Appointment appointment) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            "Schedule Appointment: " + appointment.getSpecialty(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);

        final boolean[] result = {false};

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Venue selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Venue:"), gbc);

        String[] venues = {"NMGH", "MRI", "Wythenshawe", "SRH", "Other"};
        JComboBox<String> venueCombo = new JComboBox<>(venues);
        venueCombo.setEditable(true);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(venueCombo, gbc);

        // Date selection 
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Date:"), gbc);
        
        JTextField dateField = new JTextField(15);
        dateField.setText(LocalDate.now().plusDays(7).toString());
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(dateField, gbc);

        // Time selection
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Time:"), gbc);

        JTextField timeField = new JTextField(8);
        timeField.setText("09:00");
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        formPanel.add(timeField, gbc);

        // Info labels
        gbc. gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JLabel formatLabel = new JLabel("<html><i>Date: YYYY-MM-DD (e.g, 2025-01-30)<br>Time: HH:MM (e.g., 14:30)</i></html>");
        formatLabel.setForeground(Color.GRAY);
        formPanel.add(formatLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton scheduleBtn = new JButton("Schedule");
        JButton cancelBtn = new JButton("Cancel");
        buttonPanel.add(scheduleBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Focus on venue field
        venueCombo.requestFocusInWindow();

        scheduleBtn.addActionListener(e -> {
            String venue = (String) venueCombo.getSelectedItem();
            String dateStr = dateField.getText().trim();
            String timeStr = timeField.getText().trim();

            if (venue != null && !venue.trim().isEmpty() &&
                !dateStr.isEmpty() && !timeStr.isEmpty()) { // Check if time field is empty
                
                try {
                    LocalDate date = LocalDate.parse(dateStr);
                    LocalTime time = LocalTime.parse(timeStr);
                    LocalDateTime dateTime = LocalDateTime.of(date, time);

                    // Update appointment
                    appointment.setLocation(venue.trim());
                    appointment.setDateTime(dateTime);

                    result[0] = true;
                    dialog.dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                    "Invalid date or time format.\nDate: YYYY-MM-DD\nTime: HH:MM (24-hour format)",
                    "Invalid Format", 
                    JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                "Please fill in all fields", 
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        timeField.addActionListener(e -> scheduleBtn.doClick());

        dialog.setVisible(true);
        return result[0];
    }

    public void refreshForNewPatient() {
        loadPatientAppointments();
    }
}

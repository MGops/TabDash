package src.ui.physical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
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
import java.util.ArrayList;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import src.model.Appointment;
import src.ui.TabDash;

public class AppointmentsSection extends JPanel {
    private JPanel toReferPanel;
    private JPanel referredPanel;
    private JPanel scheduledPanel;
    private JPanel attendedPanel;
    private JPanel missedPanel;
    private List<Appointment> appointments;

    public AppointmentsSection() {
        setBorder(BorderFactory.createTitledBorder("Appointments"));
        setLayout(new GridBagLayout());
        appointments = new ArrayList<>();
        initialiseComponents();
        addSampleAppointments();
    }

    private void initialiseComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

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

    private void addSampleAppointments() {
        // Add some sample draggable appointments
        Appointment cardio = new Appointment("Cardiology", 
            LocalDateTime.now().plusDays(7), "NMGH");
        Appointment neuro = new Appointment("Neurology", 
            LocalDateTime.now().plusDays(14), "SRH");
        Appointment ortho = new Appointment("Orthopaedics", 
            LocalDateTime.now().plusDays(7), "Wythenshawe");

        appointments.add(cardio);
        appointments.add(neuro);
        appointments.add(ortho);

        toReferPanel.add(createAppointmentLabel(cardio));
        toReferPanel.add(createAppointmentLabel(neuro));
        toReferPanel.add(createAppointmentLabel(ortho));
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
        
        //Make label draggable
        label.setTransferHandler(new AppointmentTransferHandler());
        label.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                TransferHandler handler = component.getTransferHandler();
                handler.exportAsDrag(component, e, TransferHandler.MOVE);
            }
        });
        return label;
    }


    // Custom TransferHandler for dragging labels
    private class AppointmentTransferHandler extends TransferHandler {
        @Override
        protected Transferable createTransferable(JComponent c) {
            if (c instanceof JLabel) {
                return new StringSelection(((JLabel) c).getText());
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
                
                // Update appointment status based on target panel
                updateAppointmentStatus(appointment, targetPanel);

                // Create new label and add to target panel
                JLabel newLabel = createAppointmentLabel(appointment);
                targetPanel.add(newLabel);
                targetPanel.revalidate();
                targetPanel.repaint();
                dtde.dropComplete(true);
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
}

package src.ui.panels;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import src.ui.TabDash;

public class NotesPanel extends JPanel {
    private TabDash tabDash;
    private JTextArea notesTextArea;
    private JButton saveBtn;
    private String originalText = "";
    private static final String NOTES_DIR = "data/notes/";
    
    public NotesPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initialiseComponents();
    }
    
    private void initialiseComponents() {
        // Header panel with title and save button
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Patient Notes");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        saveBtn = new JButton("Save Notes");
        saveBtn.setEnabled(false);
        saveBtn.setPreferredSize(new Dimension(100, 30));
        headerPanel.add(saveBtn, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Text area for notes
        notesTextArea = new JTextArea();
        notesTextArea.setLineWrap(true);
        notesTextArea.setWrapStyleWord(true);
        notesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(notesTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Status panel at bottom
        // JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // JLabel statusLabel = new JLabel("Notes are automatically linked to the selected patient");
        // statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC));
        // statusLabel.setForeground(Color.GRAY);
        // statusPanel.add(statusLabel);
        
        // add(statusPanel, BorderLayout.SOUTH);
        
        setupEventListeners();
        loadNotesForCurrentPatient();
    }
    
    private void setupEventListeners() {
        // Save button action
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveNotesToFile();
            }
        });
        
        // Document listener to detect changes
        notesTextArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                checkTextChanged();
            }
            public void removeUpdate(DocumentEvent e) {
                checkTextChanged();
            }
            public void changedUpdate(DocumentEvent e) {
                checkTextChanged();
            }
        });
        
        // Keyboard shortcut for saving (Ctrl+S)
        notesTextArea.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke("ctrl S"), "save");
        notesTextArea.getActionMap().put("save", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (saveBtn.isEnabled()) {
                    saveNotesToFile();
                }
            }
        });
    }
    
    private void checkTextChanged() {
        String currentText = notesTextArea.getText();
        boolean hasPatient = tabDash.getCurrentPatient() != null;
        boolean textChanged = !currentText.equals(originalText);
        saveBtn.setEnabled(hasPatient && textChanged);
    }
    
    private void saveNotesToFile() {
        if (tabDash.getCurrentPatient() == null) {
            JOptionPane.showMessageDialog(this, 
                "No patient selected.", 
                "Cannot Save", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create notes directory if it doesn't exist
        File notesDir = new File(NOTES_DIR);
        if (!notesDir.exists()) {
            notesDir.mkdirs();
        }
        
        String patientId = tabDash.getCurrentPatient().getPatientId();
        String filename = NOTES_DIR + patientId.replace(" ", "_") + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(notesTextArea.getText());
            originalText = notesTextArea.getText();
            saveBtn.setEnabled(false);
            
            // Show brief confirmation
            JLabel confirmLabel = new JLabel("Notes saved!");
            confirmLabel.setForeground(Color.GREEN);
            Timer timer = new Timer(2000, e -> {
                // This will remove the confirmation after 2 seconds
                ((Timer)e.getSource()).stop();
            });
            timer.start();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving notes: " + e.getMessage(), 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private String loadNotesFromFile() {
        if (tabDash.getCurrentPatient() == null) {
            return "";
        }
        
        String patientId = tabDash.getCurrentPatient().getPatientId();
        String filename = NOTES_DIR + patientId.replace(" ", "_") + ".txt";
        File file = new File(filename);
        
        if (!file.exists()) {
            return "";
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading notes: " + e.getMessage(), 
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return "";
        }
    }
    
    private void loadNotesForCurrentPatient() {
        // Check if there are unsaved changes before switching
        if (!notesTextArea.getText().equals(originalText) && !originalText.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "You have unsaved changes. Save before switching patients?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                saveNotesToFile();
            } else if (confirm == JOptionPane.CANCEL_OPTION) {
                return; // Don't switch patients
            }
        }
        
        String content = loadNotesFromFile();
        notesTextArea.setText(content);
        originalText = content;
        saveBtn.setEnabled(false);
    }
    
    public void refreshForNewPatient() {
        loadNotesForCurrentPatient();
    }
    
    // Method to check if there are unsaved changes (useful for warning before closing)
    public boolean hasUnsavedChanges() {
        return !notesTextArea.getText().equals(originalText);
    }
    
    // Method to get current notes text (useful for other components)
    public String getCurrentNotes() {
        return notesTextArea.getText();
    }
    
    // Method to set notes text programmatically
    public void setNotesText(String text) {
        notesTextArea.setText(text);
        originalText = text;
        saveBtn.setEnabled(false);
    }
}

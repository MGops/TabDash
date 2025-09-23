package src.ui.physical.frailty;

import javax.swing.*;
import java.awt.*;

import src.ui.TabDash;

public class FourATDialog extends JDialog {
    private TabDash tabDash;
    private FourATCalculator calculator;

    public FourATDialog(JFrame parent, TabDash tabDash) {
        super(parent, "4AT Calculator", true);
        this.tabDash = tabDash;
        initialiseDialog();
    }

    private void initialiseDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Create 4AT calculator
        calculator = new FourATCalculator(tabDash);
        
        // Create button panel for dialog
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton resetButton = new JButton("Reset");
        
        resetButton.addActionListener(e -> calculator.resetAssessment());
        
        buttonPanel.add(resetButton);
        
        // Set up dialog layout
        setLayout(new BorderLayout());
        add(calculator, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Size and position dialog
        setSize(600, 350);
        setLocationRelativeTo(getParent());
        setResizable(true);
    }

    public static void showDialog(Component parent, TabDash tabDash) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
        FourATDialog dialog = new FourATDialog(parentFrame, tabDash);
        dialog.setVisible(true);
    }
}
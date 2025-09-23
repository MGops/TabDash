package src.ui.physical.frailty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import src.ui.TabDash;

public class FourATCalculator extends JPanel {
    private TabDash tabDash;
    
    // 4AT Calculator components
    private ButtonGroup alertnessGroup;
    private ButtonGroup amt4Group;
    private ButtonGroup attentionGroup;
    private ButtonGroup acuteChangeGroup;
    private JLabel totalScoreLabel;
    private JRadioButton[] alertnessButtons;
    private JRadioButton[] amt4Buttons;
    private JRadioButton[] attentionButtons;
    private JRadioButton[] acuteChangeButtons;

    public FourATCalculator(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(new Color(255, 220, 220));
        initialiseComponents();
    }

    private void initialiseComponents() {
        // Main content panel with 3 columns
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Column 1: Alertness and AMT4
        JPanel column1 = createColumn1();
        
        // Column 2: Attention and Acute Change  
        JPanel column2 = createColumn2();
        
        // Column 3: Total Score
        JPanel column3 = createColumn3();
        
        contentPanel.add(column1);
        contentPanel.add(column2);
        contentPanel.add(column3);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createColumn1() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);
        
        // Alertness section
        JPanel alertnessPanel = new JPanel();
        alertnessPanel.setLayout(new BoxLayout(alertnessPanel, BoxLayout.Y_AXIS));
        alertnessPanel.setOpaque(false);
        
        JLabel alertnessTitle = new JLabel("Alertness");
        alertnessTitle.setFont(alertnessTitle.getFont().deriveFont(Font.BOLD, 12f));
        alertnessTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        alertnessPanel.add(alertnessTitle);
        alertnessPanel.add(Box.createVerticalStrut(8));
        
        alertnessGroup = new ButtonGroup();
        alertnessButtons = new JRadioButton[3];
        alertnessButtons[0] = new JRadioButton("Normal (0)");
        alertnessButtons[1] = new JRadioButton("Mild sleepy (0)");
        alertnessButtons[2] = new JRadioButton("Abnormal (4)");
        
        //Font smallFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        for (int i = 0; i < alertnessButtons.length; i++) {
            alertnessButtons[i].setOpaque(false);
            //alertnessButtons[i].setFont(smallFont);
            alertnessButtons[i].addActionListener(e -> calculateTotal());
            alertnessGroup.add(alertnessButtons[i]);
            alertnessPanel.add(alertnessButtons[i]);
            if (i < alertnessButtons.length - 1) {
                alertnessPanel.add(Box.createVerticalStrut(2));
            }
        }
        
        // AMT4 section
        JPanel amt4Panel = new JPanel();
        amt4Panel.setLayout(new BoxLayout(amt4Panel, BoxLayout.Y_AXIS));
        amt4Panel.setOpaque(false);
        
        JLabel amt4Title = new JLabel("AMT4 Mistakes");
        amt4Title.setFont(amt4Title.getFont().deriveFont(Font.BOLD, 12f));
        amt4Title.setAlignmentX(Component.CENTER_ALIGNMENT);
        amt4Panel.add(amt4Title);
        amt4Panel.add(Box.createVerticalStrut(8));
        
        amt4Group = new ButtonGroup();
        amt4Buttons = new JRadioButton[3];
        amt4Buttons[0] = new JRadioButton("0 errors (0)");
        amt4Buttons[1] = new JRadioButton("1 error (1)");
        amt4Buttons[2] = new JRadioButton("2+ errors (2)");
        
        for (int i = 0; i < amt4Buttons.length; i++) {
            amt4Buttons[i].setOpaque(false);
            //amt4Buttons[i].setFont(smallFont);
            amt4Buttons[i].addActionListener(e -> calculateTotal());
            amt4Group.add(amt4Buttons[i]);
            amt4Panel.add(amt4Buttons[i]);
            if (i < amt4Buttons.length - 1) {
                amt4Panel.add(Box.createVerticalStrut(2));
            }
        }
        
        column.add(alertnessPanel);
        column.add(Box.createVerticalStrut(20));
        column.add(amt4Panel);
        column.add(Box.createVerticalGlue());
        
        return column;
    }
    
    private JPanel createColumn2() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);
        
        // Attention section
        JPanel attentionPanel = new JPanel();
        attentionPanel.setLayout(new BoxLayout(attentionPanel, BoxLayout.Y_AXIS));
        attentionPanel.setOpaque(false);
        
        JLabel attentionTitle = new JLabel("Attention");
        attentionTitle.setFont(attentionTitle.getFont().deriveFont(Font.BOLD, 12f));
        attentionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        attentionPanel.add(attentionTitle);
        attentionPanel.add(Box.createVerticalStrut(8));
        
        attentionGroup = new ButtonGroup();
        attentionButtons = new JRadioButton[3];
        attentionButtons[0] = new JRadioButton("≥7 correct (0)");
        attentionButtons[1] = new JRadioButton("<7 correct (1)");
        attentionButtons[2] = new JRadioButton("Untestable (2)");
        
        //Font smallFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        for (int i = 0; i < attentionButtons.length; i++) {
            attentionButtons[i].setOpaque(false);
            //attentionButtons[i].setFont(smallFont);
            attentionButtons[i].addActionListener(e -> calculateTotal());
            attentionGroup.add(attentionButtons[i]);
            attentionPanel.add(attentionButtons[i]);
            if (i < attentionButtons.length - 1) {
                attentionPanel.add(Box.createVerticalStrut(2));
            }
        }
        
        // Acute Change section
        JPanel acuteChangePanel = new JPanel();
        acuteChangePanel.setLayout(new BoxLayout(acuteChangePanel, BoxLayout.Y_AXIS));
        acuteChangePanel.setOpaque(false);
        
        JLabel acuteChangeTitle = new JLabel("Acute Change");
        acuteChangeTitle.setFont(acuteChangeTitle.getFont().deriveFont(Font.BOLD, 12f));
        acuteChangeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        acuteChangePanel.add(acuteChangeTitle);
        acuteChangePanel.add(Box.createVerticalStrut(8));
        
        acuteChangeGroup = new ButtonGroup();
        acuteChangeButtons = new JRadioButton[2];
        acuteChangeButtons[0] = new JRadioButton("No (0)");
        acuteChangeButtons[1] = new JRadioButton("Yes (4)");
        
        for (int i = 0; i < acuteChangeButtons.length; i++) {
            acuteChangeButtons[i].setOpaque(false);
            //acuteChangeButtons[i].setFont(smallFont);
            acuteChangeButtons[i].addActionListener(e -> calculateTotal());
            acuteChangeGroup.add(acuteChangeButtons[i]);
            acuteChangePanel.add(acuteChangeButtons[i]);
            if (i < acuteChangeButtons.length - 1) {
                acuteChangePanel.add(Box.createVerticalStrut(2));
            }
        }
        
        column.add(attentionPanel);
        column.add(Box.createVerticalStrut(20));
        column.add(acuteChangePanel);
        column.add(Box.createVerticalGlue());
        
        return column;
    }
    
    private JPanel createColumn3() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setOpaque(false);
        
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setOpaque(false);
        
        JLabel scoreTitle = new JLabel("Total Score");
        scoreTitle.setFont(scoreTitle.getFont().deriveFont(Font.BOLD, 12f));
        scoreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(scoreTitle);
        scorePanel.add(Box.createVerticalStrut(15));
        
        totalScoreLabel = new JLabel("Select all options");
        totalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalScoreLabel.setFont(totalScoreLabel.getFont().deriveFont(Font.BOLD, 12f));
        totalScoreLabel.setForeground(Color.DARK_GRAY);
        totalScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        scorePanel.add(totalScoreLabel);
        
        column.add(Box.createVerticalGlue());
        column.add(scorePanel);
        column.add(Box.createVerticalGlue());
        
        return column;
    }
    
    private void calculateTotal() {
        // Check if all categories have selections
        if (alertnessGroup.getSelection() != null && 
            amt4Group.getSelection() != null && 
            attentionGroup.getSelection() != null && 
            acuteChangeGroup.getSelection() != null) {
            
            int total = 0;
            
            // Calculate alertness score
            if (alertnessButtons[0].isSelected()) total += 0;
            else if (alertnessButtons[1].isSelected()) total += 0;
            else if (alertnessButtons[2].isSelected()) total += 4;
            
            // Calculate AMT4 score
            if (amt4Buttons[0].isSelected()) total += 0;
            else if (amt4Buttons[1].isSelected()) total += 1;
            else if (amt4Buttons[2].isSelected()) total += 2;
            
            // Calculate attention score
            if (attentionButtons[0].isSelected()) total += 0;
            else if (attentionButtons[1].isSelected()) total += 1;
            else if (attentionButtons[2].isSelected()) total += 2;
            
            // Calculate acute change score
            if (acuteChangeButtons[0].isSelected()) total += 0;
            else if (acuteChangeButtons[1].isSelected()) total += 4;
            
            // Update display with interpretation
            String interpretation = getInterpretation(total);
            totalScoreLabel.setText("<html><center><b>" + total + "</b><br><i>" + interpretation + "</i></center></html>");
            
            // Color coding
            if (total >= 4) {
                totalScoreLabel.setForeground(Color.RED);
            } else {
                totalScoreLabel.setForeground(new Color(0, 128, 0)); // Dark green
            }
        } else {
            totalScoreLabel.setText("Select all options");
            totalScoreLabel.setForeground(Color.DARK_GRAY);
        }
    }
    
    private String getInterpretation(int score) {
        if (score >= 4) {
            return "Possible delirium or cognitive impairment";
        } else {
            return "Delirium unlikely";
        }
    }
    
    public void resetAssessment() {
        // Clear all selections
        alertnessGroup.clearSelection();
        amt4Group.clearSelection();
        attentionGroup.clearSelection();
        acuteChangeGroup.clearSelection();
        
        // Reset display
        totalScoreLabel.setText("Select all options");
        totalScoreLabel.setForeground(Color.DARK_GRAY);
    }
}
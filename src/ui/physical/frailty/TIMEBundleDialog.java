package src.ui.physical.frailty;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import src.ui.TabDash;

public class TIMEBundleDialog extends JDialog {
    private TabDash tabDash;

    public TIMEBundleDialog(JFrame parent, TabDash tabDash) {
        super(parent, "TIME Bundle", true);
        this.tabDash = tabDash;
        initialiseDialog();
    }

    private void initialiseDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        try {
            // Load the TIME_Bundle image from the same package
            java.net.URL imageURL = getClass().getResource("TIME_Bundle.png");
            
            if (imageURL != null) {
                BufferedImage originalImage = ImageIO.read(imageURL);
                
                // Set maximum dimensions (adjust these as needed)
                int maxWidth = 800;
                int maxHeight = 700;
                
                // Calculate scaled dimensions while maintaining aspect ratio
                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();
                
                double scaleWidth = (double) maxWidth / originalWidth;
                double scaleHeight = (double) maxHeight / originalHeight;
                double scale = Math.min(scaleWidth, scaleHeight);
                
                int scaledWidth = (int) (originalWidth * scale);
                int scaledHeight = (int) (originalHeight * scale);
                
                // Create scaled image
                Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(imageIcon);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                
                // Add image directly to center (no scroll pane needed)
                add(imageLabel, BorderLayout.CENTER);
                
            } else {
                // Image not found in package
                JLabel errorLabel = new JLabel("<html><center>TIME_Bundle.png not found<br>in src/ui/physical/frailty/ package</center></html>");
                errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                errorLabel.setVerticalAlignment(SwingConstants.CENTER);
                add(errorLabel, BorderLayout.CENTER);
            }
            
        } catch (IOException e) {
            // Error loading image
            JLabel errorLabel = new JLabel("<html><center>Error loading TIME_Bundle.png<br>" + e.getMessage() + "</center></html>");
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setVerticalAlignment(SwingConstants.CENTER);
            add(errorLabel, BorderLayout.CENTER);
        }
        
        // Create close button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Size and position dialog
        pack(); // Size to fit content
        setLocationRelativeTo(getParent());
        setResizable(true);
        
        // Set minimum size
        setMinimumSize(new Dimension(400, 300));
    }

    public static void showDialog(Component parent, TabDash tabDash) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
        TIMEBundleDialog dialog = new TIMEBundleDialog(parentFrame, tabDash);
        dialog.setVisible(true);
    }
}
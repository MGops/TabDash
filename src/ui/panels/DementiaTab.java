package src.ui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DementiaTab extends JPanel{
    private BufferedImage originalImage;
    private JLabel imageLabel;

    public DementiaTab() {
        setLayout(new BorderLayout());
        loadAndDisplayImage();
    }
    
    private void loadAndDisplayImage() {
        File imageFile = new File("data/dementia_pathway.png");

        try {
            originalImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image", e);
        }
        
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        add(imageLabel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImageToFit();
            }
        });

        SwingUtilities.invokeLater(this::resizeImageToFit);
            
    }

    private void resizeImageToFit() {
        
        // Get the current size of this panel
        Dimension panelSize = getSize();
        
        // Leave some margin (10 pixels on each side)
        int availableWidth = panelSize.width - 20;
        int availableHeight = panelSize.height - 20;
        
        // Don't resize if panel is too small
        if (availableWidth <= 0 || availableHeight <= 0) return;
        
        // Get original image dimensions
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Calculate scale factor to fit within available space while maintaining aspect ratio
        double scaleWidth = (double) availableWidth / originalWidth;
        double scaleHeight = (double) availableHeight / originalHeight;
        double scale = Math.min(scaleWidth, scaleHeight);
        
        // Calculate new dimensions
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);
        
        // Scale the image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(scaledIcon);
        
        // Force repaint
        revalidate();
        repaint();
    }
}

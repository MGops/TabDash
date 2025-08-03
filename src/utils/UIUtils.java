package src.utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public final class UIUtils {

    private UIUtils() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    
    /**
     * Creates a standardized info label for empty states or messages
     * @param text The message to display
     * @param color The text color
     * @return Formatted JLabel
     */
    public static JLabel createInfoLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.ITALIC, 11f));
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }


    /**
     * Capitalizes the first letter of a string
     * @param text the string to capitalize
     * @return the string with first letter capitalized, or original if null/empty
     */
    public static String capitaliseFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0,1).toUpperCase() +text.substring(1);
    }


    public static final Color INFO_GRAY = Color.GRAY;
}
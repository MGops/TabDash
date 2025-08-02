package src.utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public final class UIUtils {

    private UIUtils() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    public static JLabel createInfoLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.ITALIC, 11f));
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    public static final Color INFO_GRAY = Color.GRAY;
}

package src;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class MHAPanel extends JPanel{
    private TabDash tabDash;

    public MHAPanel(TabDash tabDash) {
        this.tabDash = tabDash;
        setLayout(new BorderLayout());
        initialiseComponents();
    }

    private void initialiseComponents() {
        JPanel topSection = createTopSection();
        JPanel middleSection = createMiddleSection();
        JPanel bottomSection = createBottomSection();

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));

        mainContainer.add(topSection);
        mainContainer.add(createSeparator());
        mainContainer.add(middleSection);
        mainContainer.add(createSeparator());
        mainContainer.add(bottomSection);
    
        add(mainContainer, BorderLayout.CENTER);
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return separator;
    }

    private JPanel createTopSection() {
        return new JPanel();
    }

    private JPanel createMiddleSection() {
            return new JPanel();
        }

    private JPanel createBottomSection() {
            return new JPanel();
        }
}

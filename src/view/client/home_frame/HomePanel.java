package view.client.home_frame;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private final JButton backToGameButton = new JButton("Back to game");
    public HomePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.setLayout(new GridLayout(2,2,5,5));

        for (int i = 0; i < 3; i++)
            this.add(new JButton("Hello : " + i));

        this.add(backToGameButton);
    }

    public JButton getBackToGameButton() {
        return backToGameButton;
    }
}

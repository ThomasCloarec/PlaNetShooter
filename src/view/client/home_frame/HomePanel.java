package view.client.home_frame;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private final JButton backToGameButton = new JButton("Back to game");
    public HomePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.setLayout(new GridLayout(1,3,5,5));

        this.add(new JButton("Hello"));

        JPanel center = new JPanel(new BorderLayout(5,5));
        center.setBackground(Color.lightGray);
        center.add(new JButton("Hello"), BorderLayout.NORTH);
        center.add(new JButton("Hello"), BorderLayout.CENTER);
        center.add(backToGameButton, BorderLayout.SOUTH);
        this.add(center);

        JPanel right = new JPanel(new GridLayout(4,1,5,5));
        right.setBackground(Color.lightGray);
        for (int i = 0; i < 4; i++) {
            right.add(new JButton("Hello"));
        }
        this.add(right);
    }

    public JButton getBackToGameButton() {
        return backToGameButton;
    }
}

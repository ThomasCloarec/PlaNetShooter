package view.client.game_frame.menu;

import javax.swing.*;
import java.awt.*;

public class GameMenuPanel extends JPanel {
    public GameMenuPanel() {
        super();
        this.setBackground(Color.gray);
        this.setLayout(new GridLayout(1,5));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        this.add(new JButton("Button"));
        for (int i = 0; i < 4 ; i++ )
            this.add(new JLabel());
    }
}
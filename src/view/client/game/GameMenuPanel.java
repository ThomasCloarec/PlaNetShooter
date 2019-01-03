package view.client.game;

import javax.swing.*;
import java.awt.*;

class GameMenuPanel extends JPanel {
    GameMenuPanel() {
        super();
        this.setBackground(Color.gray);
        this.setLayout(new GridLayout(1,5));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        this.add(new JButton("Button"));
        for (int i = 0; i < 4 ; i++ )
            this.add(new JLabel());
    }
}
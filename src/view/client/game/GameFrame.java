package view.client.game;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        super();
        this.setSize(768,432);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.add(new MenuGame());
        this.add(new Game());
        this.setVisible(true);
    }
}

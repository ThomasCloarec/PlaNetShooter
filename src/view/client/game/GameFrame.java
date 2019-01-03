package view.client.game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private MenuGame menuGame = new MenuGame();
    private GamePanel gamePanel = new GamePanel();

    public GameFrame() {
        super();
        this.setSize(768,432);
        this.setMinimumSize(new Dimension(574,330));
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.add(menuGame);
        this.add(gamePanel);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        menuGame.setSize(this.getWidth(), 30);
        gamePanel.setSize(this.getWidth(), this.getHeight()-30);
    }
}
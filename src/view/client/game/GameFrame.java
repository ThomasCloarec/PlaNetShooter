package view.client.game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        super();
        this.setTitle("PlaNetShooter Client");
        this.setSize(768,432);
        this.setMinimumSize(new Dimension(574,330));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        GameMenuPanel gameMenuPanel = new GameMenuPanel();
        gameMenuPanel.setPreferredSize(new Dimension(this.getWidth(), 30));
        this.add(gameMenuPanel, BorderLayout.NORTH);

        GamePanel gamePanel = new GamePanel();
        this.add(gamePanel, BorderLayout.CENTER);

        this.setVisible(true);
    }
}
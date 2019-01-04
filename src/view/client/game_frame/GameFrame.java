package view.client.game_frame;

import view.client.game_frame.game_only.GamePanel;
import view.client.game_frame.menu.GameMenuPanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        super();
        // TODO : Add the name of the Client in the GameFrame title
        this.setTitle("PlaNetShooter Client");
        this.setSize(768, 432);
        this.setMinimumSize(new Dimension(574, 330));
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
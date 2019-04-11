package view.client.game_frame;

import view.client.home_frame.HomePanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private final GamePanel gamePanel = new GamePanel();
    private final HomePanel homePanel = new HomePanel();
    private final CardLayout cardLayout = new CardLayout();
    private String clientName;

    public GameFrame (String clientName) {
        super();
        this.clientName = clientName;

        this.setTitle("PlaNetShooter Client : (" +this.clientName+ ")");

        this.setSize(768, 402);
        this.setMinimumSize(new Dimension(574, 300));
        this.setLocationRelativeTo(null);

        this.setLayout(cardLayout);
        this.add(homePanel);
        this.add(gamePanel);

        this.setVisible(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setIsClientAdmin(boolean isClientAdmin) {
        if (isClientAdmin)
            this.setTitle("ADMIN | PlaNetShooter Client : (" +this.clientName+ ")");
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }
}
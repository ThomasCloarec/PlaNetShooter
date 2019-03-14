package view.client.game_frame;

import view.client.home_frame.HomePanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private static boolean isClientAdmin = false;

    private final GamePanel gamePanel = new GamePanel();
    private final HomePanel homePanel = new HomePanel();
    private final CardLayout cardLayout = new CardLayout();

    public GameFrame (String clientName) {
        super();

        if (isClientAdmin)
            this.setTitle("ADMIN | PlaNetShooter Client : (" +clientName+ ")");
        else
            this.setTitle("PlaNetShooter Client : (" +clientName+ ")");

        this.setSize(768, 402);
        this.setMinimumSize(new Dimension(574, 300));
        this.setLocationRelativeTo(null);

        this.setLayout(cardLayout);
        this.add(gamePanel);
        this.add(homePanel);

        this.setVisible(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public static void setIsClientAdmin(boolean isClientAdmin) {
        GameFrame.isClientAdmin = isClientAdmin;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public HomePanel getHomePanel() {
        return homePanel;
    }
}
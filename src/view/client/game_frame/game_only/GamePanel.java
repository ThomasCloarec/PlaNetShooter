package view.client.game_frame.game_only;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static PlatformView[] platforms;

    public GamePanel() {
        super();
        this.setBackground(Color.lightGray);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (PlatformView platform : platforms) {
            if (platform != null) {
                g.fillRect((int)(platform.getRelativeX()*this.getWidth()),
                        (int)(platform.getRelativeY()*this.getHeight()),
                        (int)(platform.getRelativeWidth()*this.getWidth()),
                        (int)(platform.getRelativeHeight()*this.getHeight()));
            }
        }
    }

    public static void setPlatformsView(PlatformView[] platforms) {
        GamePanel.platforms = platforms;
    }

    public static void setEachPlatformView(int i, PlatformView platformView) {
        GamePanel.platforms[i] = platformView;
    }
}
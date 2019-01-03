package view.client.game;

import view.client.game.objects.PlatformView;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public static PlatformView[] platforms = new PlatformView[9];
    GamePanel() {
        super();
        this.setBackground(Color.lightGray);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (PlatformView platform : platforms) {
            if (platform != null) {
                g.fillRect((int)(platform.relativeX*this.getWidth()), (int)(platform.relativeY*this.getHeight()), (int)(platform.relativeWidth*this.getWidth()), (int)(platform.relativeHeight*this.getHeight()));
            }
        }
    }
}
package view.client.game_frame.game_only;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static PlatformView[] platforms;
    private static CharacterView characterView;

    public GamePanel() {
        super();
        this.setBackground(Color.lightGray);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (PlatformView platform : platforms) {
            if (platform != null) {
                // Will be replaced by some images to make it look better
                g.fillRect((int)(platform.getRelativeX()*this.getWidth()),
                        (int)(platform.getRelativeY()*this.getHeight()),
                        (int)(platform.getRelativeWidth()*this.getWidth()),
                        (int)(platform.getRelativeHeight()*this.getHeight()));
            }
        }

        if (characterView != null) {
            g.setColor(Color.red);
            g.fillRect((int)(characterView.getRelativeX()*this.getWidth()),
                    (int)(characterView.getRelativeY()*this.getHeight()),
                    (int)(characterView.getRelativeWidth()*this.getWidth()),
                    (int)(characterView.getRelativeHeight()*this.getHeight()));        }
    }

    public static void setPlatformsView(PlatformView[] platforms) {
        GamePanel.platforms = platforms;
    }

    public static void setEachPlatformView(int i, PlatformView platformView) {
        GamePanel.platforms[i] = platformView;
    }

    public static void setCharacterView(CharacterView characterView) {
        GamePanel.characterView = characterView;
    }
}
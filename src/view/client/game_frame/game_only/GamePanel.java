package view.client.game_frame.game_only;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private static PlatformView[] platforms;
    private static CharacterView characterView;

    public GamePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
                    characterView.setRelativeX(characterView.getRelativeX()+ 0.01f);
                else if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_LEFT)
                    characterView.setRelativeX(characterView.getRelativeX()- 0.01f);
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
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
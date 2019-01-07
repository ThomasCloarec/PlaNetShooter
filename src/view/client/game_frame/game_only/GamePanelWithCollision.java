package view.client.game_frame.game_only;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
class GamePanelWithCollision extends JPanel {
    private PlatformView[] platforms;
    private CharacterView characterView;

    public GamePanelWithCollision() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (PlatformView platform : platforms) {
            if (platform != null) {
                // Will be replaced by some images to make it look better
                g.setColor(Color.blue);
                //Platforms
                g.fillRect((int)(platform.getRelativeX()*this.getWidth()),
                        (int)(platform.getRelativeY()*this.getHeight()),
                        (int)(platform.getRelativeWidth()*this.getWidth()),
                        (int)(platform.getRelativeHeight()*this.getHeight()));

                g.setColor(Color.red);
                // Line top
                g.fillRect((int)(platform.getRelativeX()*this.getWidth()),
                        (int)(platform.getRelativeY()*this.getHeight()),
                        (int)(platform.getRelativeWidth()*this.getWidth()),
                        2);

                // Line bottom
                g.fillRect((int)(platform.getRelativeX()*this.getWidth()),
                        (int)((platform.getRelativeY()+platform.getRelativeHeight())*this.getHeight()-1),
                        (int)(platform.getRelativeWidth()*this.getWidth()),
                        2);

                // Line left
                g.fillRect((int)(platform.getRelativeX()*this.getWidth()),
                        (int)(platform.getRelativeY()*this.getHeight()),
                        2,
                        (int)(platform.getRelativeHeight()*this.getHeight()));

                // Line right
                g.fillRect((int)((platform.getRelativeX()+platform.getRelativeWidth())*this.getWidth()-3),
                        (int)(platform.getRelativeY()*this.getHeight()+1),
                        2,
                        (int)(platform.getRelativeHeight()*this.getHeight()));

            }
        }

        if (characterView != null) {
            g.setColor(Color.green);
            g.fillRect((int)(characterView.getRelativeX()*this.getWidth()),
                    (int)(characterView.getRelativeY()*this.getHeight()),
                    (int)(characterView.getRelativeWidth()*this.getWidth()),
                    (int)(characterView.getRelativeHeight()*this.getHeight()));
        }
    }

    public void setPlatformsView(PlatformView[] platforms) {
        this.platforms = platforms;
    }

    public void setEachPlatformView(int i, PlatformView platformView) {
        this.platforms[i] = platformView;
    }

    public void setCharacterView(CharacterView characterView) {
        this.characterView = characterView;
    }
}
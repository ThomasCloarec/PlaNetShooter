package view.client.game_frame.game_only;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private PlatformView[] platforms;
    private CharacterView characterView;

    public GamePanel() {
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
            }
        }

        if (characterView != null) {
            g.setColor(Color.green);
            g.fillRect((int)(characterView.getRelativeX()*this.getWidth()),
                    (int)(characterView.getRelativeY()*this.getHeight()),
                    (int)(characterView.getRelativeWidth()*this.getWidth()),
                    (int)(characterView.getRelativeHeight()*this.getHeight()));

            if (characterView.getNameLabel().getParent() == null)
                this.add(characterView.getNameLabel());

            // Could add the playerID on the hat/cap of the player
            // And the first letter of the playerName on it's t-shirt
            // Then we could have an array (visible through one of the menu button)
            // that show all the full playerNames referenced by the PlayerID
            // ALSO NEED TO PUT A RELATIVE FONT SIZE WHEN THE FRAME IS RESIZED
            characterView.getNameLabel().setLocation((int)((characterView.getRelativeX()+characterView.getRelativeWidth()/2)*this.getWidth()-characterView.getNameLabel().getWidth()/2), (int)((characterView.getRelativeY()+characterView.getRelativeHeight()/2)*this.getHeight())-characterView.getNameLabel().getHeight()/2);
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
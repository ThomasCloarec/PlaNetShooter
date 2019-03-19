package view.client.game_frame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterView {
    private float relativeX;
    private float relativeY;
    private final float relativeWidth;
    private final float relativeHeight;
    private final JLabel nameLabel = new JLabel();
    private final JLabel characterLabel = new JLabel();
    double scaleWidthCharacter = 0;
    private double scaleHeightCharacter = 0;
    private double horizontal_direction = 1;
    private final String classCharacter;
    private List<BulletView> bulletsViews = new ArrayList<>();
    private float health;
    private float characterIconWidth;
    private float characterIconHeight;

    public CharacterView(float relativeX, float relativeY, float relativeWidth, float relativeHeight, String name, String classCharacter, float health) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.classCharacter = classCharacter;
        this.health = health;

        nameLabel.setText(String.valueOf(name.charAt(0)));
    }

    class CharacterIcon extends ImageIcon {
        CharacterIcon(String filename) {
            super(CharacterView.class.getResource(filename));
            characterIconWidth = this.getIconWidth();
            characterIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            if(horizontal_direction == -1) {
                g2.translate(getIconWidth(), 0);
            }

            g2.scale(horizontal_direction*scaleWidthCharacter, scaleHeightCharacter);
            super.paintIcon(c, g2, x, y);
        }
    }

    float getRelativeX() {
        return relativeX;
    }

    float getRelativeY() {
        return relativeY;
    }

    float getRelativeWidth() {
        return relativeWidth;
    }

    float getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getCharacterLabel() {
        return characterLabel;
    }

    void setScaleWidthCharacter(double scaleWidthCharacter) {
        this.scaleWidthCharacter = scaleWidthCharacter;
    }
    void setScaleHeightCharacter(double scaleHeightCharacter) {
        this.scaleHeightCharacter = scaleHeightCharacter;
    }

    public void setHorizontal_direction(double horizontal_direction) {
        if(horizontal_direction != 0) {
            this.horizontal_direction = horizontal_direction;
            try {
                characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" +classCharacter.toLowerCase()+ "/run.gif"));
            }
            catch (NullPointerException e) {
                System.err.println("Can't find \"/view/resources/game/characters/" +classCharacter.toLowerCase()+ "/run.gif\" !");
            }
        }
        else {
            try {
                characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" +classCharacter.toLowerCase()+ "/idle.gif"));
            }
            catch (NullPointerException e) {
                System.err.println("Can't find \"/view/resources/game/characters/" +classCharacter.toLowerCase()+ "/idle.gif\" !");
            }
        }
    }

    public double getHorizontal_direction() {
        return horizontal_direction;
    }

    public List<BulletView> getBulletsViews() {
        return bulletsViews;
    }

    public void setBulletsViews(List<BulletView> bulletsViews) {
        this.bulletsViews = bulletsViews;
    }

    float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    float getCharacterIconWidth() {
        return characterIconWidth;
    }

    float getCharacterIconHeight() {
        return characterIconHeight;
    }

    String getClassCharacter() {
        return classCharacter;
    }
}

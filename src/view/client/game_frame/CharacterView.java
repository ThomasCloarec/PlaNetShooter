package view.client.game_frame;

import model.characters.ClassCharacters;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterView {
    private float relativeX;
    private float relativeY;
    private float relativeWidth;
    private float relativeHeight;
    private final JLabel characterLabel = new JLabel();
    double scaleWidthCharacter = 0;
    private double scaleHeightCharacter = 0;
    private double horizontal_direction = 1;
    private ClassCharacters classCharacter;
    private final List<BulletView> bulletsViews = new ArrayList<>();
    private float health;
    private float characterIconWidth;
    private float characterIconHeight;
    private Icon runCharacterIcon;
    private Icon idleCharacterIcon;
    private float ultimateLoading = 0;
    private boolean ultimate1Running = false;
    private boolean ultimate2Running = false;
    private boolean ultimate3Running = false;

    public CharacterView(float relativeX, float relativeY, float relativeWidth, float relativeHeight, @SuppressWarnings("unused") String name, ClassCharacters classCharacter, float health) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.classCharacter = classCharacter;
        this.health = health;
        this.runCharacterIcon = new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/run.gif");
        this.idleCharacterIcon = new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/idle.gif");
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
        if (horizontal_direction != 0) {
            this.horizontal_direction = horizontal_direction;
        }

        if (!ultimate1Running && !ultimate2Running && !ultimate3Running) {
            if (horizontal_direction != 0) {
                try {
                    characterLabel.setIcon(runCharacterIcon);
                } catch (NullPointerException e) {
                    System.err.println("Can't find \"/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/run.gif\" !");
                }
            } else {
                try {
                    characterLabel.setIcon(idleCharacterIcon);
                } catch (NullPointerException e) {
                    System.err.println("Can't find \"/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/idle.gif\" !");
                }
            }
        }
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        this.classCharacter = classCharacter;
        this.runCharacterIcon = new CharacterIcon("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/run.gif");
        this.idleCharacterIcon = new CharacterIcon("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/idle.gif");
    }

    public void ultimate1() {
        ultimate1Running = true;
        if (classCharacter.equals(ClassCharacters.ANGELO) || classCharacter.equals(ClassCharacters.TATITATOO))
            characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/ultimate1.gif"));
    }

    public void ultimate2() {
        ultimate2Running = true;
        if (classCharacter.equals(ClassCharacters.ANGELO)) {
            characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/ultimate2.gif"));
        }
    }

    public void ultimate3() {
        ultimate3Running = true;
        if (classCharacter.equals(ClassCharacters.ANGELO)) {
            characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/ultimate3.gif"));
        }
    }

    public double getHorizontal_direction() {
        return horizontal_direction;
    }

    public List<BulletView> getBulletsViews() {
        return bulletsViews;
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

    public ClassCharacters getClassCharacter() {
        return classCharacter;
    }

    public void setRelativeWidth(float relativeWidth) {
        this.relativeWidth = relativeWidth;
    }

    public void setRelativeHeight(float relativeHeight) {
        this.relativeHeight = relativeHeight;
    }

    float getUltimateLoading() {
        return ultimateLoading;
    }

    public void setUltimateLoading(float ultimateLoading) {
        this.ultimateLoading = ultimateLoading;
    }

    public void setUltimate1Running(boolean ultimate1Running) {
        this.ultimate1Running = ultimate1Running;
    }

    public void setUltimate2Running(boolean ultimate2Running) {
        this.ultimate2Running = ultimate2Running;
    }

    public void setUltimate3Running(boolean ultimate3Running) {
        this.ultimate3Running = ultimate3Running;
    }

    public boolean isUltimate1Running() {
        return ultimate1Running;
    }

    public boolean isUltimate2Running() {
        return ultimate2Running;
    }

    public boolean isUltimate3Running() {
        return ultimate3Running;
    }
}

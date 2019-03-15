package view.client.game_frame;

import model.characters.ClassCharacters;

import javax.swing.*;
import java.awt.*;

public class CharacterView {
    private float relativeX;
    private float relativeY;
    private final float RELATIVE_WIDTH;
    private final float RELATIVE_HEIGHT;
    private final JLabel nameLabel = new JLabel();
    private final JLabel characterLabel = new JLabel();
    double scaleWidthCharacter = 0;
    private double scaleHeightCharacter = 0;
    private double horizontal_direction = 1;
    private String classCharacter;

    public CharacterView(float relativeX, float relativeY, float relativeWidth, float relativeHeight, String name, String classCharacter) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.RELATIVE_WIDTH = relativeWidth;
        this.RELATIVE_HEIGHT = relativeHeight;
        this.classCharacter = classCharacter;

        nameLabel.setText(String.valueOf(name.charAt(0)));
    }

    class CharacterIcon extends ImageIcon {
        CharacterIcon(String filename) {
            super(CharacterView.class.getResource(filename));
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
        return RELATIVE_WIDTH;
    }

    float getRelativeHeight() {
        return RELATIVE_HEIGHT;
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
            if (this.classCharacter.equals(ClassCharacters.BOB.name()))
                characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/bob/run.gif"));
            else if (this.classCharacter.equals(ClassCharacters.MEDUSO.name()))
                characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/meduso/run.gif"));
        }
        else {
            if (this.classCharacter.equals(ClassCharacters.BOB.name()))
                characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/bob/idle.png"));
            else if (this.classCharacter.equals(ClassCharacters.MEDUSO.name()))
                characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/meduso/run.gif"));
        }
    }

    double getHorizontal_direction() {
        return horizontal_direction;
    }

    public void setClassCharacter(String classCharacter) {
        this.classCharacter = classCharacter;
        setHorizontal_direction(horizontal_direction);
    }
}

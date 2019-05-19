package view.client.game_frame;

import model.SolidObject;
import model.characters.ClassCharacters;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterView extends SolidObject {
    private final JLabel characterLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final JLabel underLifeBarLabel = new JLabel();
    private final JLabel lifeBarLabel = new JLabel();
    private final JLabel underUltimateBarLabel = new JLabel();
    private final JLabel ultimateBarLabel = new JLabel();
    private final JLabel ultimateReadyBarLabel = new JLabel();
    private double scaleWidthCharacter = 0;
    private double scaleHeightCharacter = 0;
    private double scaleWidthName = 0;
    private double scaleHeightName = 0;
    private double scaleWidthUnderBar = 0;
    private double scaleHeightUnderBar = 0;
    private double scaleWidthLifeBar = 0;
    private double scaleHeightLifeBar = 0;
    private double scaleWidthUltimateBar = 0;
    private double scaleHeightUltimateBar = 0;
    private double horizontalDirection = 1;
    private ClassCharacters classCharacter;
    private final List<BulletView> bulletsViews = new ArrayList<>();
    private float health;
    private float characterIconWidth;
    private float characterIconHeight;
    private float nameIconWidth;
    private float nameIconHeight;
    private float underBarIconWidth;
    private float underBarIconHeight;
    private float ultimateBarIconWidth;
    private float ultimateBarIconHeight;
    private float lifeBarIconWidth;
    private float lifeBarIconHeight;
    private Icon runCharacterIcon;
    private Icon idleCharacterIcon;
    private float ultimateLoading = 0;
    private boolean ultimate1Running = false;
    private boolean ultimate2Running = false;
    private boolean ultimate3Running = false;
    private List<Object> inventory = new ArrayList<>();

    public CharacterView(float relativeX, float relativeY, float relativeWidth, float relativeHeight, String name, ClassCharacters classCharacter, float health) {
        super();
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
        this.classCharacter = classCharacter;
        this.health = health;
        this.runCharacterIcon = new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/run.gif");
        this.idleCharacterIcon = new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/idle.gif");
        Icon nameCharacterIcon = new NameIcon("/view/resources/game/names/" + name + ".png");
        this.nameLabel.setIcon(nameCharacterIcon);
        Icon underLifeBarCharacterIcon = new UnderBarIcon("/view/resources/game/characters/bars/white.png");
        this.underLifeBarLabel.setIcon(underLifeBarCharacterIcon);
        Icon lifeBarCharacterIcon = new LifeBarIcon("/view/resources/game/characters/bars/red.png");
        this.lifeBarLabel.setIcon(lifeBarCharacterIcon);
        Icon underUltimateBarCharacterIcon = new UnderBarIcon("/view/resources/game/characters/bars/white.png");
        this.underUltimateBarLabel.setIcon(underUltimateBarCharacterIcon);
        Icon ultimateBarCharacterIcon = new UltimateBarIcon("/view/resources/game/characters/bars/blue.png");
        this.ultimateBarLabel.setIcon(ultimateBarCharacterIcon);
        Icon ultimateReadyBarCharacterIcon = new UltimateBarIcon("/view/resources/game/characters/bars/green.png");
        this.ultimateReadyBarLabel.setIcon(ultimateReadyBarCharacterIcon);
    }

    public double getHorizontalDirection() {
        return horizontalDirection;
    }

    public JLabel getCharacterLabel() {
        return characterLabel;
    }

    double getScaleWidthCharacter() {
        return scaleWidthCharacter;
    }

    void setScaleWidthCharacter(double scaleWidthCharacter) {
        this.scaleWidthCharacter = scaleWidthCharacter;
    }
    void setScaleHeightCharacter(double scaleHeightCharacter) {
        this.scaleHeightCharacter = scaleHeightCharacter;
    }

    double getScaleWidthName() {
        return scaleWidthName;
    }

    void setScaleWidthName(double scaleWidthName) {
        this.scaleWidthName = scaleWidthName;
    }

    double getScaleHeightName() {
        return scaleHeightName;
    }

    void setScaleHeightName(double scaleHeightName) {
        this.scaleHeightName = scaleHeightName;
    }

    float getNameIconHeight() {
        return nameIconHeight;
    }

    float getNameIconWidth() {
        return nameIconWidth;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    void setScaleWidthUnderBar(double scaleWidthUnderBar) {
        this.scaleWidthUnderBar = scaleWidthUnderBar;
    }

    void setScaleHeightUnderBar(double scaleHeightUnderBar) {
        this.scaleHeightUnderBar = scaleHeightUnderBar;
    }

    float getUnderBarIconHeight() {
        return underBarIconHeight;
    }

    float getUnderBarIconWidth() {
        return underBarIconWidth;
    }

    JLabel getUnderLifeBarLabel() {
        return underLifeBarLabel;
    }

    JLabel getUnderUltimateBarLabel() {
        return underUltimateBarLabel;
    }

    void setScaleWidthLifeBar(double scaleWidthLifeBar) {
        this.scaleWidthLifeBar = scaleWidthLifeBar;
    }

    void setScaleHeightLifeBar(double scaleHeightLifeBar) {
        this.scaleHeightLifeBar = scaleHeightLifeBar;
    }

    float getLifeBarIconHeight() {
        return lifeBarIconHeight;
    }

    float getLifeBarIconWidth() {
        return lifeBarIconWidth;
    }

    JLabel getLifeBarLabel() {
        return lifeBarLabel;
    }

    void setScaleWidthUltimateBar(double scaleWidthUltimateBar) {
        this.scaleWidthUltimateBar = scaleWidthUltimateBar;
    }

    void setScaleHeightUltimateBar(double scaleHeightUltimateBar) {
        this.scaleHeightUltimateBar = scaleHeightUltimateBar;
    }

    float getUltimateBarIconHeight() {
        return ultimateBarIconHeight;
    }

    float getUltimateBarIconWidth() {
        return ultimateBarIconWidth;
    }

    JLabel getUltimateBarLabel() {
        return ultimateBarLabel;
    }

    JLabel getUltimateReadyBarLabel() {
        return ultimateReadyBarLabel;
    }

    public void setHorizontalDirection(double horizontalDirection) {
        if (horizontalDirection == 0 && classCharacter.equals(ClassCharacters.TATITATOO) && ultimate1Running) {
            characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/tatitatoo/idle_ultimate1.png"));
        } else if (horizontalDirection != 0 && classCharacter.equals(ClassCharacters.TATITATOO) && ultimate1Running) {
            characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/tatitatoo/ultimate1.gif"));
        }

        if (horizontalDirection != 0) {
            this.horizontalDirection = horizontalDirection;
        }

        if (!ultimate1Running && !ultimate2Running && !ultimate3Running) {
            if (horizontalDirection != 0) {
                characterLabel.setIcon(runCharacterIcon);
            } else {
                characterLabel.setIcon(idleCharacterIcon);
            }
        }
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        this.classCharacter = classCharacter;
        this.runCharacterIcon = new CharacterIcon("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/run.gif");
        this.idleCharacterIcon = new CharacterIcon("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/idle.gif");
    }

    public void ultimate1() {
        this.ultimate1Running = true;
        try {
            this.characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" + this.classCharacter.name().toLowerCase() + "/ultimate1.gif"));
        } catch (NullPointerException ignored) {
        }
    }

    public void ultimate2() {
        this.ultimate2Running = true;
        try {
            characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" + this.classCharacter.name().toLowerCase() + "/ultimate2.gif"));
        } catch (NullPointerException ignored) {
        }
    }

    public void ultimate3() {
        this.ultimate3Running = true;
        try {
            this.characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" + this.classCharacter.name().toLowerCase() + "/ultimate3.gif"));
        } catch (NullPointerException ignored) {
        }
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
            if (horizontalDirection == -1) {
                g2.translate(getIconWidth(), 0);
            }

            g2.scale(horizontalDirection * scaleWidthCharacter, scaleHeightCharacter);
            super.paintIcon(c, g2, x, y);
        }
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

    class NameIcon extends ImageIcon {
        NameIcon(String filename) {
            super(CharacterView.class.getResource(filename));
            nameIconWidth = this.getIconWidth();
            nameIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scaleWidthName, scaleHeightName);
            super.paintIcon(c, g2, x, y);
        }
    }

    class LifeBarIcon extends ImageIcon {
        LifeBarIcon(String filelifeBar) {
            super(CharacterView.class.getResource(filelifeBar));
            lifeBarIconWidth = this.getIconWidth();
            lifeBarIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scaleWidthLifeBar, scaleHeightLifeBar);
            super.paintIcon(c, g2, x, y);
        }
    }

    class UltimateBarIcon extends ImageIcon {
        UltimateBarIcon(String fileUltimateBar) {
            super(CharacterView.class.getResource(fileUltimateBar));
            ultimateBarIconWidth = this.getIconWidth();
            ultimateBarIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scaleWidthUltimateBar, scaleHeightUltimateBar);
            super.paintIcon(c, g2, x, y);
        }
    }

    class UnderBarIcon extends ImageIcon {
        UnderBarIcon(String fileUnderBar) {
            super(CharacterView.class.getResource(fileUnderBar));
            underBarIconWidth = this.getIconWidth();
            underBarIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scaleWidthUnderBar, scaleHeightUnderBar);
            super.paintIcon(c, g2, x, y);
        }
    }

    public ClassCharacters getClassCharacter() {
        return classCharacter;
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

    boolean isUltimate1Running() {
        return ultimate1Running;
    }

    boolean isUltimate2Running() {
        return ultimate2Running;
    }

    boolean isUltimate3Running() {
        return ultimate3Running;
    }

    List<Object> getInventory() {
        return inventory;
    }

    public void setInventory(List<Object> inventory) {
        this.inventory = inventory;
    }
}

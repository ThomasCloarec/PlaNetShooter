package view.client.game_frame;

import model.SolidObject;
import model.bullets.Bullet;

import javax.swing.*;
import java.awt.*;

public class BulletView extends SolidObject {
    private float relativeX;
    private float relativeY;
    private final JLabel bulletLabel = new JLabel();
    private double scaleWidthCharacter = 0;
    private double scaleHeightCharacter = 0;
    private float relativeBulletStartX;
    private float relativeBulletStartY;

    public BulletView(float relativeX, float relativeY, float relativeBulletStartX, float relativeBulletStartY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeBulletStartX = relativeBulletStartX;
        this.relativeBulletStartY = relativeBulletStartY;
    }

    private class BulletIcon extends ImageIcon {
        BulletIcon(String filename) {
            super(BulletView.class.getResource(filename));
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scaleWidthCharacter, scaleHeightCharacter);
            super.paintIcon(c, g2, x, y);
        }
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    @SuppressWarnings("SameReturnValue")
    public float getRelativeWidth() {
        return new Bullet().getRelativeWidth();
    }

    public float getRelativeHeight() {
        return new Bullet().getRelativeHeight();
    }

    void setScaleWidthCharacter(double scaleWidthCharacter) {
        this.scaleWidthCharacter = scaleWidthCharacter;
    }

    void setScaleHeightCharacter(double scaleHeightCharacter) {
        this.scaleHeightCharacter = scaleHeightCharacter;
    }

    public float getRelativeBulletStartX() {
        return relativeBulletStartX;
    }

    public float getRelativeBulletStartY() {
        return relativeBulletStartY;
    }

    public float getRelativeMaxRange() {
        // percentage of game diagonal length
        return 1f / 3f;
    }
}

package view.client.game_frame;

import model.SolidObject;

import javax.swing.*;
import java.awt.*;

public class BulletView extends SolidObject {
    private float relativeX;
    private float relativeY;
    private final JLabel bulletLabel = new JLabel();
    private double scaleWidthBullet = 0;
    private double scaleHeightBullet = 0;
    private float bulletIconWidth;
    private float bulletIconHeight;
    private float relativeWidth;
    private float relativeHeight;

    public BulletView(float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
    }

    private class BulletIcon extends ImageIcon {
        BulletIcon(String filename) {
            super(BulletView.class.getResource(filename));
            bulletIconWidth = this.getIconWidth();
            bulletIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scaleWidthBullet, scaleHeightBullet);
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

    void setScaleWidthBullet(double scaleWidthBullet) {
        this.scaleWidthBullet = scaleWidthBullet;
    }

    void setScaleHeightBullet(double scaleHeightBullet) {
        this.scaleHeightBullet = scaleHeightBullet;
    }

    public JLabel getBulletLabel() {
        return bulletLabel;
    }

    public void setIcon(String filename) {
        bulletLabel.setIcon(new BulletView.BulletIcon(filename));
    }

    float getBulletIconWidth() {
        return bulletIconWidth;
    }

    float getBulletIconHeight() {
        return bulletIconHeight;
    }

    @Override
    public float getRelativeWidth() {
        return relativeWidth;
    }

    @Override
    public float getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeWidth(float relativeWidth) {
        this.relativeWidth = relativeWidth;
    }

    public void setRelativeHeight(float relativeHeight) {
        this.relativeHeight = relativeHeight;
    }
}



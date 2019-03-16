package view.client.game_frame;

import model.bullets.Bullet;

public class BulletView {
    private float relativeX;
    private float relativeY;

    public BulletView(float relativeX, float relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
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
}

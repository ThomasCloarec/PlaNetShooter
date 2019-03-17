package model.bullets;

import model.SolidObject;

public class Bullet extends SolidObject {
    private float relativeX;
    private float relativeY;
    private static final float SPEED  = 0.005f;
    private float movementX;
    private float movementY;
    private float relativeBulletStartX;
    private float relativeBulletStartY;

    public Bullet() {
    }

    public Bullet(float relativeX, float relativeY, float movementX, float movementY, float relativeBulletStartX, float relativeBulletStartY) {
        this();
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.movementX = movementX;
        this.movementY = movementY;
        this.relativeBulletStartX = relativeBulletStartX;
        this.relativeBulletStartY = relativeBulletStartY;
    }

    public static float getSPEED() {
        return SPEED;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getMovementY() {
        return movementY;
    }

    @SuppressWarnings("SameReturnValue")
    public float getRelativeWidth() {
        return 0.005f;
    }

    public float getRelativeHeight() {
        return 0.005f * 768f / 372f;
    }

    public float getMovementX() {
        return movementX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    @SuppressWarnings("SameReturnValue")
    public static float getShotPerSecond() {
        return 5f;
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

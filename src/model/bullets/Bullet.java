package model.bullets;

import model.SolidObject;

public class Bullet extends SolidObject {
    private float speed  = 0.01f;
    private float movementX;
    private float movementY;
    private float relativeBulletStartX;
    private float relativeBulletStartY;
    private float bulletRangeRatio;
    private float damage = 0.1f;
    private long bulletLifeTime = 0;

    public Bullet() {
        super();
        this.relativeWidth = 0.01f;
        this.relativeHeight = 0.01f * 768f / 372f;
    }

    public void setBulletLifeTime(long bulletLifeTime) { this.bulletLifeTime = bulletLifeTime; }

    public long getBulletLifeTime() { return bulletLifeTime; }

    public float getMovementY() {
        return movementY;
    }

    public float getMovementX() {
        return movementX;
    }

    public float getRelativeBulletStartX() {
        return relativeBulletStartX;
    }

    public float getRelativeBulletStartY() {
        return relativeBulletStartY;
    }

    @SuppressWarnings("SameReturnValue")
    public float getRelativeMaxRange() {
        return 0.2f;
    }

    public float getBulletRangeRatio() {
        return bulletRangeRatio;
    }

    public void setMovementX(float movementX) {
        this.movementX = movementX;
    }

    public void setMovementY(float movementY) {
        this.movementY = movementY;
    }

    public void setBulletRangeRatio(float bulletRangeRatio) {
        this.bulletRangeRatio = bulletRangeRatio;
    }

    public void setRelativeBulletStartX(float relativeBulletStartX) {
        this.relativeBulletStartX = relativeBulletStartX;
        this.relativeX = relativeBulletStartX;
    }

    public void setRelativeBulletStartY(float relativeBulletStartY) {
        this.relativeBulletStartY = relativeBulletStartY;
        this.relativeY = relativeBulletStartY;
    }

    public float getSpeed() { return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}

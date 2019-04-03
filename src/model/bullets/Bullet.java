package model.bullets;

import model.SolidObject;

public class Bullet extends SolidObject {
    private float relativeX;
    private float relativeY;
    private float speed  = 0.01f;
    private float movementX;
    private float movementY;
    private float relativeBulletStartX;
    private float relativeBulletStartY;
    private float bulletRangeRatio;
    private float relativeWidth = 0.008f;
    private float relativeHeight = 0.008f * 768f / 372f;
    private float damage = 0.1f;

    public Bullet() {
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getMovementY() {
        return movementY;
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

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}

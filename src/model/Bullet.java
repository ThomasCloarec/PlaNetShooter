package model;

public class Bullet extends SolidObject {
    private float speed  = 0.01f;
    private float movementX;
    private float movementY;
    private float relativeBulletStartX;
    private float relativeBulletStartY;
    private float range;
    private float damage = 0.1f;
    private long lifeTime = 0;

    public Bullet() {
        super();
        this.relativeWidth = 0.01f;
        this.relativeHeight = 0.01f * 768f / 372f;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

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

    public float getRange() {
        return range;
    }

    public void setMovementX(float movementX) {
        this.movementX = movementX;
    }

    public void setMovementY(float movementY) {
        this.movementY = movementY;
    }

    public void setRange(float range) {
        this.range = range;
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

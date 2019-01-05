package model.characters;

public class Character {
    private static final float relativeWidth = 0.025f;
    private static final float relativeHeight = 0.1f;
    private float relativeX = 0.4875f;
    private float relativeY = 0.3f;

    public Character() {
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public static float getRelativeWidth() {
        return relativeWidth;
    }

    public static float getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }
}
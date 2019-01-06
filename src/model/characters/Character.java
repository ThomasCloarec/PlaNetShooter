package model.characters;

public class Character {
    private static final float RELATIVE_SPEED = 0.004f;
    private static final float RELATIVE_WIDTH = 0.025f;
    private static final float RELATIVE_HEIGHT = 0.1f;
    private float relativeX = 0.45f;
    private float relativeY = 0.1f;

    public Character() {
    }

    @Override
    public String toString() {
        return "Character (" +relativeX+ ", " +relativeY+ ", " +RELATIVE_WIDTH+ ", " +RELATIVE_HEIGHT+ ", " +RELATIVE_SPEED+ ")";
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public static float getRelativeWidth() {
        return RELATIVE_WIDTH;
    }

    public static float getRelativeHeight() {
        return RELATIVE_HEIGHT;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    static float getRelativeSpeed() {
        return RELATIVE_SPEED;
    }
}
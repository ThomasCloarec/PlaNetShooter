package model.characters;

public class Character {
    private static final float RELATIVE_WIDTH = 0.025f;
    private static final float RELATIVE_HEIGHT = 0.1f;
    private float relativeX = 0.45f;

    public Character() {
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return 0.1f;
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

    public static float getRelativeSpeed() {
        return 0.003f;
    }
}
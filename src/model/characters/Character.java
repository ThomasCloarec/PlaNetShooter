package model.characters;

public class Character {
    private static final float relativeWidth = 0.025f;
    private static final float relativeHeight = 0.1f;

    public Character() {
    }

    public float getRelativePositionX() {
        return 0.4875f;
    }

    public float getRelativePositionY() {
        return 0.3f;
    }

    public static float getRelativeWidth() {
        return relativeWidth;
    }

    public static float getRelativeHeight() {
        return relativeHeight;
    }
}
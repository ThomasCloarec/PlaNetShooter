package model.characters;

@SuppressWarnings("unused")
public class Character {
    private static final float relativeWidth = 0.025f;
    private static final float relativeHeight = 0.1f;
    private float relativePositionX = 0.4875f;
    private float relativePositionY = 0.3f;

    public Character() {
    }

    public float getRelativePositionX() {
        return relativePositionX;
    }

    public float getRelativePositionY() {
        return relativePositionY;
    }

    public static float getRelativeWidth() {
        return relativeWidth;
    }

    public static float getRelativeHeight() {
        return relativeHeight;
    }
}
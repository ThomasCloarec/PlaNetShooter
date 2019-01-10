package model.characters;

@SuppressWarnings("CanBeFinal")
public class PlayableCharacter {
    private static final float RELATIVE_MAX_SPEED = 0.0015f;
    private static final float RELATIVE_SPEED_GROWTH = RELATIVE_MAX_SPEED/60;
    private static final float RELATIVE_JUMP_STRENGTH = RELATIVE_MAX_SPEED*3f;
    private static final float RELATIVE_WIDTH = 0.025f;
    private static final float RELATIVE_HEIGHT = 0.1f;
    private float relativeX = 0.45f;
    private float relativeY = 0.1f;
    private String name;

    public PlayableCharacter(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PlayableCharacter (" +relativeX+ ", " +relativeY+ ", " +RELATIVE_WIDTH+ ", " +RELATIVE_HEIGHT+ ", " +RELATIVE_MAX_SPEED+ ")";
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

    public static float getRelativeMaxSpeed() {
        return RELATIVE_MAX_SPEED;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public static float getRelativeSpeedGrowth() {
        return RELATIVE_SPEED_GROWTH;
    }

    public static float getRelativeJumpStrength() {
        return RELATIVE_JUMP_STRENGTH;
    }

    public String getName() {
        return name;
    }
}
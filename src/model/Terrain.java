package model;

public class Terrain {
    private static final float RELATIVE_FRICTION = 0.005f / 5f;
    private static final float RELATIVE_MAX_GRAVITY = 0.013f;
    private static final float RELATIVE_GRAVITY_GROWTH = RELATIVE_MAX_GRAVITY/30;

    public static float getRelativeGravityGrowth() {
        return RELATIVE_GRAVITY_GROWTH;
    }

    public static float getRelativeMaxGravity() {
        return RELATIVE_MAX_GRAVITY;
    }

    public static float getRelativeFriction() {
        return RELATIVE_FRICTION;
    }
}

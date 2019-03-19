package model;

public class Terrain {
    private static final float RELATIVE_FRICTION = 0.0025f / 20f *2f;
    private static final float RELATIVE_MAX_GRAVITY = 0.0095f;
    private static final float RELATIVE_GRAVITY_GROWTH = RELATIVE_MAX_GRAVITY/60;

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

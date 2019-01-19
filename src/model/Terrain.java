package model;

import model.characters.PlayableCharacter;

public class Terrain {
    private static final float RELATIVE_FRICTION = PlayableCharacter.getRelativeSpeedGrowth()*2;
    private static final float RELATIVE_MAX_GRAVITY = 0.0175f;
    private static final float RELATIVE_GRAVITY_GROWTH = RELATIVE_MAX_GRAVITY/35;

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

package model;

import model.characters.PlayableCharacter;

public class Terrain {
    private static final float RELATIVE_FRICTION = PlayableCharacter.getRelativeSpeedGrowth();
    private static final float RELATIVE_MAX_GRAVITY = PlayableCharacter.getRelativeJumpStrength();
    private static final float RELATIVE_GRAVITY_GROWTH = RELATIVE_MAX_GRAVITY/85;

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

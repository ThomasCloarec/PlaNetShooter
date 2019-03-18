package model.characters;

import model.SolidObject;
import model.bullets.Bullet;

import java.util.ArrayList;
import java.util.List;

public class PlayableCharacter extends SolidObject {
    private static final float RELATIVE_MAX_SPEED = 0.0025f;
    private static final float RELATIVE_SPEED_GROWTH = RELATIVE_MAX_SPEED/20;
    private static final float RELATIVE_JUMP_STRENGTH = 0.0090f;
    private static final float RELATIVE_WIDTH = 0.05f;
    private static final float RELATIVE_HEIGHT = 0.05f*768f/372f;
    private String classCharacter = ClassCharacters.BOB.name();
    private float relativeX = 0.45f;
    private float relativeY = 0.1f;
    private String name;
    private double horizontal_direction = 1;
    private final List<Bullet> bullets = new ArrayList<>();
    private float health = 1f;

    // Default constructor used for reflection (by Kryo serialization)
    private PlayableCharacter() {
    }

    public PlayableCharacter(String name) {
        this();
        this.name = name;
    }

    @Override
    public String toString() {
        return "PlayableCharacter (" +relativeX+ ", " +relativeY+ ", " +RELATIVE_WIDTH+ ", " +RELATIVE_HEIGHT+ ", " +RELATIVE_MAX_SPEED+ ", " +name+ ")";
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public float getRelativeWidth() {
        return RELATIVE_WIDTH;
    }

    public float getRelativeHeight() {
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

    public String getClassCharacter() {
        return this.classCharacter;
    }

    public void setClassCharacter(String classCharacter) {
        this.classCharacter = classCharacter;
    }

    public void setHorizontal_direction(double horizontal_direction) {
        this.horizontal_direction = horizontal_direction;
    }

    public double getHorizontal_direction() {
        return horizontal_direction;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }
}
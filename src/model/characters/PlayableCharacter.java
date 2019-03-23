package model.characters;

import model.SolidObject;
import model.bullets.Bullet;

import java.util.ArrayList;
import java.util.List;

public class PlayableCharacter extends SolidObject {
    private float relativeMaxSpeed = 0.0025f;
    private final float RELATIVE_SPEED_GROWTH = relativeMaxSpeed/20;
    private static final float RELATIVE_JUMP_STRENGTH = 0.0090f;
    private float relativeWidth = 0.05f;
    private float relativeHeight = 0.05f*768f/372f;
    private ClassCharacters classCharacter;
    private float relativeX = 0.45f;
    private float relativeY = 0.1f;
    private String name;
    private double horizontal_direction = 1;
    private final List<Bullet> bullets = new ArrayList<>();
    private float health = 1f;
    private float attackNumberPerSecond = 4f;

    // Default constructor used for reflection (by Kryo serialization)
    private PlayableCharacter() {
    }

    public PlayableCharacter(String name) {
        this();
        this.name = name;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public float getRelativeWidth() {
        return relativeWidth;
    }

    public float getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public float getRelativeMaxSpeed() {
        return relativeMaxSpeed;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public float getRelativeSpeedGrowth() {
        return RELATIVE_SPEED_GROWTH;
    }

    public static float getRelativeJumpStrength() {
        return RELATIVE_JUMP_STRENGTH;
    }

    public String getName() {
        return name;
    }

    public ClassCharacters getClassCharacter() {
        return this.classCharacter;
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        this.classCharacter = classCharacter;
        if (this.classCharacter.equals(ClassCharacters.ANGELO)) {
            this.relativeWidth = 200f / 308f * 0.06f;
            this.relativeHeight = 200f / 180f * 0.06625f * 768f / 372f;
            this.relativeMaxSpeed = 0.002f;
            this.attackNumberPerSecond = 2f;
        }
        else if (this.classCharacter.equals(ClassCharacters.TATITATOO)) {
            this.relativeWidth = 0.05f;
            this.relativeHeight = 0.05f*768f/372f;
            this.relativeMaxSpeed = 0.003f;
            this.attackNumberPerSecond = 4f;
        }
        else {
            this.relativeWidth = 0.05f;
            this.relativeHeight = 0.05f*768f/372f;
            this.relativeMaxSpeed = 0.0025f;
            this.attackNumberPerSecond = 4f;
        }

        this.setRelativeY(-1.15f);
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

    public float getAttackNumberPerSecond() {
        return attackNumberPerSecond;
    }
}
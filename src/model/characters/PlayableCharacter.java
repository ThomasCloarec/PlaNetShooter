package model.characters;

import model.SolidObject;
import model.bullets.Bullet;

import java.util.ArrayList;
import java.util.List;

public class PlayableCharacter extends SolidObject {
    private final static int MAX_BULLET_NUMBER_PER_PLAYER = 100;
    private float relativeMaxSpeed = 0.004f;
    private final float RELATIVE_SPEED_GROWTH = relativeMaxSpeed/10;
    private float relativeJumpStrength = 0.013f;
    private float relativeWidth = 0.04f;
    private float relativeHeight = 0.04f*768f/372f;
    private ClassCharacters classCharacter;
    private float relativeX = 0.45f;
    private float relativeY = 0.1f;
    private String name;
    private double horizontal_direction = 1;
    private List<Bullet> bullets = new ArrayList<>();
    private float health = 1f;
    private float attackNumberPerSecond = 4f;
    private float ultimateLoading = 0;
    private boolean atHome = true;

    // Default constructor used for reflection (by Kryo serialization)
    public PlayableCharacter() {
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

    public void setRelativeWidth(float relativeWidth) {
        this.relativeWidth = relativeWidth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelativeHeight(float relativeHeight) {
        this.relativeHeight = relativeHeight;
    }

    public float getRelativeSpeedGrowth() {
        return RELATIVE_SPEED_GROWTH;
    }

    public float getRelativeJumpStrength() {
        return relativeJumpStrength;
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
            this.relativeWidth = 200f / 308f * 0.048f;
            this.relativeHeight = 200f / 180f * 0.053f * 768f / 372f;
            this.relativeMaxSpeed = 0.0035f;
            this.attackNumberPerSecond = 3f;
        }
        else if (this.classCharacter.equals(ClassCharacters.TATITATOO)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 0.04f*768f/372f;
            this.relativeMaxSpeed = 0.0045f;
            this.attackNumberPerSecond = 4f;
        }
        else {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 0.04f*768f/372f;
            this.relativeMaxSpeed = 0.004f;
            this.attackNumberPerSecond = 4f;
        }
    }

    public void ultimate2() {
        if (classCharacter.equals(ClassCharacters.ANGELO)) {
            this.relativeWidth = 160f / 120f * 0.036f;
            this.relativeHeight = 120f / 160f * 0.048f * 768f / 372f;
            this.relativeMaxSpeed = 0f;
            this.relativeJumpStrength = 0f;
            this.attackNumberPerSecond = 1000f;
        }
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

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
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

    public float getUltimateLoading() {
        return ultimateLoading;
    }

    public void setUltimateLoading(float ultimateLoading) {
        this.ultimateLoading = ultimateLoading;
    }

    @SuppressWarnings("SameReturnValue")
    public float getUltimateLoadingPerSecond() {
        return 0.2f;
    }

    public void setAtHome(boolean atHome) {
        this.atHome = atHome;
    }

    public boolean isAtHome() {
        return atHome;
    }

    public static int getMaxBulletNumberPerPlayer() {
        return MAX_BULLET_NUMBER_PER_PLAYER;
    }
}
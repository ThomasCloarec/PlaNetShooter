package model.characters;

import model.SolidObject;
import model.bullets.Bullet;

import java.util.ArrayList;
import java.util.List;

public class PlayableCharacter extends SolidObject {
    private float relativeMaxSpeed = 0.0025f;
    private final float RELATIVE_SPEED_GROWTH = relativeMaxSpeed/20;
    private float relativeJumpStrength = 0.0090f;
    private float relativeWidth = 0.05f;
    private float relativeHeight = 0.05f*768f/372f;
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
            this.relativeWidth = 200f / 308f * 0.06f;
            this.relativeHeight = 200f / 180f * 0.06625f * 768f / 372f;
            this.relativeMaxSpeed = 0.002f;
            this.attackNumberPerSecond = 3f;
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

        this.relativeJumpStrength = 0.0090f;
    }

    public void ultimate2() {
        if (classCharacter.equals(ClassCharacters.ANGELO)) {
            this.relativeWidth = 160f / 120f * 0.045f;
            this.relativeHeight = 120f / 160f * 0.060f * 768f / 372f;
            this.relativeMaxSpeed = 0f;
            this.relativeJumpStrength = 0f;
            this.attackNumberPerSecond = 80f;
            // MAX attackNumberPerSecond ~ 50, if exceeded, it will result in a crash | A FIX IS ACTIVELY RESEARCHED, consider spliting packets to make them smaller in order to solve this issue
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
}
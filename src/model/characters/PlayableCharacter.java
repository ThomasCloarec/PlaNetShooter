package model.characters;

import model.SolidObject;
import model.bullets.Bullet;

import java.util.ArrayList;
import java.util.List;

public class PlayableCharacter extends SolidObject {
    private final static int MAX_BULLET_NUMBER_PER_PLAYER = 10;
    private float relativeMaxSpeed = 0.004f;
    private final float RELATIVE_SPEED_GROWTH = relativeMaxSpeed/10;
    private float relativeJumpStrength = 0.013f;
    private float relativeWidth = 0.04f;
    private float relativeHeight = 0.04f*768f/372f;
    private ClassCharacters classCharacter;
    private float relativeX = 0.45f;
    private float relativeY = 0.1f;
    private String name;
    private double horizontalDirection = 1;
    private double lastHorizontalDirection;
    private List<Bullet> bullets = new ArrayList<>();
    private float maxHealth = 1f;
    private float health = maxHealth;
    private float attackNumberPerSecond = 4f;
    private float ultimateLoading = 0;
    private boolean atHome = true;
    private boolean ultimate1Running = false;
    private boolean ultimate2Running = false;
    private boolean ultimate3Running = false;
    private int ultimate1DurationMillis;
    private long ultimate1StartTimeMillis;
    private int ultimate2DurationMillis;
    private long ultimate2StartTimeMillis;
    private int ultimate3DurationMillis;
    private long ultimate3StartTimeMillis;
    private boolean classCharacterChanged = false;

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

        this.relativeJumpStrength = 0.013f;
        this.maxHealth = 1f;

        if (this.classCharacter.equals(ClassCharacters.ANGELO)) {
            this.relativeWidth = 0.03f;
            this.relativeHeight = 140f/80f * 0.035f * 768f/372f;
            this.relativeMaxSpeed = 0.0035f;
            this.attackNumberPerSecond = 3f;
        }
        else if (this.classCharacter.equals(ClassCharacters.TATITATOO)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 200f/200f * 0.04f * 768f/372f;
            this.relativeMaxSpeed = 0.0045f;
            this.attackNumberPerSecond = 1f;
        }
        else if (this.classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 200f / 200f * 0.04f * 768f / 372f;
            this.relativeMaxSpeed = 0.004f;
            this.attackNumberPerSecond = 3f;

            if (!(ultimate1Running && ultimate2Running && ultimate3Running)) {
                this.relativeX += 0.045f - 0.04f;
                this.relativeY += 0.044f * 768f / 372f - 0.04f * 768f / 372f;
            }
        }
        else {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 200f/200f * 0.04f * 768f/372f;
            this.relativeMaxSpeed = 0.004f;
            this.attackNumberPerSecond = 4f;
        }

        this.ultimate1Running = false;
        this.ultimate2Running = false;
        this.ultimate3Running = false;
    }

    public void ultimate1() {
        if (classCharacter.equals(ClassCharacters.ANGELO)) {
            this.ultimate1DurationMillis = 1400;
            this.relativeWidth = 0.0386f;
            this.relativeHeight = 140f/80f * 0.035f * 768f/372f;
            this.relativeMaxSpeed = 0f;
            this.relativeJumpStrength = 0f;
            this.attackNumberPerSecond = 0f;
        }
        else if (classCharacter.equals(ClassCharacters.TATITATOO)) {
            this.ultimate1DurationMillis = 10_000;
            this.relativeWidth = 0.04f;
            this.relativeHeight = 200f/200f * 0.04f * 768f/372f;
            this.relativeMaxSpeed = 0.0055f;
            this.attackNumberPerSecond = 0f;
            this.maxHealth = 2f;
        }
        else if (classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.ultimate1DurationMillis = 660;
            this.relativeWidth = 0.05f;
            this.relativeHeight = 200f/200f * 0.044f * 768f/372f;
            this.attackNumberPerSecond = 0f;
            this.relativeX -= (0.045f - 0.04f);
            this.relativeY -= (0.044f * 768f/372f - 0.04f * 768f/372f);
        }
        else {
            this.ultimate1DurationMillis = 0;
        }
        this.ultimate1StartTimeMillis = System.currentTimeMillis();
        this.ultimate1Running = true;
    }

    public void ultimate2() {
        if (classCharacter.equals(ClassCharacters.ANGELO)) {
            this.ultimate2DurationMillis = 10_000;
            this.relativeWidth = 0.0386f;
            this.relativeHeight = 76f/102f * 0.044f * 768f/372f;
            this.attackNumberPerSecond = 8f;
            this.relativeY += (140f/80f * 0.035f * 768f/372f - 76f/102f * 0.044f * 768f/372f);
        }
        else if (classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.ultimate2DurationMillis = 5000;
            this.relativeWidth = 0.045f;
            this.relativeHeight = 200f / 200f * 0.045f * 768f / 372f;
            this.attackNumberPerSecond = 1f;
        }
        else {
            this.ultimate2DurationMillis = 0;
        }
        this.ultimate2StartTimeMillis = System.currentTimeMillis();
        this.ultimate2Running = true;
        this.ultimate1Running = false;
    }

    public void ultimate3() {
        if (classCharacter.equals(ClassCharacters.ANGELO)) {
            this.ultimate3DurationMillis = 1400;
            this.relativeWidth = 0.0386f;
            this.relativeHeight = 140f/80f * 0.035f * 768f/372f;
            this.relativeY -= (140f/80f * 0.035f * 768f/372f - 76f/102f * 0.044f * 768f/372f);
            this.attackNumberPerSecond = 0f;
        }
        else if (classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.ultimate3DurationMillis = 1100;
            this.relativeWidth = 0.045f;
            this.relativeHeight = 200f/200f * 0.045f * 768f/372f;
            this.attackNumberPerSecond = 0f;
        }
        else {
            this.ultimate3DurationMillis = 0;
        }
        this.ultimate3Running = true;
        this.ultimate2Running = false;
        this.ultimate3StartTimeMillis = System.currentTimeMillis();
    }

    public double getHorizontalDirection() {
        return horizontalDirection;
    }

    public void setHorizontalDirection(double horizontalDirection) {
        if (horizontalDirection != 0) {
            lastHorizontalDirection = horizontalDirection;
        }

        this.horizontalDirection = horizontalDirection;
    }

    public double getLastHorizontalDirection() {
        return lastHorizontalDirection;
    }

    public void setLastHorizontalDirection(double lastHorizontalDirection) {
        this.lastHorizontalDirection = lastHorizontalDirection;
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

    public int getUltimate1DurationMillis() {
        return ultimate1DurationMillis;
    }

    public int getUltimate2DurationMillis() {
        return ultimate2DurationMillis;
    }

    public int getUltimate3DurationMillis() {
        return ultimate3DurationMillis;
    }

    public long getUltimate1StartTimeMillis() {
        return ultimate1StartTimeMillis;
    }

    public long getUltimate2StartTimeMillis() {
        return ultimate2StartTimeMillis;
    }

    public long getUltimate3StartTimeMillis() {
        return ultimate3StartTimeMillis;
    }

    public boolean isUltimate1Running() {
        return ultimate1Running;
    }

    public boolean isUltimate2Running() {
        return ultimate2Running;
    }

    public boolean isUltimate3Running() {
        return ultimate3Running;
    }

    public void setUltimate1Running(boolean ultimate1Running) {
        this.ultimate1Running = ultimate1Running;
    }

    public void setUltimate2Running(boolean ultimate2Running) {
        this.ultimate2Running = ultimate2Running;
    }

    public void setUltimate3Running(boolean ultimate3Running) {
        this.ultimate3Running = ultimate3Running;
    }

    public boolean isClassCharacterChanged() {
        return classCharacterChanged;
    }

    public void setClassCharacterChanged(boolean classCharacterChanged) {
        this.classCharacterChanged = classCharacterChanged;
    }

    public float getMaxHealth() {
        return maxHealth;
    }
}
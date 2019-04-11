package model.characters;

import model.SolidObject;
import model.bullets.Bullet;

import java.util.ArrayList;
import java.util.List;

public class PlayableCharacter extends SolidObject {
    private final static int MAX_BULLET_NUMBER_PER_PLAYER = 10;
    private float relativeMaxSpeed;
    private final float RELATIVE_SPEED_GROWTH = relativeMaxSpeed/10;
    private float relativeJumpStrength;
    private float relativeWidth;
    private float relativeHeight;
    private ClassCharacters classCharacter;
    private float relativeX = 0.45f;
    private float relativeY = 0.1f;
    private String name;
    private double horizontalDirection = 1;
    private double lastHorizontalDirection;
    private List<Bullet> bullets = new ArrayList<>();
    private float maxHealth;
    private float health = maxHealth;
    private float reloadTimeSmallWaves;
    private long lastSmallWave;
    private int numberOfSmallWavesInMediumWaves;
    private float reloadTimeMediumWaves;
    private long lastMediumWave;
    private int numberOfMediumWavesInLargeWaves;
    private float reloadTimeLargeWaves;
    private long lastLargeWave;
    private float ultimateLoading;
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
    private int kills = 0;
    private int deaths = 0;
    private int money = 0;

    // Default constructor used for reflection (by Kryo serialization)
    public PlayableCharacter() {
    }

    public PlayableCharacter(String name) {
        this();
        this.name = name;
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        this.classCharacter = classCharacter;
        this.ultimateLoading = 0;

        if (this.classCharacter.equals(ClassCharacters.BOB)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 0.04f*768f/372f;
            this.relativeMaxSpeed = 0.0045f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1f;
            this.reloadTimeSmallWaves = 0.1f;
            this.numberOfSmallWavesInMediumWaves = 3;
            this.reloadTimeMediumWaves = 0.5f;
            this.numberOfMediumWavesInLargeWaves = 2;
            this.reloadTimeLargeWaves = 2f;
        }
        else if (this.classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 0.04f*768f/372f;
            this.relativeMaxSpeed = 0.004f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1f;
            this.reloadTimeSmallWaves = 0f;
            this.numberOfSmallWavesInMediumWaves = 1;
            this.reloadTimeMediumWaves = 0f;
            this.numberOfMediumWavesInLargeWaves = 1;
            this.reloadTimeLargeWaves = 0.3f;

            if (!(ultimate1Running && ultimate2Running && ultimate3Running)) {
                this.relativeX += 0.045f - 0.04f;
                this.relativeY += 0.044f * 768f / 372f - 0.04f * 768f / 372f;
            }
        }
        else if (this.classCharacter.equals(ClassCharacters.ANGELO)) {
            this.relativeWidth = 0.03f;
            this.relativeHeight = 140f/80f * 0.035f * 768f/372f;
            this.relativeMaxSpeed = 0.0035f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1f;
            this.relativeMaxSpeed = 0.004f;
            this.reloadTimeSmallWaves = 0f;
            this.numberOfSmallWavesInMediumWaves = 1;
            this.reloadTimeMediumWaves = 0f;
            this.numberOfMediumWavesInLargeWaves = 1;
            this.reloadTimeLargeWaves = 0.3f;
        }
        else if (this.classCharacter.equals(ClassCharacters.TATITATOO)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 0.04f*768f/372f;
            this.relativeMaxSpeed = 0.0045f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1f;
            this.reloadTimeSmallWaves = 0f;
            this.numberOfSmallWavesInMediumWaves = 1;
            this.reloadTimeMediumWaves = 0f;
            this.numberOfMediumWavesInLargeWaves = 1;
            this.reloadTimeLargeWaves = 1f;
        }
        else if (this.classCharacter.equals(ClassCharacters.MONK)) {
            this.relativeWidth = 0.03f;
            this.relativeHeight = 163f / 130f * 0.03f * 768f / 372f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1f;
            this.relativeMaxSpeed = 0.004f;
            this.reloadTimeSmallWaves = 0f;
            this.numberOfSmallWavesInMediumWaves = 1;
            this.reloadTimeMediumWaves = 0f;
            this.numberOfMediumWavesInLargeWaves = 1;
            this.reloadTimeLargeWaves = 0.75f;
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
            this.numberOfSmallWavesInMediumWaves = 0;
            this.numberOfMediumWavesInLargeWaves = 0;
        }
        else if (classCharacter.equals(ClassCharacters.TATITATOO)) {
            this.ultimate1DurationMillis = 10_000;
            this.relativeWidth = 0.04f;
            this.relativeHeight = 200f/200f * 0.04f * 768f/372f;
            this.relativeMaxSpeed = 0.0055f;
            this.numberOfSmallWavesInMediumWaves = 0;
            this.numberOfMediumWavesInLargeWaves = 0;
            this.maxHealth = 2f;
        }
        else if (classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.ultimate1DurationMillis = 660;
            this.relativeWidth = 0.05f;
            this.relativeHeight = 200f/200f * 0.044f * 768f/372f;
            this.reloadTimeLargeWaves = 1f;
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
            this.numberOfSmallWavesInMediumWaves = 1;
            this.numberOfMediumWavesInLargeWaves = 1;
            this.reloadTimeLargeWaves = 0.15f;
            this.relativeY += (140f/80f * 0.035f * 768f/372f - 76f/102f * 0.044f * 768f/372f);
        }
        else if (classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.ultimate2DurationMillis = 5000;
            this.relativeWidth = 0.045f;
            this.relativeHeight = 200f / 200f * 0.045f * 768f / 372f;
            this.reloadTimeLargeWaves = 1f;
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
            this.numberOfSmallWavesInMediumWaves = 0;
            this.numberOfMediumWavesInLargeWaves = 0;
        }
        else if (classCharacter.equals(ClassCharacters.MEDUSO)) {
            this.ultimate3DurationMillis = 1100;
            this.relativeWidth = 0.045f;
            this.relativeHeight = 200f/200f * 0.045f * 768f/372f;
            this.reloadTimeLargeWaves = 1f;
        }
        else {
            this.ultimate3DurationMillis = 0;
        }
        this.ultimate3Running = true;
        this.ultimate2Running = false;
        this.ultimate3StartTimeMillis = System.currentTimeMillis();
    }

    public void setLastLargeWave(long lastLargeWave) {
        this.lastLargeWave = lastLargeWave;
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

    public double getHorizontalDirection() {
        return horizontalDirection;
    }

    public void setHorizontalDirection(double horizontalDirection) {
        if (horizontalDirection != 0) {
            lastHorizontalDirection = horizontalDirection;
        }

        this.horizontalDirection = horizontalDirection;
    }

    public float getReloadTimeSmallWaves() {
        return reloadTimeSmallWaves;
    }

    public long getLastSmallWave() {
        return lastSmallWave;
    }

    public int getNumberOfSmallWavesInMediumWaves() {
        return numberOfSmallWavesInMediumWaves;
    }

    public float getReloadTimeMediumWaves() {
        return reloadTimeMediumWaves;
    }

    public long getLastMediumWave() {
        return lastMediumWave;
    }

    public int getNumberOfMediumWavesInLargeWaves() {
        return numberOfMediumWavesInLargeWaves;
    }

    public long getLastLargeWave() {
        return lastLargeWave;
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

    public float getReloadTimeLargeWaves() {
        return reloadTimeLargeWaves;
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

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getMoney() {
        return money;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
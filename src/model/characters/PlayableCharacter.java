package model.characters;

import model.SolidObject;
import model.bullets.Bullet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayableCharacter extends SolidObject {
    private final static int MAX_BULLET_NUMBER_PER_PLAYER = 12;
    private float relativeMaxSpeed;
    private float relativeSpeedGrowth;
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
    private long lastSmallWaveTime = 0;
    private int numberOfSmallWavesInMediumWaves;
    private int numberOfSmallWavesAlreadySentInMediumWaves;
    private float reloadTimeMediumWaves;
    private long lastMediumWaveTime = 0;
    private int numberOfMediumWavesInLargeWaves;
    private int numberOfMediumWavesAlreadySentInLargeWaves;
    private float reloadTimeLargeWaves;
    private long lastLargeWaveTime = 0;
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
    private List<Hit> hits = new ArrayList<>();
    private List<Object> inventory = new ArrayList<>();
    private List<SmallWave> smallWaves;
    private List<Integer> angleDegreesBulletsInSmallWave;

    public PlayableCharacter() {
        for (int i = 0; i < 50; i ++) {
            hits.add(new Hit());
        }
    }

    public PlayableCharacter(String name) {
        this();
        this.name = name;
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        this.classCharacter = classCharacter;
        this.ultimateLoading = 0;
        smallWaves = new ArrayList<>();
        angleDegreesBulletsInSmallWave = new ArrayList<>();
        angleDegreesBulletsInSmallWave.add(0);

        if (this.classCharacter.equals(ClassCharacters.BOB)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 181f/164f *  0.04f*768f/372f;
            this.relativeMaxSpeed = 0.0045f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1f;
            this.reloadTimeSmallWaves = 0.04f;
            this.numberOfSmallWavesInMediumWaves = 2;
            this.reloadTimeMediumWaves = 0.4f;
            this.numberOfMediumWavesInLargeWaves = 3;
            this.reloadTimeLargeWaves = 0.8f;
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
            this.reloadTimeLargeWaves = 0.7f;
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
            this.relativeWidth = 0.035f;
            this.relativeHeight = 163f / 130f * 0.035f * 768f / 372f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1f;
            this.relativeMaxSpeed = 0.004f;
            this.reloadTimeSmallWaves = 0f;
            this.numberOfSmallWavesInMediumWaves = 1;
            this.reloadTimeMediumWaves = 0.2f;
            this.numberOfMediumWavesInLargeWaves = 2;
            this.reloadTimeLargeWaves = 1f;
            this.angleDegreesBulletsInSmallWave.add(10);
            this.angleDegreesBulletsInSmallWave.add(-10);
        }
        else if (this.classCharacter.equals(ClassCharacters.ELBOMBAS)) {
            this.relativeWidth = 0.04f;
            this.relativeHeight = 193f / 135f * 0.04f * 768f / 372f;
            this.relativeJumpStrength = 0.013f;
            this.maxHealth = 1.5f;
            this.relativeMaxSpeed = 0.004f;
            this.reloadTimeSmallWaves = 0.08f;
            this.numberOfSmallWavesInMediumWaves = 3;
            this.reloadTimeMediumWaves = 0.4f;
            this.numberOfMediumWavesInLargeWaves = 3;
            this.reloadTimeLargeWaves = 1f;
        }

        this.numberOfSmallWavesAlreadySentInMediumWaves = this.numberOfSmallWavesInMediumWaves;
        this.relativeSpeedGrowth = this.relativeMaxSpeed/10;
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
        this.numberOfSmallWavesAlreadySentInMediumWaves = this.numberOfSmallWavesInMediumWaves;
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
        this.numberOfSmallWavesAlreadySentInMediumWaves = this.numberOfSmallWavesInMediumWaves;
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
        this.numberOfSmallWavesAlreadySentInMediumWaves = this.numberOfSmallWavesInMediumWaves;
    }

    private class SmallWave {
        private List<Integer> angleDegreesBullets = new ArrayList<>();

        SmallWave(int[] angleDegreesBullets) {
            for (int angleDegreesBullet : angleDegreesBullets)
            this.angleDegreesBullets.add(angleDegreesBullet);
        }

        public List<Integer> getAngleDegreesBullets() {
            return angleDegreesBullets;
        }
    }

    public void setLastLargeWaveTime(long lastLargeWaveTime) {
        this.lastLargeWaveTime = lastLargeWaveTime;
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

    public List<Integer> getAngleDegreesBulletsInSmallWave() {
        return angleDegreesBulletsInSmallWave;
    }

    public float getRelativeMaxSpeed() {
        return relativeMaxSpeed;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public List<Hit> getHits() {
        return hits;
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
        return relativeSpeedGrowth;
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

    public long getLastSmallWaveTime() {
        return lastSmallWaveTime;
    }

    public int getNumberOfSmallWavesInMediumWaves() {
        return numberOfSmallWavesInMediumWaves;
    }

    public float getReloadTimeMediumWaves() {
        return reloadTimeMediumWaves;
    }

    public long getLastMediumWaveTime() {
        return lastMediumWaveTime;
    }

    public int getNumberOfMediumWavesInLargeWaves() {
        return numberOfMediumWavesInLargeWaves;
    }

    public long getLastLargeWaveTime() {
        return lastLargeWaveTime;
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

    public int getNumberOfSmallWavesAlreadySentInMediumWaves() {
        return numberOfSmallWavesAlreadySentInMediumWaves;
    }

    public int getNumberOfMediumWavesAlreadySentInLargeWaves() {
        return numberOfMediumWavesAlreadySentInLargeWaves;
    }

    public void setNumberOfSmallWavesAlreadySentInMediumWaves(int numberOfSmallWavesAlreadySentInMediumWaves) {
        this.numberOfSmallWavesAlreadySentInMediumWaves = numberOfSmallWavesAlreadySentInMediumWaves;
    }

    public void setNumberOfMediumWavesAlreadySentInLargeWaves(int numberOfMediumWavesAlreadySentInLargeWaves) {
        this.numberOfMediumWavesAlreadySentInLargeWaves = numberOfMediumWavesAlreadySentInLargeWaves;
    }

    public void setLastMediumWaveTime(long lastMediumWaveTime) {
        this.lastMediumWaveTime = lastMediumWaveTime;
    }

    public void setLastSmallWaveTime(long lastSmallWaveTime) {
        this.lastSmallWaveTime = lastSmallWaveTime;
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

    public List<Object> getInventory() {
        return inventory;
    }

    public void setInventory(List<Object> inventory) {
        this.inventory = inventory;
    }
}
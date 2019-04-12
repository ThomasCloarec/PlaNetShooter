package model.characters;

public class Hit {
    private long hitTime = 0;
    private String playerHit = "";
    private float hitDamage = 0;

    Hit() {

    }

    public long getHitTime() {
        return hitTime;
    }

    public void setHitTime(long hitTime) {
        this.hitTime = hitTime;
    }

    public String getPlayerHit() {
        return playerHit;
    }

    public void setPlayerHit(String playerHit) {
        this.playerHit = playerHit;
    }

    public float getHitDamage() {
        return hitDamage;
    }

    public void setHitDamage(float hitDamage) {
        this.hitDamage = hitDamage;
    }
}

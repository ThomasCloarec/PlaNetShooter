package model.characters;

public enum Direction {
    LEFT(-1), RIGHT(1);

    private final float DELTA;

    Direction(float delta) {
        this.DELTA = delta;
    }

    public float getDelta() {
        return DELTA;
    }
}
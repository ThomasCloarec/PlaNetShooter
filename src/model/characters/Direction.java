package model.characters;

public enum Direction {
    LEFT(-Character.getRelativeSpeed()), RIGHT(Character.getRelativeSpeed());

    private float delta;

    Direction(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }
}
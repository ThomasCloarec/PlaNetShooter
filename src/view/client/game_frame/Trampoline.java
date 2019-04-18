package view.client.game_frame;

import model.SolidObject;

public class Trampoline extends SolidObject {
    private long creationTime;
    @SuppressWarnings("FieldCanBeLocal")
    private final float durationTime = 3f;

    public Trampoline() {
        super();
        this.relativeWidth = 0.06f;
        this.relativeHeight = 0.015f;
    }

    public Trampoline(float relativeX, float relativeY) {
        this();
        this.relativeX = relativeX;
        this.relativeY = relativeY;

        this.creationTime = System.currentTimeMillis();
    }

    public long getCreationTime() {
        return creationTime;
    }

    public float getDurationTime() {
        return durationTime;
    }
}
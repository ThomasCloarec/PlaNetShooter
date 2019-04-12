package view.client.game_frame;

import model.SolidObject;

public class Trampoline extends SolidObject {
    private float relativeX;
    private float relativeY;
    @SuppressWarnings("FieldCanBeLocal")
    private float relativeWidth = 0.06f;
    private float relativeHeight = 0.015f;
    private long creationTime;
    @SuppressWarnings("FieldCanBeLocal")
    private float durationTime = 3f;

    public Trampoline() {
    }

    @SuppressWarnings("unused")
    public Trampoline(float relativeX, float relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;

        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public float getRelativeX() {
        return relativeX;
    }

    @Override
    public float getRelativeY() {
        return relativeY;
    }

    @Override
    public float getRelativeWidth() {
        return relativeWidth;
    }

    @Override
    public float getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public float getDurationTime() {
        return durationTime;
    }
}
package view.client.game_frame;

import model.SolidObject;

public class Trampoline extends SolidObject {
    private float relativeX;
    private float relativeY;
    private final float relativeWidth = 0.06f;
    private final float relativeHeight = 0.015f;

    private Trampoline() {
    }

    @SuppressWarnings("unused")
    public Trampoline(float relativeX, float relativeY) {
        this();
        this.relativeX = relativeX;
        this.relativeY = relativeY;
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
}
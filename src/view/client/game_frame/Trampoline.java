package view.client.game_frame;

import model.SolidObject;

public class Trampoline extends SolidObject {
    private final float relativeWidth = 0.06f;
    private final float relativeHeight = 0.015f;
    private float relativeX = 0.6f - relativeWidth/2;

    Trampoline() {
    }

    public Trampoline (float playableCharacterRelativeX) {
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return 0.8f - relativeHeight;
    }

    public float getRelativeWidth() {
        return relativeWidth;
    }

    public float getRelativeHeight() {
        return relativeHeight;
    }
}

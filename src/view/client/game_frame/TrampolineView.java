package view.client.game_frame;

import model.SolidObject;

public class TrampolineView extends SolidObject {
    private final float relativeWidth = 0.06f;
    private final float relativeHeight = 0.015f;

    TrampolineView() {
    }

    public TrampolineView(float playableCharacterRelativeX) {
    }

    public float getRelativeX() {
        return 0.6f - relativeWidth / 2;
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

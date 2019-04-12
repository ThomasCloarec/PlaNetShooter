package view.client.game_frame;

import model.SolidObject;
import model.bullets.Bullet;

public class TrampolineView extends Bullet {
    private final float relativeWidth = 0.06f;
    private final float relativeHeight = 0.015f;

    public TrampolineView() {
    }





    @SuppressWarnings("unused")
    public TrampolineView(float playableCharacterRelativeX) { }

    public float getRelativeX() { return 0.6f - relativeWidth / 2; }

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

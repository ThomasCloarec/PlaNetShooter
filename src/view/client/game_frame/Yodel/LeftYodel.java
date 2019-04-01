package view.client.game_frame.Yodel;

import model.SolidObject;

public class LeftYodel extends SolidObject {
    private static final float relativeWidth = 0.06f;
    private static final float relativeHeight = 0.1f;
    private static final float relativeX = 0.15f - relativeWidth/2;
    private static final float relativeY = 0.3f - relativeHeight;

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
}


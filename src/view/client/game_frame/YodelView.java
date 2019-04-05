package view.client.game_frame;

import model.SolidObject;

public class YodelView extends SolidObject {
    private static final float relativeWidth = 0.06f;
    private static final float relativeHeight = 0.1f;
    private static float relativeX;
    private static final float relativeY = 0.27f - relativeHeight;

    public YodelView(String yodelSide) {
        if (yodelSide.equals("left"))
            relativeX = 0.15f - relativeWidth/2;
        else if (yodelSide.equals("right"))
            relativeX = 0.858f - relativeWidth/2;
    }
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


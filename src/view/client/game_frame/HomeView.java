package view.client.game_frame;

public class HomeView {
    private static final float relativeWidth = 0.06f;
    private static final float relativeHeight = 0.1f;
    private static final float relativeX = 0.505f - relativeWidth/2;
    private static final float relativeY = 0.25f - relativeHeight;

    public static float getRelativeX() {
        return relativeX;
    }

    public static float getRelativeY() {
        return relativeY;
    }

    public static float getRelativeWidth() {
        return relativeWidth;
    }

    public static float getRelativeHeight() {
        return relativeHeight;
    }
}

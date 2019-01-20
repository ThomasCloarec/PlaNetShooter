package view.client.game_frame;

public class HomeView {
    private static final float relativeWidth = 0.075f;
    private static final float relativeHeight = 0.125f;
    private static final float relativeX = 0.5f - relativeWidth/2;
    private static final float relativeY = 0.55f - relativeHeight;

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

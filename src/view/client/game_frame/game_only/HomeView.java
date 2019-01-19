package view.client.game_frame.game_only;

class HomeView {
    private static final float relativeWidth = 0.075f;
    private static final float relativeHeight = 0.125f;
    private static final float relativeX = 0.5f - relativeWidth/2;
    private static final float relativeY = 0.55f - relativeHeight;

    static float getRelativeX() {
        return relativeX;
    }

    static float getRelativeY() {
        return relativeY;
    }

    static float getRelativeWidth() {
        return relativeWidth;
    }

    static float getRelativeHeight() {
        return relativeHeight;
    }
}

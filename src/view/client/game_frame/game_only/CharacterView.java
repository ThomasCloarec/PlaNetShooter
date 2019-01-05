package view.client.game_frame.game_only;

public class CharacterView {
    private float relativeX;
    private float relativeY;
    private final float RELATIVE_WIDTH;
    private final float RELATIVE_HEIGHT;
    public CharacterView(float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.RELATIVE_WIDTH = relativeWidth;
        this.RELATIVE_HEIGHT = relativeHeight;
    }

    float getRelativeX() {
        return relativeX;
    }

    float getRelativeY() {
        return relativeY;
    }

    float getRelativeWidth() {
        return RELATIVE_WIDTH;
    }

    float getRelativeHeight() {
        return RELATIVE_HEIGHT;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }
}

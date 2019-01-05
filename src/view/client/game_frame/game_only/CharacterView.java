package view.client.game_frame.game_only;

public class CharacterView {
    private float relativeX;
    private float relativeY;
    private final float relativeWidth;
    private final float relativeHeight;
    public CharacterView(float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
    }

    float getRelativeX() {
        return relativeX;
    }

    float getRelativeY() {
        return relativeY;
    }

    float getRelativeWidth() {
        return relativeWidth;
    }

    float getRelativeHeight() {
        return relativeHeight;
    }

    void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }
}

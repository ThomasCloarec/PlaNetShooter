package view.client.game.objects;

public class PlatformView {
    public float relativeX, relativeY, relativeWidth, relativeHeight;
    public PlatformView(float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
    }
}
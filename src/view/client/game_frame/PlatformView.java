package view.client.game_frame;

import model.SolidObject;

public class PlatformView extends SolidObject {
    public PlatformView(float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
        super();
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeWidth = relativeWidth;
        this.relativeHeight = relativeHeight;
    }
}
package view.client.game_frame;

import model.SolidObject;

public class YodelView extends SolidObject {
    public YodelView(String yodelSide) {
        super();
        this.relativeWidth = 0.06f;
        this.relativeHeight = 0.1f;
        this.relativeY = 0.25f - relativeHeight;

        if (yodelSide.equals("left"))
            relativeX = 0.14f - relativeWidth/2;
        else if (yodelSide.equals("right"))
            relativeX = 0.868f - relativeWidth/2;
    }
}


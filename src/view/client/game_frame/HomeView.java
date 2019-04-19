package view.client.game_frame;

import model.SolidObject;

public class HomeView extends SolidObject {
    public HomeView() {
        super();
        this.relativeWidth = 0.06f;
        this.relativeHeight = 0.1f;
        this.relativeX = 0.505f - relativeWidth/2;
        this.relativeY = 0.25f - relativeHeight;
    }
}

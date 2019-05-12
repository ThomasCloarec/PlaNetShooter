package view.client.game_frame;

import model.SolidObject;

public class HomeView extends SolidObject {
    public HomeView() {
        super();
        this.relativeWidth = 0.07f;
        this.relativeHeight = 0.12f;
        this.relativeX = 0.5f - relativeWidth/2;
        this.relativeY = 0.13f;
    }
}

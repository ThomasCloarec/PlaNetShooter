package model;

@SuppressWarnings("unused")
public class SolidObject {
    private float relativeX;
    private float relativeY;
    private float relativeWidth;
    private float relativeHeight;

    protected SolidObject() {
    }

    public float getRelativeHeight() {
        return relativeHeight;
    }

    public float getRelativeWidth() {
        return relativeWidth;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public float getRelativeX() {
        return relativeX;
    }
}

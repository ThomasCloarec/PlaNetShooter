package model;

@SuppressWarnings("unused")
public class SolidObject {
    protected float relativeX;
    protected float relativeY;
    protected float relativeWidth;
    protected float relativeHeight;

    protected SolidObject() {
    }

    public float getRelativeHeight() {
        return relativeHeight;
    }

    public float getRelativeWidth() {
        return relativeWidth;
    }

    public void setRelativeWidth(float relativeWidth) {
        this.relativeWidth = relativeWidth;
    }

    public void setRelativeHeight(float relativeHeight) {
        this.relativeHeight = relativeHeight;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
    }

    public float getCenterX() {
        return this.relativeX + this.relativeWidth / 2;
    }

    public float getCenterY() {
        return this.relativeY + this.relativeHeight / 2;
    }
}

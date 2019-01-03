package model.platforms;

public class Platform {
    public static final float relativeWidth = 0.1f;
    public static final float relativeHeight = 0.01f;
    public float relativeX;
    public float relativeY;
    private static int platformCount = 0;

    public Platform() {
        /* HOW THE PLATFORMS ARE :
        _7_                               _8_
                _5_               _6_
                         _4_
                _2_               _3_
        _0_                               _1_

         */

        if (platformCount == 0 || platformCount == 7)
            this.relativeX = 0f;
        else if (platformCount == 2 || platformCount == 5)
            this.relativeX = 0.2f;
        else if (platformCount == 3 || platformCount == 6)
            this.relativeX = 0.7f;
        else if (platformCount == 1|| platformCount == 8)
            this.relativeX = 0.9f;

        if (platformCount == 0 || platformCount == 1)
            this.relativeY = 0.95f;
        else if (platformCount == 2 || platformCount == 3)
            this.relativeY = 0.75f;
        else if (platformCount == 5 || platformCount == 6)
            this.relativeY = 0.35f;
        else if (platformCount == 7 || platformCount == 8)
            this.relativeY = 0.15f;

        if (platformCount == 4) {
            this.relativeX = 0.45f;
            this.relativeY = 0.55f;
        }

        platformCount++;
    }
}
package model.platforms;

public class Platform {
    public static final float relativeWidth = 0.1f;
    public static final float relativeHeight = 0.01f;
    public float relativeX;
    public float relativeY;
    private static int platformLoopCount = 0;
    public static int platformNumber = 11;

    public Platform() {
        /* HOW THE PLATFORMS ARE :
        _9_                               _10
                _7_               _8_
        _4_              _5_              _6_
                _2_               _3_
        _0_                               _1_

         */

        if (platformLoopCount == 0 || platformLoopCount == 9 || platformLoopCount == 4)
            this.relativeX = 0f;
        else if (platformLoopCount == 2 || platformLoopCount == 7)
            this.relativeX = 0.225f;
        else if (platformLoopCount == 3 || platformLoopCount == 8)
            this.relativeX = 0.675f;
        else if (platformLoopCount == 1|| platformLoopCount == 10 || platformLoopCount == 6)
            this.relativeX = 0.9f;
        else if (platformLoopCount == 5)
            this.relativeX = 0.45f;

        if (platformLoopCount == 0 || platformLoopCount == 1)
            this.relativeY = 0.95f;
        else if (platformLoopCount == 2 || platformLoopCount == 3)
            this.relativeY = 0.75f;
        else if (platformLoopCount == 7 || platformLoopCount == 8)
            this.relativeY = 0.35f;
        else if (platformLoopCount == 9 || platformLoopCount == 10)
            this.relativeY = 0.15f;
        else if (platformLoopCount == 4 || platformLoopCount == 5 || platformLoopCount == 6)
            this.relativeY = 0.55f;
        
        platformLoopCount++;
    }
}
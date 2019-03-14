package model.platforms;

public class Platform {
    private float relativeWidth = 0.12f;
    private float relativeHeight = 0.01f;
    private float relativeX;
    private float relativeY;
    private static int platformLoopCount = 0;

    public Platform() {
        /* HOW THE PLATFORMS ARE :

                 _5_
            _4_       _7_
       _2_                 _8_
          ___3___   ___6___
       _1_                 _9_
         */

        if (platformLoopCount == 1 || platformLoopCount == 2)
            this.relativeX = 0f;
        else if (platformLoopCount == 3)
            this.relativeX = 0.222f;
        else if (platformLoopCount == 4)
            this.relativeX = 0.166f;
        else if (platformLoopCount == 5)
            this.relativeX = 0.444f;
        else if (platformLoopCount == 6)
            this.relativeX = 0.555f;
        else if (platformLoopCount == 7)
            this.relativeX = 0.721f;
        else if (platformLoopCount == 8|| platformLoopCount == 9)
            this.relativeX = 0.9f;


        if (platformLoopCount == 1 || platformLoopCount == 9)
            this.relativeY = 0.95f;
        else if (platformLoopCount == 3 || platformLoopCount == 6)
            this.relativeY = 0.75f;
        else if (platformLoopCount == 2 || platformLoopCount == 8)
            this.relativeY = 0.55f;
        else if (platformLoopCount == 4 || platformLoopCount == 7)
            this.relativeY = 0.35f;
        else if (platformLoopCount == 5)
            this.relativeY = 0.25f;

        if (platformLoopCount == 3 || platformLoopCount == 6)
            this.relativeWidth = 0.222f;
        // in order to move back the platform5 at the center, otherwise it would be pushed on right :
        if (platformLoopCount == 1)
            this.relativeX = 0f;

        platformLoopCount++;
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public float getRelativeWidth() {
        return relativeWidth;
    }

    public float getRelativeHeight() {
        return relativeHeight;
    }

    @SuppressWarnings("SameReturnValue")
    public static int getPlatformNumber() {
        return 12;
    }
}
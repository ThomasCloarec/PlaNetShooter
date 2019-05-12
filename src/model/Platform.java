package model;

import model.SolidObject;

public class Platform extends SolidObject {
    private static int platformLoopCount = 0;

    public Platform() {
        super();
        this.relativeWidth = 0.12f;
        this.relativeHeight = 0.01f;

        /* HOW THE PLATFORMS ARE :



              ___9___






                 _8_
            _6_       _7_
       _4_                 _5_
          ___2___   ___3___
       _0_                 _1_
         */

        if (platformLoopCount == 0 || platformLoopCount == 4)
            this.relativeX = 0f;
        else if (platformLoopCount == 2)
            this.relativeX = 0.2f;
        else if (platformLoopCount == 6)
            this.relativeX = 0.165f;
        else if (platformLoopCount == 8 || platformLoopCount == 9)
            this.relativeX = 0.44f;
        else if (platformLoopCount == 3)
            this.relativeX = 0.55f;
        else if (platformLoopCount == 7)
            this.relativeX = 0.715f;
        else if (platformLoopCount == 5|| platformLoopCount == 1)
            this.relativeX = 0.88f;


        if (platformLoopCount == 0 || platformLoopCount == 1)
            this.relativeY = 0.95f;
        else if (platformLoopCount == 2 || platformLoopCount == 3)
            this.relativeY = 0.80f;
        else if (platformLoopCount == 4 || platformLoopCount == 5)
            this.relativeY = 0.65f;
        else if (platformLoopCount == 6 || platformLoopCount == 7)
            this.relativeY = 0.50f;
        else if (platformLoopCount == 8)
            this.relativeY = 0.25f;
        else if (platformLoopCount == 9)
            this.relativeY = -1f;

        if (platformLoopCount == 2 || platformLoopCount == 3)
            this.relativeWidth = 0.25f;

        platformLoopCount++;
    }

    @SuppressWarnings("SameReturnValue")
    public static int getPlatformNumber() {
        return 10;
    }
}
package model.platforms;

import model.SolidObject;

public class Platform extends SolidObject {
    private static int platformLoopCount = 0;

    public Platform() {
        super();
        this.relativeWidth = 0.12f;
        this.relativeHeight = 0.01f;

        /* HOW THE PLATFORMS ARE :



              ___9___






                 _4_
            _3_       _6_
       _1_                 _7_
          ___2___   ___5___
       _0_                 _8_
         */

        if (platformLoopCount == 0 || platformLoopCount == 1)
            this.relativeX = 0f;
        else if (platformLoopCount == 2)
            this.relativeX = 0.194f;
        else if (platformLoopCount == 3)
            this.relativeX = 0.166f;
        else if (platformLoopCount == 4 || platformLoopCount == 9)
            this.relativeX = 0.444f;
        else if (platformLoopCount == 5)
            this.relativeX = 0.555f;
        else if (platformLoopCount == 6)
            this.relativeX = 0.721f;
        else if (platformLoopCount == 7|| platformLoopCount == 8)
            this.relativeX = 0.88f;


        if (platformLoopCount == 0 || platformLoopCount == 8)
            this.relativeY = 0.95f;
        else if (platformLoopCount == 2 || platformLoopCount == 5)
            this.relativeY = 0.80f;
        else if (platformLoopCount == 1 || platformLoopCount == 7)
            this.relativeY = 0.65f;
        else if (platformLoopCount == 3 || platformLoopCount == 6)
            this.relativeY = 0.50f;
        else if (platformLoopCount == 4)
            this.relativeY = 0.28f;
        else if (platformLoopCount == 9)
            this.relativeY = -1f;

        if (platformLoopCount == 2 || platformLoopCount == 5)
            this.relativeWidth = 0.25f;
        // in order to move back the platform5 at the center, otherwise it would be pushed on right :
        if (platformLoopCount == 0)
            this.relativeX = 0f;

        platformLoopCount++;
    }

    @SuppressWarnings("SameReturnValue")
    public static int getPlatformNumber() {
        return 10;
    }
}
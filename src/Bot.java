import model.platforms.Platform;

class Bot {
    private static Platform closerPlatformAbove = null;
    private static float distanceCloserPlatform = 1f;
    private static float[] jumpInterval = {0,10};
    private static float lastJump;

    static Platform getCloserPlatformAbove() {
        return closerPlatformAbove;
    }

    static void setCloserPlatformAbove(Platform closerPlatformAbove) {
        Bot.closerPlatformAbove = closerPlatformAbove;
    }

    static float getDistanceCloserPlatform() {
        return distanceCloserPlatform;
    }

    static void setDistanceCloserPlatform(float distanceCloserPlatform) {
        Bot.distanceCloserPlatform = distanceCloserPlatform;
    }

    public static float[] getJumpInterval() {
        return jumpInterval;
    }

    public static float getLastJump() {
        return lastJump;
    }

    public static void setLastJump(float lastJump) {
        Bot.lastJump = lastJump;
    }
}

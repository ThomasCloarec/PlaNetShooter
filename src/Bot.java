import model.platforms.Platform;

class Bot {
    private static Platform closestPlatformAbove = null;
    private static float distanceXClosestPlatform = 1f;
    private static float distanceYClosestPlatform = 1f;
    private static float[] jumpInterval = {0,1};
    private static float lastJump;

    static Platform getClosestPlatformAbove() {
        return closestPlatformAbove;
    }

    static void setClosestPlatformAbove(Platform closestPlatformAbove) {
        Bot.closestPlatformAbove = closestPlatformAbove;
    }

    static float getDistanceXClosestPlatform() {
        return distanceXClosestPlatform;
    }

    static void setDistanceXClosestPlatform(float distanceXClosestPlatform) {
        Bot.distanceXClosestPlatform = distanceXClosestPlatform;
    }
    static float getDistanceYClosestPlatform() {
        return distanceYClosestPlatform;
    }

    static void setDistanceYClosestPlatform(float distanceYClosestPlatform) {
        Bot.distanceYClosestPlatform = distanceYClosestPlatform;
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

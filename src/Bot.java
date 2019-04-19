import model.platforms.Platform;

class Bot {
    private static Platform closestPlatformAbove;
    private static float distanceXClosestPlatform = 1f;
    private static float distanceYClosestPlatform = 1f;
    private static float maxJumpDistance = 1f;
    private static float actualJump;

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

    public static float getMaxJumpDistance() {
        return maxJumpDistance;
    }

    public static void setMaxJumpDistance(float maxJumpDistance) {
        Bot.maxJumpDistance = maxJumpDistance;
    }

    static float getActualJump() {
        return actualJump;
    }

    static void setActualJump(float actualJump) {
        Bot.actualJump = actualJump;
    }
}
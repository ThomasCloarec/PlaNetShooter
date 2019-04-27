import model.platforms.Platform;

class Bot {
    private static Platform closestPlatformAbove;
    private static float distanceXClosestPlatform = 1f;
    private static float distanceYClosestPlatform = 1f;
    private static float maxJumpDistanceX = 1f;
    private static float actualJumpX;
    private static Platform lastClosestPlatformAbove;

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

    static float getMaxJumpDistanceX() {
        return maxJumpDistanceX;
    }

    static void setMaxJumpDistanceX(float maxJumpDistanceX) {
        Bot.maxJumpDistanceX = maxJumpDistanceX;
    }

    static float getActualJumpX() {
        return actualJumpX;
    }

    static void setActualJumpX(float actualJumpX) {
        Bot.actualJumpX = actualJumpX;
    }

    static Platform getLastClosestPlatformAbove() {
        return lastClosestPlatformAbove;
    }

    static void setLastClosestPlatformAbove(Platform lastClosestPlatformAbove) {
        Bot.lastClosestPlatformAbove = lastClosestPlatformAbove;
    }
}
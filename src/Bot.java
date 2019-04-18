import model.platforms.Platform;

class Bot {
    private static Platform closerPlatformAbove = null;
    private static float distanceCloserPlatform = 1f;

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
}

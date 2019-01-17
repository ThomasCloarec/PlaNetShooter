package model;

import model.characters.PlayableCharacter;
import model.platforms.Platform;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {
    private static PlayableCharacter solidObject1;
    private static Platform solidObject2;
    public static PlayerCollisionSide isCollisionBetween(Object object1, Object object2) {
        if (object1 instanceof PlayableCharacter && object2 instanceof Platform) {
            solidObject1 = (PlayableCharacter)object1;
            solidObject2 = (Platform)object2;
        }
        else if (object1 instanceof Platform && object2 instanceof PlayableCharacter) {
            solidObject1 = (PlayableCharacter)object2;
            solidObject2 = (Platform)object1;
        }
        else
            System.err.println("Trying to use isCollisionBetween() method on an unregistered object");

        if (solidObject1 != null && solidObject2 != null) {
            Rectangle2D rectangleCharacter = new Rectangle2D.Float(solidObject1.getRelativeX(), solidObject1.getRelativeY(), PlayableCharacter.getRelativeWidth(), PlayableCharacter.getRelativeHeight());
            Rectangle2D rectanglePlatform = new Rectangle2D.Float(solidObject2.getRelativeX(), solidObject2.getRelativeY(), Platform.getRelativeWidth(), Platform.getRelativeHeight());

            if (rectangleCharacter.intersects(rectanglePlatform)) {
                double wy = (rectangleCharacter.getWidth() + rectanglePlatform.getWidth()) * (rectangleCharacter.getCenterY() - rectanglePlatform.getCenterY());
                double hx = (rectangleCharacter.getHeight() + rectanglePlatform.getHeight()) * (rectangleCharacter.getCenterX() - rectanglePlatform.getCenterX());

                if (wy > hx)
                    if (wy > -hx)
                        return PlayerCollisionSide.TOP;
                    else
                        return PlayerCollisionSide.RIGHT;
                else
                    if (wy > -hx)
                        return PlayerCollisionSide.LEFT;
                    else
                        return PlayerCollisionSide.BOTTOM;
            }

        }

        return PlayerCollisionSide.NONE;
    }
}
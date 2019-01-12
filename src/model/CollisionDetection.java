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

            Rectangle2D lineTopPlatform = new Rectangle2D.Float(solidObject2.getRelativeX()+(0.0000000001f+PlayableCharacter.getRelativeMaxSpeed()), solidObject2.getRelativeY(), Platform.getRelativeWidth()-2*(0.0000000001f+PlayableCharacter.getRelativeMaxSpeed()), 0.0000000001f);
            Rectangle2D lineBottomPlatform = new Rectangle2D.Float(solidObject2.getRelativeX()+(0.0000000001f+PlayableCharacter.getRelativeMaxSpeed()), solidObject2.getRelativeY()+Platform.getRelativeHeight(), Platform.getRelativeWidth()-2*(0.0000000001f+PlayableCharacter.getRelativeMaxSpeed()), 0.0000000001f);
            Rectangle2D lineRightPlatform = new Rectangle2D.Float(solidObject2.getRelativeX() + Platform.getRelativeWidth(), solidObject2.getRelativeY()+(0.0000000001f+0.004999999f),0.0000000001f, Platform.getRelativeHeight()-2*(0.0000000001f+0.004999999f));
            Rectangle2D lineLeftPlatform = new Rectangle2D.Float((solidObject2.getRelativeX()), solidObject2.getRelativeY()+(0.0000000001f+0.004999999f), 0.0000000001f, Platform.getRelativeHeight()-2*(0.0000000001f+0.004999999f));

            // Inversely because for example, that's the BOTTOM of the player that collide with the TOP of the platform
            if (rectangleCharacter.intersects(lineTopPlatform))
                return PlayerCollisionSide.BOTTOM;
            else if (rectangleCharacter.intersects(lineBottomPlatform))
                return PlayerCollisionSide.TOP;
            else if (rectangleCharacter.intersects(lineRightPlatform))
                return PlayerCollisionSide.LEFT;
            else if (rectangleCharacter.intersects(lineLeftPlatform))
                return PlayerCollisionSide. RIGHT;
        }

        return PlayerCollisionSide.NONE;
    }
}
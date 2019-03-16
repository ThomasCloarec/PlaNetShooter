package model;

import model.characters.PlayableCharacter;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {
    private static Rectangle2D rectangleCharacter;
    private static Rectangle2D rectangle2;

    public static PlayerCollisionSide isCollisionBetween(Object object1, Object object2) {
        SolidObject solidCharacter = null;
        SolidObject solidObject2 = null;

        if (object1 instanceof SolidObject && object2 instanceof PlayableCharacter) {
            solidCharacter = (SolidObject)object2;
            solidObject2 = (SolidObject)object1;
        }
        else if (object1 instanceof PlayableCharacter && object2 instanceof SolidObject) {
            solidCharacter = (SolidObject)object1;
            solidObject2 = (SolidObject)object2;
        }
        else
            System.err.println("Trying to use isCollisionBetween() method on an unregistered object");

        if (solidCharacter != null) {
            rectangleCharacter = new Rectangle2D.Float(solidCharacter.getRelativeX(), solidCharacter.getRelativeY(), solidCharacter.getRelativeWidth(), solidCharacter.getRelativeHeight());
            rectangle2 = new Rectangle2D.Float(solidObject2.getRelativeX(), solidObject2.getRelativeY(), solidObject2.getRelativeWidth(), solidObject2.getRelativeHeight());
        }


        if (rectangleCharacter.intersects(rectangle2)) {
            double wy = (rectangleCharacter.getWidth() + rectangle2.getWidth()) * (rectangleCharacter.getCenterY() - rectangle2.getCenterY());
            double hx = (rectangleCharacter.getHeight() + rectangle2.getHeight()) * (rectangleCharacter.getCenterX() - rectangle2.getCenterX());

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
        else
            return PlayerCollisionSide.NONE;
    }
}
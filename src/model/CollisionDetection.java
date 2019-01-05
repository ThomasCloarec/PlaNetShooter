package model;

import model.characters.Character;
import model.platforms.Platform;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {
    public static boolean isCollisionBetween(Object object1, Object object2) {
        if (object1 instanceof Character && object2 instanceof Platform) {
            Character solidObject1 = (Character)object1;
            Platform solidObject2 = (Platform)object2;

            Rectangle2D rectangle1 = new Rectangle2D.Float(solidObject1.getRelativeX(), solidObject1.getRelativeY(), Character.getRelativeWidth(), Character.getRelativeHeight());
            Rectangle2D rectangle2 = new Rectangle2D.Float(solidObject2.getRelativeX(), solidObject2.getRelativeY(), Platform.getRelativeWidth(), Platform.getRelativeHeight());

            return rectangle1.intersects(rectangle2);
        }
        else if (object1 instanceof Platform && object2 instanceof Character) {
            Platform solidObject1 = (Platform)object1;
            Character solidObject2 = (Character)object2;

            Rectangle2D rectangle1 = new Rectangle2D.Float(solidObject1.getRelativeX(), solidObject1.getRelativeY(), Character.getRelativeWidth(), Character.getRelativeHeight());
            Rectangle2D rectangle2 = new Rectangle2D.Float(solidObject2.getRelativeX(), solidObject2.getRelativeY(), Platform.getRelativeWidth(), Platform.getRelativeHeight());

            return rectangle1.intersects(rectangle2);
        }
        else
            System.err.println("Trying to use isCollisionBetween() method on an unregistered object");

        return false;
    }
}
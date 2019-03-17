package model;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {

    // return the side of the object1 colliding with the object2 | NONE => no collision detected
    public static PlayerCollisionSide isCollisionBetween(SolidObject solidObject1, SolidObject solidObject2) {
        Rectangle2D rectangle1 = new Rectangle2D.Float(solidObject1.getRelativeX(), solidObject1.getRelativeY(), solidObject1.getRelativeWidth(), solidObject1.getRelativeHeight());
        Rectangle2D rectangle2 = new Rectangle2D.Float(solidObject2.getRelativeX(), solidObject2.getRelativeY(), solidObject2.getRelativeWidth(), solidObject2.getRelativeHeight());

        if (rectangle1.intersects(rectangle2)) {
            double wy = (rectangle1.getWidth() + rectangle2.getWidth()) * (rectangle1.getCenterY() - rectangle2.getCenterY());
            double hx = (rectangle1.getHeight() + rectangle2.getHeight()) * (rectangle1.getCenterX() - rectangle2.getCenterX());

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
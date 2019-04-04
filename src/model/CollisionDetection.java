package model;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {

    // return the side of the object1 colliding with the object2 | NONE => no collision detected
    public static PlayerCollisionSide isCollisionBetween(SolidObject solidObject1, SolidObject solidObject2) {
        Rectangle2D rectangle1 = new Rectangle2D.Float(solidObject1.getRelativeX(), solidObject1.getRelativeY(), solidObject1.getRelativeWidth(), solidObject1.getRelativeHeight());
        Rectangle2D rectangle2 = new Rectangle2D.Float(solidObject2.getRelativeX(), solidObject2.getRelativeY(), solidObject2.getRelativeWidth(), solidObject2.getRelativeHeight());

        if (rectangle1.intersects(rectangle2)) {
            if ((rectangle2.intersectsLine(rectangle1.getCenterX(), rectangle1.getCenterY(), rectangle1.getX(), rectangle1.getY() + rectangle1.getHeight()))
            || (rectangle2.intersectsLine(rectangle1.getCenterX(), rectangle1.getCenterY(), rectangle1.getX() + rectangle1.getWidth(), rectangle1.getY() + rectangle1.getHeight()))
            || (rectangle2.intersectsLine(rectangle1.getCenterX(), rectangle1.getCenterY(), rectangle1.getCenterX(), rectangle1.getY() + rectangle1.getHeight()))) {
                return PlayerCollisionSide.BOTTOM;
            }

            else if ((rectangle2.intersectsLine(rectangle1.getCenterX(), rectangle1.getCenterY(), rectangle1.getX(), rectangle1.getY()))
                    || (rectangle2.intersectsLine(rectangle1.getCenterX(), rectangle1.getCenterY(), rectangle1.getX() + rectangle1.getWidth(), rectangle1.getY()))
                    || (rectangle2.intersectsLine(rectangle1.getCenterX(), rectangle1.getCenterY(), rectangle1.getCenterX(), rectangle1.getY()))) {
                return PlayerCollisionSide.TOP;
            }

            else if (rectangle2.intersectsLine(rectangle1.getX(), rectangle1.getY(), rectangle1.getX(), rectangle1.getY() + rectangle1.getHeight())) {
                return PlayerCollisionSide.LEFT;
            }
            else if (rectangle2.intersectsLine(rectangle1.getX() + rectangle1.getWidth(), rectangle1.getY(), rectangle1.getX() + rectangle1.getWidth(), rectangle1.getY() + rectangle1.getHeight())) {
                return PlayerCollisionSide.RIGHT;
            }
        }
        return PlayerCollisionSide.NONE;
    }
}
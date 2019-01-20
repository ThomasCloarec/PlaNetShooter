package model;

import model.characters.PlayableCharacter;
import model.platforms.Platform;
import view.client.game_frame.game_only.HomeView;

import java.awt.geom.Rectangle2D;

public class CollisionDetection {
    private static Rectangle2D rectangleCharacter;
    private static Rectangle2D rectangle2;

    public static PlayerCollisionSide isCollisionBetween(Object object1, Object object2) {
        PlayableCharacter solidObject1 = null;
        Platform solidObject2 = null;

        if (object1 instanceof PlayableCharacter && object2 instanceof Platform) {
            solidObject1 = (PlayableCharacter)object1;
            solidObject2 = (Platform)object2;
        }
        else if (object1 instanceof Platform && object2 instanceof PlayableCharacter) {
            solidObject1 = (PlayableCharacter)object2;
            solidObject2 = (Platform)object1;
        }
        else if (object1 instanceof PlayableCharacter && object2 instanceof HomeView) {
            solidObject1 = (PlayableCharacter)object1;
        }
        else if (object1 instanceof HomeView && object2 instanceof PlayableCharacter) {
            solidObject1 = (PlayableCharacter)object2;
        }
        else
            System.err.println("Trying to use isCollisionBetween() method on an unregistered object");

        if (solidObject1 != null && solidObject2 != null) {
            rectangleCharacter = new Rectangle2D.Float(solidObject1.getRelativeX(), solidObject1.getRelativeY(), PlayableCharacter.getRelativeWidth(), PlayableCharacter.getRelativeHeight());
            rectangle2 = new Rectangle2D.Float(solidObject2.getRelativeX(), solidObject2.getRelativeY(), Platform.getRelativeWidth(), Platform.getRelativeHeight());
        }
        else if (solidObject1 != null) {
            rectangleCharacter = new Rectangle2D.Float(solidObject1.getRelativeX(), solidObject1.getRelativeY(), PlayableCharacter.getRelativeWidth(), PlayableCharacter.getRelativeHeight());
            rectangle2 = new Rectangle2D.Float(HomeView.getRelativeX(), HomeView.getRelativeY(), HomeView.getRelativeWidth(), HomeView.getRelativeHeight());
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
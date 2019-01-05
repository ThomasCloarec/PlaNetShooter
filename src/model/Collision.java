package model;

import model.characters.Character;
import model.platforms.Platform;

public class Collision {
    public boolean isCollisionBetween(Object object1, Object object2) {
        if (object1 instanceof Character && object2 instanceof Platform) {
            Character solidObject1 = (Character)object1;
            Platform solidObject2 = (Platform)object2;
        }
        else if (object1 instanceof Platform && object2 instanceof Character) {
            Platform solidObject1 = (Platform)object1;
            Character solidObject2 = (Character)object2;
        }

        return false;
    }
}

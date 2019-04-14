package model.characters;

import java.util.ArrayList;
import java.util.List;

public class SmallWave {
    private List<Integer> angleDegreesBullets = new ArrayList<>();

    SmallWave() {
        this.angleDegreesBullets.add(0);
    }

    SmallWave(int[] angleDegreesBullets) {
        for (int angleDegreesBullet : angleDegreesBullets)
            this.angleDegreesBullets.add(angleDegreesBullet);
    }

    public List<Integer> getAngleDegreesBullets() {
        return angleDegreesBullets;
    }
}

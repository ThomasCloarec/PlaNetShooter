package model.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public enum ClassCharacters {
    BOB,
    MEDUSO,
    ANGELO,
    TATITATOO;

    ClassCharacters() {
    }

    public static List<ClassCharacters> getClassCharactersList() {
        return new ArrayList<>(Arrays.asList(values()));
    }
}

package view.client.game_frame.game_only.action;

import model.characters.Direction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class PressAction extends AbstractAction {

    private final Set<Direction> movements;
    private final Direction value;

    public PressAction(Set<Direction> movementState, Direction value) {
        this.movements = movementState;
        this.value = value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movements.add(value);
    }
}
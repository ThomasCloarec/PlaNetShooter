package view.client.keyboard_actions;

import model.characters.Direction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class ReleaseAction extends AbstractAction {

    private final Set<Direction> movements;
    private final Direction value;

    public ReleaseAction(Set<Direction> movementState, Direction value) {
        this.movements = movementState;
        this.value = value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movements.remove(value);
    }

    public void removeMovements() {
        movements.remove(value);
    }
}
package view.client.game_frame.game_only.keyboard_actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class JumpAction extends AbstractAction {

    private static boolean jumpKeyJustPressed;

    @Override
    public void actionPerformed(ActionEvent e) {
        jumpKeyJustPressed = true;
    }

    public static boolean isJumpKeyJustPressed() {
        return jumpKeyJustPressed;
    }

    public static void setJumpKeyJustPressed(boolean jumpKeyJustPressed) {
        JumpAction.jumpKeyJustPressed = jumpKeyJustPressed;
    }
}
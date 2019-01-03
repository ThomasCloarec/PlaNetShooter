package view.client.connection;

import javax.swing.*;

public class AskClientName extends JOptionPane {
    public static String getClientName() {
        final String input = (String) showInputDialog(null, "Your name :", "Connect to game server",
                JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskClientName.class.getResource("/view/resources/new_user.png")), null, "Guest");

        if (input == null || input.trim().length() == 0)
            System.exit(0);

        return input.trim();
    }
}
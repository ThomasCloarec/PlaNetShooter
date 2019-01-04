package view.client.connection;

import javax.swing.*;

public class AskIPHost extends JOptionPane {
    public static String getIPHost() {
        final String input = (String) showInputDialog(null, "Server host:", "Connect to game_frame server",
                JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskIPHost.class.getResource("/view/resources/client_connection/host.png")), null, "localhost");

        if (input == null || input.trim().length() == 0)
            System.exit(0);

        return input.trim();
    }
}
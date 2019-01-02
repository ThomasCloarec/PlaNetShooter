package view.client.connection;

import javax.swing.*;

public class AskIPHost {
    public static String IPHost() {
        final String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to chat server",
                JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskIPHost.class.getResource("/view/resources/host.png")), null, "localhost");

        if (input == null || input.trim().length() == 0)
            System.exit(0);

        return input.trim();
    }
}
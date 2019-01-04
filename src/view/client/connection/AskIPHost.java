package view.client.connection;

import javax.swing.*;

public class AskIPHost extends JOptionPane {
    public static String getIPHost() {
        new IPAddressValidator();
        String input = (String) showInputDialog(null, "Server host : ", "Connect to game server",
                JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskIPHost.class.getResource("/view/resources/client_connection/host.png")), null, "localhost");

        if (input == null || input.trim().length() == 0)
            System.exit(0);

        while (!(IPAddressValidator.validate(input.trim()) || input.trim().equals("localhost"))) {
            input = (String) showInputDialog(null, "Server host (\"" +input.trim()+ "\" is not a valid IP) :", "Connect to game server",
                    JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskIPHost.class.getResource("/view/resources/client_connection/host.png")), null, "localhost");

            if (input == null || input.trim().length() == 0)
                System.exit(0);
        }

        return input.trim();
    }
}
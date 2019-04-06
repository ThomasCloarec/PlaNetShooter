package view.client.connection;

import javax.swing.*;

public class AskIPHost extends JOptionPane {
    private static boolean goBack = false;
    private static String input = "localhost";

    public static String getIPHost() {
        new IPAddressValidator();
        String messageBeginning = "";

        do {
            if (goBack)
                messageBeginning = "(\"" + input.trim() + "\" is not a valid game server IP) -> ";

            input = (String) showInputDialog(null, messageBeginning + "Server host : ", "Connect to game server",
                    JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskIPHost.class.getResource("/view/resources/client_connection/host.jpg")), null, input);

            if (input == null)
                System.exit(0);

            if (!(IPAddressValidator.validate(input.trim()) || input.trim().equals("localhost"))) {
                messageBeginning = "(\"" + input.trim() + "\" is not a valid IP) -> ";
                System.out.println("The IP is not valid");
            }

            goBack = false;
        } while (!(IPAddressValidator.validate(input.trim()) || input.trim().equals("localhost")));

        return input.trim();
    }

    public static void setGoBack(boolean goBack) {
        AskIPHost.goBack = goBack;
    }

    public static boolean isGoBack() {
        return goBack;
    }
}
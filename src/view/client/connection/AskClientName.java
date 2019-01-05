package view.client.connection;

import javax.swing.*;

public class AskClientName extends JOptionPane {
    private static boolean goBack = false;
    private static String message = "";
    private static String input = "Guest";
    public static String getClientName() {
        if (goBack) {
            message = "(" +input.trim()+ " is not available) -> ";
            goBack = false;
        }

        input = (String) showInputDialog(null, message+"Your name :", "Connect to game server",
                JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskClientName.class.getResource("/view/resources/client_connection/new_user.png")), null, input);

        if (input == null || input.trim().length() == 0)
            System.exit(0);

        return input.trim();
    }

    public static void setGoBack(boolean goBack) {
        AskClientName.goBack = goBack;
    }
}
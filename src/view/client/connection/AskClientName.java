package view.client.connection;

import javax.swing.*;
import java.util.List;

public class AskClientName extends JOptionPane {
    private static String message = "";
    private static String input = "Guest";
    private static List registerNameList;

    public static String getClientName() {
        do {
            while (registerNameList.contains(input.trim())) {
                if (stringToInteger(String.valueOf(input.trim().charAt(input.trim().length()-1))) >= 0)
                    input = input.trim().substring(0, input.trim().length()-1)
                            + (stringToInteger(String.valueOf(input.trim().charAt(input.trim().length()-1)))+1);
                else
                    input = input.trim() + 0;
            }

            input = (String) showInputDialog(null, message+"Your name :", "Connect to game server",
                    JOptionPane.QUESTION_MESSAGE, new ImageIcon(AskClientName.class.getResource("/view/resources/client_connection/new_user.png")), null, input);

            if (input == null || input.trim().length() == 0)
                System.exit(0);

            if (registerNameList.contains(input.trim())) {
                message = "(" +input.trim()+ " is not available) -> ";
            }
        }
        while (registerNameList.contains(input.trim()));

        return input.trim();
    }

    public static void setRegisterNameList(List registerNameList) {
        AskClientName.registerNameList = registerNameList;
    }

    private static int stringToInteger(String string) {
        int integer;
        try {
            integer = Integer.parseInt(string);
            return integer;
        }
        catch (NumberFormatException ignored) {
            return -1;
        }
    }
}
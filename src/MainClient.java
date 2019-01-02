import network.GameClient;
import view.client.connection.AskIPHost;

import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {
        GameClient gameClient = new GameClient(AskIPHost.IPHost());
    }
}
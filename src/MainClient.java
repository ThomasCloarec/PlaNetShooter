import com.esotericsoftware.kryonet.Client;
import network.GameClient;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;

import java.io.IOException;

public class MainClient {
    public static void main(String[] args) throws IOException {
        if (new Client().discoverHost(30083, 5000) != null) {
            GameClient gameClient = new GameClient(AskIPHost.IPHost());
        }
        else {
            new NoServerError();
        }
    }
}
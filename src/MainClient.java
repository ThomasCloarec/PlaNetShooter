import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.GameClient;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;

import javax.swing.*;
import java.io.IOException;

class MainClient {
    public static void main(String[] args) throws IOException {
        if (new Client().discoverHost(30083, 5000) != null) {
            GameClient gameClient = new GameClient(AskIPHost.IPHost());
            gameClient.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    gameClient.receivedListener(object);
                }
            });
            JFrame jFrame = new JFrame();
            jFrame.setVisible(true);
        }
        else {
            new NoServerError();
        }
    }
}
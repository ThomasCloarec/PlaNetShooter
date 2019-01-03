import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.GameClient;
import view.client.connection.AskClientName;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;

import javax.swing.*;
import java.io.IOException;

class MainClient {
    public static void main(String[] args) throws IOException {
        if (new Client().discoverHost(30083, 5000) != null) {
            GameClient gameClient = new GameClient(AskIPHost.getIPHost());
            gameClient.connectedListener(AskClientName.getClientName());

            gameClient.addListener(new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    gameClient.receivedListener(object);
                }
            });

            // jFRAME : Just to not close the Client while test, will be removed asap
            JFrame jFrame = new JFrame();
            jFrame.setVisible(true);
            // END jFRAME
        }
        else {
            new NoServerError();
        }
    }
}
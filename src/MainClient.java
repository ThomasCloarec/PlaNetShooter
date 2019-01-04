import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import model.platforms.Platform;
import network.GameClient;
import network.Network;
import view.client.connection.AskClientName;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;
import view.client.game.GameFrame;
import view.client.game.GamePanel;
import view.client.game.objects.PlatformView;

import javax.swing.*;
import java.io.IOException;

class MainClient {
    public static void main(String[] args) throws IOException {
        // If a game server is up on the network
        if (new Client().discoverHost(Network.udpPort, 5000) != null) {
            launchGameClient();

            SwingUtilities.invokeLater(() -> {
                GameFrame gameFrame = new GameFrame();
                Platform[] platforms = new Platform[Platform.platformNumber];
                GamePanel.platforms = new PlatformView[Platform.platformNumber];
                for (int i = 0; i < Platform.platformNumber ; i++) {
                    platforms[i] = new Platform();
                    GamePanel.platforms[i] = new PlatformView(platforms[i].relativeX,
                            platforms[i].relativeY, Platform.relativeWidth, Platform.relativeHeight);
                }
            });
        }
        else {
            new NoServerError();
        }
    }

    private static void launchGameClient() throws IOException {
        GameClient gameClient = new GameClient(AskIPHost.getIPHost());
        gameClient.connectedListener(AskClientName.getClientName());

        gameClient.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                gameClient.receivedListener(object);
            }
        });
    }
}
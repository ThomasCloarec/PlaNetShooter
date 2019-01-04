import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import model.platforms.Platform;
import network.GameClient;
import network.Network;
import view.client.connection.AskClientName;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;
import view.client.game_frame.GameFrame;
import view.client.game_frame.game_only.GamePanel;
import view.client.game_frame.game_only.PlatformView;

import javax.swing.*;
import java.io.IOException;

class MainClient {
    public static void main(String[] args) throws IOException {
        // If a game server is up on the network
        if (new Client().discoverHost(Network.udpPort, 5000) != null) {
            launchGameClient();

            SwingUtilities.invokeLater(() -> {
                GameFrame gameFrame = new GameFrame();
                Platform[] platforms = new Platform[Platform.getPlatformNumber()];
                GamePanel.setPlatformsView(new PlatformView[Platform.getPlatformNumber()]);
                for (int i = 0; i < Platform.getPlatformNumber() ; i++) {
                    platforms[i] = new Platform();
                    GamePanel.setEachPlatformView(i, new PlatformView(
                            platforms[i].getRelativeX(),
                            platforms[i].getRelativeY(),
                            Platform.getRelativeWidth(),
                            Platform.getRelativeHeight()));
                }
            });
        }
        else {
            new NoServerError();
        }
    }

    private static void launchGameClient() throws IOException {
        // TODO Handle when it's not a GameServer IP
        GameClient gameClient = new GameClient(AskIPHost.getIPHost());
        gameClient.connectedListener(AskClientName.getClientName());

        gameClient.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                gameClient.receivedListener(object);
            }

            @Override
            public void disconnected (Connection connection) {
                System.out.println("Server disconnected.");
                System.exit(1);
            }
        });
    }
}
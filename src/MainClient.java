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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

class MainClient {
    private static GameClient gameClient;
    public static void main(String[] args) {
        // If a game server is up on the network
        if (new Client().discoverHost(Network.udpPort, 5000) != null) {
            launchGameClient();

            SwingUtilities.invokeLater(() -> {
                GameFrame gameFrame = new GameFrame();
                gameFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing (WindowEvent evt) {
                        System.out.println("You are disconnected !");
                        System.exit(0);
                    }
                });
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

    private static void launchGameClient() {
        while(true) {
            try {
                gameClient = new GameClient(AskIPHost.getIPHost());
                break;
            } catch (IOException e) {
                System.out.println("No game server found with this IP on the network.");
                AskIPHost.goBack = true;
            }
        }

        gameClient.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                gameClient.receivedListener(object);
            }

            @Override
            public void disconnected (Connection connection) {
                System.out.println("You are disconnected !\nServer closed.");
                System.exit(1);
            }
        });

        String clientName;
        do {
            clientName = AskClientName.getClientName();
            if (gameClient.getRegisterNameList().list.indexOf(clientName) >= 0) {
                AskClientName.setGoBack(true);
            }
        }
        while (gameClient.getRegisterNameList().list.indexOf(clientName) >= 0);

        gameClient.connectedListener(clientName);
    }
}
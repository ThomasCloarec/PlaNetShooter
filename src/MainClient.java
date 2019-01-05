import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import model.platforms.Platform;
import model.characters.Character;
import network.GameClient;
import network.Network;
import view.client.connection.AskClientName;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;
import view.client.game_frame.GameFrame;
import view.client.game_frame.game_only.CharacterView;
import view.client.game_frame.game_only.PlatformView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

class MainClient {
    private static String clientName;
    private static GameClient gameClient;
    public static void main(String[] args) {
        // If a game server is up on the network
        if (new Client().discoverHost(Network.udpPort, 5000) != null) {
            launchGameClient();

            SwingUtilities.invokeLater(() -> {
                GameFrame gameFrame = new GameFrame(clientName);
                gameFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing (WindowEvent evt) {
                        System.out.println("You are disconnected !");
                        System.exit(0);
                    }
                });

                Platform[] platforms = new Platform[Platform.getPlatformNumber()];
                gameFrame.getGamePanel().setPlatformsView(new PlatformView[Platform.getPlatformNumber()]);
                for (int i = 0; i < Platform.getPlatformNumber() ; i++) {
                    platforms[i] = new Platform();
                    gameFrame.getGamePanel().setEachPlatformView(i, new PlatformView(
                            platforms[i].getRelativeX(),
                            platforms[i].getRelativeY(),
                            Platform.getRelativeWidth(),
                            Platform.getRelativeHeight()));
                }

                Character character = new Character();
                gameFrame.getGamePanel().setCharacterView(new CharacterView(
                        character.getRelativePositionX(),
                        character.getRelativePositionY(),
                        Character.getRelativeWidth(),
                        Character.getRelativeHeight()));

                gameFrame.getGamePanel().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
                            gameFrame.getGamePanel().getCharacterView().setRelativeX(gameFrame.getGamePanel().getCharacterView().getRelativeX()+ 0.01f);
                        else if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_LEFT)
                            gameFrame.getGamePanel().getCharacterView().setRelativeX(gameFrame.getGamePanel().getCharacterView().getRelativeX()- 0.01f);

                        gameFrame.getGamePanel().repaint();
                    }
                });

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
                AskIPHost.setGoBack(true);
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

        do {
            clientName = AskClientName.getClientName();
            if (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0) {
                AskClientName.setGoBack(true);
            }
        }
        while (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0);

        gameClient.connectedListener(clientName);
    }
}
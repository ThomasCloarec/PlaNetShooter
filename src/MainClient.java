import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import model.CollisionDetection;
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
import java.util.ArrayList;
import java.util.List;

class MainClient {
    private static String clientName;
    private static GameClient gameClient;
    private static List<Object> allSolidObjects = new ArrayList<>();
    private static boolean stopRight = false;
    private static boolean stopLeft = false;

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

                    allSolidObjects.add(platforms[i]);
                }

                Character character = new Character();
                gameFrame.getGamePanel().setCharacterView(new CharacterView(
                        character.getRelativeX(),
                        character.getRelativeY(),
                        Character.getRelativeWidth(),
                        Character.getRelativeHeight()));

                gameFrame.getGamePanel().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            for (int i = 0; i < 64; i++) {
                                stopLeft = false;
                                if (!stopRight) {
                                    character.setRelativeX(character.getRelativeX() + 0.00015625f);
                                    for (Object object : allSolidObjects) {
                                        if (CollisionDetection.isCollisionBetween(character, object)) {
                                            stopRight = true;
                                        }
                                    }
                                }
                            }
                        }
                        else if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_LEFT) {
                            for (int i = 0; i < 64; i++) {
                                stopRight = false;
                                if (!stopLeft) {
                                    character.setRelativeX(character.getRelativeX() - 0.00015625f);
                                    for (Object object : allSolidObjects) {
                                        if (CollisionDetection.isCollisionBetween(character, object)) {
                                            stopLeft = true;
                                        }
                                    }
                                }
                            }
                        }

                        gameFrame.getGamePanel().setCharacterView(new CharacterView(
                                character.getRelativeX(),
                                character.getRelativeY(),
                                Character.getRelativeWidth(),
                                Character.getRelativeHeight()));

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
            System.out.println(gameClient.getRegisterNameList());
            System.out.println(gameClient.getRegisterNameList().getList());
            if (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0) {
                AskClientName.setGoBack(true);
            }
        }
        while (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0);

        gameClient.connectedListener(clientName);
    }
}
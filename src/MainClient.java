import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import model.CollisionDetection;
import model.characters.Direction;
import model.platforms.Platform;
import model.characters.Character;
import network.GameClient;
import network.Network;
import view.client.connection.AskClientName;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;
import view.client.game_frame.GameFrame;
import view.client.game_frame.game_only.action.PressAction;
import view.client.game_frame.game_only.action.ReleaseAction;
import view.client.game_frame.game_only.CharacterView;
import view.client.game_frame.game_only.PlatformView;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class MainClient {
private static String clientName;
private static GameClient gameClient;
private static List<Object> allSolidObjects = new ArrayList<>();
private static boolean stopRight = false, stopLeft = false;
private static GameFrame gameFrame;
private static Character character;
private static CharacterView characterView;
private static final int FPS = 60;
private static final String RELEASE_LEFT = "Release.left", RELEASE_RIGHT = "Release.right", PRESS_LEFT = "Press.left", PRESS_RIGHT = "Press.right";
private static Set<Direction> movements = new TreeSet<>();

    public static void main(String[] args) {
        // If a game server is up on the network
        if (new Client().discoverHost(Network.getUdpPort(), 5000) != null) {
            launchGameClient();

            SwingUtilities.invokeLater(MainClient::launchGameFrame);
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

    private static void launchGameFrame() {
        gameFrame = new GameFrame(clientName);
        gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.out.println("You are disconnected !");
                System.exit(0);
            }
        });

        defineObjects();

        createKeyMap();

        launchGameLoop();
    }

    private static void defineObjects() {
        Platform[] platforms = new Platform[Platform.getPlatformNumber()];
        gameFrame.getGamePanel().setPlatformsView(new PlatformView[Platform.getPlatformNumber()]);
        for (int i = 0; i < Platform.getPlatformNumber(); i++) {
            platforms[i] = new Platform();
            gameFrame.getGamePanel().setEachPlatformView(i, new PlatformView(
                    platforms[i].getRelativeX(),
                    platforms[i].getRelativeY(),
                    Platform.getRelativeWidth(),
                    Platform.getRelativeHeight()));

            allSolidObjects.add(platforms[i]);
        }

        character = new Character();
        characterView = new CharacterView(
                character.getRelativeX(),
                character.getRelativeY(),
                Character.getRelativeWidth(),
                Character.getRelativeHeight());

        gameFrame.getGamePanel().setCharacterView(characterView);
    }

    private static void createKeyMap() {
        final InputMap IM = gameFrame.getGamePanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap AM = gameFrame.getGamePanel().getActionMap();

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), RELEASE_LEFT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), RELEASE_LEFT);

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), RELEASE_RIGHT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), RELEASE_RIGHT);

        AM.put(RELEASE_LEFT, new ReleaseAction(movements, Direction.LEFT));
        AM.put(RELEASE_RIGHT, new ReleaseAction(movements, Direction.RIGHT));

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), PRESS_RIGHT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), PRESS_RIGHT);

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), PRESS_LEFT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), PRESS_LEFT);

        AM.put(PRESS_LEFT, new PressAction(movements, Direction.LEFT));
        AM.put(PRESS_RIGHT, new PressAction(movements, Direction.RIGHT));
    }

    private static void launchGameLoop() {
        Timer timer = new Timer(1000/FPS, e -> {
            gameClient.sendPlayerPosition(character);

            float movement = 0;
            for (Direction direction : movements) {
                movement += direction.getDelta();
            }

            if (movement < 0) {
                stopRight = false;
                if (!stopLeft) {
                    character.setRelativeX(character.getRelativeX() + movement);
                    for (Object object : allSolidObjects) {
                        if (CollisionDetection.isCollisionBetween(character, object)) {
                            stopLeft = true;
                        }
                    }
                }
            } else if (movement > 0) {
                stopLeft = false;
                if (!stopRight) {
                    character.setRelativeX(character.getRelativeX() + movement);
                    for (Object object : allSolidObjects) {
                        if (CollisionDetection.isCollisionBetween(character, object)) {
                            stopRight = true;
                        }
                    }
                }
            }

            characterView.setRelativeX(character.getRelativeX());
            characterView.setRelativeY(character.getRelativeY());
            gameFrame.getGamePanel().repaint();
        });
        timer.start();
    }
}
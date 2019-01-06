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
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MainClient {
private static String clientName;
private static GameClient gameClient;
private static List<Object> allSolidObjects = new ArrayList<>();
private static boolean stopRight = false, stopLeft = false;
private static GameFrame gameFrame;
private static Character character;
private static CharacterView characterView;
private static final String MOVE_LEFT = "move left", MOVE_RIGHT = "move right", MOVE_STOP = "move stop";
private static final int FPS = 60;

    public static void main(String[] args) {
        // If a game server is up on the network
        if (new Client().discoverHost(Network.getUdpPort(), 5000) != null) {
            launchGameClient();

            SwingUtilities.invokeLater(() -> {
                gameFrame = new GameFrame(clientName);
                gameFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent evt) {
                        System.out.println("You are disconnected !");
                        System.exit(0);
                    }
                });

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

                final InputMap IM = gameFrame.getGamePanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
                final ActionMap AM = gameFrame.getGamePanel().getActionMap();
                MovementState movementState = new MovementState();

                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), MOVE_STOP);
                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), MOVE_STOP);
                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), MOVE_STOP);
                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), MOVE_STOP);
                AM.put(MOVE_STOP, new MoveXAction(movementState, 0f));

                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), MOVE_RIGHT);
                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), MOVE_RIGHT);
                AM.put(MOVE_RIGHT, new MoveXAction(movementState, Character.getRelativeSpeed()));

                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), MOVE_LEFT);
                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), MOVE_LEFT);
                AM.put(MOVE_LEFT, new MoveXAction(movementState, -Character.getRelativeSpeed()));
                Timer timer = new Timer(1000/FPS, e -> {
                    gameClient.sendPlayerPosition(character);
                    if (movementState.xDirection < 0) {
                        stopRight = false;
                        if (!stopLeft) {
                            character.setRelativeX(character.getRelativeX() + movementState.xDirection);
                            for (Object object : allSolidObjects) {
                                if (CollisionDetection.isCollisionBetween(character, object)) {
                                    stopLeft = true;
                                }
                            }
                        }
                    } else if (movementState.xDirection > 0) {
                        stopLeft = false;
                        if (!stopRight) {
                            character.setRelativeX(character.getRelativeX() + movementState.xDirection);
                            for (Object object : allSolidObjects) {
                                if (CollisionDetection.isCollisionBetween(character, object)) {
                                    stopRight = true;
                                }
                            }
                        }
                    }

                    characterView.setRelativeX(character.getRelativeX());
                    characterView.setRelativeY(character.getRelativeY());
                    gameFrame.getGamePanel().setCharacterView(characterView);

                    gameFrame.getGamePanel().repaint();
                });
                timer.start();

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
        /*
        TODO : Figure out why sometimes, the below variable is null
         6 jan, 00:27 | Put client/server register() right after the start(), before the connect()/bind()
         Have to see if it fix the problem
         */
        if (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0) {
            AskClientName.setGoBack(true);
        }
    }
    while (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0);

    gameClient.connectedListener(clientName);
}

    static class MoveXAction extends AbstractAction {
        private final MovementState movementState;
        private final float value;

        MoveXAction(MovementState movementState, float value) {
            this.movementState = movementState;
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.movementState.xDirection = this.value;
        }
    }

    static class MovementState {
        float xDirection;
    }
}
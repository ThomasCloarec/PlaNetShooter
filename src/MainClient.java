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
    private static boolean stopRight = false;
    private static boolean stopLeft = false;

    private static GameFrame gameFrame;
    private static Character character;

    private static final String MOVE_LEFT = "move left";
    private static final String MOVE_RIGHT = "move right";
    private static final String MOVE_STOP = "move stop";

    public static void main(String[] args) {
        // If a game server is up on the network
        if (new Client().discoverHost(Network.udpPort, 5000) != null) {
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
                gameFrame.getGamePanel().setCharacterView(new CharacterView(
                        character.getRelativeX(),
                        character.getRelativeY(),
                        Character.getRelativeWidth(),
                        Character.getRelativeHeight()));


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
                AM.put(MOVE_RIGHT, new MoveXAction(movementState, 0.00015625f));

                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), MOVE_LEFT);
                IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), MOVE_LEFT);
                AM.put(MOVE_LEFT, new MoveXAction(movementState, -0.00015625f));

                Timer timer = new Timer(40, e -> {
                    System.out.println(movementState.xDirection);
                    if (movementState.xDirection < 0) {
                        stopRight = false;
                        for (int i = 0; i < 64; i++) {
                            if (!stopLeft) {
                                character.setRelativeX(character.getRelativeX() + movementState.xDirection);
                                for (Object object : allSolidObjects) {
                                    if (CollisionDetection.isCollisionBetween(character, object)) {
                                        stopLeft = true;
                                    }
                                }
                            }
                        }
                    } else if (movementState.xDirection > 0) {
                        stopLeft = false;
                        for (int i = 0; i < 64; i++) {
                            if (!stopRight) {
                                character.setRelativeX(character.getRelativeX() + movementState.xDirection);
                                for (Object object : allSolidObjects) {
                                    if (CollisionDetection.isCollisionBetween(character, object)) {
                                        stopRight = true;
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
            // TODO : Figure out why sometimes, the below variable is null
            System.out.println(gameClient.getRegisterNameList());
            if (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0) {
                AskClientName.setGoBack(true);
            }
        }
        while (gameClient.getRegisterNameList().getList().indexOf(clientName) >= 0);

        gameClient.connectedListener(clientName);
    }

    public static class MoveXAction extends AbstractAction {
        private final MovementState movementState;
        private final float value;

        MoveXAction(MovementState movementState, float value) {
            this.movementState = movementState;
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getMovementState().xDirection = getValue();
        }

        float getValue() {
            return value;
        }

        MovementState getMovementState() {
            return movementState;
        }
    }

    public static class MovementState {
        float xDirection;
    }
}
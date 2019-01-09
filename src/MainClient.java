import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import model.CollisionDetection;
import model.PlayerCollisionSide;
import model.Terrain;
import model.characters.Direction;
import model.characters.PlayableCharacter;
import model.platforms.Platform;
import network.GameClient;
import network.Network;
import view.client.connection.AskClientName;
import view.client.connection.AskIPHost;
import view.client.connection.NoServerError;
import view.client.game_frame.GameFrame;
import view.client.game_frame.game_only.keyboard_actions.PressAction;
import view.client.game_frame.game_only.keyboard_actions.ReleaseAction;
import view.client.game_frame.game_only.CharacterView;
import view.client.game_frame.game_only.PlatformView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class MainClient {
private static String clientName;
private static GameClient gameClient;
private static final List<Object> allSolidObjects = new ArrayList<>();
private static float relativeMovementX = 0f;
private static float relativeMovementY = 0f;
private static boolean collisionOnRight = false, collisionOnLeft = false, collisionOnTop = false, collisionOnBottom = false;
private static boolean jumpKeyJustPressed = false;
private static GameFrame gameFrame;
private static PlayableCharacter playableCharacter;
private static CharacterView characterView;
private static final String RELEASE_LEFT = "Release.left", RELEASE_RIGHT = "Release.right", PRESS_LEFT = "Press.left", PRESS_RIGHT = "Press.right";
private static final Set<Direction> directions = new TreeSet<>();
private static final String OS = System.getProperty("os.name").toLowerCase();
private static final boolean IS_UNIX_OS = OS.contains("nix") || OS.contains("nux") || OS.contains("aix");

    public static void main(String[] args) {
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

        while (true) {
            if (gameClient.getRegisterNameList() != null) {
                AskClientName.setRegisterNameList(gameClient.getRegisterNameList().getList());
                clientName = AskClientName.getClientName();

                gameClient.connectedListener(clientName);
                break;
            }
        }

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

        playableCharacter = new PlayableCharacter();
        characterView = new CharacterView(
                playableCharacter.getRelativeX(),
                playableCharacter.getRelativeY(),
                PlayableCharacter.getRelativeWidth(),
                PlayableCharacter.getRelativeHeight());

        gameFrame.getGamePanel().setCharacterView(characterView);
    }

    private static void createKeyMap() {
        final InputMap IM = gameFrame.getGamePanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap AM = gameFrame.getGamePanel().getActionMap();

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), RELEASE_LEFT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), RELEASE_LEFT);

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), RELEASE_RIGHT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), RELEASE_RIGHT);

        AM.put(RELEASE_LEFT, new ReleaseAction(directions, Direction.LEFT));
        AM.put(RELEASE_RIGHT, new ReleaseAction(directions, Direction.RIGHT));

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), PRESS_RIGHT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), PRESS_RIGHT);

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), PRESS_LEFT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), PRESS_LEFT);

        AM.put(PRESS_LEFT, new PressAction(directions, Direction.LEFT));
        AM.put(PRESS_RIGHT, new PressAction(directions, Direction.RIGHT));

        gameFrame.getGamePanel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP)
                    if (collisionOnBottom)
                        jumpKeyJustPressed = true;
            }
        });
    }

    private static void launchGameLoop() {
        Timer timer = new Timer(1000/120, e -> {
            gameClient.sendPlayerPosition(playableCharacter);

            collisionOnTop = false;
            collisionOnBottom = false;
            collisionOnRight = false;
            collisionOnLeft = false;

            for (Object object : allSolidObjects) {
                if (CollisionDetection.isCollisionBetween(playableCharacter, object).equals(PlayerCollisionSide.TOP))
                    collisionOnTop = true;
                if (CollisionDetection.isCollisionBetween(playableCharacter, object).equals(PlayerCollisionSide.BOTTOM))
                    collisionOnBottom = true;
                if (CollisionDetection.isCollisionBetween(playableCharacter, object).equals(PlayerCollisionSide.RIGHT))
                    collisionOnRight = true;
                if (CollisionDetection.isCollisionBetween(playableCharacter, object).equals(PlayerCollisionSide.LEFT))
                    collisionOnLeft = true;
            }

            float totalDirection = 0;
            for (Direction direction : directions) {
                totalDirection += direction.getDelta();
            }

            if (collisionOnRight || collisionOnLeft)
                relativeMovementX = 0;
            else if (collisionOnBottom) {
                if (totalDirection == 1 && relativeMovementX < PlayableCharacter.getRelativeMaxSpeed())
                    relativeMovementX += PlayableCharacter.getRelativeSpeedGrowth();
                else if (totalDirection == -1 && relativeMovementX > -PlayableCharacter.getRelativeMaxSpeed())
                    relativeMovementX -= PlayableCharacter.getRelativeSpeedGrowth();
                else if (totalDirection == 0) {
                    if (Math.abs(relativeMovementX) < Terrain.getRelativeFriction())
                        relativeMovementX = 0;
                    else if (relativeMovementX > 0)
                        relativeMovementX -= Terrain.getRelativeFriction();
                    else if (relativeMovementX < 0)
                        relativeMovementX += Terrain.getRelativeFriction();
                }
            }
            else {
                if (totalDirection == 1 && relativeMovementX < PlayableCharacter.getRelativeMaxSpeed())
                    relativeMovementX += PlayableCharacter.getRelativeSpeedGrowth()/2;
                else if (totalDirection == -1 && relativeMovementX > -PlayableCharacter.getRelativeMaxSpeed())
                    relativeMovementX -= PlayableCharacter.getRelativeSpeedGrowth()/2;
            }

            if (collisionOnBottom) {
                if (jumpKeyJustPressed) {
                    while (collisionOnBottom) {
                        playableCharacter.setRelativeY(playableCharacter.getRelativeY()-PlayableCharacter.getRelativeJumpStrength());

                        for (Object object : allSolidObjects) {
                            collisionOnBottom = CollisionDetection.isCollisionBetween(playableCharacter, object).equals(PlayerCollisionSide.BOTTOM);
                            if (collisionOnBottom) {
                                break;
                            }
                        }
                    }
                    relativeMovementY -= PlayableCharacter.getRelativeJumpStrength();
                    playableCharacter.setRelativeY(playableCharacter.getRelativeY()+PlayableCharacter.getRelativeJumpStrength());

                    jumpKeyJustPressed = false;
                }
                else
                    relativeMovementY = 0;
            }
            else if (collisionOnTop)
                relativeMovementY = Terrain.getRelativeGravityGrowth();
            else if (relativeMovementY < Terrain.getRelativeMaxGravity())
                relativeMovementY += Terrain.getRelativeGravityGrowth();

            if (playableCharacter.getRelativeY() >= 1) {
                playableCharacter.setRelativeX(0.45f);
                playableCharacter.setRelativeY(0.1f);
            }

            playableCharacter.setRelativeX(playableCharacter.getRelativeX()+relativeMovementX);
            playableCharacter.setRelativeY(playableCharacter.getRelativeY()+relativeMovementY);
            characterView.setRelativeX(playableCharacter.getRelativeX());
            characterView.setRelativeY(playableCharacter.getRelativeY());
            gameFrame.getGamePanel().repaint();

            if (IS_UNIX_OS)
                Toolkit.getDefaultToolkit().sync();
        });
        timer.start();
    }
}
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import model.*;
import model.characters.Character;
import model.characters.ClassCharacters;
import model.characters.Direction;
import model.characters.Hit;
import network.GameClient;
import network.Network;
import view.client.Audio;
import view.client.connection.ConnectionFrame;
import view.client.connection.ServerFullError;
import view.client.game_frame.BulletView;
import view.client.game_frame.CharacterView;
import view.client.game_frame.GameFrame;
import view.client.game_frame.PlatformView;
import view.client.keyboard_actions.PressAction;
import view.client.keyboard_actions.ReleaseAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

class MainClient {
    private static boolean botActivated = false;
    private static String clientName;
    private static GameClient gameClient;
    private static float relativeMovementX = 0f;
    private static float relativeMovementY = 0f;
    private static boolean collisionOnRight = false, collisionOnLeft = false, collisionOnTop = false, collisionOnBottom = false;
    private static boolean collisionTrampoline = false;
    private static boolean jumpKeyJustPressed = false;
    private static GameFrame gameFrame;
    private static CharacterView characterView;
    private static final String RELEASE_LEFT = "Release.left", RELEASE_RIGHT = "Release.right", PRESS_LEFT = "Press.left", PRESS_RIGHT = "Press.right";
    private static final Set<Direction> directions = new TreeSet<>();
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_UNIX_OS = OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    private static boolean gameServerFull = false;
    private static String serverIP;
    private static float totalDirection = 0;
    private static volatile boolean readyToLaunchGameLoop = false;
    private static final ReleaseAction releaseActionLeft = new ReleaseAction(directions, Direction.LEFT);
    private static final ReleaseAction releaseActionRight = new ReleaseAction(directions, Direction.RIGHT);
    private static boolean readyToFire = false;
    private static MouseEvent lastMousePressedEvent;
    private static Platform[] platforms;
    private static Character character;
    private static boolean ultimateClick = false;
    private static boolean playerOnLeftYodel = false;
    private static boolean playerOnRightYodel = false;
    private static boolean yodelDetection = false;
    private static boolean cancelUltimate = false;
    private static long lastDamageOnPlayerTime = 0;
    private static String lastAttackerOnPlayer;
    private static long lastUltimateFire = 0;
    private static boolean clientSuccessfullyStarted = false;
    private static final HashMap<String, Audio> sounds = new HashMap<>();

    public static void main(String[] args) {
        Log.set(Log.LEVEL_NONE);
        System.out.println("Starting client...");
        launchGameClient();
        if (!gameServerFull) {
            SwingUtilities.invokeLater(MainClient::launchGameFrame);

            while (true)
                if (readyToLaunchGameLoop) break;

            launchGameLoop();
        }
        else {
            System.out.println("Server full. Closing client...");
            new ServerFullError();
            System.out.println("Client closed");
            System.exit(0);
        }
    }

    private static void restartGame() throws IOException, URISyntaxException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(MainClient.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        if(!currentJar.getName().endsWith(".jar"))
            return;

        final ArrayList<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }

    private static void launchGameClient() {
        boolean[] finalGameClientLaunched = {false};
        Listener gameClientListener = null;

        while (!finalGameClientLaunched[0]) {
            if (gameClient != null) {
                System.out.println("(" +((GameClient.getConnectingTimeout() / 2)/1000)+ "s timeout) Connecting failed. Trying again");

                try {
                    if (gameClientListener != null)
                        gameClient.removeListener(gameClientListener);
                    gameClient.reconnect((int) Math.ceil(GameClient.getConnectingTimeout()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                while (gameClient == null || ConnectionFrame.isGoBack()) {
                    try {
                        serverIP = ConnectionFrame.getIPHost();
                        System.out.println("\nConnecting to the server... (" + serverIP + ")");
                        gameClient = new GameClient(serverIP);
                        break;
                    } catch (IOException e) {
                        System.out.println("No game server found with this IP on the network");
                        ConnectionFrame.setGoBack(true);
                    }
                }
            }
            gameClientListener = new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    SwingUtilities.invokeLater(() -> {
                        if (finalGameClientLaunched[0]) {
                            if (object instanceof Character && clientSuccessfullyStarted) {
                                gameClient.receivedListener(object);
                                Character otherPlayer = (Character) object;

                                for (Hit hit : otherPlayer.getHits()) {
                                    if (hit.getVictim().equals((character.getName())) && hit.getTime() != gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).getHits().get(otherPlayer.getHits().indexOf(hit)).getTime()) {
                                        lastDamageOnPlayerTime = System.currentTimeMillis();
                                        lastAttackerOnPlayer = otherPlayer.getName();

                                        if ((!character.getClassCharacter().equals(ClassCharacters.MEDUSO)) || (!character.isUltimate1Running() && !character.isUltimate2Running() && !character.isUltimate3Running())) {
                                            character.setHealth(character.getHealth() - hit.getDamage() / character.getMaxHealth());
                                            if (character.getHealth() <= 0) {
                                                character.setLastDeathTime(System.currentTimeMillis());
                                                character.setLastKiller(otherPlayer.getName());
                                                character.setDeaths(character.getDeaths() + 1);
                                                sounds.get(character.getClassCharacter().name().toLowerCase() + ".dead").play();

                                                if (character.getMoney() >= 1)
                                                    character.setMoney(character.getMoney() - 1);

                                                randomSpawn();
                                            }
                                        }
                                    }
                                }
                                if (gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName()) < gameClient.getOtherPlayers().size()) {
                                    gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).setHits(otherPlayer.getHits());
                                    if (otherPlayer.getLastKiller().equals(character.getName()) && otherPlayer.getLastDeathTime() != gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).getLastDeathTime()) {
                                        character.setKills(character.getKills() + 1);
                                        character.setMoney(character.getMoney() + 3);
                                        gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).setLastKiller(otherPlayer.getLastKiller());
                                        gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).setLastDeathTime(otherPlayer.getLastDeathTime());
                                    }
                                }
                            }
                            if (object instanceof Network.RemoveName) {
                                Network.RemoveName removeName = (Network.RemoveName) object;

                                for (BulletView bulletView : gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.getRegisterList().getNameList().indexOf(removeName.name)).getBulletsViews()) {
                                    gameFrame.getGamePanel().remove(bulletView.getBulletLabel());
                                }

                                if (gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.getRegisterList().getNameList().indexOf(removeName.name)).getNameLabel().getParent() != null)
                                    gameFrame.getGamePanel().remove(gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.getRegisterList().getNameList().indexOf(removeName.name)).getNameLabel());

                                if (gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.getRegisterList().getNameList().indexOf(removeName.name)).getCharacterLabel().getParent() != null)
                                    gameFrame.getGamePanel().remove(gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.getRegisterList().getNameList().indexOf(removeName.name)).getCharacterLabel());

                                gameFrame.getGamePanel().getOtherPlayersViews().remove(gameClient.getRegisterList().getNameList().indexOf(removeName.name));
                                gameFrame.getHomePanel().getRemoveOthersPlayersHomeIndex().add(gameClient.getRegisterList().getNameList().indexOf(removeName.name));

                                gameClient.receivedListener(object);
                            }
                        }
                        if (!(object instanceof Network.RemoveName) && !(object instanceof Character)) {
                            gameClient.receivedListener(object);
                        }
                    });
                }

                @Override
                public void disconnected(Connection connection) {
                    if (finalGameClientLaunched[0]) {
                        System.out.println("You are disconnected !\nServer closed");
                        System.exit(1);
                    }
                }
            };
            gameClient.addListener(gameClientListener);

            clientName = "P1";

            long startGetRegisterList = System.currentTimeMillis();
            while (true) {
                if (gameClient.getRegisterList() != null) {
                    if (gameClient.getRegisterList().getNameList().size() == 20) {
                        gameServerFull = true;
                    } else {
                        while (gameClient.getRegisterList().getNameList().contains(clientName)) {
                            clientName = "P" + (Integer.parseInt(clientName.substring(1)) + 1);
                        }
                        gameClient.connectedListener(clientName);
                    }
                    finalGameClientLaunched[0] = true;
                    break;
                } else {
                    if (System.currentTimeMillis() - GameClient.getConnectingTimeout() > startGetRegisterList) {
                        if (GameClient.getConnectingTimeout() >= 4000) {
                            try {
                                restartGame();
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                        GameClient.setConnectingTimeout(GameClient.getConnectingTimeout() * 2);
                        break;
                    }
                }
            }
        }
    }

    private static void launchGameFrame() {
        for (ClassCharacters classCharacter : ClassCharacters.getClassCharactersList()) {
            sounds.put(classCharacter.name().toLowerCase() + ".bullet", new Audio("/view/resources/game/audio/characters/" + classCharacter.name().toLowerCase() + "/bullet.wav"));
            sounds.put(classCharacter.name().toLowerCase() + ".dead", new Audio("/view/resources/game/audio/characters/" + classCharacter.name().toLowerCase() + "/dead.wav"));
            sounds.put(classCharacter.name().toLowerCase() + ".home", new Audio("/view/resources/game/audio/characters/" + classCharacter.name().toLowerCase() + "/home.wav"));
            sounds.put(classCharacter.name().toLowerCase() + ".ultimate", new Audio("/view/resources/game/audio/characters/" + classCharacter.name().toLowerCase() + "/ultimate.wav"));
        }

        gameFrame = new GameFrame(clientName);
        gameFrame.setIsClientAdmin(serverIP.equals("localhost"));

        gameFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.out.println("You are disconnected !");
                System.exit(0);
            }
        });

        defineObjects();

        createKeyMap();

        readyToLaunchGameLoop = true;
    }

    private static void defineObjects() {
        platforms = new Platform[Platform.getPlatformNumber()];
        gameFrame.getGamePanel().setPlatformsView(new PlatformView[Platform.getPlatformNumber()]);
        for (int i = 0; i < Platform.getPlatformNumber(); i++) {
            platforms[i] = new Platform();
            gameFrame.getGamePanel().setEachPlatformView(i, new PlatformView(
                    platforms[i].getRelativeX(),
                    platforms[i].getRelativeY(),
                    platforms[i].getRelativeWidth(),
                    platforms[i].getRelativeHeight()));
        }

        character = new Character(clientName);
        character.setRelativeY(-1.15f);
        character.setRelativeX(0.5f);
        character.setClassCharacter(ClassCharacters.BOB);
        for (int i = 0; i < Character.getMaxBulletNumberPerPlayer(); i++) {
            Bullet bullet = new Bullet();
            bullet.setRelativeWidth(0);
            bullet.setRelativeHeight(0);

            character.getBullets().add(bullet);
        }

        characterView = new CharacterView(
                character.getRelativeX(),
                character.getRelativeY(),
                character.getRelativeWidth(),
                character.getRelativeHeight(),
                character.getName(),
                character.getClassCharacter(),
                character.getHealth());

        for (int i = 0; i < Character.getMaxBulletNumberPerPlayer(); i++) {
            BulletView bulletView = new BulletView(0,0,0,0);
            characterView.getBulletsViews().add(bulletView);
        }

        gameFrame.getGamePanel().setCharacterView(characterView);
        gameFrame.getHomePanel().setClassCharacter(character.getClassCharacter());
        gameFrame.getHomePanel().setPlayerName(character.getName());
    }

    private static void createKeyMap() {
        final InputMap IM = gameFrame.getGamePanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap AM = gameFrame.getGamePanel().getActionMap();

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), RELEASE_LEFT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), RELEASE_LEFT);

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), RELEASE_RIGHT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), RELEASE_RIGHT);

        AM.put(RELEASE_LEFT, releaseActionLeft);
        AM.put(RELEASE_RIGHT, releaseActionRight);

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), PRESS_RIGHT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), PRESS_RIGHT);

        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), PRESS_LEFT);
        IM.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), PRESS_LEFT);

        AM.put(PRESS_LEFT, new PressAction(directions, Direction.LEFT));
        AM.put(PRESS_RIGHT, new PressAction(directions, Direction.RIGHT));

        gameFrame.getGamePanel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    ultimateClick = false;
                    readyToFire = false;
                    jumpKeyJustPressed = false;
                    cancelUltimate = false;

                    character.setClassCharacter(character.getClassCharacter());
                    characterView.setClassCharacter(character.getClassCharacter());

                    for (BulletView bulletView : characterView.getBulletsViews()) {
                        try {
                            bulletView.setIcon("/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                        } catch (NullPointerException ex) {
                            System.err.println("Can't find \"/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png\" !");
                        }
                    }
                    gameFrame.getHomePanel().setClassCharacter(character.getClassCharacter());

                    botActivated = !botActivated;
                }
                else if (e.getKeyCode() == KeyEvent.VK_H) {
                    gameFrame.getGamePanel().setHitBoxMode(gameFrame.getGamePanel().isHitBoxMode());
                }

                if (!botActivated) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_Z) {
                        if (collisionOnBottom)
                            jumpKeyJustPressed = true;
                        yodelDetection = false;
                    } else if (e.getKeyCode() == KeyEvent.VK_E && !(CollisionDetection.isCollisionBetween(character, new Home()).equals(CollisionSide.NONE))) {
                        gameFrame.getCardLayout().next(gameFrame.getContentPane());

                        if (character.getMoney() >= 5)
                            character.setMoney(character.getMoney() - 5);
                        else
                            character.setMoney(0);
                        character.setRelativeX(0.5f);
                        character.setRelativeY(-1.15f);
                        character.setAtHome(true);
                    }
                }
            }
        });

        gameFrame.getGamePanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!botActivated) {
                    super.mousePressed(e);
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        lastMousePressedEvent = e;
                        readyToFire = true;
                    } else if (SwingUtilities.isRightMouseButton(e) && character.getUltimateLoading() == 1) {
                        lastMousePressedEvent = e;
                        sounds.get(character.getClassCharacter().name().toLowerCase() + ".ultimate").play();
                        ultimateClick = true;
                    } else if (ultimateClick) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            cancelUltimate = true;
                            lastMousePressedEvent = e;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!botActivated) {
                    super.mouseReleased(e);
                    if (SwingUtilities.isLeftMouseButton(e))
                        readyToFire = false;
                }
            }
        });

        gameFrame.getGamePanel().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!botActivated) {
                    super.mouseMoved(e);
                    lastMousePressedEvent = e;
                }
            }
        });

        gameFrame.getHomePanel().getPlayButton().addActionListener(e -> {
            gameFrame.getCardLayout().next(gameFrame.getContentPane());
            gameFrame.getGamePanel().requestFocus();
            character.setAtHome(false);
            randomSpawn();
            sounds.get(character.getClassCharacter().name().toLowerCase() + ".home").play();
        });

        gameFrame.getHomePanel().getChangeCharacterButton().addActionListener(e -> {
            ultimateClick = false;
            character.setClassCharacter(ClassCharacters.getClassCharactersList().get((ClassCharacters.getClassCharactersList().indexOf(character.getClassCharacter()) + 1) % ClassCharacters.getClassCharactersList().size()));
            characterView.setClassCharacter(character.getClassCharacter());

            for (BulletView bulletView : characterView.getBulletsViews()) {
                try {
                    bulletView.setIcon("/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                } catch (NullPointerException ex) {
                    System.err.println("Can't find \"/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png\" !");
                }
            }

            character.setUltimateLoading(0f);
            character.setRelativeY(-1.15f);
            character.setRelativeX(0.5f);
            gameFrame.getHomePanel().setClassCharacter(character.getClassCharacter());
        });

        System.out.println("Client successfully started !");
        clientSuccessfullyStarted = true;
    }

    private static void botThinking() {
        if (collisionOnBottom) {
            for (Platform platform : platforms) {
                if (CollisionDetection.isCollisionBetween(character, platform).equals(CollisionSide.BOTTOM)) {
                    if (Bot.getLastClosestPlatformAbove() != null) {
                        if (Bot.getActualJumpX() < Bot.getMaxJumpDistanceX())
                            Bot.setMaxJumpDistanceX(Bot.getActualJumpX());
                    }

                    Bot.setLastClosestPlatformAbove(Bot.getClosestPlatformAbove());
                    Bot.setActualJumpX(Bot.getDistanceXClosestPlatform());

                    if (Bot.getDistanceXClosestPlatform() < Bot.getMaxJumpDistanceX()) {
                        jumpKeyJustPressed = true;
                    }
                }
            }
        }

        Bot.setDistanceXClosestPlatform(1f);
        Bot.setDistanceYClosestPlatform(1f);

        if (relativeMovementY >= 0f) {
            // Distance Y closest platform
            for (Platform platform : platforms) {
                if (Math.abs(character.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2 < Bot.getDistanceYClosestPlatform() && character.getCenterY() > platform.getCenterY()) {
                    Bot.setDistanceYClosestPlatform(Math.abs(character.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2);
                }
            }

            // Distance X closest platform
            for (Platform platform : platforms) {
                if (Math.abs(character.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2 == Bot.getDistanceYClosestPlatform() && character.getCenterY() > platform.getCenterY()) {
                    if (Math.abs(character.getCenterX() - platform.getCenterX()) - platform.getRelativeWidth() / 2 < Bot.getDistanceXClosestPlatform()) {
                        Bot.setDistanceXClosestPlatform(Math.abs(character.getCenterX() - platform.getCenterX()) - platform.getRelativeWidth() / 2);
                    }
                }
            }

            // Set closest platform
            for (Platform platform : platforms) {
                if (Math.abs(character.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2 == Bot.getDistanceYClosestPlatform() && character.getCenterY() > platform.getCenterY()) {
                    if (Math.abs(character.getCenterX() - platform.getCenterX()) - platform.getRelativeWidth() / 2 == Bot.getDistanceXClosestPlatform()) {
                        if (Bot.getClosestPlatformAbove() == null || !Bot.getClosestPlatformAbove().equals(platform)) {
                            Bot.setClosestPlatformAbove(platform);
                        }
                    }
                }
            }
        }


        // Set direction toward closest platform
        if (Bot.getClosestPlatformAbove().getCenterX() < character.getCenterX()) {
            totalDirection = -1;
        } else {
            totalDirection = 1;
        }
    }

    private static void launchGameLoop() {
        int[] fpsRecord = new int[1];
        String gameFrameTitleWithoutFPS = gameFrame.getTitle();
        final long[] a = {System.currentTimeMillis()};

        Thread gameLoopThread;
        gameLoopThread = new Thread(() -> {
            long lastTime = System.currentTimeMillis();

            while (true) {
                if (System.currentTimeMillis() - lastTime > 16) {
                    lastTime = System.currentTimeMillis();
                    fpsRecord[0]++;
                    if (System.currentTimeMillis() - a[0] > 250) {
                        gameFrame.setTitle(gameFrameTitleWithoutFPS + " | FPS : " + fpsRecord[0] * 4);
                        fpsRecord[0] = 0;
                        a[0] = System.currentTimeMillis();
                    }

                    collisionOnTop = false;
                    collisionOnBottom = false;
                    collisionOnRight = false;
                    collisionOnLeft = false;
                    collisionTrampoline = false;

                    for (Platform platform : platforms) {
                        if (CollisionDetection.isCollisionBetween(character, platform).equals(CollisionSide.TOP))
                            collisionOnTop = true;
                        if (CollisionDetection.isCollisionBetween(character, platform).equals(CollisionSide.BOTTOM))
                            collisionOnBottom = true;
                        if (CollisionDetection.isCollisionBetween(character, platform).equals(CollisionSide.RIGHT))
                            collisionOnRight = true;
                        if (CollisionDetection.isCollisionBetween(character, platform).equals(CollisionSide.LEFT))
                            collisionOnLeft = true;
                    }

                    if (botActivated) {
                        botThinking();
                    }
                    else {
                        totalDirection = 0;
                        try {
                            for (Direction direction : directions) {
                                totalDirection += direction.getDelta();
                            }
                        } catch (ConcurrentModificationException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    if (character.getClassCharacter().equals(ClassCharacters.ANGELO) && ultimateClick) {
                        totalDirection = 0;
                    }

                    if (!gameFrame.getGamePanel().hasFocus()) {
                        releaseActionLeft.removeMovements();
                        releaseActionRight.removeMovements();
                    }

                    character.setHorizontalDirection(-totalDirection);
                    characterView.setHorizontalDirection(character.getHorizontalDirection());

                    if (!character.isAtHome()) {
                        if (character.getUltimateLoading() >= 1f - 0.2f / 60f)
                            character.setUltimateLoading(1f);
                        else
                            character.setUltimateLoading(character.getUltimateLoading() + 0.2f / 60f);
                    }

                    if (ultimateClick) {
                        if (character.getClassCharacter().equals(ClassCharacters.MONK)) {
                            character.setClassCharacter(character.getClassCharacter());
                            characterView.setClassCharacter(character.getClassCharacter());
                            character.setRelativeY((lastMousePressedEvent.getY() - character.getRelativeHeight() * gameFrame.getGamePanel().getHeight() / 2f) / (float) gameFrame.getGamePanel().getHeight());
                            character.setRelativeX((lastMousePressedEvent.getX() - character.getRelativeWidth() * gameFrame.getGamePanel().getWidth() / 2f) / (float) gameFrame.getGamePanel().getWidth());
                            ultimateClick = false;
                            character.setUltimateLoading(0f);
                        } else if (character.getClassCharacter().equals(ClassCharacters.BOB)) {
                            character.setClassCharacter(character.getClassCharacter());
                            characterView.setClassCharacter(character.getClassCharacter());
                            ultimateClick = false;
                            character.setUltimateLoading(0f);
                            Bullet bullet = new Bullet();
                            bullet.setDamage(0.5f);
                            bullet.setRelativeHeight(0.05f * 768f / 372f);
                            bullet.setRelativeWidth(0.05f);

                            float relativeBulletStartX = character.getRelativeX() + ((float) -characterView.getHorizontalDirection() + 1) * (character.getRelativeWidth() / 2f - bullet.getRelativeWidth() / 2f);
                            float relativeBulletStartY = character.getCenterY() - bullet.getRelativeHeight() / 2f;
                            bullet.setRelativeBulletStartX(relativeBulletStartX);
                            bullet.setRelativeBulletStartY(relativeBulletStartY);

                            float relativeCursorGoX = lastMousePressedEvent.getX() - bullet.getRelativeWidth() * gameFrame.getGamePanel().getWidth() / 2f;
                            float relativeCursorGoY = lastMousePressedEvent.getY() - bullet.getRelativeHeight() * gameFrame.getGamePanel().getHeight() / 2f;

                            float tempDeltaX = Math.abs(relativeBulletStartX * (float) gameFrame.getGamePanel().getWidth() - relativeCursorGoX);
                            float tempDeltaY = Math.abs(relativeBulletStartY * (float) gameFrame.getGamePanel().getHeight() - relativeCursorGoY);

                            float bulletSpeedRatio = ((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f * ((float) gameFrame.getGamePanel().getHeight() / (float) gameFrame.getGamePanel().getWidth() - 372f / 768f) * 768f / 372f + 1f;

                            float bulletMovementX = bulletSpeedRatio * tempDeltaX / (tempDeltaX + tempDeltaY) * bullet.getSpeed() * (relativeCursorGoX - relativeBulletStartX * gameFrame.getGamePanel().getWidth()) / tempDeltaX;
                            float bulletMovementY = bulletSpeedRatio * tempDeltaY / (tempDeltaX + tempDeltaY) * bullet.getSpeed() * (relativeCursorGoY - relativeBulletStartY * gameFrame.getGamePanel().getHeight()) / tempDeltaY;
                            bullet.setMovementX(bulletMovementX);
                            bullet.setMovementY(bulletMovementY);

                            bullet.setRange((((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f + 1f) * 0.4f);

                            SwingUtilities.invokeLater(() -> {
                                for (Bullet bullet1 : character.getBullets()) {
                                    if (bullet1.getRelativeWidth() == 0 && bullet1.getRelativeHeight() == 0) {
                                        character.getBullets().set(character.getBullets().indexOf(bullet1), bullet);
                                        break;
                                    }
                                }
                            });
                            lastUltimateFire = System.currentTimeMillis();
                        }
                        else //noinspection StatementWithEmptyBody
                            if (character.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {

                        } else if (!character.getClassCharacter().equals(ClassCharacters.BOB) && !character.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {
                                if (!character.getClassCharacter().equals(ClassCharacters.MONK)) {
                                    if (!character.isUltimate1Running() && !character.isUltimate2Running() && !character.isUltimate3Running()) {
                                        character.ultimate1();
                                    characterView.ultimate1();
                                    } else if (character.isUltimate1Running()) {
                                        if ((character.getClassCharacter().equals(ClassCharacters.TATITATOO)) && (cancelUltimate)) {
                                            character.setClassCharacter(character.getClassCharacter());
                                            characterView.setClassCharacter(character.getClassCharacter());
                                        ultimateClick = false;
                                        cancelUltimate = false;
                                    }

                                        if (System.currentTimeMillis() - character.getUltimate1StartTimeMillis() > character.getUltimate1DurationMillis()) {
                                            character.ultimate2();
                                        characterView.ultimate2();
                                    }
                                    } else if (character.isUltimate2Running()) {
                                        if ((character.getClassCharacter().equals(ClassCharacters.MEDUSO)) && (character.getHealth() < 1f)) {
                                            character.setHealth(character.getHealth() + 0.0013f);
                                    }
                                        if (((character.getClassCharacter().equals(ClassCharacters.ANGELO)) && (cancelUltimate)) || ((character.getClassCharacter().equals(ClassCharacters.MEDUSO)) && (cancelUltimate))) {
                                            character.ultimate3();
                                        characterView.ultimate3();
                                        } else if (System.currentTimeMillis() - character.getUltimate2StartTimeMillis() > character.getUltimate2DurationMillis()) {
                                            character.ultimate3();
                                        characterView.ultimate3();
                                    }
                                    } else if (character.isUltimate3Running()) {
                                        if (System.currentTimeMillis() - character.getUltimate3StartTimeMillis() > character.getUltimate3DurationMillis()) {
                                            character.setClassCharacter(character.getClassCharacter());
                                            characterView.setClassCharacter(character.getClassCharacter());

                                        ultimateClick = false;
                                        cancelUltimate = false;
                                        jumpKeyJustPressed = false;
                                    }
                                } else {
                                    ultimateClick = false;
                                    cancelUltimate = false;
                                }
                                    character.setUltimateLoading(0f);
                            }
                        }
                    }

                    Iterator<Object> itr = character.getInventory().iterator();
                    while (itr.hasNext()) {
                        Object object = itr.next();
                        if (object instanceof Trampoline) {
                            if (System.currentTimeMillis() - ((Trampoline) object).getCreationTime() > ((Trampoline) object).getDurationTime() * 1000) {
                                itr.remove();
                            }
                        }
                    }

                    for (Object object : character.getInventory()) {
                        if (object instanceof Trampoline) {

                            if ((!CollisionDetection.isCollisionBetween(character, (Trampoline) object).equals(CollisionSide.NONE)) && (!collisionOnTop)) {
                                collisionTrampoline = true;
                                break;
                            }
                        }
                    }

                    if (!collisionTrampoline) {
                        for (Character otherPlayer : gameClient.getOtherPlayers()) {
                            for (Object object : otherPlayer.getInventory()) {
                                if (object instanceof Trampoline) {
                                    if ((!CollisionDetection.isCollisionBetween(character, (Trampoline) object).equals(CollisionSide.NONE)) && (!collisionOnTop)) {
                                        collisionTrampoline = true;
                                        break;
                                    }
                                }
                            }
                            if (collisionTrampoline) {
                                break;
                            }
                        }
                    }

                    if (!yodelDetection) {
                        if (!CollisionDetection.isCollisionBetween(character, new Yodel("left")).equals(CollisionSide.NONE))
                            playerOnLeftYodel = true;
                        else if (CollisionDetection.isCollisionBetween(character, new Yodel("left")).equals(CollisionSide.NONE))
                            playerOnLeftYodel = false;
                        if (!CollisionDetection.isCollisionBetween(character, new Yodel("right")).equals(CollisionSide.NONE))
                            playerOnRightYodel = true;
                        else if (CollisionDetection.isCollisionBetween(character, new Yodel("right")).equals(CollisionSide.NONE))
                            playerOnRightYodel = false;
                    }

                    if ((playerOnLeftYodel) || (playerOnRightYodel)) {
                        yodelDetection = true;
                        if (playerOnRightYodel) {
                            if (character.getRelativeX() > 0.61f) {
                                character.setRelativeX(character.getRelativeX() - 0.004f);
                                character.setRelativeY(0.21f);
                                relativeMovementY = -0.005f;
                                relativeMovementX = -0.004f;

                            } else if (character.getRelativeX() <= 0.61f) {
                                playerOnRightYodel = false;
                                yodelDetection = false;
                                relativeMovementY = -0.01f;
                                relativeMovementX = -0.005f;
                            }
                        }
                        if (playerOnLeftYodel) {
                            if (character.getRelativeX() < 0.35f) {
                                character.setRelativeX(character.getRelativeX() + 0.004f);
                                character.setRelativeY(0.21f);
                                relativeMovementY = -0.005f;
                                relativeMovementX = 0.004f;

                            } else if (character.getRelativeX() >= 0.35f) {
                                playerOnLeftYodel = false;
                                yodelDetection = false;
                                relativeMovementY = -0.01f;
                                relativeMovementX = 0.005f;
                            }
                        }
                    }
                    else if (collisionTrampoline) {
                        relativeMovementY = -0.017f;
                        character.setRelativeY(character.getRelativeY() - new Trampoline().getRelativeWidth());
                    }
                    else {
                        if ((collisionOnRight && relativeMovementX > 0) || (collisionOnLeft && relativeMovementX < 0))
                            relativeMovementX = 0;

                        else if (collisionOnBottom) {
                            if (totalDirection == 1 && relativeMovementX < character.getRelativeMaxSpeed())
                                relativeMovementX += character.getRelativeSpeedGrowth();
                            else if (totalDirection == -1 && relativeMovementX > -character.getRelativeMaxSpeed())
                                relativeMovementX -= character.getRelativeSpeedGrowth();
                            else {
                                if (Math.abs(relativeMovementX) < Terrain.getRelativeFriction())
                                    relativeMovementX = 0;
                                else if (relativeMovementX > 0)
                                    relativeMovementX -= Terrain.getRelativeFriction();
                                else if (relativeMovementX < 0)
                                    relativeMovementX += Terrain.getRelativeFriction();
                            }
                        } else {
                            if (totalDirection == 1 && relativeMovementX < character.getRelativeMaxSpeed())
                                relativeMovementX += character.getRelativeSpeedGrowth() / 2;
                            else if (totalDirection == -1 && relativeMovementX > -character.getRelativeMaxSpeed())
                                relativeMovementX -= character.getRelativeSpeedGrowth() / 2;
                            else {
                                if (Math.abs(relativeMovementX) < Terrain.getRelativeFriction() / 10)
                                    relativeMovementX = 0;
                                else if (relativeMovementX > 0)
                                    relativeMovementX -= Terrain.getRelativeFriction() / 10;

                                else if (relativeMovementX < 0)
                                    relativeMovementX += Terrain.getRelativeFriction() / 10;
                            }
                        }

                        if (collisionOnBottom) {
                            if (jumpKeyJustPressed && character.getRelativeJumpStrength() > 0.0001f) {
                                relativeMovementY = -character.getRelativeJumpStrength();
                                jumpKeyJustPressed = false;
                            } else if (relativeMovementY > 0)
                                relativeMovementY = 0;
                        } else if (collisionOnTop && relativeMovementY < 0)
                            relativeMovementY = Terrain.getRelativeGravityGrowth();
                        else if (relativeMovementY < Terrain.getRelativeMaxGravity()) {
                            relativeMovementY += Terrain.getRelativeGravityGrowth();
                            jumpKeyJustPressed = false;
                        }

                        if (character.getRelativeY() >= 1) {
                            if (System.currentTimeMillis()  - 1500 < lastDamageOnPlayerTime) {
                                character.setLastKiller(lastAttackerOnPlayer);
                                character.setLastDeathTime(lastDamageOnPlayerTime);
                            }

                            if (character.getMoney() >= 1)
                                character.setMoney(character.getMoney() - 1);

                            character.setDeaths(character.getDeaths() + 1);
                            sounds.get(character.getClassCharacter().name().toLowerCase() + ".dead").play();
                            randomSpawn();
                        }

                        character.setRelativeX(character.getRelativeX() + relativeMovementX);
                        character.setRelativeY(character.getRelativeY() + relativeMovementY);
                    }

                    for (Platform platform : platforms) {
                        collisionOnBottom = CollisionDetection.isCollisionBetween(character, platform).equals(CollisionSide.BOTTOM);
                        if (collisionOnBottom) {
                            break;
                        }
                    }

                    if (collisionOnBottom) {
                        while (collisionOnBottom) {
                            character.setRelativeY(character.getRelativeY() - 0.001f);

                            for (Platform platform : platforms) {
                                collisionOnBottom = CollisionDetection.isCollisionBetween(character, platform).equals(CollisionSide.BOTTOM);
                                if (collisionOnBottom) {
                                    break;
                                }
                            }
                        }
                        character.setRelativeY(character.getRelativeY() + 0.001f);
                        collisionOnBottom = true;
                    }

                    if ((System.currentTimeMillis() - character.getLastSmallWaveTime() > 3000f) && (System.currentTimeMillis() - lastDamageOnPlayerTime > 3000f) && (System.currentTimeMillis() - lastUltimateFire > 3000f)) {
                        if (character.getHealth() < 1f) {
                            character.setHealth(character.getHealth() + 0.0015f);
                        }
                    }

                    if (System.currentTimeMillis() - (long) (character.getReloadTimeLargeWaves() * 1000L) > character.getLastMediumWaveTime()) {
                        character.setNumberOfMediumWavesAlreadySentInLargeWaves(0);
                    }

                    if (System.currentTimeMillis() - character.getLastSmallWaveTime() > 1000f * character.getReloadTimeSmallWaves()) {
                        if (System.currentTimeMillis() - character.getLastLargeWaveTime() > 1000f * character.getReloadTimeLargeWaves() && readyToFire && System.currentTimeMillis() - character.getLastMediumWaveTime() > 1000f * character.getReloadTimeMediumWaves()) {
                            character.setNumberOfSmallWavesAlreadySentInMediumWaves(0);
                            character.setLastMediumWaveTime(System.currentTimeMillis());
                            character.setNumberOfMediumWavesAlreadySentInLargeWaves(character.getNumberOfMediumWavesAlreadySentInLargeWaves() + 1);

                            if (character.getNumberOfMediumWavesAlreadySentInLargeWaves() == character.getNumberOfMediumWavesInLargeWaves()) {
                                character.setNumberOfMediumWavesAlreadySentInLargeWaves(0);
                                character.setLastLargeWaveTime(System.currentTimeMillis());
                            }
                        }
                        else {
                            if (character.getNumberOfSmallWavesAlreadySentInMediumWaves() != character.getSmallWaves().size()) {
                                if (character.getClassCharacter().equals(ClassCharacters.TATITATOO)) {
                                    if (character.isUltimate1Running()) {
                                        if (collisionOnBottom) {
                                            Trampoline trampoline = new Trampoline(character.getCenterX() - new Trampoline().getRelativeWidth() / 2, character.getRelativeY() + character.getRelativeHeight());
                                            trampoline.setRelativeY(trampoline.getRelativeY() - trampoline.getRelativeHeight());
                                            character.getInventory().add(trampoline);
                                            character.setLastLargeWaveTime(System.currentTimeMillis());
                                        }
                                    }
                                    else {
                                        if (collisionOnBottom) {
                                            Bullet bullet = new Bullet();

                                            bullet.setRelativeWidth(0.012f);
                                            bullet.setRelativeHeight(0.012f * 768f / 372f);
                                            bullet.setDamage(0.25f);
                                            bullet.setRelativeX(character.getRelativeX() + ((float) -characterView.getHorizontalDirection() + 1) * (character.getRelativeWidth() / 2f - bullet.getRelativeWidth() / 2f));
                                            bullet.setRelativeY(character.getRelativeY() + character.getRelativeHeight() - bullet.getRelativeHeight());

                                            bullet.setMovementX(0);
                                            bullet.setMovementY(0);

                                            for (Platform platform : platforms) {
                                                if (!CollisionDetection.isCollisionBetween(bullet, platform).equals(CollisionSide.NONE)) {
                                                    SwingUtilities.invokeLater(() -> {
                                                        for (Bullet bullet1 : character.getBullets()) {
                                                            if (bullet1.getRelativeWidth() == 0 && bullet1.getRelativeHeight() == 0) {
                                                                character.getBullets().set(character.getBullets().indexOf(bullet1), bullet);
                                                                character.setLastLargeWaveTime(System.currentTimeMillis());
                                                                break;
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Bullet normalBullet = new Bullet();

                                    if (character.getClassCharacter().equals(ClassCharacters.ANGELO)) {
                                        normalBullet.setRelativeWidth(0.012f);
                                        normalBullet.setRelativeHeight(0.012f * 768f / 372f);
                                        normalBullet.setDamage(0.15f);
                                    } else if (character.getClassCharacter().equals(ClassCharacters.MEDUSO)) {
                                        if (ultimateClick) {
                                            normalBullet.setRelativeWidth(0.04f);
                                            normalBullet.setRelativeHeight(0.04f * 768f / 372f);
                                        } else {
                                            normalBullet.setRelativeWidth(0.02f);
                                            normalBullet.setRelativeHeight(0.02f * 768f / 372f);
                                        }
                                        normalBullet.setSpeed(0.0075f);
                                    } else if (character.getClassCharacter().equals(ClassCharacters.MONK)) {
                                        normalBullet.setRelativeWidth(0.02f);
                                        normalBullet.setRelativeHeight(0.02f * 768f / 372f);
                                        normalBullet.setDamage(0.07f);
                                    } else if (character.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {
                                        normalBullet.setRelativeWidth(0.015f);
                                        normalBullet.setRelativeHeight(0.015f * 768f / 372f);
                                        normalBullet.setDamage(0.13f);
                                        normalBullet.setSpeed(0.008f);
                                    } else if (character.getClassCharacter().equals(ClassCharacters.BOB)) {
                                        normalBullet.setRelativeWidth(0.015f);
                                        normalBullet.setRelativeHeight(0.015f * 768f / 372f);
                                        normalBullet.setDamage(0.25f);
                                    }

                                    normalBullet.setRelativeBulletStartX(character.getRelativeX() + ((float) -characterView.getHorizontalDirection() + 1) * (character.getRelativeWidth() / 2f - normalBullet.getRelativeWidth() / 2f));
                                    normalBullet.setRelativeBulletStartY(character.getCenterY() - normalBullet.getRelativeHeight() / 2f);

                                    float relativeCursorGoX = (lastMousePressedEvent.getX() - normalBullet.getRelativeWidth() * gameFrame.getGamePanel().getWidth() / 2f) / (float) gameFrame.getGamePanel().getWidth();
                                    float relativeCursorGoY = (lastMousePressedEvent.getY() - normalBullet.getRelativeHeight() * gameFrame.getGamePanel().getHeight() / 2f) / (float) gameFrame.getGamePanel().getHeight();

                                    float tempDeltaX = Math.abs((normalBullet.getRelativeBulletStartX() - relativeCursorGoX) * (float) gameFrame.getGamePanel().getWidth());
                                    float tempDeltaY = Math.abs((normalBullet.getRelativeBulletStartY() - relativeCursorGoY) * (float) gameFrame.getGamePanel().getHeight());

                                    float bulletSpeedRatio = ((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f * ((float) gameFrame.getGamePanel().getHeight() / (float) gameFrame.getGamePanel().getWidth() - 372f / 768f) * 768f / 372f + 1f;

                                    if (character.getClassCharacter().equals(ClassCharacters.MONK)) {
                                        normalBullet.setRange((((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f + 1f) * 0.15f);
                                    } else if (character.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {
                                        normalBullet.setRange((((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f + 1f) * 0.018f);
                                    }
                                    else {
                                        normalBullet.setRange((((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f + 1f) * 0.2f);
                                    }

                                    normalBullet.setMovementX(bulletSpeedRatio * tempDeltaX / (tempDeltaX + tempDeltaY) * normalBullet.getSpeed() * ((relativeCursorGoX - normalBullet.getRelativeBulletStartX()) / tempDeltaX) * gameFrame.getGamePanel().getWidth());
                                    normalBullet.setMovementY(bulletSpeedRatio * tempDeltaY / (tempDeltaX + tempDeltaY) * normalBullet.getSpeed() * ((relativeCursorGoY - normalBullet.getRelativeBulletStartY()) / tempDeltaY) * gameFrame.getGamePanel().getHeight());

                                    double shootingAngle = Math.toDegrees(Math.atan(normalBullet.getMovementY() / normalBullet.getMovementX()));
                                    double hypotenuse = Math.sqrt(Math.pow(normalBullet.getMovementX(), 2) + Math.pow(normalBullet.getMovementY(), 2));
                                    double sign = Math.abs(normalBullet.getMovementX())/normalBullet.getMovementX();

                                    for (Integer angleChangeInDegrees : character.getSmallWaves().get(character.getNumberOfSmallWavesAlreadySentInMediumWaves()).getAngleDegreesBullets()) {
                                        Bullet bullet = new Bullet();

                                        bullet.setRelativeWidth(normalBullet.getRelativeWidth());
                                        bullet.setRelativeHeight(normalBullet.getRelativeHeight());
                                        bullet.setDamage(normalBullet.getDamage());
                                        bullet.setSpeed(normalBullet.getSpeed());
                                        bullet.setRelativeBulletStartX(normalBullet.getRelativeBulletStartX());
                                        bullet.setRelativeBulletStartY(normalBullet.getRelativeBulletStartY());
                                        bullet.setRange(normalBullet.getRange());
                                        bullet.setRange(normalBullet.getRange());

                                        bullet.setMovementX((float) (hypotenuse * Math.cos(Math.toRadians(shootingAngle - angleChangeInDegrees)) * sign));
                                        bullet.setMovementY((float) (hypotenuse * Math.sin(Math.toRadians(shootingAngle - angleChangeInDegrees)) * sign));

                                        SwingUtilities.invokeLater(() -> {
                                            for (Bullet bullet1 : character.getBullets()) {
                                                if (bullet1.getRelativeWidth() == 0 && bullet1.getRelativeHeight() == 0) {
                                                    character.getBullets().set(character.getBullets().indexOf(bullet1), bullet);
                                                    sounds.get(character.getClassCharacter().name().toLowerCase() + ".bullet").play();
                                                    break;
                                                }
                                            }
                                        });
                                    }
                                }

                                character.setLastSmallWaveTime(System.currentTimeMillis());
                                character.setNumberOfSmallWavesAlreadySentInMediumWaves(character.getNumberOfSmallWavesAlreadySentInMediumWaves() + 1);
                            }
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        for (Bullet bullet : character.getBullets()) {
                            bullet.setRelativeX(bullet.getRelativeX() + bullet.getMovementX());
                            bullet.setRelativeY(bullet.getRelativeY() + (float) gameFrame.getGamePanel().getWidth() / (float) gameFrame.getGamePanel().getHeight() * bullet.getMovementY());

                            characterView.getBulletsViews().get(character.getBullets().indexOf(bullet)).setRelativeX(bullet.getRelativeX());
                            characterView.getBulletsViews().get(character.getBullets().indexOf(bullet)).setRelativeY(bullet.getRelativeY());
                            characterView.getBulletsViews().get(character.getBullets().indexOf(bullet)).setRelativeWidth(bullet.getRelativeWidth());
                            characterView.getBulletsViews().get(character.getBullets().indexOf(bullet)).setRelativeHeight(bullet.getRelativeHeight());
                        }

                        for (Bullet bullet : character.getBullets()) {
                            int bulletIndex = character.getBullets().indexOf(bullet);

                            for (Platform platform : platforms) {
                                if (!CollisionDetection.isCollisionBetween(bullet, platform).equals(CollisionSide.NONE)) {
                                    if (!(character.getClassCharacter().equals(ClassCharacters.BOB) && bullet.getRelativeWidth() == 0.05f)) {
                                        if (!(character.getClassCharacter().equals(ClassCharacters.MONK))) {
                                            if (!character.getClassCharacter().equals(ClassCharacters.TATITATOO)) {
                                                character.getBullets().get(bulletIndex).setRelativeWidth(0);
                                                character.getBullets().get(bulletIndex).setRelativeHeight(0);
                                            }
                                        }
                                    }
                                    if (character.getClassCharacter().equals(ClassCharacters.MONK)) {
                                        if (bullet.getLifeTime() == 0) {
                                            bullet.setDamage(0.04f);
                                            bullet.setSpeed(0);
                                            bullet.setMovementY(0);
                                            bullet.setMovementX(0);
                                            bullet.setLifeTime(System.currentTimeMillis());
                                        }
                                    }
                                }
                            }

                            if (character.getClassCharacter().equals(ClassCharacters.MONK)) {
                                if (bullet.getMovementX() == 0){
                                    if (System.currentTimeMillis() - bullet.getLifeTime() > 1000) {
                                        character.getBullets().get(bulletIndex).setRelativeWidth(0);
                                        character.getBullets().get(bulletIndex).setRelativeHeight(0);
                                        bullet.setLifeTime(0);
                                    }
                                }
                            }

                            for (Character otherPlayer : gameClient.getOtherPlayers()) {
                                if (!CollisionDetection.isCollisionBetween(otherPlayer, bullet).equals(CollisionSide.NONE)) {
                                    addHit(otherPlayer.getName(), bullet.getDamage());
                                    character.getBullets().get(bulletIndex).setRelativeWidth(0);
                                    character.getBullets().get(bulletIndex).setRelativeHeight(0);
                                }
                            }

                            if ((bullet.getRelativeX() + bullet.getRelativeWidth() < 0)
                                    || (bullet.getRelativeX() > 1)
                                    || (bullet.getRelativeY() + bullet.getRelativeHeight() < 0)
                                    || (bullet.getRelativeY() > 1)
                                    || (Math.sqrt(Math.pow(bullet.getRelativeX() - bullet.getRelativeBulletStartX(), 2) + Math.pow(bullet.getRelativeY() - bullet.getRelativeBulletStartY(), 2))) > Math.sqrt(2) * bullet.getRange()) {

                                if (!character.getClassCharacter().equals(ClassCharacters.TATITATOO)) {
                                    character.getBullets().get(bulletIndex).setRelativeWidth(0);
                                    character.getBullets().get(bulletIndex).setRelativeHeight(0);
                                }
                            }
                        }

                        if (character.getClassCharacter().equals(ClassCharacters.TATITATOO) && character.isUltimate1Running()) {
                            for (Character otherPlayer : gameClient.getOtherPlayers()) {
                                if (!CollisionDetection.isCollisionBetween(otherPlayer, character).equals(CollisionSide.NONE)) {
                                    addHit(otherPlayer.getName(), 0.01f);
                                }
                            }
                        }
                    });

                    gameClient.sendPlayerInformation(character);
                    gameClient.sendBulletsInformation(character);

                    gameFrame.getHomePanel().refreshHome(gameClient.getOtherPlayers());
                    gameFrame.getHomePanel().setPlayerValues(character);

                    characterView.setHealth(character.getHealth());
                    characterView.setUltimateLoading(character.getUltimateLoading());
                    characterView.setUltimate1Running(character.isUltimate1Running());
                    characterView.setUltimate2Running(character.isUltimate2Running());
                    characterView.setUltimate3Running(character.isUltimate3Running());
                    characterView.setRelativeX(character.getRelativeX());

                    characterView.setRelativeY(character.getRelativeY());

                    characterView.setRelativeWidth(character.getRelativeWidth());
                    characterView.setRelativeHeight(character.getRelativeHeight());

                    characterView.setInventory(character.getInventory());

                    gameFrame.getGamePanel().otherPlayersPainting(gameClient.getOtherPlayers());

                    if (IS_UNIX_OS)
                        Toolkit.getDefaultToolkit().sync();
                }
            }
        });
        gameLoopThread.start();
    }

    private static void addHit(String playerName, float hitDamage) {
        int oldestHitIndex = 0;
        long oldestHitTime = 0;

        for (Hit hit : character.getHits()) {
            if (oldestHitTime == 0) {
                oldestHitTime = hit.getTime();
            } else if (character.getHits().get(oldestHitIndex).getTime() > hit.getTime()) {
                oldestHitIndex = character.getHits().indexOf(hit);
                oldestHitTime = hit.getTime();
            }
        }

        character.getHits().get(oldestHitIndex).setTime(System.currentTimeMillis());
        character.getHits().get(oldestHitIndex).setVictim(playerName);
        character.getHits().get(oldestHitIndex).setDamage(hitDamage);
    }

    private static void randomSpawn() {
        playerOnLeftYodel = false;
        playerOnRightYodel = false;
        yodelDetection = false;
        relativeMovementX = 0;
        relativeMovementY = 0;
        ultimateClick = false;
        character.setHealth(1);
        if (character.getUltimateLoading() < 0.5f)
            character.setUltimateLoading(0);
        else
            character.setUltimateLoading(character.getUltimateLoading() - 0.5f);

        character.setClassCharacter(characterView.getClassCharacter());
        characterView.setClassCharacter(character.getClassCharacter());

        while (true) {
            double RandSpawn = Math.random();
            int platformIndex;

            if (RandSpawn < 0.25) {
                platformIndex = 0;
            } else if (RandSpawn < 0.50) {
                platformIndex = 4;
            } else if (RandSpawn < 0.75) {
                platformIndex = 1;
            } else {
                platformIndex = 5;
            }

            if (CollisionDetection.isCollisionBetween(platforms[platformIndex], character).equals(CollisionSide.NONE)) {
                character.setRelativeX(platforms[platformIndex].getCenterX() - character.getRelativeWidth() / 2);
                character.setRelativeY(platforms[platformIndex].getRelativeY() - character.getRelativeHeight() - 0.01f);
                character.setHorizontalDirection((Math.abs(character.getCenterX() - 0.5)) / (character.getCenterX() - 0.5));
                characterView.setHorizontalDirection(character.getHorizontalDirection());
                character.setHorizontalDirection(0);
                characterView.setHorizontalDirection(character.getHorizontalDirection());
                break;
            }
        }
    }
}
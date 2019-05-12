import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import model.*;
import model.Bullet;
import model.characters.ClassCharacters;
import model.characters.Direction;
import model.characters.Hit;
import model.characters.PlayableCharacter;
import model.Platform;
import network.GameClient;
import network.Network;
import view.client.Audio;
import view.client.connection.AskIPHost;
import view.client.connection.ServerFullError;
import view.client.game_frame.*;
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
    private static PlayableCharacter playableCharacter;
    private static boolean ultimateClick = false;
    private static boolean playerOnLeftYodel = false;
    private static boolean playerOnRightYodel = false;
    private static boolean yodelDetection = false;
    private static boolean cancelUltimate = false;
    private static long lastDamageOnPlayerTime = 0;
    private static String lastAttackerOnPlayer;
    private static long lastUltimateFire = 0;
    private static boolean clientSuccessfullyStarted = false;
    private static HashMap<String, Audio> sounds = new HashMap<>();

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
                while (gameClient == null || AskIPHost.isGoBack()) {
                    try {
                        serverIP = AskIPHost.getIPHost();
                        System.out.println("\nConnecting to the server... (" + serverIP + ")");
                        gameClient = new GameClient(serverIP);
                        break;
                    } catch (IOException e) {
                        System.out.println("No game server found with this IP on the network");
                        AskIPHost.setGoBack(true);
                    }
                }
            }
            gameClientListener = new Listener() {
                @Override
                public void received(Connection connection, Object object) {
                    SwingUtilities.invokeLater(() -> {
                        if (finalGameClientLaunched[0]) {
                            if (object instanceof PlayableCharacter && clientSuccessfullyStarted) {
                                gameClient.receivedListener(object);
                                PlayableCharacter otherPlayer = (PlayableCharacter) object;

                                for (Hit hit : otherPlayer.getHits()) {
                                    if (hit.getVictim().equals((playableCharacter.getName())) && hit.getTime() != gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).getHits().get(otherPlayer.getHits().indexOf(hit)).getTime()) {
                                        lastDamageOnPlayerTime = System.currentTimeMillis();
                                        lastAttackerOnPlayer = otherPlayer.getName();

                                        if ((!playableCharacter.getClassCharacter().equals(ClassCharacters.MEDUSO)) || (!playableCharacter.isUltimate1Running() && !playableCharacter.isUltimate2Running() && !playableCharacter.isUltimate3Running())) {
                                            playableCharacter.setHealth(playableCharacter.getHealth() - hit.getDamage() / playableCharacter.getMaxHealth());
                                            if (playableCharacter.getHealth() <= 0) {
                                                playableCharacter.setLastDeathTime(System.currentTimeMillis());
                                                playableCharacter.setLastKiller(otherPlayer.getName());
                                                playableCharacter.setDeaths(playableCharacter.getDeaths() + 1);
                                                sounds.get(playableCharacter.getClassCharacter().name().toLowerCase()+ ".dead").play();

                                                if (playableCharacter.getMoney() >= 1)
                                                    playableCharacter.setMoney(playableCharacter.getMoney() - 1);

                                                randomSpawn();
                                            }
                                        }
                                    }
                                }
                                if (gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName()) < gameClient.getOtherPlayers().size()) {
                                    gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).setHits(otherPlayer.getHits());
                                    if (otherPlayer.getLastKiller().equals(playableCharacter.getName()) && otherPlayer.getLastDeathTime() != gameClient.getOtherPlayers().get(gameClient.getRegisterList().getNameList().indexOf(otherPlayer.getName())).getLastDeathTime()) {
                                        playableCharacter.setKills(playableCharacter.getKills() + 1);
                                        playableCharacter.setMoney(playableCharacter.getMoney() + 3);
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
                        if (!(object instanceof Network.RemoveName) && !(object instanceof PlayableCharacter)) {
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

        playableCharacter = new PlayableCharacter(clientName);
        playableCharacter.setRelativeY(-1.15f);
        playableCharacter.setRelativeX(0.5f);
        playableCharacter.setClassCharacter(ClassCharacters.BOB);
        for (int i = 0; i < PlayableCharacter.getMaxBulletNumberPerPlayer() ; i++) {
            Bullet bullet = new Bullet();
            bullet.setRelativeWidth(0);
            bullet.setRelativeHeight(0);

            playableCharacter.getBullets().add(bullet);
        }

        characterView = new CharacterView(
                playableCharacter.getRelativeX(),
                playableCharacter.getRelativeY(),
                playableCharacter.getRelativeWidth(),
                playableCharacter.getRelativeHeight(),
                playableCharacter.getName(),
                playableCharacter.getClassCharacter(),
                playableCharacter.getHealth());

        for (int i = 0; i < PlayableCharacter.getMaxBulletNumberPerPlayer() ; i++) {
            BulletView bulletView = new BulletView(0,0,0,0);
            characterView.getBulletsViews().add(bulletView);
        }

        gameFrame.getGamePanel().setCharacterView(characterView);
        gameFrame.getHomePanel().setClassCharacter(playableCharacter.getClassCharacter());
        gameFrame.getHomePanel().setPlayerName(playableCharacter.getName());
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

                    playableCharacter.setClassCharacter(playableCharacter.getClassCharacter());
                    characterView.setClassCharacter(playableCharacter.getClassCharacter());

                    for (BulletView bulletView : characterView.getBulletsViews()) {
                        try {
                            bulletView.setIcon("/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                        } catch (NullPointerException ex) {
                            System.err.println("Can't find \"/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png\" !");
                        }
                    }
                    gameFrame.getHomePanel().setClassCharacter(playableCharacter.getClassCharacter());

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
                    } else if (e.getKeyCode() == KeyEvent.VK_E && !(CollisionDetection.isCollisionBetween(playableCharacter, new HomeView()).equals(PlayerCollisionSide.NONE))) {
                        gameFrame.getCardLayout().next(gameFrame.getContentPane());

                        if (playableCharacter.getMoney() >= 5)
                            playableCharacter.setMoney(playableCharacter.getMoney() - 5);
                        else
                            playableCharacter.setMoney(0);
                        playableCharacter.setRelativeX(0.5f);
                        playableCharacter.setRelativeY(-1.15f);
                        playableCharacter.setAtHome(true);
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
                    } else if (SwingUtilities.isRightMouseButton(e) && playableCharacter.getUltimateLoading() == 1) {
                        lastMousePressedEvent = e;
                        sounds.get(playableCharacter.getClassCharacter().name().toLowerCase()+ ".ultimate").play();
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
            playableCharacter.setAtHome(false);
            randomSpawn();
            sounds.get(playableCharacter.getClassCharacter().name().toLowerCase()+ ".home").play();
        });

        gameFrame.getHomePanel().getChangeCharacterButton().addActionListener(e -> {
            ultimateClick = false;
            playableCharacter.setClassCharacter(ClassCharacters.getClassCharactersList().get((ClassCharacters.getClassCharactersList().indexOf(playableCharacter.getClassCharacter()) + 1) % ClassCharacters.getClassCharactersList().size()));
            characterView.setClassCharacter(playableCharacter.getClassCharacter());

            for (BulletView bulletView : characterView.getBulletsViews()) {
                try {
                    bulletView.setIcon("/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                } catch (NullPointerException ex) {
                    System.err.println("Can't find \"/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png\" !");
                }
            }

            playableCharacter.setUltimateLoading(0f);
            playableCharacter.setRelativeY(-1.15f);
            playableCharacter.setRelativeX(0.5f);
            gameFrame.getHomePanel().setClassCharacter(playableCharacter.getClassCharacter());
        });

        System.out.println("Client successfully started !");
        clientSuccessfullyStarted = true;
    }

    private static void botThinking() {
        if (collisionOnBottom) {
            for (Platform platform : platforms) {
                if (CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.BOTTOM)) {
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
                if (Math.abs(playableCharacter.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2 < Bot.getDistanceYClosestPlatform() && playableCharacter.getCenterY() > platform.getCenterY()) {
                    Bot.setDistanceYClosestPlatform(Math.abs(playableCharacter.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2);
                }
            }

            // Distance X closest platform
            for (Platform platform : platforms) {
                if (Math.abs(playableCharacter.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2 == Bot.getDistanceYClosestPlatform() && playableCharacter.getCenterY() > platform.getCenterY()) {
                    if (Math.abs(playableCharacter.getCenterX() - platform.getCenterX()) - platform.getRelativeWidth() / 2 < Bot.getDistanceXClosestPlatform()) {
                        Bot.setDistanceXClosestPlatform(Math.abs(playableCharacter.getCenterX() - platform.getCenterX()) - platform.getRelativeWidth() / 2);
                    }
                }
            }

            // Set closest platform
            for (Platform platform : platforms) {
                if (Math.abs(playableCharacter.getCenterY() - platform.getCenterY()) - platform.getRelativeHeight() / 2 == Bot.getDistanceYClosestPlatform() && playableCharacter.getCenterY() > platform.getCenterY()) {
                    if (Math.abs(playableCharacter.getCenterX() - platform.getCenterX()) - platform.getRelativeWidth() / 2 == Bot.getDistanceXClosestPlatform()) {
                        if (Bot.getClosestPlatformAbove() == null || !Bot.getClosestPlatformAbove().equals(platform)) {
                            Bot.setClosestPlatformAbove(platform);
                        }
                    }
                }
            }
        }


        // Set direction toward closest platform
        if (Bot.getClosestPlatformAbove().getCenterX() < playableCharacter.getCenterX()) {
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
                        if (CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.TOP))
                            collisionOnTop = true;
                        if (CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.BOTTOM))
                            collisionOnBottom = true;
                        if (CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.RIGHT))
                            collisionOnRight = true;
                        if (CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.LEFT))
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

                    if (playableCharacter.getClassCharacter().equals(ClassCharacters.ANGELO) && ultimateClick) {
                        totalDirection = 0;
                    }

                    if (!gameFrame.getGamePanel().hasFocus()) {
                        releaseActionLeft.removeMovements();
                        releaseActionRight.removeMovements();
                    }

                    playableCharacter.setHorizontalDirection(-totalDirection);
                    characterView.setHorizontalDirection(playableCharacter.getHorizontalDirection());

                    if (!playableCharacter.isAtHome()) {
                        if (playableCharacter.getUltimateLoading() >= 1f - 0.2f / 60f)
                            playableCharacter.setUltimateLoading(1f);
                        else
                            playableCharacter.setUltimateLoading(playableCharacter.getUltimateLoading() + 0.2f / 60f);
                    }

                    if (ultimateClick) {
                        if (playableCharacter.getClassCharacter().equals(ClassCharacters.MONK)){
                            playableCharacter.setClassCharacter(playableCharacter.getClassCharacter());
                            characterView.setClassCharacter(playableCharacter.getClassCharacter());
                            playableCharacter.setRelativeY((lastMousePressedEvent.getY() - playableCharacter.getRelativeHeight() * gameFrame.getGamePanel().getHeight() / 2f) / (float) gameFrame.getGamePanel().getHeight());
                            playableCharacter.setRelativeX((lastMousePressedEvent.getX() - playableCharacter.getRelativeWidth() * gameFrame.getGamePanel().getWidth() / 2f) / (float) gameFrame.getGamePanel().getWidth());
                            ultimateClick = false;
                            playableCharacter.setUltimateLoading(0f);
                        }
                        else if (playableCharacter.getClassCharacter().equals(ClassCharacters.BOB)) {
                            playableCharacter.setClassCharacter(playableCharacter.getClassCharacter());
                            characterView.setClassCharacter(playableCharacter.getClassCharacter());
                            ultimateClick = false;
                            playableCharacter.setUltimateLoading(0f);
                            Bullet bullet = new Bullet();
                            bullet.setDamage(0.5f);
                            bullet.setRelativeHeight(0.05f * 768f / 372f);
                            bullet.setRelativeWidth(0.05f);

                            float relativeBulletStartX = playableCharacter.getRelativeX() + ((float) -characterView.getHorizontalDirection() + 1) * (playableCharacter.getRelativeWidth() / 2f - bullet.getRelativeWidth()/2f);
                            float relativeBulletStartY = playableCharacter.getCenterY() - bullet.getRelativeHeight() / 2f;
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

                            bullet.setBulletRangeRatio(2f);

                            SwingUtilities.invokeLater(() -> {
                                for (Bullet bullet1 : playableCharacter.getBullets()) {
                                    if (bullet1.getRelativeWidth() == 0 && bullet1.getRelativeHeight() == 0) {
                                        playableCharacter.getBullets().set(playableCharacter.getBullets().indexOf(bullet1), bullet);
                                        break;
                                    }
                                }
                            });
                            lastUltimateFire = System.currentTimeMillis();
                        }
                        else //noinspection StatementWithEmptyBody
                            if (playableCharacter.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {

                        }
                        else if (!playableCharacter.getClassCharacter().equals(ClassCharacters.BOB) && !playableCharacter.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {
                            if (!playableCharacter.getClassCharacter().equals(ClassCharacters.MONK)) {
                                if (!playableCharacter.isUltimate1Running() && !playableCharacter.isUltimate2Running() && !playableCharacter.isUltimate3Running()) {
                                    playableCharacter.ultimate1();
                                    characterView.ultimate1();
                                } else if (playableCharacter.isUltimate1Running()) {
                                    if ((playableCharacter.getClassCharacter().equals(ClassCharacters.TATITATOO)) && (cancelUltimate)) {
                                        playableCharacter.setClassCharacter(playableCharacter.getClassCharacter());
                                        characterView.setClassCharacter(playableCharacter.getClassCharacter());
                                        ultimateClick = false;
                                        cancelUltimate = false;
                                    }

                                    if (System.currentTimeMillis() - playableCharacter.getUltimate1StartTimeMillis() > playableCharacter.getUltimate1DurationMillis()) {
                                        playableCharacter.ultimate2();
                                        characterView.ultimate2();
                                    }
                                } else if (playableCharacter.isUltimate2Running()) {
                                    if ((playableCharacter.getClassCharacter().equals(ClassCharacters.MEDUSO)) && (playableCharacter.getHealth() < 1f)) {
                                        playableCharacter.setHealth(playableCharacter.getHealth() + 0.0013f);
                                    }
                                    if (((playableCharacter.getClassCharacter().equals(ClassCharacters.ANGELO)) && (cancelUltimate)) || ((playableCharacter.getClassCharacter().equals(ClassCharacters.MEDUSO)) && (cancelUltimate))) {
                                        playableCharacter.ultimate3();
                                        characterView.ultimate3();
                                    } else if (System.currentTimeMillis() - playableCharacter.getUltimate2StartTimeMillis() > playableCharacter.getUltimate2DurationMillis()) {
                                        playableCharacter.ultimate3();
                                        characterView.ultimate3();
                                    }
                                } else if (playableCharacter.isUltimate3Running()) {
                                    if (System.currentTimeMillis() - playableCharacter.getUltimate3StartTimeMillis() > playableCharacter.getUltimate3DurationMillis()) {
                                        playableCharacter.setClassCharacter(playableCharacter.getClassCharacter());
                                        characterView.setClassCharacter(playableCharacter.getClassCharacter());

                                        ultimateClick = false;
                                        cancelUltimate = false;
                                        jumpKeyJustPressed = false;
                                    }
                                } else {
                                    ultimateClick = false;
                                    cancelUltimate = false;
                                }
                                playableCharacter.setUltimateLoading(0f);
                            }
                        }
                    }

                    Iterator<Object> itr = playableCharacter.getInventory().iterator();
                    while (itr.hasNext()) {
                        Object object = itr.next();
                        if (object instanceof Trampoline) {
                            if (System.currentTimeMillis() - ((Trampoline) object).getCreationTime() > ((Trampoline) object).getDurationTime() * 1000) {
                                itr.remove();
                            }
                        }
                    }

                    for (Object object : playableCharacter.getInventory()) {
                        if (object instanceof Trampoline) {

                            if ((!CollisionDetection.isCollisionBetween(playableCharacter, (Trampoline) object).equals(PlayerCollisionSide.NONE)) && (!collisionOnTop)) {
                                collisionTrampoline = true;
                                break;
                            }
                        }
                    }

                    if (!collisionTrampoline) {
                        for (PlayableCharacter otherPlayer : gameClient.getOtherPlayers()) {
                            for (Object object : otherPlayer.getInventory()) {
                                if (object instanceof Trampoline) {
                                    if ((!CollisionDetection.isCollisionBetween(playableCharacter, (Trampoline) object).equals(PlayerCollisionSide.NONE)) && (!collisionOnTop)) {
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
                        if (!CollisionDetection.isCollisionBetween(playableCharacter, new YodelView("left")).equals(PlayerCollisionSide.NONE))
                            playerOnLeftYodel = true;
                        else if (CollisionDetection.isCollisionBetween(playableCharacter, new YodelView("left")).equals(PlayerCollisionSide.NONE))
                            playerOnLeftYodel = false;
                        if (!CollisionDetection.isCollisionBetween(playableCharacter, new YodelView("right")).equals(PlayerCollisionSide.NONE))
                            playerOnRightYodel = true;
                        else if (CollisionDetection.isCollisionBetween(playableCharacter, new YodelView("right")).equals(PlayerCollisionSide.NONE))
                            playerOnRightYodel = false;
                    }

                    if ((playerOnLeftYodel) || (playerOnRightYodel)) {
                        yodelDetection = true;
                        if (playerOnRightYodel) {
                            if (playableCharacter.getRelativeX() > 0.61f) {
                                playableCharacter.setRelativeX(playableCharacter.getRelativeX() - 0.004f);
                                playableCharacter.setRelativeY(0.21f);
                                relativeMovementY = -0.005f;
                                relativeMovementX = -0.004f;

                            } else if (playableCharacter.getRelativeX() <= 0.61f) {
                                playerOnRightYodel = false;
                                yodelDetection = false;
                                relativeMovementY = -0.01f;
                                relativeMovementX = -0.005f;
                            }
                        }
                        if (playerOnLeftYodel) {
                            if (playableCharacter.getRelativeX() < 0.35f) {
                                playableCharacter.setRelativeX(playableCharacter.getRelativeX() + 0.004f);
                                playableCharacter.setRelativeY(0.21f);
                                relativeMovementY = -0.005f;
                                relativeMovementX = 0.004f;

                            } else if (playableCharacter.getRelativeX() >= 0.35f) {
                                playerOnLeftYodel = false;
                                yodelDetection = false;
                                relativeMovementY = -0.01f;
                                relativeMovementX = 0.005f;
                            }
                        }
                    }
                    else if (collisionTrampoline) {
                        relativeMovementY = -0.017f;
                        playableCharacter.setRelativeY(playableCharacter.getRelativeY() - new Trampoline().getRelativeWidth());
                    }
                    else {
                        if ((collisionOnRight && relativeMovementX > 0) || (collisionOnLeft && relativeMovementX < 0))
                            relativeMovementX = 0;

                        else if (collisionOnBottom) {
                            if (totalDirection == 1 && relativeMovementX < playableCharacter.getRelativeMaxSpeed())
                                relativeMovementX += playableCharacter.getRelativeSpeedGrowth();
                            else if (totalDirection == -1 && relativeMovementX > -playableCharacter.getRelativeMaxSpeed())
                                relativeMovementX -= playableCharacter.getRelativeSpeedGrowth();
                            else {
                                if (Math.abs(relativeMovementX) < Terrain.getRelativeFriction())
                                    relativeMovementX = 0;
                                else if (relativeMovementX > 0)
                                    relativeMovementX -= Terrain.getRelativeFriction();
                                else if (relativeMovementX < 0)
                                    relativeMovementX += Terrain.getRelativeFriction();
                            }
                        } else {
                            if (totalDirection == 1 && relativeMovementX < playableCharacter.getRelativeMaxSpeed())
                                relativeMovementX += playableCharacter.getRelativeSpeedGrowth() / 2;
                            else if (totalDirection == -1 && relativeMovementX > -playableCharacter.getRelativeMaxSpeed())
                                relativeMovementX -= playableCharacter.getRelativeSpeedGrowth() / 2;
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
                            if (jumpKeyJustPressed && playableCharacter.getRelativeJumpStrength() > 0.0001f) {
                                relativeMovementY = -playableCharacter.getRelativeJumpStrength();
                                jumpKeyJustPressed = false;
                            } else if (relativeMovementY > 0)
                                relativeMovementY = 0;
                        } else if (collisionOnTop && relativeMovementY < 0)
                            relativeMovementY = Terrain.getRelativeGravityGrowth();
                        else if (relativeMovementY < Terrain.getRelativeMaxGravity()) {
                            relativeMovementY += Terrain.getRelativeGravityGrowth();
                            jumpKeyJustPressed = false;
                        }

                        if (playableCharacter.getRelativeY() >= 1) {
                            if (System.currentTimeMillis()  - 1500 < lastDamageOnPlayerTime) {
                                playableCharacter.setLastKiller(lastAttackerOnPlayer);
                                playableCharacter.setLastDeathTime(lastDamageOnPlayerTime);
                            }

                            if (playableCharacter.getMoney() >= 1)
                                playableCharacter.setMoney(playableCharacter.getMoney() - 1);

                            playableCharacter.setDeaths(playableCharacter.getDeaths() + 1);
                            sounds.get(playableCharacter.getClassCharacter().name().toLowerCase()+ ".dead").play();
                            randomSpawn();
                        }

                        playableCharacter.setRelativeX(playableCharacter.getRelativeX() + relativeMovementX);
                        playableCharacter.setRelativeY(playableCharacter.getRelativeY() + relativeMovementY);
                    }

                    for (Platform platform : platforms) {
                        collisionOnBottom = CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.BOTTOM);
                        if (collisionOnBottom) {
                            break;
                        }
                    }

                    if (collisionOnBottom) {
                        while (collisionOnBottom) {
                            playableCharacter.setRelativeY(playableCharacter.getRelativeY() - 0.001f);

                            for (Platform platform : platforms) {
                                collisionOnBottom = CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.BOTTOM);
                                if (collisionOnBottom) {
                                    break;
                                }
                            }
                        }
                        playableCharacter.setRelativeY(playableCharacter.getRelativeY() + 0.001f);
                        collisionOnBottom = true;
                    }

                    if ((System.currentTimeMillis() - playableCharacter.getLastSmallWaveTime() > 3000f) && (System.currentTimeMillis() - lastDamageOnPlayerTime > 3000f) && (System.currentTimeMillis() - lastUltimateFire > 3000f)) {
                        if (playableCharacter.getHealth() < 1f){
                            playableCharacter.setHealth(playableCharacter.getHealth() + 0.0015f);
                        }
                    }

                    if (System.currentTimeMillis() - (long) (playableCharacter.getReloadTimeLargeWaves() * 1000L) > playableCharacter.getLastMediumWaveTime()) {
                        playableCharacter.setNumberOfMediumWavesAlreadySentInLargeWaves(0);
                    }

                    if (System.currentTimeMillis() - playableCharacter.getLastSmallWaveTime() > 1000f * playableCharacter.getReloadTimeSmallWaves()) {
                        if (System.currentTimeMillis() - playableCharacter.getLastLargeWaveTime() > 1000f * playableCharacter.getReloadTimeLargeWaves() && readyToFire && System.currentTimeMillis() - playableCharacter.getLastMediumWaveTime() > 1000f * playableCharacter.getReloadTimeMediumWaves()) {
                            playableCharacter.setNumberOfSmallWavesAlreadySentInMediumWaves(0);
                            playableCharacter.setLastMediumWaveTime(System.currentTimeMillis());
                            playableCharacter.setNumberOfMediumWavesAlreadySentInLargeWaves(playableCharacter.getNumberOfMediumWavesAlreadySentInLargeWaves() + 1);

                            if (playableCharacter.getNumberOfMediumWavesAlreadySentInLargeWaves() == playableCharacter.getNumberOfMediumWavesInLargeWaves()) {
                                playableCharacter.setNumberOfMediumWavesAlreadySentInLargeWaves(0);
                                playableCharacter.setLastLargeWaveTime(System.currentTimeMillis());
                            }
                        }
                        else {
                            if (playableCharacter.getNumberOfSmallWavesAlreadySentInMediumWaves() != playableCharacter.getSmallWaves().size()) {
                                if (playableCharacter.getClassCharacter().equals(ClassCharacters.TATITATOO)) {
                                    if (playableCharacter.isUltimate1Running()) {
                                        if (collisionOnBottom) {
                                            Trampoline trampoline = new Trampoline(playableCharacter.getCenterX() - new Trampoline().getRelativeWidth()/2, playableCharacter.getRelativeY() + playableCharacter.getRelativeHeight());
                                            trampoline.setRelativeY(trampoline.getRelativeY() - trampoline.getRelativeHeight());
                                            playableCharacter.getInventory().add(trampoline);
                                            playableCharacter.setLastLargeWaveTime(System.currentTimeMillis());
                                        }
                                    }
                                    else {
                                        if (collisionOnBottom) {
                                            Bullet bullet = new Bullet();

                                            bullet.setRelativeWidth(0.012f);
                                            bullet.setRelativeHeight(0.012f * 768f / 372f);
                                            bullet.setDamage(0.25f);
                                            bullet.setRelativeX(playableCharacter.getRelativeX() + ((float) -characterView.getHorizontalDirection() + 1) * (playableCharacter.getRelativeWidth() / 2f - bullet.getRelativeWidth() / 2f));
                                            bullet.setRelativeY(playableCharacter.getRelativeY() + playableCharacter.getRelativeHeight() - bullet.getRelativeHeight());

                                            bullet.setMovementX(0);
                                            bullet.setMovementY(0);

                                            for (Platform platform : platforms) {
                                                if (!CollisionDetection.isCollisionBetween(bullet, platform).equals(PlayerCollisionSide.NONE)) {
                                                    SwingUtilities.invokeLater(() -> {
                                                        for (Bullet bullet1 : playableCharacter.getBullets()) {
                                                            if (bullet1.getRelativeWidth() == 0 && bullet1.getRelativeHeight() == 0) {
                                                                playableCharacter.getBullets().set(playableCharacter.getBullets().indexOf(bullet1), bullet);
                                                                playableCharacter.setLastLargeWaveTime(System.currentTimeMillis());
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

                                    if (playableCharacter.getClassCharacter().equals(ClassCharacters.ANGELO)) {
                                        normalBullet.setRelativeWidth(0.012f);
                                        normalBullet.setRelativeHeight(0.012f * 768f / 372f);
                                        normalBullet.setDamage(0.15f);
                                    } else if (playableCharacter.getClassCharacter().equals(ClassCharacters.MEDUSO)) {
                                        if (ultimateClick) {
                                            normalBullet.setRelativeWidth(0.04f);
                                            normalBullet.setRelativeHeight(0.04f * 768f / 372f);
                                        } else {
                                            normalBullet.setRelativeWidth(0.02f);
                                            normalBullet.setRelativeHeight(0.02f * 768f / 372f);
                                        }
                                        normalBullet.setSpeed(0.0075f);
                                    } else if (playableCharacter.getClassCharacter().equals(ClassCharacters.MONK)) {
                                        normalBullet.setRelativeWidth(0.02f);
                                        normalBullet.setRelativeHeight(0.02f * 768f / 372f);
                                        normalBullet.setDamage(0.07f);
                                    } else if (playableCharacter.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {
                                        normalBullet.setRelativeWidth(0.015f);
                                        normalBullet.setRelativeHeight(0.015f * 768f / 372f);
                                        normalBullet.setDamage(0.13f);
                                        normalBullet.setSpeed(0.008f);
                                    }
                                    else if (playableCharacter.getClassCharacter().equals(ClassCharacters.BOB)) {
                                        normalBullet.setRelativeWidth(0.015f);
                                        normalBullet.setRelativeHeight(0.015f * 768f / 372f);
                                        normalBullet.setDamage(0.25f);
                                    }

                                    normalBullet.setRelativeBulletStartX(playableCharacter.getRelativeX() + ((float) -characterView.getHorizontalDirection() + 1) * (playableCharacter.getRelativeWidth() / 2f - normalBullet.getRelativeWidth()/2f));
                                    normalBullet.setRelativeBulletStartY(playableCharacter.getCenterY() - normalBullet.getRelativeHeight() / 2f);

                                    float relativeCursorGoX = (lastMousePressedEvent.getX() - normalBullet.getRelativeWidth() * gameFrame.getGamePanel().getWidth() / 2f) / (float) gameFrame.getGamePanel().getWidth();
                                    float relativeCursorGoY = (lastMousePressedEvent.getY() - normalBullet.getRelativeHeight() * gameFrame.getGamePanel().getHeight() / 2f) / (float) gameFrame.getGamePanel().getHeight();

                                    float tempDeltaX = Math.abs((normalBullet.getRelativeBulletStartX() - relativeCursorGoX) * (float) gameFrame.getGamePanel().getWidth());
                                    float tempDeltaY = Math.abs((normalBullet.getRelativeBulletStartY() - relativeCursorGoY) * (float) gameFrame.getGamePanel().getHeight());

                                    float bulletSpeedRatio = ((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f * ((float) gameFrame.getGamePanel().getHeight() / (float) gameFrame.getGamePanel().getWidth() - 372f / 768f) * 768f / 372f + 1f;

                                    if (playableCharacter.getClassCharacter().equals(ClassCharacters.MONK)) {
                                        normalBullet.setBulletRangeRatio(1.8f);
                                    }
                                    else if (playableCharacter.getClassCharacter().equals(ClassCharacters.ELBOMBAS)) {
                                        normalBullet.setBulletRangeRatio(0.09f);
                                    }
                                    else {
                                        normalBullet.setBulletRangeRatio(((float) Math.toDegrees(Math.atan(Math.abs(tempDeltaY / tempDeltaX)))) / 90f + 1f);
                                    }

                                    normalBullet.setMovementX(bulletSpeedRatio * tempDeltaX / (tempDeltaX + tempDeltaY) * normalBullet.getSpeed() * ((relativeCursorGoX - normalBullet.getRelativeBulletStartX()) / tempDeltaX) * gameFrame.getGamePanel().getWidth());
                                    normalBullet.setMovementY(bulletSpeedRatio * tempDeltaY / (tempDeltaX + tempDeltaY) * normalBullet.getSpeed() * ((relativeCursorGoY - normalBullet.getRelativeBulletStartY()) / tempDeltaY) * gameFrame.getGamePanel().getHeight());

                                    double shootingAngle = Math.toDegrees(Math.atan(normalBullet.getMovementY() / normalBullet.getMovementX()));
                                    double hypotenuse = Math.sqrt(Math.pow(normalBullet.getMovementX(), 2) + Math.pow(normalBullet.getMovementY(), 2));
                                    double sign = Math.abs(normalBullet.getMovementX())/normalBullet.getMovementX();

                                    for (Integer angleChangeInDegrees : playableCharacter.getSmallWaves().get(playableCharacter.getNumberOfSmallWavesAlreadySentInMediumWaves()).getAngleDegreesBullets()) {
                                        Bullet bullet = new Bullet();

                                        bullet.setRelativeWidth(normalBullet.getRelativeWidth());
                                        bullet.setRelativeHeight(normalBullet.getRelativeHeight());
                                        bullet.setDamage(normalBullet.getDamage());
                                        bullet.setSpeed(normalBullet.getSpeed());
                                        bullet.setRelativeBulletStartX(normalBullet.getRelativeBulletStartX());
                                        bullet.setRelativeBulletStartY(normalBullet.getRelativeBulletStartY());
                                        bullet.setBulletRangeRatio(normalBullet.getBulletRangeRatio());
                                        bullet.setBulletRangeRatio(normalBullet.getBulletRangeRatio());

                                        bullet.setMovementX((float) (hypotenuse * Math.cos(Math.toRadians(shootingAngle - angleChangeInDegrees)) * sign));
                                        bullet.setMovementY((float) (hypotenuse * Math.sin(Math.toRadians(shootingAngle - angleChangeInDegrees)) * sign));

                                        SwingUtilities.invokeLater(() -> {
                                            for (Bullet bullet1 : playableCharacter.getBullets()) {
                                                if (bullet1.getRelativeWidth() == 0 && bullet1.getRelativeHeight() == 0) {
                                                    playableCharacter.getBullets().set(playableCharacter.getBullets().indexOf(bullet1), bullet);
                                                    sounds.get(playableCharacter.getClassCharacter().name().toLowerCase()+ ".bullet").play();
                                                    break;
                                                }
                                            }
                                        });
                                    }
                                }

                                playableCharacter.setLastSmallWaveTime(System.currentTimeMillis());
                                playableCharacter.setNumberOfSmallWavesAlreadySentInMediumWaves(playableCharacter.getNumberOfSmallWavesAlreadySentInMediumWaves() + 1);
                            }
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        for (Bullet bullet : playableCharacter.getBullets()) {
                            bullet.setRelativeX(bullet.getRelativeX() + bullet.getMovementX());
                            bullet.setRelativeY(bullet.getRelativeY() + (float) gameFrame.getGamePanel().getWidth() / (float) gameFrame.getGamePanel().getHeight() * bullet.getMovementY());

                            characterView.getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeX(bullet.getRelativeX());
                            characterView.getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeY(bullet.getRelativeY());
                            characterView.getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeWidth(bullet.getRelativeWidth());
                            characterView.getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeHeight(bullet.getRelativeHeight());
                        }

                        for (Bullet bullet : playableCharacter.getBullets()) {
                            int bulletIndex = playableCharacter.getBullets().indexOf(bullet);

                            for (Platform platform : platforms) {
                                if (!CollisionDetection.isCollisionBetween(bullet, platform).equals(PlayerCollisionSide.NONE)) {
                                    if (!(playableCharacter.getClassCharacter().equals(ClassCharacters.BOB) && bullet.getRelativeWidth() == 0.05f)) {
                                        if (!(playableCharacter.getClassCharacter().equals(ClassCharacters.MONK))){
                                            if (!playableCharacter.getClassCharacter().equals(ClassCharacters.TATITATOO)) {
                                                playableCharacter.getBullets().get(bulletIndex).setRelativeWidth(0);
                                                playableCharacter.getBullets().get(bulletIndex).setRelativeHeight(0);
                                            }
                                        }
                                    }
                                    if (playableCharacter.getClassCharacter().equals(ClassCharacters.MONK)) {
                                        if (bullet.getBulletLifeTime() == 0) {
                                            bullet.setDamage(0.04f);
                                            bullet.setSpeed(0);
                                            bullet.setMovementY(0);
                                            bullet.setMovementX(0);
                                            bullet.setBulletLifeTime(System.currentTimeMillis());
                                        }
                                    }
                                }
                            }

                            if (playableCharacter.getClassCharacter().equals(ClassCharacters.MONK)) {
                                if (bullet.getMovementX() == 0){
                                    if (System.currentTimeMillis() - bullet.getBulletLifeTime() > 1000) {
                                        playableCharacter.getBullets().get(bulletIndex).setRelativeWidth(0);
                                        playableCharacter.getBullets().get(bulletIndex).setRelativeHeight(0);
                                        bullet.setBulletLifeTime(0);
                                    }
                                }
                            }

                            for (PlayableCharacter otherPlayer : gameClient.getOtherPlayers()) {
                                if (!CollisionDetection.isCollisionBetween(otherPlayer, bullet).equals(PlayerCollisionSide.NONE)) {
                                    addHit(otherPlayer.getName(), bullet.getDamage());
                                    playableCharacter.getBullets().get(bulletIndex).setRelativeWidth(0);
                                    playableCharacter.getBullets().get(bulletIndex).setRelativeHeight(0);
                                }
                            }

                            if ((bullet.getRelativeX() + bullet.getRelativeWidth() < 0)
                                    || (bullet.getRelativeX() > 1)
                                    || (bullet.getRelativeY() + bullet.getRelativeHeight() < 0)
                                    || (bullet.getRelativeY() > 1)
                                    || (Math.sqrt(Math.pow(bullet.getRelativeX() - bullet.getRelativeBulletStartX(), 2) + Math.pow(bullet.getRelativeY() - bullet.getRelativeBulletStartY(), 2))) > Math.sqrt(2) * bullet.getRelativeMaxRange() * bullet.getBulletRangeRatio()) {

                                if (!playableCharacter.getClassCharacter().equals(ClassCharacters.TATITATOO)) {
                                    playableCharacter.getBullets().get(bulletIndex).setRelativeWidth(0);
                                    playableCharacter.getBullets().get(bulletIndex).setRelativeHeight(0);
                                }
                            }
                        }

                        if (playableCharacter.getClassCharacter().equals(ClassCharacters.TATITATOO) && playableCharacter.isUltimate1Running()) {
                            for (PlayableCharacter otherPlayer : gameClient.getOtherPlayers()) {
                                if (!CollisionDetection.isCollisionBetween(otherPlayer, playableCharacter).equals(PlayerCollisionSide.NONE)) {
                                    addHit(otherPlayer.getName(), 0.01f);
                                }
                            }
                        }
                    });

                    gameClient.sendPlayerInformation(playableCharacter);
                    gameClient.sendBulletsInformation(playableCharacter);

                    gameFrame.getHomePanel().refreshHome(gameClient.getOtherPlayers());
                    gameFrame.getHomePanel().setPlayerValues(playableCharacter);

                    characterView.setHealth(playableCharacter.getHealth());
                    characterView.setUltimateLoading(playableCharacter.getUltimateLoading());
                    characterView.setUltimate1Running(playableCharacter.isUltimate1Running());
                    characterView.setUltimate2Running(playableCharacter.isUltimate2Running());
                    characterView.setUltimate3Running(playableCharacter.isUltimate3Running());
                    characterView.setRelativeX(playableCharacter.getRelativeX());

                    characterView.setRelativeY(playableCharacter.getRelativeY());

                    characterView.setRelativeWidth(playableCharacter.getRelativeWidth());
                    characterView.setRelativeHeight(playableCharacter.getRelativeHeight());

                    characterView.setInventory(playableCharacter.getInventory());

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

        for (Hit hit : playableCharacter.getHits()) {
            if (oldestHitTime == 0) {
                oldestHitTime = hit.getTime();
            } else if (playableCharacter.getHits().get(oldestHitIndex).getTime() > hit.getTime()) {
                oldestHitIndex = playableCharacter.getHits().indexOf(hit);
                oldestHitTime = hit.getTime();
            }
        }

        playableCharacter.getHits().get(oldestHitIndex).setTime(System.currentTimeMillis());
        playableCharacter.getHits().get(oldestHitIndex).setVictim(playerName);
        playableCharacter.getHits().get(oldestHitIndex).setDamage(hitDamage);
    }

    private static void randomSpawn() {
        playerOnLeftYodel = false;
        playerOnRightYodel = false;
        yodelDetection = false;
        relativeMovementX = 0;
        relativeMovementY = 0;
        ultimateClick = false;
        playableCharacter.setHealth(1);
        if (playableCharacter.getUltimateLoading() < 0.5f)
            playableCharacter.setUltimateLoading(0);
        else
            playableCharacter.setUltimateLoading(playableCharacter.getUltimateLoading() - 0.5f);

        playableCharacter.setClassCharacter(characterView.getClassCharacter());
        characterView.setClassCharacter(playableCharacter.getClassCharacter());

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

            if (CollisionDetection.isCollisionBetween(platforms[platformIndex], playableCharacter).equals(PlayerCollisionSide.NONE)) {
                playableCharacter.setRelativeX(platforms[platformIndex].getCenterX() - playableCharacter.getRelativeWidth() / 2);
                playableCharacter.setRelativeY(platforms[platformIndex].getRelativeY() - playableCharacter.getRelativeHeight() - 0.01f);
                playableCharacter.setHorizontalDirection((Math.abs(playableCharacter.getCenterX() - 0.5)) / (playableCharacter.getCenterX() - 0.5));
                characterView.setHorizontalDirection(playableCharacter.getHorizontalDirection());
                playableCharacter.setHorizontalDirection(0);
                characterView.setHorizontalDirection(playableCharacter.getHorizontalDirection());
                break;
            }
        }
    }
}
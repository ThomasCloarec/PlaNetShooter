import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import model.CollisionDetection;
import model.PlayerCollisionSide;
import model.Terrain;
import model.bullets.Bullet;
import model.characters.ClassCharacters;
import model.characters.Direction;
import model.characters.PlayableCharacter;
import model.platforms.Platform;
import network.GameClient;
import network.Network;
import view.client.connection.AskClientName;
import view.client.connection.AskIPHost;
import view.client.connection.ServerFullError;
import view.client.game_frame.*;
import view.client.keyboard_actions.PressAction;
import view.client.keyboard_actions.ReleaseAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

class MainClient {
    private static String clientName;
    private static GameClient gameClient;
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
    private static boolean gameServerFull = false;
    private static String serverIP;
    private static float totalDirection = 0;
    private static volatile boolean readyToLaunchGameLoop = false;
    private static final ReleaseAction releaseActionLeft = new ReleaseAction(directions, Direction.LEFT);
    private static final ReleaseAction releaseActionRight = new ReleaseAction(directions, Direction.RIGHT);
    private static boolean readyToFire = false;
    private static long lastShot = 0;
    private static MouseEvent lastMousePressedEvent;
    private static Platform[] platforms;

    public static void main(String[] args) {
        launchGameClient();
        if (!gameServerFull) {
            SwingUtilities.invokeLater(MainClient::launchGameFrame);

            while (true)
                if (readyToLaunchGameLoop) break;

            launchGameLoop();
        }
        else
            new ServerFullError();
    }

    private static void launchGameClient() {
        while(true) {
            try {
                serverIP = AskIPHost.getIPHost();
                gameClient = new GameClient(serverIP);
                break;
            } catch (IOException e) {
                System.out.println("No game server found with this IP on the network.");
                AskIPHost.setGoBack(true);
            }
        }

        gameClient.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Network.RemoveName) {
                    Network.RemoveName removeName = (Network.RemoveName) object;
                    System.out.println("\"" + removeName.name + "\" is disconnected !");

                    if (gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.registerNameList.getList().indexOf(removeName.name)).getNameLabel().getParent() != null)
                        gameFrame.getGamePanel().remove(gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.registerNameList.getList().indexOf(removeName.name)).getNameLabel());
                    if (gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.registerNameList.getList().indexOf(removeName.name)).getCharacterLabel().getParent() != null)
                        gameFrame.getGamePanel().remove(gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.registerNameList.getList().indexOf(removeName.name)).getCharacterLabel());

                    gameFrame.getGamePanel().getOtherPlayersViews().remove(gameClient.registerNameList.getList().indexOf(removeName.name));
                }
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
                if (gameClient.getRegisterNameList().getList().size() == 4) {
                    gameServerFull = true;
                }
                else {
                    AskClientName.setRegisterNameList(gameClient.getRegisterNameList().getList());
                    clientName = AskClientName.getClientName();

                    gameClient.connectedListener(clientName);
                }
                break;
            }
        }
    }

    private static void launchGameFrame() {
        GameFrame.setIsClientAdmin(serverIP.equals("localhost"));

        gameFrame = new GameFrame(clientName);

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
        RandomSpawn();
        playableCharacter.setClassCharacter(ClassCharacters.BOB.name());
        characterView = new CharacterView(
                playableCharacter.getRelativeX(),
                playableCharacter.getRelativeY(),
                playableCharacter.getRelativeWidth(),
                playableCharacter.getRelativeHeight(),
                playableCharacter.getName(),
                playableCharacter.getClassCharacter());

        gameFrame.getGamePanel().setCharacterView(characterView);
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
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_Z) {
                    if (collisionOnBottom)
                        jumpKeyJustPressed = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_E && !(CollisionDetection.isCollisionBetween(playableCharacter, new HomeView()).equals(PlayerCollisionSide.NONE))) {
                    gameFrame.getCardLayout().next(gameFrame.getContentPane());
                    playableCharacter.setRelativeY(-1.15f);
                }
            }
        });

        gameFrame.getGamePanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    lastMousePressedEvent = e;
                    readyToFire = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (SwingUtilities.isLeftMouseButton(e))
                    readyToFire = false;
            }
        });

        gameFrame.getGamePanel().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseMoved(e);
                if (readyToFire)
                    lastMousePressedEvent = e;            }
        });

        gameFrame.getHomePanel().getBackToGameButton().addActionListener(e -> {
            gameFrame.getCardLayout().next(gameFrame.getContentPane());
            gameFrame.getGamePanel().requestFocus();
            RandomSpawn();
        });
    }

    private static void launchGameLoop() {
        int[] fpsRecord = new int[1];
        fpsRecord[0] = -1;
        String gameFrameTitleWithoutFPS = gameFrame.getTitle();
        final long[] a = {System.currentTimeMillis()};

        Thread gameLoopThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            while (true) {
                if (System.nanoTime() - lastTime > 1_000_000_000L/120L) {
                    lastTime = System.nanoTime();
                    fpsRecord[0]++;
                    if (System.currentTimeMillis() - a[0] > 250) {
                        gameFrame.setTitle(gameFrameTitleWithoutFPS+ " | FPS : " +fpsRecord[0]*4);
                        fpsRecord[0] = -1;
                        a[0] = System.currentTimeMillis();
                    }

                    totalDirection = 0;
                    for (Direction direction : directions) {
                        totalDirection += direction.getDelta();
                    }

                    if(!gameFrame.getGamePanel().hasFocus()) {
                        releaseActionLeft.removeMovements();
                        releaseActionRight.removeMovements();
                    }

                    playableCharacter.setHorizontal_direction(-totalDirection);
                    characterView.setHorizontal_direction(playableCharacter.getHorizontal_direction());

                    gameClient.sendPlayerInformation(playableCharacter);
                    collisionOnTop = false;
                    collisionOnBottom = false;
                    collisionOnRight = false;
                    collisionOnLeft = false;

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

                    if ((collisionOnRight && relativeMovementX > 0) || (collisionOnLeft && relativeMovementX < 0))
                        relativeMovementX = 0;
                    else if (collisionOnBottom) {
                        if (totalDirection == 1 && relativeMovementX < PlayableCharacter.getRelativeMaxSpeed())
                            relativeMovementX += PlayableCharacter.getRelativeSpeedGrowth();
                        else if (totalDirection == -1 && relativeMovementX > -PlayableCharacter.getRelativeMaxSpeed())
                            relativeMovementX -= PlayableCharacter.getRelativeSpeedGrowth();
                        else {
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
                        else {
                            if (Math.abs(relativeMovementX) < Terrain.getRelativeFriction()/10)
                                relativeMovementX = 0;
                            else if (relativeMovementX > 0)
                                relativeMovementX -= Terrain.getRelativeFriction()/10;
                            else if (relativeMovementX < 0)
                                relativeMovementX += Terrain.getRelativeFriction()/10;
                        }
                    }

                    if (collisionOnBottom) {
                        if (jumpKeyJustPressed) {
                            while (collisionOnBottom) {
                                playableCharacter.setRelativeY(playableCharacter.getRelativeY()-PlayableCharacter.getRelativeJumpStrength());

                                for (Platform platform : platforms) {
                                    collisionOnBottom = CollisionDetection.isCollisionBetween(playableCharacter, platform).equals(PlayerCollisionSide.BOTTOM);
                                    if (collisionOnBottom) {
                                        break;
                                    }
                                }
                            }
                            relativeMovementY -= PlayableCharacter.getRelativeJumpStrength();
                            playableCharacter.setRelativeY(playableCharacter.getRelativeY()+PlayableCharacter.getRelativeJumpStrength());

                            jumpKeyJustPressed = false;
                        }
                        else if (relativeMovementY > 0)
                            relativeMovementY = 0;
                    }
                    else if (collisionOnTop && relativeMovementY < 0)
                        relativeMovementY = Terrain.getRelativeGravityGrowth();
                    else if (relativeMovementY < Terrain.getRelativeMaxGravity())
                        relativeMovementY += Terrain.getRelativeGravityGrowth();

                    if (playableCharacter.getRelativeY() >= 1)
                        RandomSpawn();

                    playableCharacter.setRelativeX(playableCharacter.getRelativeX() + relativeMovementX);
                    playableCharacter.setRelativeY(playableCharacter.getRelativeY() + relativeMovementY);

                    if(readyToFire) {
                        if(System.currentTimeMillis() - lastShot > 1000f/Bullet.getShotPerSecond()) {
                            float relativeBulletStartX = playableCharacter.getRelativeX() + ((float)-characterView.getHorizontal_direction() + 1) * playableCharacter.getRelativeWidth()/2f;
                            float relativeBulletStartY = playableCharacter.getRelativeY() + playableCharacter.getRelativeHeight()/2f - new Bullet().getRelativeHeight() / 2f;

                            float relativeCursorGoX = lastMousePressedEvent.getX() - new Bullet().getRelativeWidth() * gameFrame.getGamePanel().getWidth() / 2f;
                            float relativeCursorGoY = lastMousePressedEvent.getY() - new Bullet().getRelativeHeight()* gameFrame.getGamePanel().getHeight() / 2f;

                            float tempDeltaX = Math.abs(relativeBulletStartX * (float)gameFrame.getGamePanel().getWidth() - relativeCursorGoX);
                            float tempDeltaY = Math.abs(relativeBulletStartY * (float)gameFrame.getGamePanel().getHeight() - relativeCursorGoY);

                            float bulletSpeedRatio = ((float)Math.toDegrees(Math.atan(Math.abs(tempDeltaY/tempDeltaX)))) / 90f * ((float)gameFrame.getGamePanel().getHeight()/(float)gameFrame.getGamePanel().getWidth() - 372f/768f) * 768f / 372f + 1f;

                            float bulletMovementX = bulletSpeedRatio * tempDeltaX/(tempDeltaX + tempDeltaY) * Bullet.getSPEED() * (relativeCursorGoX -relativeBulletStartX*gameFrame.getGamePanel().getWidth()) / tempDeltaX;
                            float bulletMovementY = bulletSpeedRatio * tempDeltaY/(tempDeltaX + tempDeltaY) * Bullet.getSPEED() * (relativeCursorGoY -relativeBulletStartY*gameFrame.getGamePanel().getHeight()) / tempDeltaY;

                            float bulletRangeRatio = ((float)Math.toDegrees(Math.atan(Math.abs(tempDeltaY/tempDeltaX)))) / 90f + 1f;

                            SwingUtilities.invokeLater(() -> {
                                playableCharacter.getBullets().add(new Bullet(relativeBulletStartX, relativeBulletStartY, bulletMovementX, bulletMovementY, relativeBulletStartX, relativeBulletStartY, bulletRangeRatio));
                                gameFrame.getGamePanel().getBulletsViews().add(new BulletView(relativeBulletStartX, relativeBulletStartY, relativeBulletStartX, relativeBulletStartY, bulletRangeRatio));
                            });
                            lastShot = System.currentTimeMillis(); }
                    }

                    SwingUtilities.invokeLater(() -> {
                        for(Bullet bullet : playableCharacter.getBullets()) {
                            bullet.setRelativeX(bullet.getRelativeX() + bullet.getMovementX());
                            bullet.setRelativeY(bullet.getRelativeY() + (float)gameFrame.getGamePanel().getWidth() / (float)gameFrame.getGamePanel().getHeight() * bullet.getMovementY());

                            gameFrame.getGamePanel().getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeX(bullet.getRelativeX());
                            gameFrame.getGamePanel().getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeY(bullet.getRelativeY());
                        }

                        // Remove bullets
                        playableCharacter.getBullets().removeIf(e -> ((e.getRelativeX() + e.getRelativeWidth() < 0) || e.getRelativeX() > 1));
                        playableCharacter.getBullets().removeIf(e -> ((e.getRelativeY() + e.getRelativeHeight() < 0) || e.getRelativeY() > 1));

                        playableCharacter.getBullets().removeIf(e -> (Math.sqrt(Math.pow(e.getRelativeX()-e.getRelativeBulletStartX(), 2) + Math.pow(e.getRelativeY()-e.getRelativeBulletStartY(), 2))) > Math.sqrt(2) * e.getRelativeMaxRange() * e.getBulletRangeRatio());

                        for (Platform platform : platforms)
                            playableCharacter.getBullets().removeIf(e -> (!CollisionDetection.isCollisionBetween(e, platform).equals(PlayerCollisionSide.NONE)));

                        // Remove associated bullets views
                        gameFrame.getGamePanel().getBulletsViews().removeIf(e -> ((e.getRelativeX() + e.getRelativeWidth() < 0) || e.getRelativeX() > 1));
                        gameFrame.getGamePanel().getBulletsViews().removeIf(e -> ((e.getRelativeY() + e.getRelativeHeight() < 0) || e.getRelativeY() > 1));

                        gameFrame.getGamePanel().getBulletsViews().removeIf(e -> (Math.sqrt(Math.pow(e.getRelativeX()-e.getRelativeBulletStartX(), 2) + Math.pow(e.getRelativeY() - e.getRelativeBulletStartY(), 2))) > Math.sqrt(2) * e.getRelativeMaxRange() * e.getBulletRangeRatio());

                        for (Platform platform : platforms)
                            gameFrame.getGamePanel().getBulletsViews().removeIf(e -> (!CollisionDetection.isCollisionBetween(e, platform).equals(PlayerCollisionSide.NONE)));
                    });

                    characterView.setRelativeX(playableCharacter.getRelativeX());
                    characterView.setRelativeY(playableCharacter.getRelativeY());

                    SwingUtilities.invokeLater(() -> {
                        otherPlayersPainting();
                        gameFrame.getGamePanel().repaint();
                    });

                    if (IS_UNIX_OS)
                        Toolkit.getDefaultToolkit().sync();
                }
            }
        });
        gameLoopThread.start();
    }

    private static void otherPlayersPainting() {
        for (int i = 0; i < gameClient.getOtherPlayers().size(); i++) {
            if (gameFrame.getGamePanel().getOtherPlayersViews().size() > i) {

                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setRelativeX(gameClient.getOtherPlayers().get(i).getRelativeX());
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setRelativeY(gameClient.getOtherPlayers().get(i).getRelativeY());
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setHorizontal_direction(gameClient.getOtherPlayers().get(i).getHorizontal_direction());
            }
            else {
                gameFrame.getGamePanel().addOtherPlayerViewToArray(new CharacterView(
                        gameClient.getOtherPlayers().get(i).getRelativeX(),
                        gameClient.getOtherPlayers().get(i).getRelativeY(),
                        playableCharacter.getRelativeWidth(),
                        playableCharacter.getRelativeHeight(),
                        gameClient.getOtherPlayers().get(i).getName(),
                        gameClient.getOtherPlayers().get(i).getClassCharacter()));
            }
        }
    }

    private static void RandomSpawn(){
        double RandSpawn = Math.random();
        if (RandSpawn  < 0.25){
            playableCharacter.setRelativeX(0.03f);
            playableCharacter.setRelativeY(0.85f);
        }
        else if (RandSpawn > 0.25 & RandSpawn < 0.50){
            playableCharacter.setRelativeX(0.03f);
            playableCharacter.setRelativeY(0.45f);
        }
        else if (RandSpawn > 0.50 & RandSpawn < 0.75){
            playableCharacter.setRelativeX(0.91f);
            playableCharacter.setRelativeY(0.85f);
        }
        else if (RandSpawn > 0.75){
            playableCharacter.setRelativeX(0.91f);
            playableCharacter.setRelativeY(0.45f);
        }
    }
}
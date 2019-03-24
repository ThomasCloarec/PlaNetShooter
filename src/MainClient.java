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
import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

class MainClient {
    private static String clientName;
    private static GameClient gameClient;
    private static float relativeMovementX = 0f;
    private static float relativeMovementY = 0f;
    private static boolean collisionOnRight = false, collisionOnLeft = false, collisionOnTop = false, collisionOnBottom = false;
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
    private static long lastShot = 0;
    private static MouseEvent lastMousePressedEvent;
    private static Platform[] platforms;
    private static PlayableCharacter playableCharacter;
    private static boolean ultimateClick = false;

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
                SwingUtilities.invokeLater(() -> {
                    if (object instanceof Network.RemoveName) {
                        Network.RemoveName removeName = (Network.RemoveName) object;

                        if (gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.getRegisterList().getNameList().indexOf(removeName.name)).getCharacterLabel().getParent() != null)
                            gameFrame.getGamePanel().remove(gameFrame.getGamePanel().getOtherPlayersViews().get(gameClient.getRegisterList().getNameList().indexOf(removeName.name)).getCharacterLabel());

                        gameFrame.getGamePanel().getOtherPlayersViews().remove(gameClient.getRegisterList().getNameList().indexOf(removeName.name));

                    }
                    if (object instanceof Network.Hit) {
                        Network.Hit hit = (Network.Hit) object;
                        playableCharacter.setHealth(playableCharacter.getHealth() - hit.getDamage());
                    }
                    gameClient.receivedListener(object);
                });
            }

            @Override
            public void disconnected (Connection connection) {
                System.out.println("You are disconnected !\nServer closed.");
                System.exit(1);
            }
        });

        while (true) {
            if (gameClient.getRegisterList() != null) {
                if (gameClient.getRegisterList().getNameList().size() == 4) {
                    gameServerFull = true;
                }
                else {
                    AskClientName.setRegisterNameList(gameClient.getRegisterList().getNameList());
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
        playableCharacter.setRelativeY(-1.15f);
        playableCharacter.setClassCharacter(ClassCharacters.TATITATOO);
        characterView = new CharacterView(
                playableCharacter.getRelativeX(),
                playableCharacter.getRelativeY(),
                playableCharacter.getRelativeWidth(),
                playableCharacter.getRelativeHeight(),
                playableCharacter.getName(),
                playableCharacter.getClassCharacter(),
                playableCharacter.getHealth());

        gameFrame.getGamePanel().setCharacterView(characterView);
        gameFrame.getHomePanel().setClassCharacter(playableCharacter.getClassCharacter());
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
                    playableCharacter.setAtHome(true);
                }
                else if (e.getKeyCode() == KeyEvent.VK_H) {
                    gameFrame.getGamePanel().setHitBoxMode(gameFrame.getGamePanel().isHitBoxMode());
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
                else if (SwingUtilities.isRightMouseButton(e) && playableCharacter.getUltimateLoading() == 1) {
                    ultimateClick = true;
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
            playableCharacter.setAtHome(false);
            randomSpawn();
        });

        gameFrame.getHomePanel().getChangeCharacterButton().addActionListener(e -> {
            playableCharacter.setClassCharacter(ClassCharacters.getClassCharactersList().get((ClassCharacters.getClassCharactersList().indexOf(playableCharacter.getClassCharacter()) + 1) % ClassCharacters.getClassCharactersList().size()));
            characterView.setClassCharacter(playableCharacter.getClassCharacter());

            playableCharacter.setUltimateLoading(0f);

            gameFrame.getHomePanel().setClassCharacter(playableCharacter.getClassCharacter());
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
                    try {
                        for (Direction direction : directions) {
                            totalDirection += direction.getDelta();
                        }
                    }
                    catch (ConcurrentModificationException ignored) {
                    }
                    catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    if(!gameFrame.getGamePanel().hasFocus()) {
                        releaseActionLeft.removeMovements();
                        releaseActionRight.removeMovements();
                    }

                    playableCharacter.setHorizontal_direction(-totalDirection);
                    characterView.setHorizontal_direction(playableCharacter.getHorizontal_direction());

                    if (!playableCharacter.isAtHome()) {
                        if (playableCharacter.getUltimateLoading() >= 1f - playableCharacter.getUltimateLoadingPerSecond() / 120f)
                            playableCharacter.setUltimateLoading(1f);
                        else
                            playableCharacter.setUltimateLoading(playableCharacter.getUltimateLoading() + playableCharacter.getUltimateLoadingPerSecond() / 120f);
                    }

                    if (ultimateClick) {
                        if (playableCharacter.getClassCharacter().equals(ClassCharacters.ANGELO)) {
                            playableCharacter.ultimate2();
                            characterView.ultimate2();
                        }
                        ultimateClick = false;
                        playableCharacter.setUltimateLoading(0f);
                    }

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
                    }
                    else {
                        if (totalDirection == 1 && relativeMovementX < playableCharacter.getRelativeMaxSpeed())
                            relativeMovementX += playableCharacter.getRelativeSpeedGrowth()/2;
                        else if (totalDirection == -1 && relativeMovementX > -playableCharacter.getRelativeMaxSpeed())
                            relativeMovementX -= playableCharacter.getRelativeSpeedGrowth()/2;
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
                        randomSpawn();

                    playableCharacter.setRelativeX(playableCharacter.getRelativeX() + relativeMovementX);
                    playableCharacter.setRelativeY(playableCharacter.getRelativeY() + relativeMovementY);

                    if(readyToFire) {
                        if(System.currentTimeMillis() - lastShot > 1000f/playableCharacter.getAttackNumberPerSecond()) {
                            Bullet bullet = new Bullet();

                            if (playableCharacter.getClassCharacter().equals(ClassCharacters.ANGELO)) {
                                bullet.setSpeed(0.004f);
                                bullet.setRelativeWidth(0.015f);
                                bullet.setRelativeHeight(0.015f * 768f / 372f);
                                bullet.setDamage(0.15f);
                            }

                            float relativeBulletStartX = playableCharacter.getRelativeX() + ((float)-characterView.getHorizontal_direction() + 1) * playableCharacter.getRelativeWidth()/2f;
                            float relativeBulletStartY = playableCharacter.getRelativeY() + playableCharacter.getRelativeHeight()/2f -bullet.getRelativeHeight() / 2f;
                            bullet.setRelativeBulletStartX(relativeBulletStartX);
                            bullet.setRelativeBulletStartY(relativeBulletStartY);

                            float relativeCursorGoX = lastMousePressedEvent.getX() - bullet.getRelativeWidth() * gameFrame.getGamePanel().getWidth() / 2f;
                            float relativeCursorGoY = lastMousePressedEvent.getY() - bullet.getRelativeHeight() * gameFrame.getGamePanel().getHeight() / 2f;

                            float tempDeltaX = Math.abs(relativeBulletStartX * (float)gameFrame.getGamePanel().getWidth() - relativeCursorGoX);
                            float tempDeltaY = Math.abs(relativeBulletStartY * (float)gameFrame.getGamePanel().getHeight() - relativeCursorGoY);

                            float bulletSpeedRatio = ((float)Math.toDegrees(Math.atan(Math.abs(tempDeltaY/tempDeltaX)))) / 90f * ((float)gameFrame.getGamePanel().getHeight()/(float)gameFrame.getGamePanel().getWidth() - 372f/768f) * 768f / 372f + 1f;

                            float bulletMovementX = bulletSpeedRatio * tempDeltaX/(tempDeltaX + tempDeltaY) * bullet.getSpeed() * (relativeCursorGoX -relativeBulletStartX*gameFrame.getGamePanel().getWidth()) / tempDeltaX;
                            float bulletMovementY = bulletSpeedRatio * tempDeltaY/(tempDeltaX + tempDeltaY) * bullet.getSpeed() * (relativeCursorGoY -relativeBulletStartY*gameFrame.getGamePanel().getHeight()) / tempDeltaY;
                            bullet.setMovementX(bulletMovementX);
                            bullet.setMovementY(bulletMovementY);

                            float bulletRangeRatio = ((float)Math.toDegrees(Math.atan(Math.abs(tempDeltaY/tempDeltaX)))) / 90f + 1f;
                            bullet.setBulletRangeRatio(bulletRangeRatio);

                            SwingUtilities.invokeLater(() -> {
                                playableCharacter.getBullets().add(bullet);
                                BulletView bulletView = new BulletView(relativeBulletStartX, relativeBulletStartY, bullet.getRelativeWidth(), bullet.getRelativeHeight());
                                bulletView.setDeltaX(tempDeltaX);
                                bulletView.setDeltaY(tempDeltaY);

                                characterView.getBulletsViews().add(bulletView);
                            });
                            lastShot = System.currentTimeMillis();
                        }
                    }

                    SwingUtilities.invokeLater(() -> {
                        for(Bullet bullet : playableCharacter.getBullets()) {
                            bullet.setRelativeX(bullet.getRelativeX() + bullet.getMovementX());
                            bullet.setRelativeY(bullet.getRelativeY() + (float)gameFrame.getGamePanel().getWidth() / (float)gameFrame.getGamePanel().getHeight() * bullet.getMovementY());

                            characterView.getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeX(bullet.getRelativeX());
                            characterView.getBulletsViews().get(playableCharacter.getBullets().indexOf(bullet)).setRelativeY(bullet.getRelativeY());
                        }

                        ListIterator<Bullet> iterOutOfRange = playableCharacter.getBullets().listIterator();
                        while(iterOutOfRange.hasNext()){
                            int iterOutOfRangeBulletIndex = iterOutOfRange.nextIndex();
                            Bullet iterOutOfRangeBullet = iterOutOfRange.next();

                            if((iterOutOfRangeBullet.getRelativeX() + iterOutOfRangeBullet.getRelativeWidth() < 0)
                                    || (iterOutOfRangeBullet.getRelativeX() > 1)
                                    || (iterOutOfRangeBullet.getRelativeY() + iterOutOfRangeBullet.getRelativeHeight() < 0)
                                    || (iterOutOfRangeBullet.getRelativeY() > 1)
                                    || (Math.sqrt(Math.pow(iterOutOfRangeBullet.getRelativeX()-iterOutOfRangeBullet.getRelativeBulletStartX(), 2) + Math.pow(iterOutOfRangeBullet.getRelativeY() - iterOutOfRangeBullet.getRelativeBulletStartY(), 2))) > Math.sqrt(2) * iterOutOfRangeBullet.getRelativeMaxRange() * iterOutOfRangeBullet.getBulletRangeRatio()){

                                gameFrame.getGamePanel().remove(characterView.getBulletsViews().get(iterOutOfRangeBulletIndex).getBulletLabel());
                                characterView.getBulletsViews().remove(iterOutOfRangeBulletIndex);
                                iterOutOfRange.remove();
                            }
                        }

                        ListIterator<Bullet> bullets = playableCharacter.getBullets().listIterator();
                        while(bullets.hasNext()) {
                            int bulletIndex = bullets.nextIndex();
                            Bullet bullet = bullets.next();

                            for (Platform platform : platforms) {
                                if (!CollisionDetection.isCollisionBetween(bullet, platform).equals(PlayerCollisionSide.NONE)) {
                                    gameFrame.getGamePanel().remove(characterView.getBulletsViews().get(bulletIndex).getBulletLabel());
                                    characterView.getBulletsViews().remove(bulletIndex);
                                    bullets.remove();
                                }
                            }

                            for (PlayableCharacter otherPlayer : gameClient.getOtherPlayers()) {
                                if (!CollisionDetection.isCollisionBetween(otherPlayer, bullet).equals(PlayerCollisionSide.NONE)) {
                                    gameClient.sendHit(new Network.Hit(otherPlayer.getName(), bullet.getDamage()));
                                    gameFrame.getGamePanel().remove(characterView.getBulletsViews().get(bulletIndex).getBulletLabel());
                                    characterView.getBulletsViews().remove(bulletIndex);
                                    bullets.remove();
                                }
                            }
                        }
                    });
                    characterView.setHealth(playableCharacter.getHealth());
                    characterView.setRelativeX(playableCharacter.getRelativeX());
                    characterView.setRelativeY(playableCharacter.getRelativeY());
                    characterView.setRelativeWidth(playableCharacter.getRelativeWidth());
                    characterView.setRelativeHeight(playableCharacter.getRelativeHeight());
                    characterView.setUltimateLoading(playableCharacter.getUltimateLoading());

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
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setRelativeWidth(gameClient.getOtherPlayers().get(i).getRelativeWidth());
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setRelativeHeight(gameClient.getOtherPlayers().get(i).getRelativeHeight());
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setHealth(gameClient.getOtherPlayers().get(i).getHealth());
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setHorizontal_direction(gameClient.getOtherPlayers().get(i).getHorizontal_direction());
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setClassCharacter(gameClient.getOtherPlayers().get(i).getClassCharacter());
                gameFrame.getGamePanel().getOtherPlayersViews().get(i).setUltimateLoading(gameClient.getOtherPlayers().get(i).getUltimateLoading());

                // REASON OF BULLET BUG
                // ALWAYS LET THE MAXIMUM NUMBER OF BULLET THAT BEEN SENT AT THE SAME TIME

                for (int j = 0; j < gameClient.getOtherPlayers().get(i).getBullets().size(); j++) {
                    if (gameFrame.getGamePanel().getOtherPlayersViews().get(i).getBulletsViews().size() > j) {
                        gameFrame.getGamePanel().getOtherPlayersViews().get(i).getBulletsViews().get(j).setRelativeX(gameClient.getOtherPlayers().get(i).getBullets().get(j).getRelativeX());
                        gameFrame.getGamePanel().getOtherPlayersViews().get(i).getBulletsViews().get(j).setRelativeY(gameClient.getOtherPlayers().get(i).getBullets().get(j).getRelativeY());
                    }
                    else {
                        gameFrame.getGamePanel().getOtherPlayersViews().get(i).getBulletsViews().add(new BulletView(gameClient.getOtherPlayers().get(i).getBullets().get(j).getRelativeX(), gameClient.getOtherPlayers().get(i).getBullets().get(j).getRelativeY(), gameClient.getOtherPlayers().get(i).getBullets().get(j).getRelativeWidth(), gameClient.getOtherPlayers().get(i).getBullets().get(j).getRelativeHeight()));
                    }
                }

                while (gameClient.getOtherPlayers().get(i).getBullets().size() < gameFrame.getGamePanel().getOtherPlayersViews().get(i).getBulletsViews().size()) {
                    gameFrame.getGamePanel().remove(gameFrame.getGamePanel().getOtherPlayersViews().get(i).getBulletsViews().get(0).getBulletLabel());
                    gameFrame.getGamePanel().getOtherPlayersViews().get(i).getBulletsViews().remove(0);
                }
            }
            else {
                gameFrame.getGamePanel().getOtherPlayersViews().add(new CharacterView(
                        gameClient.getOtherPlayers().get(i).getRelativeX(),
                        gameClient.getOtherPlayers().get(i).getRelativeY(),
                        gameClient.getOtherPlayers().get(i).getRelativeWidth(),
                        gameClient.getOtherPlayers().get(i).getRelativeHeight(),
                        gameClient.getOtherPlayers().get(i).getName(),
                        gameClient.getOtherPlayers().get(i).getClassCharacter(),
                        gameClient.getOtherPlayers().get(i).getHealth()));
            }
        }
    }

    private static void randomSpawn(){
        double RandSpawn = Math.random();
        if (RandSpawn  < 0.25){
            playableCharacter.setRelativeX(0.03f);
            playableCharacter.setRelativeY(0.85f - (playableCharacter.getRelativeHeight()-0.05f * 768f/372f));
        }
        else if (RandSpawn > 0.25 & RandSpawn < 0.50){
            playableCharacter.setRelativeX(0.03f);
            playableCharacter.setRelativeY(0.45f - (playableCharacter.getRelativeHeight()-0.05f * 768f/372f));
        }
        else if (RandSpawn > 0.50 & RandSpawn < 0.75){
            playableCharacter.setRelativeX(0.91f);
            playableCharacter.setRelativeY(0.85f - (playableCharacter.getRelativeHeight()-0.05f * 768f/372f));
        }
        else if (RandSpawn > 0.75){
            playableCharacter.setRelativeX(0.91f);
            playableCharacter.setRelativeY(0.45f - (playableCharacter.getRelativeHeight()-0.05f * 768f/372f));
        }
    }
}
package network;

import com.esotericsoftware.kryonet.Client;
import model.bullets.Bullet;
import model.characters.PlayableCharacter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameClient extends Client {
    private Network.RegisterList registerList;
    private final List<PlayableCharacter> otherPlayers = new ArrayList<>();
    private static double connectingTimeout = 250;

    private int i = 0;
    public GameClient(String IPHost) throws IOException {
        super(16384, 2048);
        new Thread(this).start();
        Network.register(this);
        this.connect((int) Math.ceil(connectingTimeout), IPHost, Network.getTcpPort(), Network.getUdpPort());
        this.setTimeout(Integer.MAX_VALUE);
    }

    public void connectedListener(String name) {
        Network.RegisterName registerName = new Network.RegisterName();

        registerName.name = name;

        System.out.println("\"" + registerName.name+ "\" (You) is connected");
        this.sendTCP(registerName);
    }

    public void receivedListener(Object object) {
        if (object instanceof Network.RegisterName) {
            Network.RegisterName registerName = (Network.RegisterName)object;
            System.out.println("\"" +registerName.name+ "\" is connected");
            registerList.getNameList().add(registerName.name);
        }
        if (object instanceof Network.RemoveName) {
            Network.RemoveName removeName = (Network.RemoveName)object;
            System.out.println("\"" +removeName.name+ "\" is disconnected");
            otherPlayers.remove(registerList.getNameList().indexOf(removeName.name));
            registerList.getNameList().remove(removeName.name);
        }
        if (object instanceof Network.RegisterList) {
            registerList = (Network.RegisterList)object;
        }
        if (object instanceof PlayableCharacter) {
            PlayableCharacter playableCharacter = (PlayableCharacter) object;

            if (playableCharacter.getName() != null && registerList != null) {
                if (registerList.getNameList().contains(playableCharacter.getName())) {
                    if (otherPlayers.size() > registerList.getNameList().indexOf(playableCharacter.getName())) {
                        if (!otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).getClassCharacter().equals(playableCharacter.getClassCharacter())) {
                            otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setClassCharacter(playableCharacter.getClassCharacter());
                            otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setClassCharacterChanged(true);
                        }
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setUltimateLoading(playableCharacter.getUltimateLoading());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setHealth(playableCharacter.getHealth());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setName(playableCharacter.getName());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setHorizontalDirection(playableCharacter.getHorizontalDirection());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setLastHorizontalDirection(playableCharacter.getLastHorizontalDirection());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setAtHome(playableCharacter.isAtHome());

                        if (!otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).isUltimate1Running() && playableCharacter.isUltimate1Running())
                            otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).ultimate1();
                        else if (!otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).isUltimate2Running() && playableCharacter.isUltimate2Running())
                            otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).ultimate2();
                        else if (!otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).isUltimate3Running() && playableCharacter.isUltimate3Running())
                            otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).ultimate3();

                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setUltimate1Running(playableCharacter.isUltimate1Running());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setUltimate2Running(playableCharacter.isUltimate2Running());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setUltimate3Running(playableCharacter.isUltimate3Running());

                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setKills(playableCharacter.getKills());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setDeaths(playableCharacter.getDeaths());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setMoney(playableCharacter.getMoney());

                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeX(playableCharacter.getRelativeX());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeY(playableCharacter.getRelativeY());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeWidth(playableCharacter.getRelativeWidth());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeHeight(playableCharacter.getRelativeHeight());
                    }
                    else {
                        for (int i = 0; i < PlayableCharacter.getMaxBulletNumberPerPlayer(); i++) {
                            Bullet bullet = new Bullet();
                            bullet.setRelativeWidth(0);
                            bullet.setRelativeHeight(0);

                            playableCharacter.getBullets().add(bullet);
                        }

                        playableCharacter.setHorizontalDirection(playableCharacter.getLastHorizontalDirection());
                        otherPlayers.add(playableCharacter);
                    }
                }
            }
        }
        if (object instanceof Network.UpdateBullet) {
            Network.UpdateBullet updateBullet = (Network.UpdateBullet) object;
            for (PlayableCharacter otherPlayer : otherPlayers) {
                if (otherPlayer.getName().equals(updateBullet.getName())) {
                    if (otherPlayer.getBullets().size() == PlayableCharacter.getMaxBulletNumberPerPlayer())
                        otherPlayer.getBullets().set(updateBullet.getBulletIndex(), updateBullet.getBullet());
                }
            }
        }
    }

    public void sendPlayerInformation(PlayableCharacter character) {
        PlayableCharacter characterCopy = new PlayableCharacter();

        characterCopy.setBullets(new ArrayList<>());
        characterCopy.setClassCharacter(character.getClassCharacter());
        characterCopy.setUltimateLoading(character.getUltimateLoading());
        characterCopy.setHealth(character.getHealth());
        characterCopy.setName(character.getName());
        characterCopy.setHorizontalDirection(character.getHorizontalDirection());
        characterCopy.setAtHome(character.isAtHome());
        characterCopy.setUltimate1Running(character.isUltimate1Running());
        characterCopy.setUltimate2Running(character.isUltimate2Running());
        characterCopy.setUltimate3Running(character.isUltimate3Running());
        characterCopy.setRelativeX(character.getRelativeX());
        characterCopy.setRelativeY(character.getRelativeY());
        characterCopy.setRelativeWidth(character.getRelativeWidth());
        characterCopy.setRelativeHeight(character.getRelativeHeight());
        characterCopy.setLastHorizontalDirection(character.getLastHorizontalDirection());
        characterCopy.setKills(character.getKills());
        characterCopy.setDeaths(character.getDeaths());
        characterCopy.setMoney(character.getMoney());

        this.sendUDP(characterCopy);
    }

    public void sendBulletsInformation(PlayableCharacter character) {
        if (i % 2 == 0) {
            for (Bullet bullet : character.getBullets()) {
                Network.UpdateBullet updateBullet = new Network.UpdateBullet();
                updateBullet.setBullet(bullet);
                updateBullet.setBulletIndex(character.getBullets().indexOf(bullet));
                updateBullet.setName(character.getName());
                this.sendUDP(updateBullet);
            }
        }
        i++;
    }

    public void sendHit(Network.Hit hit) {
        this.sendTCP(hit);
    }

    public Network.RegisterList getRegisterList() {
        return registerList;
    }

    public List<PlayableCharacter> getOtherPlayers() {
        return otherPlayers;
    }

    public static double getConnectingTimeout() {
        return connectingTimeout;
    }

    public static void setConnectingTimeout(double connectingTimeout) {
        GameClient.connectingTimeout = connectingTimeout;
    }
}
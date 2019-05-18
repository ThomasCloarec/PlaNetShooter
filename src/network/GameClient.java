package network;

import com.esotericsoftware.kryonet.Client;
import model.Bullet;
import model.characters.Character;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameClient extends Client {
    private Network.RegisterList registerList;
    private final List<Character> otherPlayers = new ArrayList<>();
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
        if (object instanceof Character) {
            Character character = (Character) object;

            if (character.getName() != null && registerList != null) {
                if (registerList.getNameList().contains(character.getName())) {
                    if (otherPlayers.size() > registerList.getNameList().indexOf(character.getName())) {
                        if (!otherPlayers.get(registerList.getNameList().indexOf(character.getName())).getClassCharacter().equals(character.getClassCharacter())) {
                            otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setClassCharacter(character.getClassCharacter());
                            otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setClassCharacterChanged(true);
                        }
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setUltimateLoading(character.getUltimateLoading());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setHealth(character.getHealth());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setName(character.getName());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setHorizontalDirection(character.getHorizontalDirection());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setLastHorizontalDirection(character.getLastHorizontalDirection());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setAtHome(character.isAtHome());

                        if (!otherPlayers.get(registerList.getNameList().indexOf(character.getName())).isUltimate1Running() && character.isUltimate1Running())
                            otherPlayers.get(registerList.getNameList().indexOf(character.getName())).ultimate1();
                        else if (!otherPlayers.get(registerList.getNameList().indexOf(character.getName())).isUltimate2Running() && character.isUltimate2Running())
                            otherPlayers.get(registerList.getNameList().indexOf(character.getName())).ultimate2();
                        else if (!otherPlayers.get(registerList.getNameList().indexOf(character.getName())).isUltimate3Running() && character.isUltimate3Running())
                            otherPlayers.get(registerList.getNameList().indexOf(character.getName())).ultimate3();

                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setUltimate1Running(character.isUltimate1Running());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setUltimate2Running(character.isUltimate2Running());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setUltimate3Running(character.isUltimate3Running());

                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setKills(character.getKills());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setDeaths(character.getDeaths());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setMoney(character.getMoney());

                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setRelativeX(character.getRelativeX());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setRelativeY(character.getRelativeY());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setRelativeWidth(character.getRelativeWidth());
                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setRelativeHeight(character.getRelativeHeight());

                        otherPlayers.get(registerList.getNameList().indexOf(character.getName())).setInventory(character.getInventory());
                    }
                    else {
                        for (int i = 0; i < Character.getMaxBulletNumberPerPlayer(); i++) {
                            Bullet bullet = new Bullet();
                            bullet.setRelativeWidth(0);
                            bullet.setRelativeHeight(0);

                            character.getBullets().add(bullet);
                        }

                        character.setHorizontalDirection(character.getLastHorizontalDirection());
                        otherPlayers.add(character);
                    }
                }
            }
        }
        if (object instanceof Network.UpdateBullet) {
            Network.UpdateBullet updateBullet = (Network.UpdateBullet) object;
            for (Character otherPlayer : otherPlayers) {
                if (otherPlayer.getName().equals(updateBullet.getName())) {
                    if (otherPlayer.getBullets().size() == Character.getMaxBulletNumberPerPlayer())
                        otherPlayer.getBullets().set(updateBullet.getBulletIndex(), updateBullet.getBullet());
                }
            }
        }
    }

    public void sendPlayerInformation(Character character) {
        Character characterCopy = new Character();

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
        characterCopy.setHits(character.getHits());
        characterCopy.setInventory(character.getInventory());
        characterCopy.setLastKiller(character.getLastKiller());
        characterCopy.setLastDeathTime(character.getLastDeathTime());

        this.sendUDP(characterCopy);
    }

    public void sendBulletsInformation(Character character) {
        if (i % 2 == 0) {
            for (int j = 0; j < character.getBullets().size()/2; j++) {
                Network.UpdateBullet updateBullet = new Network.UpdateBullet();
                updateBullet.setBullet(character.getBullets().get(j));
                updateBullet.setBulletIndex(character.getBullets().indexOf(character.getBullets().get(j)));
                updateBullet.setName(character.getName());
                this.sendUDP(updateBullet);
            }
        }
        else {
            for (int j = character.getBullets().size()/2; j < character.getBullets().size(); j++) {
                Network.UpdateBullet updateBullet = new Network.UpdateBullet();
                updateBullet.setBullet(character.getBullets().get(j));
                updateBullet.setBulletIndex(character.getBullets().indexOf(character.getBullets().get(j)));
                updateBullet.setName(character.getName());
                this.sendUDP(updateBullet);
            }
        }
        i++;
    }

    public Network.RegisterList getRegisterList() {
        return registerList;
    }

    public List<Character> getOtherPlayers() {
        return otherPlayers;
    }

    public static double getConnectingTimeout() {
        return connectingTimeout;
    }

    public static void setConnectingTimeout(double connectingTimeout) {
        GameClient.connectingTimeout = connectingTimeout;
    }
}
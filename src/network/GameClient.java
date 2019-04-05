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

    private int i = 0;
    public GameClient(String IPHost) throws IOException {
        super(16384, 2048);
        new Thread(this).start();
        Network.register(this);
        this.connect(5000, IPHost, Network.getTcpPort(), Network.getUdpPort());
        this.setTimeout(Integer.MAX_VALUE);
    }

    public void connectedListener(String name) {
        Network.RegisterName registerName = new Network.RegisterName();

        registerName.name = name;

        System.out.println("\"" + registerName.name+ "\" (You) is connected !");
        this.sendTCP(registerName);
    }

    public void receivedListener(Object object) {
        if (object instanceof Network.RegisterName) {
            Network.RegisterName registerName = (Network.RegisterName)object;
            System.out.println("\"" +registerName.name+ "\" is connected !");
            registerList.getNameList().add(registerName.name);
        }
        if (object instanceof Network.RemoveName) {
            Network.RemoveName removeName = (Network.RemoveName)object;
            System.out.println("\"" +removeName.name+ "\" is disconnected !");
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
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeX(playableCharacter.getRelativeX());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeY(playableCharacter.getRelativeY());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeWidth(playableCharacter.getRelativeWidth());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setRelativeHeight(playableCharacter.getRelativeHeight());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setUltimateLoading(playableCharacter.getUltimateLoading());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setHealth(playableCharacter.getHealth());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setName(playableCharacter.getName());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setClassCharacter(playableCharacter.getClassCharacter());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setHorizontal_direction(playableCharacter.getHorizontal_direction());
                        otherPlayers.get(registerList.getNameList().indexOf(playableCharacter.getName())).setAtHome(playableCharacter.isAtHome());
                    }
                    else {
                        for (int i = 0; i < PlayableCharacter.getMaxBulletNumberPerPlayer(); i++) {
                            Bullet bullet = new Bullet();
                            bullet.setRelativeWidth(0);
                            bullet.setRelativeHeight(0);

                            playableCharacter.getBullets().add(bullet);
                        }

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
        characterCopy.setRelativeX(character.getRelativeX());
        characterCopy.setRelativeY(character.getRelativeY());
        characterCopy.setRelativeWidth(character.getRelativeWidth());
        characterCopy.setRelativeHeight(character.getRelativeHeight());
        characterCopy.setUltimateLoading(character.getUltimateLoading());
        characterCopy.setHealth(character.getHealth());
        characterCopy.setName(character.getName());
        characterCopy.setClassCharacter(character.getClassCharacter());
        characterCopy.setHorizontal_direction(character.getHorizontal_direction());
        characterCopy.setAtHome(character.isAtHome());

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
}
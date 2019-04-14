package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import model.SolidObject;
import model.bullets.Bullet;
import model.characters.ClassCharacters;
import model.characters.Hit;
import model.characters.PlayableCharacter;
import model.characters.SmallWave;
import view.client.game_frame.Trampoline;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private static final int TCP_PORT = 30082;
    private static final int UDP_PORT = 30083;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterName.class);
        kryo.register(RemoveName.class);
        kryo.register(RegisterList.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(java.util.List.class);
        kryo.register(PlayableCharacter.class);
        kryo.register(ClassCharacters.class);
        kryo.register(Bullet.class);
        kryo.register(SolidObject.class);
        kryo.register(Hit.class);
        kryo.register(UpdateBullet.class);
        kryo.register(Trampoline.class);
        kryo.register(SmallWave.class);
    }

    static class RegisterName {
        RegisterName() {
        }
        String name;
    }

    public static class RemoveName {
        RemoveName() {
        }
        public String name;
    }

    public static class RegisterList {
        RegisterList() {
        }
        private final List<String> nameList = new ArrayList<>();
        private final List<Integer> connectionIDList = new ArrayList<>();

        public List<String> getNameList() {
            return nameList;
        }

        List<Integer> getConnectionIDList() {
            return connectionIDList;
        }
    }

    static class UpdateBullet {
        UpdateBullet() {
        }

        private Bullet bullet;
        private int bulletIndex;
        private String name;

        void setBullet(Bullet bullet) {
            this.bullet = bullet;
        }

        void setName(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        Bullet getBullet() {
            return bullet;
        }

        int getBulletIndex() {
            return bulletIndex;
        }

        void setBulletIndex(int bulletIndex) {
            this.bulletIndex = bulletIndex;
        }
    }

    static int getTcpPort() {
        return TCP_PORT;
    }

    static int getUdpPort() {
        return UDP_PORT;
    }
}
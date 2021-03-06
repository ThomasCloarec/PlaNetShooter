package view.client.game_frame;

import model.Home;
import model.Trampoline;
import model.Yodel;
import model.characters.Character;
import model.characters.ClassCharacters;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel {
    private final MapView mapView = new MapView();
    private PlatformView[] platforms;
    private CharacterView characterView;
    private final List<CharacterView> otherPlayersViews = new ArrayList<>();
    private boolean hitBoxMode = false;

    GamePanel() {
        super();
        mapView.setIcon("/view/resources/game/map/maya.png");

        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.magenta);
        g.fillRect((int) (new Home().getRelativeX() * this.getWidth()),
                (int) (new Home().getRelativeY() * this.getHeight()),
                (int) (new Home().getRelativeWidth() * this.getWidth()),
                (int) (new Home().getRelativeHeight() * this.getHeight()));

        g.setColor(Color.green);
        g.fillRect((int) (new Yodel("left").getRelativeX() * this.getWidth()),
                (int) (new Yodel("left").getRelativeY() * this.getHeight()),
                (int) (new Yodel("left").getRelativeWidth() * this.getWidth()),
                (int) (new Yodel("left").getRelativeHeight() * this.getHeight()));

        g.setColor(Color.green);
        g.fillRect((int) (new Yodel("right").getRelativeX() * this.getWidth()),
                (int) (new Yodel("right").getRelativeY() * this.getHeight()),
                (int) (new Yodel("right").getRelativeWidth() * this.getWidth()),
                (int) (new Yodel("right").getRelativeHeight() * this.getHeight()));

        g.setColor(Color.red);
        for (Object object : characterView.getInventory()) {
            if (object instanceof Trampoline) {
                g.fillRect((int) (((Trampoline) object).getRelativeX() * this.getWidth()), (int) (((Trampoline) object).getRelativeY() * this.getHeight()), (int) (((Trampoline) object).getRelativeWidth() * this.getWidth()), (int) (((Trampoline) object).getRelativeHeight() * this.getHeight()));
            }
        }

        g.setColor(Color.pink);
        for (CharacterView otherPlayerView : otherPlayersViews) {
            for (Object object : otherPlayerView.getInventory()) {
                if (object instanceof Trampoline) {
                    g.fillRect((int) (((Trampoline) object).getRelativeX() * this.getWidth()), (int) (((Trampoline) object).getRelativeY() * this.getHeight()), (int) (((Trampoline) object).getRelativeWidth() * this.getWidth()), (int) (((Trampoline) object).getRelativeHeight() * this.getHeight()));
                }
            }
        }

        for (PlatformView platform : platforms) {
            if (platform != null) {
                g.setColor(Color.blue);
                g.fillRect((int) (platform.getRelativeX() * this.getWidth()),
                        (int) (platform.getRelativeY() * this.getHeight()),
                        (int) (platform.getRelativeWidth() * this.getWidth()),
                        (int) (platform.getRelativeHeight() * this.getHeight()));
            }
        }

        if (!hitBoxMode) {
            characterView.setScaleWidthCharacter(characterView.getRelativeWidth() * this.getWidth() / characterView.getCharacterIconWidth());
            characterView.setScaleHeightCharacter(characterView.getRelativeHeight() * this.getHeight() / characterView.getCharacterIconHeight());

            if (characterView != null) {
                characterView.getCharacterLabel().setVisible(true);

                if (characterView.getCharacterLabel().getParent() == null) {
                    this.add(characterView.getCharacterLabel());
                    this.revalidate();
                }

                if (characterView.getHorizontalDirection() == -1) {
                    characterView.getCharacterLabel().setLocation((int) ((characterView.getRelativeX() - characterView.getRelativeWidth() / characterView.getScaleWidthCharacter() + characterView.getRelativeWidth()) * this.getWidth()), (int) ((characterView.getRelativeY()) * this.getHeight()));
                } else
                    characterView.getCharacterLabel().setLocation((int) (characterView.getRelativeX() * this.getWidth()), (int) ((characterView.getRelativeY()) * this.getHeight()));

                if (characterView.getClassCharacter().equals(ClassCharacters.TATITATOO) && characterView.isUltimate1Running())
                {
                    characterView.getCharacterLabel().setLocation(characterView.getCharacterLabel().getLocation().x,  characterView.getCharacterLabel().getLocation().y + (int) (0.0075f * this.getHeight()));
                }
            }

            for (BulletView bulletView : Objects.requireNonNull(characterView).getBulletsViews()) {
                if (bulletView != null) {
                    bulletView.getBulletLabel().setVisible(true);

                    bulletView.setScaleWidthBullet(bulletView.getRelativeWidth() * this.getWidth() / bulletView.getBulletIconWidth());
                    bulletView.setScaleHeightBullet(bulletView.getRelativeHeight() * this.getHeight() / bulletView.getBulletIconHeight());

                    if (bulletView.getBulletLabel().getParent() == null) {
                        bulletView.setIcon("/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                        this.add(bulletView.getBulletLabel());
                        this.revalidate();
                    }

                    bulletView.getBulletLabel().setLocation((int) (bulletView.getRelativeX() * this.getWidth()), (int) (bulletView.getRelativeY() * this.getHeight()));
                }
            }

            for (CharacterView otherPlayerView : otherPlayersViews) {
                if (otherPlayerView != null) {
                    for (BulletView bulletView : otherPlayerView.getBulletsViews()) {
                        if (bulletView != null) {
                            bulletView.getBulletLabel().setVisible(true);
                            bulletView.setScaleWidthBullet(bulletView.getRelativeWidth() * this.getWidth() / bulletView.getBulletIconWidth());
                            bulletView.setScaleHeightBullet(bulletView.getRelativeHeight() * this.getHeight() / bulletView.getBulletIconHeight());

                            if (bulletView.getBulletLabel().getParent() == null) {
                                bulletView.getBulletLabel().setVisible(true);

                                bulletView.setIcon("/view/resources/game/characters/" + otherPlayerView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                                this.add(bulletView.getBulletLabel());
                                this.revalidate();
                            }

                            bulletView.getBulletLabel().setLocation((int) (bulletView.getRelativeX() * this.getWidth()), (int) (bulletView.getRelativeY() * this.getHeight()));
                        }
                    }

                    otherPlayerView.setScaleWidthCharacter(otherPlayerView.getRelativeWidth() * this.getWidth() / otherPlayerView.getCharacterIconWidth());
                    otherPlayerView.setScaleHeightCharacter(otherPlayerView.getRelativeHeight() * this.getHeight() / otherPlayerView.getCharacterIconHeight());

                    otherPlayerView.getCharacterLabel().setVisible(true);

                    if (otherPlayerView.getCharacterLabel().getParent() == null) {
                        this.add(otherPlayerView.getCharacterLabel());
                        this.revalidate();
                    }

                    if (otherPlayerView.getHorizontalDirection() == -1) {
                        otherPlayerView.getCharacterLabel().setLocation((int) ((otherPlayerView.getRelativeX() - otherPlayerView.getRelativeWidth() / otherPlayerView.getScaleWidthCharacter() + otherPlayerView.getRelativeWidth()) * this.getWidth()), (int) ((otherPlayerView.getRelativeY()) * this.getHeight()));
                    } else
                        otherPlayerView.getCharacterLabel().setLocation((int) (otherPlayerView.getRelativeX() * this.getWidth()), (int) ((otherPlayerView.getRelativeY()) * this.getHeight()));
                }
            }
        }
        else {
            for (CharacterView otherPlayerView : otherPlayersViews) {
                if (otherPlayerView != null) {
                    otherPlayerView.getCharacterLabel().setVisible(false);

                    g.setColor(Color.red);
                    g.fillRect((int) (otherPlayerView.getRelativeX() * this.getWidth()), (int) ((otherPlayerView.getRelativeY()) * this.getHeight()), (int)(otherPlayerView.getRelativeWidth() * this.getWidth()), (int)(otherPlayerView.getRelativeHeight() * this.getHeight()));

                    for (BulletView bulletView : otherPlayerView.getBulletsViews()) {
                        if (bulletView != null) {
                            bulletView.getBulletLabel().setVisible(false);

                            g.setColor(Color.yellow);
                            g.fillRect((int) (bulletView.getRelativeX() * this.getWidth()), (int) (bulletView.getRelativeY() * this.getHeight()), (int) (bulletView.getRelativeWidth()*this.getWidth()), (int) (bulletView.getRelativeHeight()*this.getHeight()));
                        }
                    }
                }
            }

            if (characterView != null) {
                characterView.getCharacterLabel().setVisible(false);

                g.setColor(Color.blue);
                g.fillRect((int) (characterView.getRelativeX() * this.getWidth()), (int) ((characterView.getRelativeY()) * this.getHeight()), (int)(characterView.getRelativeWidth() * this.getWidth()), (int)(characterView.getRelativeHeight() * this.getHeight()));
            }

            for (BulletView bulletView : Objects.requireNonNull(characterView).getBulletsViews()) {
                if (bulletView != null) {
                    bulletView.getBulletLabel().setVisible(false);

                    g.setColor(new Color(188,0,255, 255));
                    g.fillRect((int) (bulletView.getRelativeX() * this.getWidth()), (int) (bulletView.getRelativeY() * this.getHeight()), (int) (bulletView.getRelativeWidth()*this.getWidth()), (int) (bulletView.getRelativeHeight()*this.getHeight()));
                }
            }
        }

        if (characterView.getNameLabel().getParent() == null) {
            this.add(characterView.getNameLabel());
            this.revalidate();
        }
        if (characterView.getLifeBarLabel().getParent() == null) {
            this.add(characterView.getLifeBarLabel());
            this.revalidate();
        }
        if (characterView.getUltimateReadyBarLabel().getParent() == null) {
            this.add(characterView.getUltimateReadyBarLabel());
            this.revalidate();
        }
        if (characterView.getUltimateBarLabel().getParent() == null) {
            this.add(characterView.getUltimateBarLabel());
            this.revalidate();
        }
        if (characterView.getUnderLifeBarLabel().getParent() == null) {
            this.add(characterView.getUnderLifeBarLabel());
            this.revalidate();
        }
        if (characterView.getUnderUltimateBarLabel().getParent() == null) {
            this.add(characterView.getUnderUltimateBarLabel());
            this.revalidate();
        }

        characterView.setScaleWidthName(0.024f * this.getWidth() / characterView.getNameIconWidth());
        characterView.setScaleHeightName(0.04f * this.getHeight() / characterView.getNameIconHeight());
        characterView.getNameLabel().setLocation((int) ((characterView.getRelativeX() + characterView.getRelativeWidth() / 2) * this.getWidth() - characterView.getNameIconWidth() * characterView.getScaleWidthName() / 2), (int) (characterView.getRelativeY() * this.getHeight() - 0.035f * this.getHeight() - characterView.getScaleHeightName() * characterView.getNameIconHeight()));

        characterView.setScaleWidthLifeBar(characterView.getRelativeWidth() * this.getWidth() / characterView.getLifeBarIconWidth() * characterView.getHealth());
        characterView.setScaleHeightLifeBar(0.01f * this.getHeight() / characterView.getLifeBarIconHeight());
        characterView.getLifeBarLabel().setLocation((int) (characterView.getRelativeX() * this.getWidth()), (int) (characterView.getRelativeY() * this.getHeight() - 0.015f * this.getHeight()));

        characterView.setScaleWidthUltimateBar(characterView.getRelativeWidth() * this.getWidth() / characterView.getUltimateBarIconWidth() * characterView.getUltimateLoading());
        characterView.setScaleHeightUltimateBar(0.01f * this.getHeight() / characterView.getUltimateBarIconHeight());

        if (characterView.getUltimateLoading() == 1) {
            characterView.getUltimateReadyBarLabel().setLocation((int) (characterView.getRelativeX() * this.getWidth()), (int) (characterView.getRelativeY() * this.getHeight() - 0.03f * this.getHeight()));
            characterView.getUltimateReadyBarLabel().setVisible(true);
            characterView.getUltimateBarLabel().setVisible(false);
        } else {
            characterView.getUltimateBarLabel().setLocation((int) (characterView.getRelativeX() * this.getWidth()), (int) (characterView.getRelativeY() * this.getHeight() - 0.03f * this.getHeight()));
            characterView.getUltimateBarLabel().setVisible(true);
            characterView.getUltimateReadyBarLabel().setVisible(false);
        }

        characterView.setScaleWidthUnderBar(characterView.getRelativeWidth() * this.getWidth() / characterView.getUnderBarIconWidth());
        characterView.setScaleHeightUnderBar(0.01f * this.getHeight() / characterView.getUnderBarIconHeight());
        characterView.getUnderLifeBarLabel().setLocation((int) (characterView.getRelativeX() * this.getWidth()), (int) (characterView.getRelativeY() * this.getHeight() - 0.015f * this.getHeight()));
        characterView.getUnderUltimateBarLabel().setLocation((int) (characterView.getRelativeX() * this.getWidth()), (int) (characterView.getRelativeY() * this.getHeight() - 0.03f * this.getHeight()));

        for (CharacterView otherPlayerView : otherPlayersViews) {
            if (otherPlayerView != null) {
                if (otherPlayerView.getNameLabel().getParent() == null) {
                    this.add(otherPlayerView.getNameLabel());
                    this.revalidate();
                }
                if (otherPlayerView.getLifeBarLabel().getParent() == null) {
                    this.add(otherPlayerView.getLifeBarLabel());
                    this.revalidate();
                }
                if (otherPlayerView.getUnderLifeBarLabel().getParent() == null) {
                    this.add(otherPlayerView.getUnderLifeBarLabel());
                    this.revalidate();
                }

                otherPlayerView.setScaleWidthName(0.024f * this.getWidth() / otherPlayerView.getNameIconWidth());
                otherPlayerView.setScaleHeightName(0.04f * this.getHeight() / otherPlayerView.getNameIconHeight());
                otherPlayerView.getNameLabel().setLocation((int) ((otherPlayerView.getRelativeX() + otherPlayerView.getRelativeWidth() / 2) * this.getWidth() - otherPlayerView.getNameIconWidth() * otherPlayerView.getScaleWidthName() / 2), (int) (otherPlayerView.getRelativeY() * this.getHeight() - 0.02f * this.getHeight() - otherPlayerView.getScaleHeightName() * otherPlayerView.getNameIconHeight()));

                otherPlayerView.setScaleWidthLifeBar(otherPlayerView.getRelativeWidth() * this.getWidth() / otherPlayerView.getLifeBarIconWidth() * otherPlayerView.getHealth());
                otherPlayerView.setScaleHeightLifeBar(0.01f * this.getHeight() / otherPlayerView.getLifeBarIconHeight());
                otherPlayerView.getLifeBarLabel().setLocation((int) (otherPlayerView.getRelativeX() * this.getWidth()), (int) (otherPlayerView.getRelativeY() * this.getHeight() - 0.015f * this.getHeight()));

                otherPlayerView.setScaleWidthUnderBar(otherPlayerView.getRelativeWidth() * this.getWidth() / otherPlayerView.getUnderBarIconWidth());
                otherPlayerView.setScaleHeightUnderBar(0.01f * this.getHeight() / otherPlayerView.getUnderBarIconHeight());
                otherPlayerView.getUnderLifeBarLabel().setLocation((int) (otherPlayerView.getRelativeX() * this.getWidth()), (int) (otherPlayerView.getRelativeY() * this.getHeight() - 0.015f * this.getHeight()));
            }
        }

        if (!hitBoxMode) {
            mapView.setScaleWidthMap(this.getWidth() / mapView.getMapIconWidth());
            mapView.setScaleHeightMap(this.getHeight() / mapView.getMapIconHeight());

            if (mapView.getMapLabel().getParent() == null) {
                this.add(mapView.getMapLabel());
                this.revalidate();
            }

            mapView.getMapLabel().setLocation(0, 0);

            mapView.getMapLabel().setVisible(true);
        } else {
            mapView.getMapLabel().setVisible(false);
        }
    }

    public void otherPlayersPainting(List<Character> otherPlayers) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < otherPlayers.size(); i++) {
                if (this.otherPlayersViews.size() > i) {
                    if (otherPlayers.get(i).isClassCharacterChanged()) {
                        for (BulletView bulletView : this.otherPlayersViews.get(otherPlayers.indexOf(otherPlayers.get(i))).getBulletsViews()) {
                            try {
                                bulletView.setIcon("/view/resources/game/characters/" + otherPlayers.get(i).getClassCharacter().name().toLowerCase() + "/bullet.png");
                            } catch (NullPointerException ex) {
                                System.err.println("Can't find \"/view/resources/game/characters/" + otherPlayers.get(i).getClassCharacter().name().toLowerCase() + "/bullet.png\" !");
                            }
                        }
                        this.otherPlayersViews.get(otherPlayers.indexOf(otherPlayers.get(i))).setClassCharacter(otherPlayers.get(i).getClassCharacter());

                        otherPlayers.get(i).setClassCharacterChanged(false);
                    }
                    this.otherPlayersViews.get(i).setInventory(otherPlayers.get(i).getInventory());
                    this.otherPlayersViews.get(i).setHealth(otherPlayers.get(i).getHealth());
                    this.otherPlayersViews.get(i).setHorizontalDirection(otherPlayers.get(i).getLastHorizontalDirection());
                    this.otherPlayersViews.get(i).setHorizontalDirection(otherPlayers.get(i).getHorizontalDirection());
                    this.otherPlayersViews.get(i).setUltimateLoading(otherPlayers.get(i).getUltimateLoading());

                    if (otherPlayers.get(i).isUltimate1Running() && !this.otherPlayersViews.get(i).isUltimate1Running()) {
                        this.otherPlayersViews.get(i).ultimate1();
                    } else if (otherPlayers.get(i).isUltimate2Running() && !this.otherPlayersViews.get(i).isUltimate2Running()) {
                        this.otherPlayersViews.get(i).ultimate2();
                    } else if (otherPlayers.get(i).isUltimate3Running() && !this.otherPlayersViews.get(i).isUltimate3Running()) {
                        this.otherPlayersViews.get(i).ultimate3();
                    } else if ((!otherPlayers.get(i).isUltimate3Running() && this.otherPlayersViews.get(i).isUltimate3Running())
                            || (!otherPlayers.get(i).isUltimate3Running() && !otherPlayers.get(i).isUltimate2Running() && this.otherPlayersViews.get(i).isUltimate2Running())
                            || (!otherPlayers.get(i).isUltimate3Running() && !otherPlayers.get(i).isUltimate2Running() && !otherPlayers.get(i).isUltimate1Running() && this.otherPlayersViews.get(i).isUltimate1Running())) {
                        this.otherPlayersViews.get(i).setClassCharacter(otherPlayers.get(i).getClassCharacter());
                    }

                    this.otherPlayersViews.get(i).setUltimate1Running(otherPlayers.get(i).isUltimate1Running());
                    this.otherPlayersViews.get(i).setUltimate2Running(otherPlayers.get(i).isUltimate2Running());
                    this.otherPlayersViews.get(i).setUltimate3Running(otherPlayers.get(i).isUltimate3Running());

                    this.otherPlayersViews.get(i).setRelativeX(otherPlayers.get(i).getRelativeX());
                    this.otherPlayersViews.get(i).setRelativeY(otherPlayers.get(i).getRelativeY());
                    this.otherPlayersViews.get(i).setRelativeWidth(otherPlayers.get(i).getRelativeWidth());
                    this.otherPlayersViews.get(i).setRelativeHeight(otherPlayers.get(i).getRelativeHeight());

                    try {
                        for (int j = 0; j < otherPlayers.get(i).getBullets().size(); j++) {
                            this.otherPlayersViews.get(i).getBulletsViews().get(j).setRelativeX(otherPlayers.get(i).getBullets().get(j).getRelativeX());
                            this.otherPlayersViews.get(i).getBulletsViews().get(j).setRelativeY(otherPlayers.get(i).getBullets().get(j).getRelativeY());
                            this.otherPlayersViews.get(i).getBulletsViews().get(j).setRelativeWidth(otherPlayers.get(i).getBullets().get(j).getRelativeWidth());
                            this.otherPlayersViews.get(i).getBulletsViews().get(j).setRelativeHeight(otherPlayers.get(i).getBullets().get(j).getRelativeHeight());
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    CharacterView characterView = new CharacterView(
                            otherPlayers.get(i).getRelativeX(),
                            otherPlayers.get(i).getRelativeY(),
                            otherPlayers.get(i).getRelativeWidth(),
                            otherPlayers.get(i).getRelativeHeight(),
                            otherPlayers.get(i).getName(),
                            otherPlayers.get(i).getClassCharacter(),
                            otherPlayers.get(i).getHealth());
                    for (int j = 0; j < Character.getMaxBulletNumberPerPlayer(); j++) {
                        characterView.getBulletsViews().add(new BulletView(0, 0, 0, 0));
                    }
                    characterView.setHorizontalDirection(otherPlayers.get(i).getLastHorizontalDirection());
                    this.otherPlayersViews.add(characterView);
                }
            }
            this.repaint();
        });
    }

    public void setPlatformsView(PlatformView[] platforms) {
        this.platforms = platforms;
    }

    public void setEachPlatformView(int i, PlatformView platformView) {
        this.platforms[i] = platformView;
    }

    public void setCharacterView(CharacterView characterView) {
        this.characterView = characterView;
    }

    public List<CharacterView> getOtherPlayersViews() {
        return otherPlayersViews;
    }

    public boolean isHitBoxMode() {
        return !hitBoxMode;
    }

    public void setHitBoxMode(boolean hitBoxMode) {
        this.hitBoxMode = hitBoxMode;
    }
}
package view.client.game_frame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePanel extends JPanel {
    private PlatformView[] platforms;
    private CharacterView characterView;
    private final List<CharacterView> otherPlayersViews = new ArrayList<>();
    private boolean hitBoxMode = false;

    GamePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!hitBoxMode) {
            g.setColor(Color.magenta);
            g.fillRect((int) (new HomeView().getRelativeX() * this.getWidth()),
                    (int) (new HomeView().getRelativeY() * this.getHeight()),
                    (int) (new HomeView().getRelativeWidth() * this.getWidth()),
                    (int) (new HomeView().getRelativeHeight() * this.getHeight()));

            for (PlatformView platform : platforms) {
                if (platform != null) {
                    g.setColor(Color.blue);
                    g.fillRect((int) (platform.getRelativeX() * this.getWidth()),
                            (int) (platform.getRelativeY() * this.getHeight()),
                            (int) (platform.getRelativeWidth() * this.getWidth()),
                            (int) (platform.getRelativeHeight() * this.getHeight()));
                }
            }

            characterView.setScaleWidthCharacter(characterView.getRelativeWidth() * this.getWidth() / characterView.getCharacterIconWidth());
            characterView.setScaleHeightCharacter(characterView.getRelativeHeight() * this.getHeight() / characterView.getCharacterIconHeight());

            if (characterView != null) {
                characterView.getCharacterLabel().setVisible(true);

                if (characterView.getCharacterLabel().getParent() == null) {
                    this.add(characterView.getCharacterLabel());
                    this.revalidate();
                }

                if (characterView.getHorizontal_direction() == -1) {
                    characterView.getCharacterLabel().setLocation((int) ((characterView.getRelativeX() - characterView.getRelativeWidth() / characterView.scaleWidthCharacter + characterView.getRelativeWidth()) * this.getWidth()), (int) ((characterView.getRelativeY()) * this.getHeight()));
                } else
                    characterView.getCharacterLabel().setLocation((int) (characterView.getRelativeX() * this.getWidth()), (int) ((characterView.getRelativeY()) * this.getHeight()));
            }

            for (BulletView bulletView : Objects.requireNonNull(characterView).getBulletsViews()) {
                if (bulletView != null) {
                    bulletView.getBulletLabel().setVisible(true);

                    bulletView.setScaleWidthBullet(bulletView.getRelativeWidth() * this.getWidth() / bulletView.getBulletIconWidth());
                    bulletView.setScaleHeightBullet(bulletView.getRelativeHeight() * this.getHeight() / bulletView.getBulletIconHeight());

                    if (bulletView.getBulletLabel().getParent() == null) {
                        try {
                            bulletView.setIcon("/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                        }
                        catch (NullPointerException e) {
                            System.err.println("Can't find \"/view/resources/game/characters/" + characterView.getClassCharacter().name().toLowerCase() + "/bullet.png\" !");
                        }                        this.add(bulletView.getBulletLabel());
                        this.revalidate();
                    }

                    bulletView.getBulletLabel().setLocation((int) (bulletView.getRelativeX() * this.getWidth()), (int) (bulletView.getRelativeY() * this.getHeight()));
                }
            }

            for (CharacterView otherPlayerView : otherPlayersViews) {
                if (otherPlayerView != null) {
                    for (BulletView bulletView : otherPlayerView.getBulletsViews()) {
                        if (bulletView != null) {
                            bulletView.setScaleWidthBullet(bulletView.getRelativeWidth() * this.getWidth() / bulletView.getBulletIconWidth());
                            bulletView.setScaleHeightBullet(bulletView.getRelativeHeight() * this.getHeight() / bulletView.getBulletIconHeight());

                            if (bulletView.getBulletLabel().getParent() == null) {
                                bulletView.getBulletLabel().setVisible(true);

                                try {
                                    bulletView.setIcon("/view/resources/game/characters/" + otherPlayerView.getClassCharacter().name().toLowerCase() + "/bullet.png");
                                }
                                catch (NullPointerException e) {
                                    System.err.println("Can't find \"/view/resources/game/characters/" + otherPlayerView.getClassCharacter().name().toLowerCase() + "/bullet.png\" !");
                                }

                                this.add(bulletView.getBulletLabel());
                                this.revalidate();
                            }

                            bulletView.getBulletLabel().setLocation((int) (bulletView.getRelativeX() * this.getWidth()), (int) (bulletView.getRelativeY() * this.getHeight()));
                        }
                    }
                    otherPlayerView.getCharacterLabel().setVisible(true);

                    otherPlayerView.setScaleWidthCharacter(otherPlayerView.getRelativeWidth() * this.getWidth() / otherPlayerView.getCharacterIconWidth());
                    otherPlayerView.setScaleHeightCharacter(otherPlayerView.getRelativeHeight() * this.getHeight() / otherPlayerView.getCharacterIconHeight());

                    if (otherPlayerView.getCharacterLabel().getParent() == null) {
                        this.add(otherPlayerView.getCharacterLabel());
                        this.revalidate();
                    }
                    if (otherPlayerView.getHorizontal_direction() == -1) {
                        otherPlayerView.getCharacterLabel().setLocation((int) ((otherPlayerView.getRelativeX() - otherPlayerView.getRelativeWidth() / otherPlayerView.scaleWidthCharacter + otherPlayerView.getRelativeWidth()) * this.getWidth()), (int) ((otherPlayerView.getRelativeY()) * this.getHeight()));
                    } else
                        otherPlayerView.getCharacterLabel().setLocation((int) (otherPlayerView.getRelativeX() * this.getWidth()), (int) ((otherPlayerView.getRelativeY()) * this.getHeight()));

                    g.setColor(new Color(255,0,28, 51));
                    g.fillRect((int)(otherPlayerView.getRelativeX()*this.getWidth()), (int)(otherPlayerView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*otherPlayerView.getHealth()*0.8f), (int)(0.01f*this.getHeight()));
                    g.setColor(new Color(255, 0, 28, 255));
                    g.drawRect((int)(otherPlayerView.getRelativeX()*this.getWidth()), (int)(otherPlayerView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*0.8f), (int)(0.01f*this.getHeight()));

                    if (otherPlayerView.getUltimateLoading() == 1) {
                        g.setColor(new Color(16, 255, 0, 255));
                        g.fillRect((int) (otherPlayerView.getRelativeX() * this.getWidth()) + (int) (otherPlayerView.getRelativeWidth() * this.getWidth() * 0.875f), (int) (otherPlayerView.getRelativeY() * this.getHeight() - 0.015f * this.getHeight()), (int) (otherPlayerView.getRelativeWidth() * this.getWidth() * 0.125f), (int) (0.01f * this.getHeight()));
                    }
                    g.setColor(new Color(0,153,255, 255));
                    g.drawRect((int)(otherPlayerView.getRelativeX()*this.getWidth()) + (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*0.875f), (int)(otherPlayerView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*0.125f), (int)(0.01f*this.getHeight()));
                }
            }
        }
        else {
            g.setColor(Color.magenta);
            g.fillRect((int) (new HomeView().getRelativeX() * this.getWidth()),
                    (int) (new HomeView().getRelativeY() * this.getHeight()),
                    (int) (new HomeView().getRelativeWidth() * this.getWidth()),
                    (int) (new HomeView().getRelativeHeight() * this.getHeight()));

            for (PlatformView platform : platforms) {
                if (platform != null) {
                    g.setColor(Color.blue);
                    g.fillRect((int) (platform.getRelativeX() * this.getWidth()),
                            (int) (platform.getRelativeY() * this.getHeight()),
                            (int) (platform.getRelativeWidth() * this.getWidth()),
                            (int) (platform.getRelativeHeight() * this.getHeight()));
                }
            }

            for (CharacterView otherPlayerView : otherPlayersViews) {
                if (otherPlayerView != null) {
                    otherPlayerView.getCharacterLabel().setVisible(false);

                    g.setColor(Color.red);
                    g.fillRect((int) (otherPlayerView.getRelativeX() * this.getWidth()), (int) ((otherPlayerView.getRelativeY()) * this.getHeight()), (int)(otherPlayerView.getRelativeWidth() * this.getWidth()), (int)(otherPlayerView.getRelativeHeight() * this.getHeight()));

                    g.setColor(new Color(255,0,28, 51));
                    g.fillRect((int)(otherPlayerView.getRelativeX()*this.getWidth()), (int)(otherPlayerView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*otherPlayerView.getHealth()*0.8f), (int)(0.01f*this.getHeight()));
                    g.setColor(new Color(255, 0, 28, 255));
                    g.drawRect((int)(otherPlayerView.getRelativeX()*this.getWidth()), (int)(otherPlayerView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*0.8f), (int)(0.01f*this.getHeight()));

                    if (otherPlayerView.getUltimateLoading() == 1) {
                        g.setColor(new Color(16, 255, 0, 255));
                        g.fillRect((int) (otherPlayerView.getRelativeX() * this.getWidth()) + (int) (otherPlayerView.getRelativeWidth() * this.getWidth() * 0.875f), (int) (otherPlayerView.getRelativeY() * this.getHeight() - 0.015f * this.getHeight()), (int) (otherPlayerView.getRelativeWidth() * this.getWidth() * 0.125f), (int) (0.01f * this.getHeight()));
                    }
                    g.setColor(new Color(0,153,255, 255));
                    g.drawRect((int)(otherPlayerView.getRelativeX()*this.getWidth()) + (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*0.875f), (int)(otherPlayerView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(otherPlayerView.getRelativeWidth()*this.getWidth()*0.125f), (int)(0.01f*this.getHeight()));

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

        g.setColor(new Color(255,0,28, 51));
        g.fillRect((int)(characterView.getRelativeX()*this.getWidth()), (int)(characterView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(characterView.getRelativeWidth()*this.getWidth()*characterView.getHealth()), (int)(0.01f*this.getHeight()));
        g.setColor(new Color(255, 0, 28, 255));
        g.drawRect((int)(characterView.getRelativeX()*this.getWidth()), (int)(characterView.getRelativeY()*this.getHeight() - 0.015f*this.getHeight()), (int)(characterView.getRelativeWidth()*this.getWidth()), (int)(0.01f*this.getHeight()));

        if (characterView.getUltimateLoading() == 1) {
            g.setColor(new Color(16, 255, 0, 51));
            g.fillRect((int)(characterView.getRelativeX()*this.getWidth()), (int)(characterView.getRelativeY()*this.getHeight() - 0.03f*this.getHeight()), (int)(characterView.getRelativeWidth()*this.getWidth() * characterView.getUltimateLoading()), (int)(0.01f*this.getHeight()));
            g.setColor(new Color(16, 255, 0, 255));
            g.drawRect((int)(characterView.getRelativeX()*this.getWidth()), (int)(characterView.getRelativeY()*this.getHeight() - 0.03f*this.getHeight()), (int)(characterView.getRelativeWidth()*this.getWidth()), (int)(0.01f*this.getHeight()));
        }
        else {
            g.setColor(new Color(0, 153, 255, 51));
            g.fillRect((int)(characterView.getRelativeX()*this.getWidth()), (int)(characterView.getRelativeY()*this.getHeight() - 0.03f*this.getHeight()), (int)(characterView.getRelativeWidth()*this.getWidth() * characterView.getUltimateLoading()), (int)(0.01f*this.getHeight()));
            g.setColor(new Color(0, 153, 255, 255));
            g.drawRect((int)(characterView.getRelativeX()*this.getWidth()), (int)(characterView.getRelativeY()*this.getHeight() - 0.03f*this.getHeight()), (int)(characterView.getRelativeWidth()*this.getWidth()), (int)(0.01f*this.getHeight()));
        }

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
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

    GamePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.magenta);
        g.fillRect((int)(new HomeView().getRelativeX()*this.getWidth()),
                (int)(new HomeView().getRelativeY()*this.getHeight()),
                (int)(new HomeView().getRelativeWidth()*this.getWidth()),
                (int)(new HomeView().getRelativeHeight()*this.getHeight()));

        for (PlatformView platform : platforms) {
            if (platform != null) {
                g.setColor(Color.blue);
                g.fillRect((int)(platform.getRelativeX()*this.getWidth()),
                        (int)(platform.getRelativeY()*this.getHeight()),
                        (int)(platform.getRelativeWidth()*this.getWidth()),
                        (int)(platform.getRelativeHeight()*this.getHeight()));
            }
        }

        for (CharacterView otherPlayerView : otherPlayersViews) {
            if (otherPlayerView != null) {
                otherPlayerView.setScaleWidthCharacter(otherPlayerView.getRelativeWidth()*this.getWidth()/200);
                otherPlayerView.setScaleHeightCharacter(otherPlayerView.getRelativeHeight()*this.getHeight()/200);
                
                if (otherPlayerView.getCharacterLabel().getParent() == null) {
                    this.add(otherPlayerView.getCharacterLabel());
                    this.revalidate();
                }
                if (otherPlayerView.getNameLabel().getParent() == null) {
                    this.add(otherPlayerView.getNameLabel());
                    this.revalidate();
                }
                if(otherPlayerView.getHorizontal_direction() == -1) {
                    otherPlayerView.getCharacterLabel().setLocation((int)((otherPlayerView.getRelativeX() - otherPlayerView.getRelativeWidth() / otherPlayerView.scaleWidthCharacter + otherPlayerView.getRelativeWidth()) * this.getWidth()), (int) ((otherPlayerView.getRelativeY()) * this.getHeight()));
                }
                else
                    otherPlayerView.getCharacterLabel().setLocation((int)(otherPlayerView.getRelativeX()*this.getWidth()),(int)((otherPlayerView.getRelativeY())*this.getHeight()));

                otherPlayerView.getNameLabel().setLocation((int)((otherPlayerView.getRelativeX()+otherPlayerView.getRelativeWidth()/2)*this.getWidth()-otherPlayerView.getNameLabel().getWidth()/2), (int)((otherPlayerView.getRelativeY()+otherPlayerView.getRelativeHeight()/2)*this.getHeight())-otherPlayerView.getNameLabel().getHeight()/2);
            }
        }

        characterView.setScaleWidthCharacter(characterView.getRelativeWidth()*this.getWidth()/200);
        characterView.setScaleHeightCharacter(characterView.getRelativeHeight()*this.getHeight()/200);
        characterView.getNameLabel().setLocation((int)((characterView.getRelativeX()+characterView.getRelativeWidth()/2)*this.getWidth()-characterView.getNameLabel().getWidth()/2), (int)((characterView.getRelativeY()+characterView.getRelativeHeight()/2)*this.getHeight())-characterView.getNameLabel().getHeight()/2);

        if (characterView != null) {
            if (characterView.getCharacterLabel().getParent() == null) {
                this.add(characterView.getCharacterLabel());
            }

            if (characterView.getNameLabel().getParent() == null)
                this.add(characterView.getNameLabel());

            if(characterView.getHorizontal_direction() == -1) {
                characterView.getCharacterLabel().setLocation((int)((characterView.getRelativeX() - characterView.getRelativeWidth() / characterView.scaleWidthCharacter + characterView.getRelativeWidth()) * this.getWidth()), (int) ((characterView.getRelativeY()) * this.getHeight()));
            }
            else
                characterView.getCharacterLabel().setLocation((int)(characterView.getRelativeX()*this.getWidth()),(int)((characterView.getRelativeY())*this.getHeight()));
        }

        for (BulletView bulletView : Objects.requireNonNull(characterView).getBulletsViews()) {
            if (bulletView != null) {
                bulletView.setScaleWidthBullet(bulletView.getRelativeWidth()*this.getWidth()/200);
                bulletView.setScaleHeightBullet(bulletView.getRelativeHeight()*this.getHeight()/200);

                if (bulletView.getBulletLabel().getParent() == null) {
                    bulletView.setIcon();
                    this.add(bulletView.getBulletLabel());
                    this.revalidate();
                }

                bulletView.getBulletLabel().setLocation((int)(bulletView.getRelativeX()*this.getWidth()), (int)(bulletView.getRelativeY()*this.getHeight()));
            }
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
}
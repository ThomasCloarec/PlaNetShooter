package view.client.home_frame;

import model.characters.ClassCharacters;
import model.characters.PlayableCharacter;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HomePanel extends JPanel {
    private final JLabel nameLabel = new JLabel();
    private final JLabel arrowLabel = new JLabel();
    private final JLabel characterNameLabel = new JLabel();
    private final JButton changeCharacterButton = new JButton();
    private final JLabel characterLabel = new JLabel();
    private final JButton playButton = new JButton();
    private final List<PlayableCharacter> otherPlayersHome = new ArrayList<>();

    private final JPanel leftPanel = new JPanel(new GridLayout(8, 5));
    private final JPanel centerPanel = new JPanel(null);
    private final JPanel rightPanel = new JPanel(null);

    public HomePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.setLayout(null);

        arrowLabel.setIcon(new ArrowIcon("/view/resources/home/arrow.png"));
        changeCharacterButton.setIcon(new ChangeCharacterIcon("/view/resources/home/buttons/change_character.png"));
        changeCharacterButton.setFocusPainted(false);
        playButton.setFocusPainted(false);
        playButton.setIcon(new PlayIcon("/view/resources/home/buttons/play.png"));

        leftPanel.setBackground(Color.gray);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(new ArrayIcon("/view/resources/game/names/P_unknown.png"));
        jLabel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel);

        JLabel jLabel2 = new JLabel();
        jLabel2.setIcon(new ArrayIcon("/view/resources/home/faces/mystery_face.png"));
        jLabel2.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel2);

        JLabel jLabel3 = new JLabel();
        jLabel3.setIcon(new ArrayIcon("/view/resources/home/kill.png"));
        jLabel3.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel3);

        JLabel jLabel4 = new JLabel();
        jLabel4.setIcon(new ArrayIcon("/view/resources/home/death.png"));
        jLabel4.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel4);

        JLabel jLabel5 = new JLabel();
        jLabel5.setIcon(new ArrayIcon("/view/resources/home/money.png"));
        jLabel5.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, Color.ORANGE));
        leftPanel.add(jLabel5);

        for (int i = 0; i < 35; i++) {
            leftPanel.add(new JLabel());
        }

        this.add(leftPanel);

        centerPanel.setBackground(Color.gray);
        centerPanel.add(nameLabel);
        centerPanel.add(arrowLabel);
        centerPanel.add(characterNameLabel);
        centerPanel.add(changeCharacterButton);
        centerPanel.add(characterLabel);
        centerPanel.add(playButton);
        this.add(centerPanel);

        rightPanel.setBackground(Color.gray);
        this.add(rightPanel);
   }

    private void drawCenterPanel(int marginX, int marginY) {
        nameLabel.setBounds(0, 0, (int) (1f / 4f * centerPanel.getWidth()), (int) (1f / 8f * centerPanel.getHeight()));
        arrowLabel.setBounds((int) (1f / 4f * centerPanel.getWidth() + marginX), marginY * 2, (int) (1f / 4f * centerPanel.getWidth() - marginX * 2), (int) (1f / 8f * centerPanel.getHeight() - marginY * 4));
        characterNameLabel.setBounds((int) (2f / 4f * centerPanel.getWidth()), 0, (int) (2f / 4f * centerPanel.getWidth()), (int) (1f / 8f * centerPanel.getHeight()));
        changeCharacterButton.setBounds(0, (int) (1f / 8f * centerPanel.getHeight()), centerPanel.getWidth(), (int) (1f / 8f * centerPanel.getHeight()));
        characterLabel.setBounds(0, (int) (1f / 4f * centerPanel.getHeight() + marginY), centerPanel.getWidth(), (int) (2f / 4f * centerPanel.getHeight() - marginY));
        playButton.setBounds(0, marginY + centerPanel.getHeight() - (int) (1f / 4f * centerPanel.getHeight()), centerPanel.getWidth(), (int) (1f / 4f * centerPanel.getHeight()) - marginY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int marginX = (int) (0.02 * this.getWidth() * 372f / 768f);
        int marginY = (int) (0.02 * this.getHeight());

        leftPanel.setBounds(marginX, marginY, (int) (1f / 3f * this.getWidth()) - marginX, this.getHeight() - marginY * 2);
        leftPanel.doLayout();
        drawLeftPanel();

        centerPanel.setBounds(marginX + leftPanel.getX() + leftPanel.getWidth(), marginY, (int) (1f / 3f * this.getWidth()) - marginX, this.getHeight() - marginY * 2);
        drawCenterPanel(marginX, marginY);

        rightPanel.setBounds(marginX + centerPanel.getX() + centerPanel.getWidth(), marginY, (int) (1f / 3f * this.getWidth()) - marginX * 2, this.getHeight() - marginY * 2);
        drawRightPanel();
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        characterNameLabel.setIcon(new CharacterNameIcon("/view/resources/home/labels/" + classCharacter.name().toLowerCase() + "_label.png"));
        characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/idle.gif"));
        ((JLabel) leftPanel.getComponent(6)).setIcon(new ArrayIcon("/view/resources/home/faces/" + classCharacter.name().toLowerCase() + "_face.png"));
        ((JLabel) leftPanel.getComponent(6)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
    }

    public void setPlayerName(String playerName) {
        nameLabel.setIcon(new NameIcon("/view/resources/game/names/" + playerName + ".png"));
        ((JLabel) leftPanel.getComponent(5)).setIcon(new ArrayIcon("/view/resources/game/names/" + playerName + ".png"));
        ((JLabel) leftPanel.getComponent(5)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 2, 0, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
    }

    public void setPlayerValues(PlayableCharacter playableCharacter) {
        ((JLabel) leftPanel.getComponent(7)).setText(Integer.toString(playableCharacter.getKills()));
        ((JLabel) leftPanel.getComponent(7)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));

        ((JLabel) leftPanel.getComponent(8)).setText(Integer.toString(playableCharacter.getDeaths()));
        ((JLabel) leftPanel.getComponent(8)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));

        ((JLabel) leftPanel.getComponent(9)).setText(Integer.toString(playableCharacter.getMoney()));
        ((JLabel) leftPanel.getComponent(9)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 2, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK)));
    }

    public void refreshHome(List<PlayableCharacter> otherPlayers) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < otherPlayers.size(); i++) {
                if (this.otherPlayersHome.size() > i) {
                    if (!otherPlayersHome.get(i).getClassCharacter().equals(otherPlayers.get(i).getClassCharacter())) {
                        otherPlayersHome.get(i).setClassCharacter(otherPlayers.get(i).getClassCharacter());
                        ((JLabel) leftPanel.getComponent(11 + i * 5)).setIcon(new ArrayIcon("/view/resources/home/faces/" + otherPlayers.get(i).getClassCharacter().name().toLowerCase() + "_face.png"));
                        ((JLabel) leftPanel.getComponent(12 + i * 5)).setText(Integer.toString(otherPlayers.get(i).getKills()));
                        ((JLabel) leftPanel.getComponent(13 + i * 5)).setText(Integer.toString(otherPlayers.get(i).getDeaths()));
                        ((JLabel) leftPanel.getComponent(14 + i * 5)).setText(Integer.toString(otherPlayers.get(i).getMoney()));
                    }
                } else {
                    if (i < 5 ) {
                        PlayableCharacter playableCharacter = new PlayableCharacter();
                        playableCharacter.setName(otherPlayers.get(i).getName());
                        playableCharacter.setClassCharacter(otherPlayers.get(i).getClassCharacter());
                        this.otherPlayersHome.add(playableCharacter);

                        ((JLabel) leftPanel.getComponent(10 + i * 5)).setIcon(new ArrayIcon("/view/resources/game/names/" + playableCharacter.getName() + ".png"));
                        ((JLabel) leftPanel.getComponent(11 + i * 5)).setIcon(new ArrayIcon("/view/resources/home/faces/" + playableCharacter.getClassCharacter().name().toLowerCase() + "_face.png"));
                        if (i == 0) {
                            ((JLabel) leftPanel.getComponent(10 + i * 5)).setBorder(BorderFactory.createMatteBorder(2,2,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(11 + i * 5)).setBorder(BorderFactory.createMatteBorder(2,0,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(12 + i * 5)).setBorder(BorderFactory.createMatteBorder(2,0,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(13 + i * 5)).setBorder(BorderFactory.createMatteBorder(2,0,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(14 + i * 5)).setBorder(BorderFactory.createMatteBorder(2,0,2,2,Color.BLACK));
                        }
                        else {
                            ((JLabel) leftPanel.getComponent(10 + i * 5)).setBorder(BorderFactory.createMatteBorder(0,2,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(11 + i * 5)).setBorder(BorderFactory.createMatteBorder(0,0,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(12 + i * 5)).setBorder(BorderFactory.createMatteBorder(0,0,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(13 + i * 5)).setBorder(BorderFactory.createMatteBorder(0,0,2,1,Color.BLACK));
                            ((JLabel) leftPanel.getComponent(14 + i * 5)).setBorder(BorderFactory.createMatteBorder(0,0,2,2,Color.BLACK));
                        }

                        ((JLabel) leftPanel.getComponent(12 + i * 5)).setText("0");
                        ((JLabel) leftPanel.getComponent(13 + i * 5)).setText("0");
                        ((JLabel) leftPanel.getComponent(14 + i * 5)).setText("0");
                    }
                }
            }
            this.repaint();
        });
    }

    public void removeOtherPlayerHome(int index) {
        for (int i = 0; i < 5 - index; i++) {
            if ( 5 - index != 0) {
                ((JLabel) leftPanel.getComponent(10 + (index + i) * 5)).setIcon(((JLabel) leftPanel.getComponent(10 + (index + i + 1) * 5)).getIcon());
                ((JLabel) leftPanel.getComponent(11 + (index + i) * 5)).setIcon(((JLabel) leftPanel.getComponent(11 + (index + i + 1) * 5)).getIcon());
                if (((JLabel) leftPanel.getComponent(10 + (index + i + 1) * 5)).getBorder() == null && ((JLabel) leftPanel.getComponent(11 + (index + i + 1) * 5)).getBorder() == null) {
                    ((JLabel) leftPanel.getComponent(10 + (index + i) * 5)).setIcon(null);
                    ((JLabel) leftPanel.getComponent(10 + (index + i) * 5)).setBorder(null);
                    ((JLabel) leftPanel.getComponent(11 + (index + i) * 5)).setIcon(null);
                    ((JLabel) leftPanel.getComponent(11 + (index + i) * 5)).setBorder(null);
                    ((JLabel) leftPanel.getComponent(12 + (index + i) * 5)).setText(null);
                    ((JLabel) leftPanel.getComponent(12 + (index + i) * 5)).setBorder(null);
                    ((JLabel) leftPanel.getComponent(13 + (index + i) * 5)).setText(null);
                    ((JLabel) leftPanel.getComponent(13 + (index + i) * 5)).setBorder(null);
                    ((JLabel) leftPanel.getComponent(14 + (index + i) * 5)).setText(null);
                    ((JLabel) leftPanel.getComponent(14 + (index + i) * 5)).setBorder(null);
                }
                else {
                    ((JLabel) leftPanel.getComponent(12 + (index + i) * 5)).setText(((JLabel) leftPanel.getComponent(12 + (index + i + 1) * 5)).getText());
                    ((JLabel) leftPanel.getComponent(13 + (index + i) * 5)).setText(((JLabel) leftPanel.getComponent(13 + (index + i + 1) * 5)).getText());
                    ((JLabel) leftPanel.getComponent(14 + (index + i) * 5)).setText(((JLabel) leftPanel.getComponent(14 + (index + i + 1) * 5)).getText());
                }
            }
        }
        otherPlayersHome.remove(index);
    }

    private class CharacterIcon extends ImageIcon {
        private int characterIconWidth;
        private int characterIconHeight;
        CharacterIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            characterIconWidth = this.getIconWidth();
            characterIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float)(characterLabel.getWidth()) / (float)(characterIconWidth), (float)(characterLabel.getHeight()) / (float)(characterIconHeight));
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }

    private class NameIcon extends ImageIcon {
        private int nameIconWidth;
        private int nameIconHeight;
        NameIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            this.nameIconWidth = this.getIconWidth();
            this.nameIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float) (nameLabel.getWidth()) / (float) (nameIconWidth), (float) (nameLabel.getHeight()) / (float) (nameIconHeight));
            x = 0;
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }

    private class ArrayIcon extends ImageIcon {
        private int arrayIconWidth;
        private int arrayIconHeight;

        ArrayIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            this.arrayIconWidth = this.getIconWidth();
            this.arrayIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float) (leftPanel.getComponent(0).getWidth()) / (float) (arrayIconWidth), (float) (leftPanel.getComponent(0).getHeight()) / (float) (arrayIconHeight));
            x = 0;
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }

    @SuppressWarnings("EmptyMethod")
    private void drawLeftPanel() {
    }

    public JButton getPlayButton() {
        return playButton;
    }

    @SuppressWarnings("EmptyMethod")
    private void drawRightPanel() {
    }

    public JButton getChangeCharacterButton() {
        return changeCharacterButton;
    }

    private class ArrowIcon extends ImageIcon {
        private int arrowIconWidth;
        private int arrowIconHeight;
        ArrowIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            arrowIconWidth = this.getIconWidth();
            arrowIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float) (arrowLabel.getWidth()) / (float) (arrowIconWidth), (float) (arrowLabel.getHeight()) / (float) (arrowIconHeight));
            x = 0;
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }

    private class CharacterNameIcon extends ImageIcon {
        private int characterNameIconWidth;
        private int characterNameIconHeight;
        CharacterNameIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            characterNameIconWidth = this.getIconWidth();
            characterNameIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float) (characterNameLabel.getWidth()) / (float) (characterNameIconWidth), (float) (characterNameLabel.getHeight()) / (float) (characterNameIconHeight));
            x = 0;
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }

    private class ChangeCharacterIcon extends ImageIcon {
        private int changeCharacterIconWidth;
        private int changeCharacterIconHeight;
        ChangeCharacterIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            changeCharacterIconWidth = this.getIconWidth();
            changeCharacterIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float) (changeCharacterButton.getWidth()) / (float) (changeCharacterIconWidth), (float) (changeCharacterButton.getHeight()) / (float) (changeCharacterIconHeight));
            x = 0;
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }

    private class PlayIcon extends ImageIcon {
        private int playIconWidth;
        private int playIconHeight;
        PlayIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            playIconWidth = this.getIconWidth();
            playIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float) (playButton.getWidth()) / (float) (playIconWidth), (float) (playButton.getHeight()) / (float) (playIconHeight));
            x = 0;
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }
}
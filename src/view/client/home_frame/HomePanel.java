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

        arrowLabel.setIcon(new AdaptableIconToLabel("/view/resources/home/arrow.png", arrowLabel));
        changeCharacterButton.setIcon(new AdaptableIconToButton("/view/resources/home/buttons/change_character.png", changeCharacterButton));
        changeCharacterButton.setFocusPainted(false);
        playButton.setFocusPainted(false);
        playButton.setIcon(new AdaptableIconToButton("/view/resources/home/buttons/play.png", playButton));

        leftPanel.setBackground(Color.gray);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(new AdaptableIconToLabel("/view/resources/game/names/P_unknown.png", jLabel));
        jLabel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel);

        JLabel jLabel2 = new JLabel();
        jLabel2.setIcon(new AdaptableIconToLabel("/view/resources/home/faces/mystery_face.png", jLabel2));
        jLabel2.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel2);

        JLabel jLabel3 = new JLabel();
        jLabel3.setIcon(new AdaptableIconToLabel("/view/resources/home/kill.png", jLabel3));
        jLabel3.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel3);

        JLabel jLabel4 = new JLabel();
        jLabel4.setIcon(new AdaptableIconToLabel("/view/resources/home/death.png", jLabel4));
        jLabel4.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel4);

        JLabel jLabel5 = new JLabel();
        jLabel5.setIcon(new AdaptableIconToLabel("/view/resources/home/money.png", jLabel5));
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
        characterNameLabel.setIcon(new AdaptableIconToLabel("/view/resources/home/labels/" + classCharacter.name().toLowerCase() + "_label.png", characterNameLabel));
        characterLabel.setIcon(new AdaptableIconToLabel("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/idle.gif", characterLabel));
        ((JLabel) leftPanel.getComponent(6)).setIcon(new AdaptableIconToLabel("/view/resources/home/faces/" + classCharacter.name().toLowerCase() + "_face.png", (JLabel) leftPanel.getComponent(6)));
        ((JLabel) leftPanel.getComponent(6)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
    }

    public void setPlayerName(String playerName) {
        nameLabel.setIcon(new AdaptableIconToLabel("/view/resources/game/names/" + playerName + ".png", nameLabel));
        ((JLabel) leftPanel.getComponent(5)).setIcon(new AdaptableIconToLabel("/view/resources/game/names/" + playerName + ".png", (JLabel) leftPanel.getComponent(5)));
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
                        ((JLabel) leftPanel.getComponent(11 + i * 5)).setIcon(new AdaptableIconToLabel("/view/resources/home/faces/" + otherPlayers.get(i).getClassCharacter().name().toLowerCase() + "_face.png", (JLabel) (leftPanel.getComponent(11 + i * 5))));
                    }
                    ((JLabel) leftPanel.getComponent(12 + i * 5)).setText(Integer.toString(otherPlayers.get(i).getKills()));
                    ((JLabel) leftPanel.getComponent(13 + i * 5)).setText(Integer.toString(otherPlayers.get(i).getDeaths()));
                    ((JLabel) leftPanel.getComponent(14 + i * 5)).setText(Integer.toString(otherPlayers.get(i).getMoney()));
                } else {
                    if (i < 5 ) {
                        PlayableCharacter playableCharacter = new PlayableCharacter();
                        playableCharacter.setName(otherPlayers.get(i).getName());
                        playableCharacter.setClassCharacter(otherPlayers.get(i).getClassCharacter());
                        this.otherPlayersHome.add(playableCharacter);

                        ((JLabel) leftPanel.getComponent(10 + i * 5)).setIcon(new AdaptableIconToLabel("/view/resources/game/names/" + playableCharacter.getName() + ".png", (JLabel) (leftPanel.getComponent(10 + i * 5))));
                        ((JLabel) leftPanel.getComponent(11 + i * 5)).setIcon(new AdaptableIconToLabel("/view/resources/home/faces/" + playableCharacter.getClassCharacter().name().toLowerCase() + "_face.png", (JLabel) (leftPanel.getComponent(11 + i * 5))));
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

    private class AdaptableIconToLabel extends ImageIcon {
        private int characterIconWidth;
        private int characterIconHeight;
        private JLabel label;
        AdaptableIconToLabel(String filename, JLabel label) {
            super(HomePanel.class.getResource(filename));
            characterIconWidth = this.getIconWidth();
            characterIconHeight = this.getIconHeight();
            this.label = label;
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float)(this.label.getWidth()) / (float)(characterIconWidth), (float)(this.label.getHeight()) / (float)(characterIconHeight));
            x = 0;
            y = 0;
            super.paintIcon(c, g2, x, y);
        }
    }

    private class AdaptableIconToButton extends ImageIcon {
        private int characterIconWidth;
        private int characterIconHeight;
        private JButton button;
        AdaptableIconToButton(String filename, JButton button) {
            super(HomePanel.class.getResource(filename));
            characterIconWidth = this.getIconWidth();
            characterIconHeight = this.getIconHeight();
            this.button = button;
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale((float)(this.button.getWidth()) / (float)(characterIconWidth), (float)(this.button.getHeight()) / (float)(characterIconHeight));
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
}
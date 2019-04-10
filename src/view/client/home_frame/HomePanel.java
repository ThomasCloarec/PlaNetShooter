package view.client.home_frame;

import model.characters.ClassCharacters;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private final JLabel nameLabel = new JLabel();
    private final JLabel arrowLabel = new JLabel();
    private final JLabel characterNameLabel = new JLabel();
    private final JButton changeCharacterButton = new JButton();
    private final JLabel characterLabel = new JLabel();
    private final JButton playButton = new JButton();

    private final JPanel leftPanel = new JPanel(null);
    private final JPanel centerPanel = new JPanel(null);
    private final JPanel rightPanel = new JPanel(null);

    private int nameIconWidth;
    private int nameIconHeight;
    private int arrowIconWidth;
    private int arrowIconHeight;
    private int characterNameIconWidth;
    private int characterNameIconHeight;
    private int changeCharacterIconWidth;
    private int changeCharacterIconHeight;
    private int characterIconWidth;
    private int characterIconHeight;
    private int playIconWidth;
    private int playIconHeight;

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
        drawLeftPanel();

        centerPanel.setBounds(marginX + leftPanel.getX() + leftPanel.getWidth(), marginY, (int) (1f / 3f * this.getWidth()) - marginX, this.getHeight() - marginY * 2);
        drawCenterPanel(marginX, marginY);

        rightPanel.setBounds(marginX + centerPanel.getX() + centerPanel.getWidth(), marginY, (int) (1f / 3f * this.getWidth()) - marginX * 2, this.getHeight() - marginY * 2);
        drawRightPanel();
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        characterNameLabel.setIcon(new CharacterNameIcon("/view/resources/home/" + classCharacter.name().toLowerCase() + "_label.png"));
        characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/idle.gif"));
    }

    public void setPlayerName(String playerName) {
        nameLabel.setIcon(new NameIcon("/view/resources/game/names/" + playerName + ".png"));
    }

    private class CharacterIcon extends ImageIcon {
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
        NameIcon(String filename) {
            super(HomePanel.class.getResource(filename));
            nameIconWidth = this.getIconWidth();
            nameIconHeight = this.getIconHeight();
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

    private class ArrowIcon extends ImageIcon {
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

    private class ChangeCharacterIcon extends ImageIcon {
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

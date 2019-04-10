package view.client.home_frame;

import model.characters.ClassCharacters;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private final JButton changeCharacterButton = new JButton("Change character");
    private final JLabel characterLabel = new JLabel();
    private final JButton playButton = new JButton();

    private final JPanel leftPanel = new JPanel(null);
    private final JPanel centerPanel = new JPanel(null);
    private final JPanel rightPanel = new JPanel(null);

    private int characterIconWidth;
    private int characterIconHeight;
    private int playIconWidth;
    private int playIconHeight;

    public HomePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.setLayout(null);

        changeCharacterButton.setFocusPainted(false);
        playButton.setFocusPainted(false);
        playButton.setIcon(new PlayIcon("/view/resources/home/buttons/play.png"));

        leftPanel.setBackground(Color.gray);
        this.add(leftPanel);

        centerPanel.setBackground(Color.gray);
        centerPanel.add(changeCharacterButton);
        centerPanel.add(characterLabel);
        centerPanel.add(playButton);
        this.add(centerPanel);

        rightPanel.setBackground(Color.gray);
        this.add(rightPanel);

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

    private void drawCenterPanel(int marginY) {
        changeCharacterButton.setBounds(0, 0, centerPanel.getWidth(), (int) (1f / 4f * centerPanel.getHeight()) - marginY);
        characterLabel.setBounds(0, (int) (1f / 4f * centerPanel.getHeight()), centerPanel.getWidth(), (int) (2f / 4f * centerPanel.getHeight()));
        playButton.setBounds(0, marginY + centerPanel.getHeight() - (int) (1f / 4f * centerPanel.getHeight()), centerPanel.getWidth(), (int) (1f / 4f * centerPanel.getHeight()) - marginY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int marginX = (int)(0.02 * this.getWidth() * 372f / 768f);
        int marginY = (int)(0.02 * this.getHeight());

        leftPanel.setBounds(marginX, marginY, (int)(1f / 3f * this.getWidth()) - marginX, this.getHeight() - marginY * 2);
        drawLeftPanel();

        centerPanel.setBounds(marginX + leftPanel.getX() + leftPanel.getWidth(), marginY, (int)(1f / 3f * this.getWidth()) - marginX, this.getHeight() - marginY * 2);
        drawCenterPanel(marginY);

        rightPanel.setBounds(marginX + centerPanel.getX() + centerPanel.getWidth(), marginY, (int)(1f / 3f * this.getWidth()) - marginX * 2, this.getHeight() - marginY * 2);
        drawRightPanel();
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

    public JButton getChangeCharacterButton() {
        return changeCharacterButton;
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/idle.gif"));
    }
}

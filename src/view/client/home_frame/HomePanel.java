package view.client.home_frame;

import model.characters.ClassCharacters;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private final JButton changeCharacterButton = new JButton("Change character");
    private JLabel characterLabel = new JLabel();
    private final JButton backToGameButton = new JButton("Back to game");

    private final JPanel leftPanel = new JPanel(null);
    private final JPanel centerPanel = new JPanel(null);
    private final JPanel rightPanel = new JPanel(null);

    private int characterIconWidth;
    private int characterIconHeight;

    public HomePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.setLayout(null);

        changeCharacterButton.setFocusPainted(false);
        backToGameButton.setFocusPainted(false);

        leftPanel.setBackground(Color.gray);
        this.add(leftPanel);

        centerPanel.setBackground(Color.gray);
        centerPanel.add(changeCharacterButton);
        centerPanel.add(characterLabel);
        centerPanel.add(backToGameButton);
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

    private void drawLeftPanel() {
    }

    private void drawCenterPanel(int marginY) {
        changeCharacterButton.setBounds(0, 0, centerPanel.getWidth(), (int)(1f / 4f * centerPanel.getHeight()) - marginY);
        characterLabel.setBounds(0, (int)(1f / 4f * centerPanel.getHeight()), centerPanel.getWidth(), (int)(2f / 4f * centerPanel.getHeight()));
        backToGameButton.setBounds(0, marginY + centerPanel.getHeight() - (int)(1f / 4f * centerPanel.getHeight()), centerPanel.getWidth(), (int)(1f / 4f * centerPanel.getHeight()) - marginY);
    }

    private void drawRightPanel() {
    }

    public JButton getBackToGameButton() {
        return backToGameButton;
    }

    public JButton getChangeCharacterButton() {
        return changeCharacterButton;
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        try {
            characterLabel.setIcon(new CharacterIcon("/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/idle.gif"));
        }
        catch (NullPointerException e) {
            System.err.println("Can't find \"/view/resources/game/characters/" +classCharacter.name().toLowerCase()+ "/idle.gif\" !");
        }
    }
}

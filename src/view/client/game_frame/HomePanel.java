package view.client.game_frame;

import model.characters.Character;
import model.characters.ClassCharacters;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomePanel extends JPanel {
    private final List<Integer> removeOthersPlayersHomeIndex = new ArrayList<>();

    private final JLabel nameLabel = new JLabel();
    private final JLabel arrowLabel = new JLabel();
    private final JLabel characterNameLabel = new JLabel();
    private final JButton changeCharacterButton = new JButton();
    private final JLabel characterLabel = new JLabel();
    private final JButton playButton = new JButton();
    private final Character characterHome = new Character();
    private final List<Character> otherPlayersHome = new ArrayList<>();

    private final JPanel leftPanel = new JPanel(new GridLayout(8, 5));
    private final JPanel centerPanel = new JPanel(null);
    private final JPanel rightPanel = new JPanel(null);

    HomePanel() {
        super();
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.setLayout(null);

        characterHome.setKills(-1);
        characterHome.setDeaths(-1);
        characterHome.setMoney(-1);

        arrowLabel.setIcon(new AdaptableIcon("/view/resources/home/arrow.png", arrowLabel));
        changeCharacterButton.setIcon(new AdaptableIcon("/view/resources/home/buttons/change_character.png", changeCharacterButton));
        changeCharacterButton.setFocusPainted(false);
        playButton.setFocusPainted(false);
        playButton.setIcon(new AdaptableIcon("/view/resources/home/buttons/play.png", playButton));

        leftPanel.setBackground(Color.gray);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(new AdaptableIcon("/view/resources/game/names/P_unknown.png", jLabel));
        jLabel.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel);

        JLabel jLabel2 = new JLabel();
        jLabel2.setIcon(new AdaptableIcon("/view/resources/home/faces/mystery_face.png", jLabel2));
        jLabel2.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel2);

        JLabel jLabel3 = new JLabel();
        jLabel3.setIcon(new AdaptableIcon("/view/resources/home/kill.png", jLabel3));
        jLabel3.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel3);

        JLabel jLabel4 = new JLabel();
        jLabel4.setIcon(new AdaptableIcon("/view/resources/home/death.png", jLabel4));
        jLabel4.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.ORANGE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        leftPanel.add(jLabel4);

        JLabel jLabel5 = new JLabel();
        jLabel5.setIcon(new AdaptableIcon("/view/resources/home/money.png", jLabel5));
        jLabel5.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, Color.ORANGE));
        leftPanel.add(jLabel5);

        for (int i = 0; i < 35; i++) {
            //noinspection ConstantConditions
            if (i % 5 - 2 == 0 || i % 5 - 3 == 0 || i % 5 - 4 == 0) {
                JPanel jPanel = new JPanel(new GridLayout(1,3));
                jPanel.setBackground(Color.gray);
                for (int j = 0; j < 3; j++) {
                    jPanel.add(new JLabel());
                }

                leftPanel.add(jPanel);
            }
            else {
                leftPanel.add(new JLabel());
            }
        }

        ((JPanel) leftPanel.getComponent(7)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLUE),
                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
        ((JPanel) leftPanel.getComponent(8)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLUE),
                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
        ((JPanel) leftPanel.getComponent(9)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 2, Color.BLUE),
                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));

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
        for (int i = 0; i < leftPanel.getComponents().length; i++) {
            leftPanel.getComponent(i).doLayout();
        }
        drawLeftPanel();

        centerPanel.setBounds(marginX + leftPanel.getX() + leftPanel.getWidth(), marginY, (int) (1f / 3f * this.getWidth()) - marginX, this.getHeight() - marginY * 2);
        drawCenterPanel(marginX, marginY);

        rightPanel.setBounds(marginX + centerPanel.getX() + centerPanel.getWidth(), marginY, (int) (1f / 3f * this.getWidth()) - marginX * 2, this.getHeight() - marginY * 2);
        drawRightPanel();
    }

    public void setClassCharacter(ClassCharacters classCharacter) {
        characterNameLabel.setIcon(new AdaptableIcon("/view/resources/home/labels/" + classCharacter.name().toLowerCase() + "_label.png", characterNameLabel));
        characterLabel.setIcon(new AdaptableIcon("/view/resources/game/characters/" + classCharacter.name().toLowerCase() + "/idle.gif", characterLabel));
        ((JLabel) leftPanel.getComponent(6)).setIcon(new AdaptableIcon("/view/resources/home/faces/" + classCharacter.name().toLowerCase() + "_face.png", (JLabel) leftPanel.getComponent(6)));
        ((JLabel) leftPanel.getComponent(6)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        this.repaint();
    }

    public void setPlayerName(String playerName) {
        nameLabel.setIcon(new AdaptableIcon("/view/resources/game/names/" + playerName + ".png", nameLabel));
        ((JLabel) leftPanel.getComponent(5)).setIcon(new AdaptableIcon("/view/resources/game/names/" + playerName + ".png", (JLabel) leftPanel.getComponent(5)));
        ((JLabel) leftPanel.getComponent(5)).setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 2, 0, Color.BLUE),
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)));
        this.repaint();
    }

    public void setPlayerValues(Character character) {
        if (characterHome.getKills() != character.getKills()) {
            if (Integer.toString(character.getKills()).length() == 3) {
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getKills()).charAt(2) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getKills()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getKills()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(0))));
            } else if (Integer.toString(character.getKills()).length() == 2) {
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getKills()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getKills()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(0))));
            } else if (Integer.toString(character.getKills()).length() == 1) {
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getKills()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(0))));
            }
            characterHome.setKills(character.getKills());
            this.repaint();
        }

        if (characterHome.getDeaths() != character.getDeaths()) {
            if (Integer.toString(character.getDeaths()).length() == 3) {
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getDeaths()).charAt(2) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getDeaths()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getDeaths()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(0))));
            } else if (Integer.toString(character.getDeaths()).length() == 2) {
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getDeaths()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getDeaths()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(0))));
            } else if (Integer.toString(character.getDeaths()).length() == 1) {
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getDeaths()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(8)).getComponent(0))));
            }
            characterHome.setDeaths(character.getDeaths());
            this.repaint();
        }

        if (characterHome.getMoney() != character.getMoney()) {
            if (Integer.toString(character.getMoney()).length() == 3) {
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getMoney()).charAt(2) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getMoney()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getMoney()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(0))));
            } else if (Integer.toString(character.getMoney()).length() == 2) {
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getMoney()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getMoney()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(0))));
            } else if (Integer.toString(character.getMoney()).length() == 1) {
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(character.getMoney()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(2))));
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(1))));
                ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(9)).getComponent(0))));
            }
            characterHome.setMoney(character.getMoney());
            this.repaint();
        }
    }

    public void refreshHome(List<Character> otherPlayers) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < otherPlayers.size(); i++) {
                if (this.otherPlayersHome.size() > i) {
                    if (!otherPlayersHome.get(i).getClassCharacter().equals(otherPlayers.get(i).getClassCharacter())) {
                        otherPlayersHome.get(i).setClassCharacter(otherPlayers.get(i).getClassCharacter());
                        ((JLabel) leftPanel.getComponent(11 + i * 5)).setIcon(new AdaptableIcon("/view/resources/home/faces/" + otherPlayers.get(i).getClassCharacter().name().toLowerCase() + "_face.png", (JLabel) (leftPanel.getComponent(11 + i * 5))));
                        this.repaint();
                    }

                    if (otherPlayersHome.get(i).getKills() != otherPlayers.get(i).getKills()) {
                        if (Integer.toString(otherPlayers.get(i).getKills()).length() == 3) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getKills()).charAt(2) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getKills()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getKills()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(0))));
                        }
                        else if (Integer.toString(otherPlayers.get(i).getKills()).length() == 2) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getKills()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getKills()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(0))));
                        }
                        else if (Integer.toString(otherPlayers.get(i).getKills()).length() == 1) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getKills()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(7)).getComponent(0))));
                        }
                        otherPlayersHome.get(i).setKills(otherPlayers.get(i).getKills());
                        this.repaint();
                    }

                    if (otherPlayersHome.get(i).getDeaths() != otherPlayers.get(i).getDeaths()) {
                        if (Integer.toString(otherPlayers.get(i).getDeaths()).length() == 3) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getDeaths()).charAt(2) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getDeaths()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getDeaths()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(0))));
                        } else if (Integer.toString(otherPlayers.get(i).getDeaths()).length() == 2) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getDeaths()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getDeaths()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(0))));
                        } else if (Integer.toString(otherPlayers.get(i).getDeaths()).length() == 1) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getDeaths()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(13 + i * 5)).getComponent(0))));
                        }
                        otherPlayersHome.get(i).setDeaths(otherPlayers.get(i).getDeaths());
                        this.repaint();
                    }

                    if (otherPlayersHome.get(i).getMoney() != otherPlayers.get(i).getMoney()) {
                        if (Integer.toString(otherPlayers.get(i).getMoney()).length() == 3) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getMoney()).charAt(2) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getMoney()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getMoney()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(0))));
                        } else if (Integer.toString(otherPlayers.get(i).getMoney()).length() == 2) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getMoney()).charAt(1) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getMoney()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(0))));
                        } else if (Integer.toString(otherPlayers.get(i).getMoney()).length() == 1) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(2)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + Integer.toString(otherPlayers.get(i).getMoney()).charAt(0) + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(2))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(1)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(1))));
                            ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(0)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(14 + i * 5)).getComponent(0))));
                        }
                        otherPlayersHome.get(i).setMoney(otherPlayers.get(i).getMoney());
                        this.repaint();
                    }
                } else if (i < 5) {
                    Character character = new Character();
                    character.setName(otherPlayers.get(i).getName());
                    character.setClassCharacter(otherPlayers.get(i).getClassCharacter());
                    this.otherPlayersHome.add(character);

                    ((JLabel) leftPanel.getComponent(10 + i * 5)).setIcon(new AdaptableIcon("/view/resources/game/names/" + character.getName() + ".png", (JLabel) (leftPanel.getComponent(10 + i * 5))));
                    ((JLabel) leftPanel.getComponent(11 + i * 5)).setIcon(new AdaptableIcon("/view/resources/home/faces/" + character.getClassCharacter().name().toLowerCase() + "_face.png", (JLabel) (leftPanel.getComponent(11 + i * 5))));

                    if (i == 0) {
                        ((JLabel) leftPanel.getComponent(10 + i * 5)).setBorder(BorderFactory.createMatteBorder(2, 2, 2, 1, Color.BLACK));
                        ((JLabel) leftPanel.getComponent(11 + i * 5)).setBorder(BorderFactory.createMatteBorder(2, 0, 2, 1, Color.BLACK));
                        ((JPanel) leftPanel.getComponent(12 + i * 5)).setBorder(new CompoundBorder(
                                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLACK),
                                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
                        ((JPanel) leftPanel.getComponent(13 + i * 5)).setBorder(new CompoundBorder(
                                BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLACK),
                                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
                        ((JPanel) leftPanel.getComponent(14 + i * 5)).setBorder(new CompoundBorder(
                                BorderFactory.createMatteBorder(2, 0, 2, 2, Color.BLACK),
                                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
                    } else {
                        ((JLabel) leftPanel.getComponent(10 + i * 5)).setBorder(BorderFactory.createMatteBorder(0, 2, 2, 1, Color.BLACK));
                        ((JLabel) leftPanel.getComponent(11 + i * 5)).setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.BLACK));
                        ((JPanel) leftPanel.getComponent(12 + i * 5)).setBorder(new CompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK),
                                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
                        ((JPanel) leftPanel.getComponent(13 + i * 5)).setBorder(new CompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK),
                                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
                        ((JPanel) leftPanel.getComponent(14 + i * 5)).setBorder(new CompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK),
                                BorderFactory.createDashedBorder(null, 2, 1, 1, false)));
                    }

                    //  launch kills/deaths/money
                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + j + i * 5)).getComponent(k)).setIcon(new AdaptableIcon("/view/resources/home/numbers/" + 0 + ".png", ((JLabel) ((JPanel) leftPanel.getComponent(12 + j + i * 5)).getComponent(k))));
                        }
                    }
                    this.repaint();
                }
            }

            Iterator<Integer> integerIterator = removeOthersPlayersHomeIndex.iterator();
            while(integerIterator.hasNext()) {
                removeOtherPlayerHome(integerIterator.next());
                integerIterator.remove();
            }
        });
    }

    private void removeOtherPlayerHome(int index) {
        for (int i = 0; i < 5 - index; i++) {
            if ( 5 - index != 0) {
                if (((JLabel) leftPanel.getComponent(10 + (index + i + 1) * 5)).getBorder() == null && ((JLabel) leftPanel.getComponent(11 + (index + i + 1) * 5)).getBorder() == null) {

                    ((JLabel) leftPanel.getComponent(10 + (index + i) * 5)).setIcon(null);
                    ((JLabel) leftPanel.getComponent(10 + (index + i) * 5)).setBorder(null);
                    ((JLabel) leftPanel.getComponent(11 + (index + i) * 5)).setIcon(null);
                    ((JLabel) leftPanel.getComponent(11 + (index + i) * 5)).setBorder(null);

                    for (int j = 0; j < 3; j++) {
                        ((JPanel) leftPanel.getComponent(12 + j + (index + i) * 5)).setBorder(null);

                        for (int k = 0; k < 3; k++) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + k + (index + i) * 5)).getComponent(j)).setIcon(null);
                        }
                    }
                }
                else {
                    ((JLabel) leftPanel.getComponent(10 + (index + i) * 5)).setIcon(((JLabel) leftPanel.getComponent(10 + (index + i + 1) * 5)).getIcon());
                    ((JLabel) leftPanel.getComponent(11 + (index + i) * 5)).setIcon(((JLabel) leftPanel.getComponent(11 + (index + i + 1) * 5)).getIcon());

                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {
                            ((JLabel) ((JPanel) leftPanel.getComponent(12 + k + (index + i) * 5)).getComponent(j)).setIcon(((JLabel) ((JPanel) leftPanel.getComponent(12 + k + (index + i + 1) * 5)).getComponent(j)).getIcon());
                        }
                    }
                }
            }
        }
        otherPlayersHome.remove(index);
        this.repaint();
    }

    private class AdaptableIcon extends ImageIcon {
        private final int characterIconWidth;
        private final int characterIconHeight;
        private JLabel label;
        private JButton button;

        AdaptableIcon(String filename, JLabel label) {
            super(HomePanel.class.getResource(filename));
            characterIconWidth = this.getIconWidth();
            characterIconHeight = this.getIconHeight();
            this.label = label;
        }

        AdaptableIcon(String filename, JButton button) {
            super(HomePanel.class.getResource(filename));
            characterIconWidth = this.getIconWidth();
            characterIconHeight = this.getIconHeight();
            this.button = button;
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (this.label != null)
                g2.scale((float)(this.label.getWidth()) / (float)(characterIconWidth), (float)(this.label.getHeight()) / (float)(characterIconHeight));
            else
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

    public List<Integer> getRemoveOthersPlayersHomeIndex() {
        return removeOthersPlayersHomeIndex;
    }
}
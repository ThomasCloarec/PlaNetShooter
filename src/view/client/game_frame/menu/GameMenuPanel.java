package view.client.game_frame.menu;

import javax.swing.*;
import java.awt.*;

public class GameMenuPanel extends JPanel {
    private final JButton jButton = new JButton("Button");

    public GameMenuPanel() {
        super();
        this.setLayout(new GridLayout(1, 5));
        jButton.setFocusable(false);
        jButton.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.darkGray));
        jButton.setBackground(new Color(150,150,150));
        this.add(jButton);
        for (int i = 0; i < 4; i++)
            this.add(new JLabel());
        this.setBorder(BorderFactory.createMatteBorder(4, 8, 4, 8, Color.lightGray));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.gray);
        graphics.fillRoundRect(2, 2, this.getWidth()-4, this.getHeight()-4, 15, 15);//paint background
        graphics.setColor(Color.black);
        graphics.drawRoundRect(2, 2, this.getWidth()-4, this.getHeight()-4, 15, 15);//paint border
        jButton.repaint();
    }
}
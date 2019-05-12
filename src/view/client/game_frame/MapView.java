package view.client.game_frame;

import javax.swing.*;
import java.awt.*;

class MapView {
    private final JLabel mapLabel = new JLabel();
    private double scaleWidthMap = 0;
    private double scaleHeightMap = 0;
    private float mapIconWidth;
    private float mapIconHeight;

    MapView() {
    }

    private class MapIcon extends ImageIcon {
        MapIcon(String filename) {
            super(BulletView.class.getResource(filename));
            mapIconWidth = this.getIconWidth();
            mapIconHeight = this.getIconHeight();
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.scale(scaleWidthMap, scaleHeightMap);
            super.paintIcon(c, g2, x, y);
        }
    }

    void setScaleWidthMap(double scaleWidthMap) {
        this.scaleWidthMap = scaleWidthMap;
    }

    void setScaleHeightMap(double scaleHeightMap) {
        this.scaleHeightMap = scaleHeightMap;
    }

    JLabel getMapLabel() {
        return mapLabel;
    }

    void setIcon(@SuppressWarnings("SameParameterValue") String filename) {
        mapLabel.setIcon(new MapView.MapIcon(filename));
    }

    float getMapIconWidth() {
        return mapIconWidth;
    }

    float getMapIconHeight() {
        return mapIconHeight;
    }
}

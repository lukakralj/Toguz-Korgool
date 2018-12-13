package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TiledPanel extends JPanel {
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int HALFSIES = 2;

    private static BufferedImage darkImg;
    private static BufferedImage lightImg;

    private BufferedImage image;

    public TiledPanel(int color) {
        if (darkImg == null || lightImg == null) {
            loadBackgroundImage();
        }
        if (color == BLACK) {
            image = darkImg;
        }
        else if (color == WHITE) {
            image = lightImg;
        }
        else if (color == HALFSIES) {
            image = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        if (image != null) {

            int imageW = image.getWidth(this);
            int imageH = darkImg.getHeight(this);

            // Tile the image to fill all area.
            for (int x = 0; x < w; x += imageW) {
                for (int y = 0; y < h; y += imageH) {
                    g.drawImage(image, x, y, null);
                }
            }
        }
        else {
            // Tile the white image on the right.
            for (int x = w/2; x < w; x += lightImg.getWidth(this)) {
                for (int y = 0; y < h; y += lightImg.getHeight(this)) {
                    g.drawImage(lightImg, x, y, null);
                }
            }

            // Tile the white image on the right.
            if (darkImg.getWidth(this) > w) {
                for (int y = 0; y < h; y += darkImg.getHeight(this)) {
                    g.drawImage(darkImg, w/2 - darkImg.getWidth(this), y, null);
                }
            }
            else {
                for (int x = w/2 - darkImg.getWidth(this); x > -w/2; x -= darkImg.getWidth(this)) {
                    for (int y = 0; y < h; y += darkImg.getHeight(this)) {
                        g.drawImage(darkImg, x, y, null);
                    }
                }
            }
        }
    }

    private static void loadBackgroundImage() {
        try {
            darkImg = ImageIO.read(new File("src/main/resources/dark.jpg"));
            lightImg = ImageIO.read(new File("src/main/resources/light.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

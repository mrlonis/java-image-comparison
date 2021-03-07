package com.mrlonis.dto;

import com.mrlonis.utils.Constants;
import com.mrlonis.utils.Util;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.Data;

@Data
public class Image {

    private BufferedImage image;
    private int width, height;
    private String name;

    public Image(String filename) {
        try {
            String[] words = filename.split("/");
            int last = words.length - 1;
            name = words[last].substring(0, words[last].indexOf("."));
            image = ImageIO.read(new File(filename));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            System.out.println("com.mrlonis.dto.Image could not be read: " + filename);
            System.exit(1);
        }
    }

    public Image(Image source) {
        this.width = source.width;
        this.height = source.height;
        this.name = source.name;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = this.image.getGraphics();
        g.drawImage(source.image, 0, 0, null);
        g.dispose();
    }

    public Image quantize(int bitsPerChannel) {
        Image copy = new Image(this);
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) { copy.setColor(x, y, Util.unpack(Util.pack(getColor(x, y), bitsPerChannel), bitsPerChannel)); }
		}
        return copy;
    }

    public Color getColor(int x,
                          int y) {
        try {
            return new Color(image.getRGB(x, y));
        } catch (RuntimeException e) {
            return Color.WHITE;
        }
    }

    public void setColor(int x,
                         int y,
                         Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    public void draw(Graphics gr) {
        gr.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        Image image = new Image(Constants.IMAGE_DIR + "/davinci.jpg");
        System.out.println("corner color: " + image.getColor(0, 0));
    }

}

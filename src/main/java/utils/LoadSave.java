package utils;

import main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class LoadSave {

    public static final  String PLAYER_ATLAS = "Punk.png";
    public static final  String LEVEL_ATLAS = "outside_sprites.png";
    public static final  String LEVEL_ONE_DATA = "map.png";

    public static BufferedImage GetSpriteAtlas(String filename){
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        try {
            img = ImageIO.read(is);

        } catch (IOException e) {
            try {
                is.close();
            } catch (IOException ex) {
                e.printStackTrace();
            }
            e.printStackTrace();
        }
        return img;
    }
    public static int[][] GetLevelData(){
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);

        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48)
                    value = 15;
                lvlData[j][i] = value;
            }
        return lvlData;
    }
}

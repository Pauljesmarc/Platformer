package org.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tiles{

    int type;
    private Image image;
    private ImageView imageView;
    private Rectangle hitBox;
    int x,y,h,w;

    public ImageView getImageView() {
        return imageView;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Tiles(int x, int y, int h, int w, int type) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.type = type;

        switch (type){
            case 1:
                image = new Image("IndustrialTile_59.png");
                break;

            case 2:
                image = new Image("mysteryQ.png");
                break;

            case 3:
                image = new Image("hint.png");
                break;

        }

        imageView = new ImageView(image);

        hitBox = new Rectangle(w,h);
        hitBox.setTranslateX(x);
        hitBox.setTranslateY(y);

        hitBox.setFill(Color.RED);
        hitBox.getProperties().put("alive",true);

        imageView.setFitHeight(60);
        imageView.setFitWidth(60);

        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        imageView.getProperties().put("alive",true);

        hitBox.xProperty().bindBidirectional(imageView.xProperty());
        hitBox.yProperty().bindBidirectional(imageView.yProperty());
    }
}

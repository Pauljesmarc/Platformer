package org.example.demo;


import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Entity extends Node{
    protected int x , y, w, h;
    protected Color color;

    public Entity(int x, int y, int w, int h,javafx.scene.paint.Color color) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
    }

    public Node getEntityAsNode() {

        Rectangle entity = new Rectangle(w,h);

        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive",true);
        return entity;
    }



}


package org.example.demo;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainApp extends Application {


    private HashMap<KeyCode, Boolean>keys = new HashMap<>();
    private ArrayList<Node> platfomrs = new ArrayList<>();
    private ArrayList<Node> coins = new ArrayList<>();
    private ArrayList<Node> hints = new ArrayList<>();


    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();


    private Node player;
    private Point2D playervelocity = new Point2D(0,0);
    private boolean canjump = true;

    private int levelwidht;

    private boolean dialogEvent = false;
    private static boolean runniing = true;

    private void initcontent(){
        Rectangle bg = new Rectangle(1280,720);

        levelwidht  = LevelData.LEVEL_ONE[0].length() * 60;

        for (int i = 0; i < LevelData.LEVEL_ONE.length; i++) {
            String line = LevelData.LEVEL_ONE[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createentity(j*60,i*60,60,60,Color.BROWN);
                        platfomrs.add(platform);
                        break;
                    case '2':
                        Node coin = createentity(j*60,i*60,60,60,Color.GOLD);
                        coins.add(coin);
                        break;
                    case '3':
                        Node hint = createentity(j*60,i*60,60,60,Color.GREEN);
                        hints.add(hint);
                }
            }
        }

        player = createentity(0,600,40,40,Color.BLUE);

        player.translateXProperty().addListener((obs,old,newvalue)-> {
            int offset = newvalue.intValue();
            if(offset > 640 && offset < levelwidht - 640){
                gameRoot.setLayoutX(-(offset - 640));
            }
        });


        appRoot.getChildren().addAll(bg,gameRoot,uiRoot);
    }
    private void update(){
        if(ispressed(KeyCode.W) && player.getTranslateY()>=5){
            jumpplayer();
        }
        if(ispressed(KeyCode.A) && player.getTranslateX() >=5){
            movePlayerX(-5);
        }
        if(ispressed(KeyCode.D) && player.getTranslateX() +40  <= levelwidht-5){
            movePlayerX(5);
        }
        if(playervelocity.getY()<10){
            playervelocity = playervelocity.add(0,1);

        }
        movePlayerY((int)playervelocity.getY());

        for(Node coin : coins){
            if(player.getBoundsInParent().intersects(coin.getBoundsInParent())){
                coin.getProperties().put("alive",false);
                dialogEvent = true;
                runniing = false;
            }
        }

        for(Iterator<Node> it = coins.iterator(); it.hasNext();){
            Node coin = it.next();
            if(!(Boolean)coin.getProperties().get("alive")){
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }

    }
    private void movePlayerX(int value){
        boolean movingright = value>0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platfomrs) {
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingright){
                        if(player.getTranslateX()+40 == platform.getTranslateX()){
                            return;
                        }
                    }
                    else {
                        if(player.getTranslateX() == platform.getTranslateX() + 60){
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingright ? 1: -1));
        }
    }
    private void movePlayerY(int value){
        boolean movingdown = value>0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platfomrs) {
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingdown){
                        if(player.getTranslateY()+40 == platform.getTranslateY()){
                            player.setTranslateY(player.getTranslateY() - 1);
                            canjump = true;
                            return;
                        }
                    }
                    else {
                        if(player.getTranslateY() == platform.getTranslateY() + 60){
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingdown ? 1: -1));
        }

    }

    private void jumpplayer(){
        if(canjump){
            playervelocity = playervelocity.add(0,-30);
            canjump = false;
        }
    }

    private Node createentity(int x, int y, int w,int h,Color color){

        Rectangle entity = new Rectangle(w,h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive",true);

        gameRoot.getChildren().add(entity);
        return entity;
    }
    private boolean ispressed(KeyCode key){
        return keys.getOrDefault(key,false);

    }

    @Override
    public void start(Stage stage) throws Exception {
        initcontent();

        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(),true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(),false));
        stage.setTitle("QuizRun");
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(runniing){
                    update();
                }
                if(dialogEvent){
                    dialogEvent = false;
                    keys.keySet().forEach(key -> keys.put(key,false));

                    GameDialog dialog = new GameDialog();

                    dialog.setOnCloseRequest(event->{
                        if(dialog.isCorrect()){
                            System.out.println("Correct");
                        }else {
                            System.out.println("wrong");
                        }
                        runniing = true;
                    });
                    dialog.open();
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {

        launch();
    }

    public static void setRunniing(boolean runniing) {
        MainApp.runniing = runniing;
    }
}
package org.example.demo;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainApp extends Application implements Runnable{

    private HashMap<KeyCode, Boolean>keys = new HashMap<>();
    private ArrayList<Node> platfomrs = new ArrayList<>();
    private ArrayList<Node> coins = new ArrayList<>();
    private ArrayList<Node> hints = new ArrayList<>();

    //appRoot = main game window
    private Pane appRoot = new Pane();
    //gameRoot = game scene
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private GameDialog dialog = new GameDialog();

    public static int hintPoints=0;
    public static int score =0;

    public static Label hintPointsTxt = new Label();
    public static Label scoreTxt =  new Label();


    private Node player = new Player(30,600,40,40,Color.BLUE).getEntityAsNode();

    private Point2D playervelocity = new Point2D(0,0);
    private boolean canjump = true;

    private int levelwidht;

    private boolean dialogEvent = false;
    private static boolean running = true;
    private Node tile;


    private Thread gameThread = new Thread(this);

    private void initcontent(){
        Image bgk = new Image("Picasso-Room.jpg");
        ImageView bg = new ImageView(bgk);
        int n = LevelData.LEVEL_ONE.length * 60;
        System.out.println(n);
        bg.setFitHeight(LevelData.LEVEL_ONE.length*60);
        bg.setFitWidth(1280);

        hintPointsTxt.setText(String.valueOf(hintPoints));
        hintPointsTxt.setPrefHeight(hintPointsTxt.getFont().getSize());
        hintPointsTxt.setPrefWidth(hintPointsTxt.getFont().getSize());
        hintPointsTxt.setTextFill(Color.GREEN);
        hintPointsTxt.setLayoutX(30);
        hintPointsTxt.setLayoutY(30);

        scoreTxt.setText(String.valueOf(score));
        scoreTxt.setPrefHeight(scoreTxt.getFont().getSize());
        scoreTxt.setPrefWidth(scoreTxt.getFont().getSize());
        scoreTxt.setTextFill(Color.GREEN);
        scoreTxt.setLayoutX(30);
        scoreTxt.setLayoutY(60);

        levelwidht  = LevelData.LEVEL_ONE[0].length() * 60;

        //adding tiles platform
        for (int i = 0; i < LevelData.LEVEL_ONE.length; i++) {
            String line = LevelData.LEVEL_ONE[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':

                        tile = new Tiles(j*60,i*60,60,60,Color.BROWN).getEntityAsNode();
                        platfomrs.add(tile);
                        gameRoot.getChildren().add(tile);
                        break;
                    case '2':

                        tile = new Tiles(j*60,i*60,60,60,Color.GOLD).getEntityAsNode();
                        coins.add(tile);
                        gameRoot.getChildren().add(tile);
                        break;
                    case '3':

                        tile = new Tiles(j*60,i*60,60,60,Color.GREEN).getEntityAsNode();
                        hints.add(tile);
                        gameRoot.getChildren().add(tile);
                        break;
                }
            }
        }
        //adding player entity
        gameRoot.getChildren().addAll(player);

        //for moving map
        player.translateXProperty().addListener((obs,old,newvalue)-> {
            int offset = newvalue.intValue();
            if(offset > 640 && offset < levelwidht - 640){
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        //adding all Node to main game window
        appRoot.getChildren().addAll(bg,gameRoot,uiRoot,scoreTxt,hintPointsTxt);
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
                if(ispressed(KeyCode.E)){
                    dialogEvent = true;
                    if(dialog.isCorrect()){
                        gameRoot.getChildren().remove(coin);
                        coin.getProperties().put("alive",false);
                    }
                    running = false;
                }

            }
        }

        for(Iterator<Node> it = coins.iterator(); it.hasNext();){
            Node coin = it.next();
            if(dialog.isCorrect()) {
                if (!(Boolean) coin.getProperties().get("alive")) {
                    it.remove();
                    gameRoot.getChildren().remove(coin);
                }
            }
        }

        for(Node hint : hints){
            if(player.getBoundsInParent().intersects(hint.getBoundsInParent())){
                hint.getProperties().put("alive",false);
                hintPoints++;
                hintPointsTxt.setText(String.valueOf(hintPoints));
            }
        }

        for(Iterator<Node> it = hints.iterator(); it.hasNext();){
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

    private boolean ispressed(KeyCode key){
        return keys.getOrDefault(key,false);
    }

    @Override
    public void start(Stage stage) throws Exception {

        initcontent();

        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(),true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(),false));
        stage.setScene(scene);
        stage.show();
        gameThread.start();


    }

    public static void main(String[] args) {
        launch();
    }

    public static void setRunning(boolean running) {
        MainApp.running = running;
    }


    @Override
    public void run() {

        //makes the game responsive

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(running){
                    update();
                }
                if(dialogEvent){
                    dialogEvent = false;
                    keys.keySet().forEach(key -> keys.put(key,false));

                    dialog.setOnCloseRequest(event->{
                        if(dialog.isCorrect()){
                            System.out.println("Correct");
                        }else {
                            System.out.println("wrong");
                        }
                        running = true;
                    });
                    dialog.open();
                }

            }
        };
        timer.start();
    }
}
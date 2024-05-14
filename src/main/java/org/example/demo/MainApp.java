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
    private ArrayList<Node> mysteryQ = new ArrayList<>();
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

    private Player player = new Player(30,600,40,40);
    private Point2D playervelocity = new Point2D(0,0);
    private boolean canjump = true;

    private int levelwidht;

    private boolean dialogEvent = false;
    private static boolean running = true;
    private Tiles tile;


    private Thread gameThread = new Thread(this);

    private void initcontent(){
        Image bgk = new Image("Picasso-Room.jpg");
        ImageView bg = new ImageView(bgk);

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

                        tile = new Tiles(j*60,i*60,60,60,1);
                        platfomrs.add(tile.getHitBox());
                        gameRoot.getChildren().addAll(tile.getHitBox(),tile.getImageView());
                        break;
                    case '2':

                        tile = new Tiles(j*60,i*60,60,60,2);

                        mysteryQ.add(tile.getHitBox());
                        mysteryQ.add(tile.getImageView());

                        gameRoot.getChildren().addAll(tile.getHitBox(),tile.getImageView());
                        break;
                    case '3':

                        tile = new Tiles(j*60,i*60,60,60,3);

                        hints.add(tile.getHitBox());
                        hints.add(tile.getImageView());
                        gameRoot.getChildren().addAll(tile.getHitBox(),tile.getImageView());
                        break;
                }
            }
        }

        //adding player entity
        gameRoot.getChildren().addAll(player.getHitBox(),player.getImage());

        //for moving map
        player.getHitBox().translateXProperty().addListener((obs,old,newvalue)-> {
            int offset = newvalue.intValue();
            if(offset > 640 && offset < levelwidht - 640){
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        //adding all Node to main game window
        appRoot.getChildren().addAll(bg,gameRoot,uiRoot,scoreTxt,hintPointsTxt);
    }
    private void update(){
        if(ispressed(KeyCode.W) && player.getHitBox().getTranslateY()>=5){

            jumpplayer();
        }
        if(ispressed(KeyCode.A) && player.getHitBox().getTranslateX() >=5){
            movePlayerX(-5);
        }
        if(ispressed(KeyCode.D) && player.getHitBox().getTranslateX() +40  <= levelwidht-5){
            movePlayerX(5);
        }
        if(playervelocity.getY()<10){
            playervelocity = playervelocity.add(0,1);

        }

        movePlayerY((int)playervelocity.getY());

        for(Node coin : mysteryQ){
            if(player.getHitBox().getBoundsInParent().intersects(coin.getBoundsInParent())){
                if(ispressed(KeyCode.E)){
                    if((boolean) coin.getProperties().get("alive")) {
                        dialogEvent = true;
                        running = false;
                    }
                }
                if(dialog.isCorrect()){
                    gameRoot.getChildren().remove(coin);
                    gameRoot.getChildren().remove(tile.getImageView());
                    coin.getProperties().put("alive",false);


                }

            }
        }
        dialog.setCorrect(false);

        for(Iterator<Node> it = mysteryQ.iterator(); it.hasNext();){
            Node coin = it.next();
            if(dialog.isCorrect()) {
                if (!(Boolean) coin.getProperties().get("alive")) {
                    it.remove();
                    gameRoot.getChildren().remove(coin);
                }
            }
        }

        for(Node hint : hints){
            if(player.getHitBox().getBoundsInParent().intersects(hint.getBoundsInParent())){
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
                if(player.getHitBox().getBoundsInParent().intersects(platform.getBoundsInParent())){

                    if(movingright){
                        if(player.getHitBox().getTranslateX()+40 == platform.getTranslateX()){
                            return;
                        }
                    }
                    else {
                        if(player.getHitBox().getTranslateX() == platform.getTranslateX() + 60){
                            return;
                        }
                    }
                }
            }
            player.getHitBox().setTranslateX(player.getHitBox().getTranslateX() + (movingright ? 1: -1));
            player.getImage().setTranslateX(player.getImage().getTranslateX() + (movingright ? 1: -1));
        }
    }


    private void movePlayerY(int value){
        boolean movingdown = value>0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platfomrs) {
                if(player.getHitBox().getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingdown){
                        if(player.getHitBox().getTranslateY()+40 == platform.getTranslateY()){
                            player.getHitBox().setTranslateY(player.getHitBox().getTranslateY() - 1);
                            player.getImage().setTranslateY(player.getImage().getTranslateY() - 1);
                            canjump = true;
                            return;
                        }
                    }
                    else {
                        if(player.getHitBox().getTranslateY() == platform.getTranslateY() + 60){
                            return;
                        }
                    }
                }
            }
            player.getHitBox().setTranslateY(player.getHitBox().getTranslateY() + (movingdown ? 1: -1));
            player.getImage().setTranslateY(player.getImage().getTranslateY() + (movingdown ? 1: -1));
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
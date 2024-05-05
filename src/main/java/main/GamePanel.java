package main;

import inputs.KeyBoardInputs;
import inputs.MouseInputs;
import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel{

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game) {

        mouseInputs = new MouseInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(Game.GAME_WIDTH,Game.GAME_HEIGHT);
        System.out.println(Game.GAME_WIDTH +"-" + Game.GAME_HEIGHT);
        setPreferredSize(size);

    }

    public void updateGame() {

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
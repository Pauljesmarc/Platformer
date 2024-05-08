package org.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Random;

public class GameDialog extends Stage {

    private Text textQuestion = new Text();
    private TextField fieldAnswer = new TextField();
    private Text textBox = new Text();
    private String[][] questions  = new Question().getQuestions();
    private boolean correct = false;

    private Random random = new Random();

    private int randomNumber = random.nextInt(10);


    public GameDialog() {
        Button btnsubmit = new Button("Submit");
        Button btnhint = new Button("Hint");

        btnsubmit.setOnAction(event -> {
            fieldAnswer.setEditable(false);

            correct = textBox.getText().equals(fieldAnswer.getText());

            if(correct){
                MainApp.scoreTxt.setText(String.valueOf(MainApp.score+=1));
                textBox.setVisible(true);
                System.out.println(MainApp.score);
                this.close();
                MainApp.setRunning(true);
            }else {
                textBox.setText("Wrong");
                textBox.setVisible(true);
                fieldAnswer.setEditable(true);
                MainApp.setRunning(true);
            }
        });
        btnhint.setOnAction(event -> {
            textBox.setText(questions[randomNumber][2]);
            textBox.setVisible(true);

        });

        VBox vBox = new VBox(30,textQuestion,fieldAnswer, textBox,btnsubmit,btnhint);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
    }

    public void open() {

        randomNumber = random.nextInt(10);
        textQuestion.setText(questions[randomNumber][0]);
        textBox.setText(questions[randomNumber][1]);

        fieldAnswer.setText("");
        fieldAnswer.setEditable(true);
        textBox.setVisible(false);
        correct = false;
        show();
    }

    public boolean isCorrect() {
        return correct;
    }
}

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
    private Text textActualAnswer = new Text();

    private boolean correct = false;


    public GameDialog() {
        Button btnsubmit = new Button("Submit");

        btnsubmit.setOnAction(event -> {
            //fieldAnswer.setEditable(false);

            correct = textActualAnswer.getText().equals(fieldAnswer.getText());
            if(correct){
                MainApp.scoreTxt.setText(String.valueOf(MainApp.score+=1));
                textActualAnswer.setVisible(true);
                System.out.println(MainApp.score);
                this.close();
                MainApp.setRunning(true);
            }else {
                textActualAnswer.setText("Wrong");
                textActualAnswer.setVisible(true);
                fieldAnswer.setEditable(true);
                MainApp.setRunning(true);
            }

        });

        VBox vBox = new VBox(10,textQuestion,fieldAnswer,textActualAnswer,btnsubmit);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
    }


    public void open() {

        Random random = new Random();

        int randomNumber = random.nextInt(4) + 1;
        switch (randomNumber){
            case 1:
                textQuestion.setText("What is 2x2");
                textActualAnswer.setText("4");
                break;
            case 2:
                textQuestion.setText("When was was is,is was what?");
                textActualAnswer.setText("is");
                break;
            case 3:
                textQuestion.setText("What where who?");
                textActualAnswer.setText("when");
                break;
            case 4:
                textQuestion.setText("Do not answer.What number is this?");
                textActualAnswer.setText("");
                break;
        }
        fieldAnswer.setText("");
        fieldAnswer.setEditable(true);
        textActualAnswer.setVisible(false);
        correct = false;
        show();
    }

    public boolean isCorrect() {
        return correct;
    }
}

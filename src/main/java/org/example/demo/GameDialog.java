package org.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;


public class GameDialog extends Stage {

    private Text textQuestion = new Text();
    private TextField fieldAnswer = new TextField();
    private Text hintBox = new Text();
    private Text answerBox = new Text();

    private ArrayList<Object[]> questions = new Question().getQuestions();

    private boolean correct = false;

    private Random random = new Random();
    private int randomNumber;
    private int[] numbers = new int[10];
    private int[] randomQ = new int[10];
    private int ctr = 0;

    public GameDialog() {
        Button btnsubmit = new Button("Submit");
        Button btnhint = new Button("Hint");
        randomizer();

        btnsubmit.setOnAction(event -> {
            correct = answerBox.getText().equals(fieldAnswer.getText());

            if(correct){
                MainApp.scoreTxt.setText(String.valueOf(MainApp.score+=1));
                randomQ[(ctr-1)%10] = -1;
                MainApp.setRunning(true);
                this.close();
            }else {
                hintBox.setText("Wrong");
                hintBox.setVisible(true);
                MainApp.setRunning(true);
            }
        });
        btnhint.setOnAction(event -> {

            int hintPoints = MainApp.hintPoints;
            System.out.println(hintPoints);

            if(hintPoints>=2){
                for(Object[] question : questions) {
                    if(question[2].equals(randomNumber)){
                        hintBox.setText((String) question[2]);
                    }
                }
                MainApp.hintPoints--;
            }else{
                hintBox.setText("Low on Hint Points");
            }

            hintBox.setVisible(true);
        });

        VBox vBox = new VBox(30,textQuestion,fieldAnswer,answerBox,hintBox,btnsubmit,btnhint);
        fieldAnswer.setVisible(true);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
    }

    public void randomizer(){
        int count = 0;
        while (count < 10) {
            int randomNumber = random.nextInt(10); // generate number from 1 to 10
            if (numbers[randomNumber] == 0) {
                numbers[randomNumber] = 1;
                randomQ[count] = randomNumber;
                count++;
            }
        }

        System.out.println(Arrays.toString(randomQ));
    }

    public void open() {
        while (randomQ[ctr%10]==-1){
            ctr++;
        }

        System.out.println(Arrays.toString(randomQ));
        for(Object[] question : questions) {
             if(question[3].equals(randomQ[ctr%10])){
                String questionText = (String) question[0];
                String answer = (String) question[1];
                String hint = (String) question[2];

                System.out.println("Question: " + questionText);
                System.out.println("Answer: " + answer);
                System.out.println("Hint: " + hint);
                System.out.println();

                textQuestion.setText(questionText);
                answerBox.setText(answer);
                hintBox.setText(hint);
            }
        }

        fieldAnswer.setText("");
        fieldAnswer.setEditable(true);
        answerBox.setVisible(false);
        hintBox.setVisible(false);

        setCorrect(false);
        ctr++;
        show();
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

}

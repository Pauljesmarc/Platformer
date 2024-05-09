package org.example.demo;
import java.util.ArrayList;
import java.util.List;
public class Question   {
    public Question() {
    }

    public ArrayList<Object[]> getQuestions() {
        ArrayList<Object[]> questions = new ArrayList<>();

        questions.add(new Object[]{"1Solve 4x−7(2−x) = 3 x + 2", "2" ,"next to one",1});
        questions.add(new Object[]{"2Solve 1/x = 1/5", "5" ,"Really?",2});
        questions.add(new Object[]{"3Solve for z: z - 5 = 6", "11","Try Premium for $5/month",3});
        questions.add(new Object[]{"4Solve for x: 3x – 5 = 10","5","Try Premium for $5/month",4});
        questions.add(new Object[]{"5Solve for m: 2(m + 6) = 48","18","Try Premium for $5/month",5});
        questions.add(new Object[]{"6Solve for y: 13(y–4)–3(y–9)–5(y+4)=0","9","Try Premium for $5/month",6});
        questions.add(new Object[]{"7If (a−8)/3=(a−3)/2, then a = ?","-7","Try Premium for $5/month",7});
        questions.add(new Object[]{"8Solve for x: (x+2)(x+3)+(x–3)(x–2)–2x(x+1)=0","6","Try Premium for $5/month",8});
        questions.add(new Object[]{"9Everyone's favorite number.","69","Your not Man enough",9});
        questions.add(new Object[]{"0Bonus","","Try Premium for $5/month",0});

        return questions;
    }
}

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Survey implements Serializable {

    private String name;
    private ArrayList<Question> questions;
    private int responseNum;
    private String type;

    public Survey(String name) {
        this.name = name;
        this.questions = new ArrayList<Question>();
        this.responseNum = 0;
        this.type = "Definition";
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public void addQuestion(Question q) { //This adds questions to the survey
        this.questions.add(q);
    }

    public void Take() {
        Scanner scan = new Scanner(System.in);
        for (Question q : this.questions) {
            q.Display();
            for(int i = 0; i < q.getNumResponses(); i++) {
                boolean noInput = true;
                while (noInput) {
                    String response = scan.nextLine();
                    if (!response.equals("")) {
                        q.setResponse(response);
                        noInput = false;
                    } else {
                        System.out.println("Error: Null Input");
                    }
                }
            }
        }
    }


    public void Display() {
        for (Question q : this.getQuestions()) {
            q.Display();
            System.out.println("");
        }
    }

    public String getName() {
        return this.name;
    }
    public String getType() { return this.type;}

    public void makeResponse() {;
        this.type = "Response";
    }

    public void setResponseNum(int newNum) {
        this.responseNum = newNum;
    }

    public int getResponseNum() {
        return this.responseNum;
    }

    public void makeDefinition() {;
        this.type = "Definition";
    }

    public void Modify(int qNum, String newP, int cNum, String newC, String side, String op) {
        Question q = this.getQuestions().get(qNum-1);
        if (q.getClass().equals(MultipleChoice.class)) {
            if (op.equals("prompt")) {
                q.setPrompt(newP);
            } else if (op.equals("choice")) {
                ((MultipleChoice) q).modChoice(newC, cNum);
            }
        } else if (q.getClass().equals(Matching.class)) {
            if (op.equals("prompt")) {
                q.setPrompt(newP);
            } else if (op.equals("choice")) {
                ((Matching) q).modChoice(newC, side, cNum-1);
            }
        } else {
            q.setPrompt(newP);
        }
    }

    public void Tabulate() throws IOException {
        try {
            ArrayList<File> files = FindFiles.findFiles(this.getName() + "Response");
            ArrayList<Survey> responseSurveys = new ArrayList<Survey>();
            for (File s : files) {
                responseSurveys.add(TransientWhenSerializing.deserialize(s.getPath()));
            }
            int i = 0;
            while (i < this.getQuestions().size()) {
                HashMap<String, Integer> tally = new HashMap<>();
                System.out.println(this.getQuestions().get(i).getPrompt());
                Survey tempS = null;
                for (Survey responseSurvey : responseSurveys) {
                    tempS = responseSurvey;
                    ArrayList<Response> thisResponses = responseSurvey.getQuestions().get(i).getResponse();
                    String rKey = "";
                        if ((tempS.getQuestions().get(i).getClass() == MultipleChoice.class) || (tempS.getQuestions().get(i).getClass() == ShortAnswer.class)) {
                            for (Response response : thisResponses) {
                                rKey = response.getValue();
                                if (tally.containsKey(rKey)) {
                                    tally.put(rKey, tally.get(rKey) + 1);
                                } else {
                                    tally.put(rKey, 1);
                                }
                            }
                        } else {
                            for (Response response : thisResponses) {
                                rKey += response.getValue() + "\n";
                            }
                            if (tally.containsKey(rKey)) {
                                tally.put(rKey, tally.get(rKey) + 1);
                            } else {
                                tally.put(rKey, 1);
                            }
                        }
                    }
                if (tempS.getQuestions().get(i).getClass() == TrueFalse.class) {
                    if (!(tally.containsKey("T\n"))) {
                        tally.put("T\n", 0);
                    }
                    if (!(tally.containsKey("F\n"))) {
                        tally.put("F\n", 0);
                    }
                    System.out.println("True: " + tally.get("T\n"));
                    System.out.println("False: " + tally.get("F\n") + "\n");

                } else if (tempS.getQuestions().get(i).getClass() == MultipleChoice.class) {
                    char thisChar = "A".charAt(0);
                    for (int j = 0; j < ((MultipleChoice) tempS.getQuestions().get(i)).getChoices().size(); j++) {
                        if (!(tally.containsKey(String.valueOf(thisChar)))) {
                            tally.put(String.valueOf(thisChar), 0);
                        }
                        System.out.println(thisChar + ": " + tally.get(String.valueOf(thisChar)));
                        thisChar++;
                    }
                    System.out.println("");
                } else if (tempS.getQuestions().get(i).getClass() == ShortAnswer.class) {
                    for (Map.Entry<String, Integer> answer : tally.entrySet()) {
                        System.out.println(answer.getKey() + " " + answer.getValue());
                    }
                    System.out.println("");
                } else if (tempS.getQuestions().get(i).getClass() == Essay.class) {
                    for (Map.Entry<String, Integer> answer : tally.entrySet()) {
                        System.out.println(answer.getKey());
                    }
                } else if (tempS.getQuestions().get(i).getClass() == ValidDate.class) {
                    for (Map.Entry<String, Integer> answer : tally.entrySet()) {
                        System.out.println(answer.getKey() + answer.getValue());
                    }
                    System.out.println("");
                } else if (tempS.getQuestions().get(i).getClass() == Matching.class) {
                    for (Map.Entry<String, Integer> answer : tally.entrySet()) {
                        System.out.println(answer.getValue());
                        System.out.println(answer.getKey());
                    }
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("INVALID SURVEY");
        }
    }
}

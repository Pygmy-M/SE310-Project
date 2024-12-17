import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class MultipleChoice extends Question implements Serializable {

    ArrayList<String> choices;


    public MultipleChoice(int qNum, String p, int numA){
        super(qNum, p, numA);
        this.choices = new ArrayList<String>();
    }

    @Override
    public ArrayList<Response> getResponse() {
        return this.responses;
    }

    public boolean isValidAnswer(String s) {
        char charA = s.charAt(0);
        int intA = charA;
        intA -= 65;
        if (0 <= intA && intA < this.choices.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setResponse(String newA) { //add to responses if the response exists.
        if (isValidAnswer(newA)) {
            this.responses.add(new Response(newA));
        } else {
            System.out.println("Invalid Response Choice");
            Scanner scan = new Scanner(System.in);
            this.setResponse(scan.nextLine());
        }
    }

    @Override
    public void Display() {
        System.out.println(this.questionNum + ")  " + this.getPrompt());
        String toPrint = "";
        char thisChar = 'A';
        for (int i = 0; i < this.choices.size(); i++) {
            toPrint += thisChar + ")  " + this.choices.get(i) + "   ";
            thisChar++;
        }
        System.out.println(toPrint);
        this.DisplayResponses();
    }

    @Override
    public Question Clone() {
        MultipleChoice tempQ = new MultipleChoice(this.getQuestionNum(), this.getPrompt(), this.getNumResponses());
        tempQ.setChoices(this.getChoices());
        return tempQ;
    }

    public void setChoices(ArrayList<String> newC) {
        this.choices = newC;
    }

    public void addChoice(String newC) {
        this.choices.add(newC);
    }

    protected ArrayList<String> getChoices() {
        return this.choices;
    }

    protected void modChoice(String newC, int index) {
        this.choices.set(index, newC);
    }

    public void DisplayChoices() {
        String toPrint = "";
        char thisChar = 'A';
        for (int i = 0; i < this.choices.size(); i++) {
            toPrint += thisChar + ")  " + this.choices.get(i) + "   ";
            thisChar++;
        }
        System.out.println(toPrint);
    }
}

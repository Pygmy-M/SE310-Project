import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class Matching extends Question implements Serializable {

    private ArrayList<String> leftChoices;
    private ArrayList<String> rightChoices;

    public Matching(int qNum, String p, int numA) {
        super(qNum, p, numA);
        this.leftChoices = new ArrayList<String>();
        this.rightChoices = new ArrayList<String>();
    }

    @Override
    public ArrayList<Response> getResponse() {
        return this.responses;
    }

    @Override
    public void setResponse(String newA) {
        if (isValidResponse(newA)) {
            this.responses.add(new Response(newA));
        } else {
            Scanner scan = new Scanner(System.in);
            this.setResponse(scan.nextLine());
        }
    }

    public boolean isValidResponse(String a) {
        String formatPattern = "[A-Z] \\d";

        Pattern pattern = Pattern.compile(formatPattern);
        Matcher matcher = pattern.matcher(a.strip());

        if (matcher.matches()) {
            return true;
        } else {
            System.out.println("INVALID RESPONSE");
            return false;
        }
    }

    @Override
    public void Display() {
        System.out.println(this.questionNum + ")  " + this.getPrompt() + "(ex. A 1)");
        this.DisplayChoices();
        this.DisplayResponses();
    }

    @Override
    public Question Clone() {
        Matching tempQ = new Matching(this.getQuestionNum(), this.getPrompt(), this.getNumResponses());
        tempQ.setLeftChoices(this.getLeftChoices());
        tempQ.setRightChoices(this.getRightChoices());
        return tempQ;
    }

    public ArrayList<String> getLeftChoices() {
        return this.leftChoices;
    }

    public ArrayList<String> getRightChoices() {
        return this.rightChoices;
    }

    public void setLeftChoices(ArrayList<String> newC) {
        this.leftChoices = newC;
    }

    public void setRightChoices(ArrayList<String> newC) {
        this.rightChoices = newC;
    }

    public void addChoice(String newC, String side) {
        if (side.equals("Right")) {
            this.rightChoices.add(newC);
        } else if (side.equals("Left")) {
            this.leftChoices.add(newC);
        }
    }

    public ArrayList<String> getChoices(String side) {
        if (side.equals("Right")) {
            return this.rightChoices;
        } else if (side.equals("Left")) {
            return this.leftChoices;
        } else {
            return null;
        }
    }

    protected void modChoice(String newC, String side, int index) {
        if (side.equals("R")) {
            this.rightChoices.set(index, newC);
        } else if (side.equals("L")) {
            this.leftChoices.set(index, newC);
        }
    }

    public void DisplayChoices() {
        int thisInt = 1;
        char thisChar = 'A';
        for (int i = 0; i < this.leftChoices.size(); i++) {
            System.out.printf(String.format(thisChar + ")  " + "%-" + 15 + "s", this.leftChoices.get(i))
                    + String.format(thisInt + ")  " + "%-" + 15 + "s", this.rightChoices.get(i)) + "\n");
            thisInt++;
            thisChar++;
        }
    }
}

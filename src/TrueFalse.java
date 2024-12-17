import java.io.Serializable;
import java.util.Scanner;

public class TrueFalse extends MultipleChoice implements Serializable {
    public TrueFalse(int qNum, String p) {
        super(qNum, p, 1);
        this.choices.add("True");
        this.choices.add("False");
    }

    @Override
    public void Display() {
        System.out.println(this.questionNum + ")  " + this.getPrompt() + "\nT/F");
        this.DisplayResponses();
    }

    @Override
    public Question Clone() {
        return new TrueFalse(this.getQuestionNum(), this.getPrompt());
    }

    @Override
    public boolean isValidAnswer(String s) {
        if (s.equals("T") || s.equals("F")) {
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
}


import java.io.Serializable;
import java.util.Scanner;

public class ShortAnswer extends Essay implements Serializable {

    protected int wordLimit;

    public ShortAnswer(int qNum, String p, int numA) {
        super(qNum, p, numA);
        this.wordLimit = 100;
    }

    public boolean isValidResponse(String a) {
        if (a.length() <= this.wordLimit) {
            return true;
        } else {
            System.out.println("Exceeded Word Limit.");
            return false;
        }
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

    @Override
    public Question Clone() {
        return new ShortAnswer(this.getQuestionNum(), this.getPrompt(), this.getNumResponses());
    }
}

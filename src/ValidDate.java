import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ValidDate extends ShortAnswer implements Serializable {

    public ValidDate(int qNum, String p, int numA) {
        super(qNum, p, numA);
        this.wordLimit = 10;
    }

    public boolean isValidResponse(String a) {
        if (a.length() <= this.wordLimit) {
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            format.setLenient(false);
            try {
                format.parse(a);
            } catch (ParseException e) {
                System.out.println("Please enter using the MM/dd/yyyy format.");
                return false;
            }
            return true;
        }
        return false;
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
    public void Display() {
        System.out.println(this.questionNum + ")  " + this.getPrompt() + "(Please enter using the MM/dd/yyyy format)");
        this.DisplayResponses();
    }

    @Override
    public Question Clone() {
        return new ValidDate(this.getQuestionNum(), this.getPrompt(), this.getNumResponses());
    }
}

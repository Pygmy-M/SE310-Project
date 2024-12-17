import java.io.Serializable;
import java.util.ArrayList;

public class Essay extends Question implements Serializable {

    public Essay(int qNum, String p, int numA) {
        super(qNum, p, numA);
    }

    @Override
    public ArrayList<Response> getResponse() {
        return this.responses;
    }

    @Override
    public void setResponse(String newA) {
        this.responses.add(new Response(newA));
    }

    @Override
    public void Display() {
        System.out.println(this.questionNum + ")  " + this.getPrompt());
        this.DisplayResponses();
    }

    @Override
    public Question Clone() {
        return new Essay(this.getQuestionNum(), this.getPrompt(), this.getNumResponses());
    }

}

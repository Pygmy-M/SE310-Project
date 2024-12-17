import java.io.Serializable;
import java.util.ArrayList;

public abstract class Question implements Serializable {

    private String prompt;
    protected int questionNum;
    protected ArrayList<Response> responses;
    private int numResponses;


    public Question(int qNum, String p, int numA){
        this.prompt = p;
        this.questionNum = qNum;
        this.responses = new ArrayList<Response>();
        this.numResponses = numA;
        if (numA > 1) {
            this.setPrompt(this.getPrompt() + " Please give " + numA + " responses.");
        }
    }

    public abstract ArrayList<Response> getResponse();
    public abstract void setResponse(String newA);
    public abstract void Display();
    public abstract Question Clone();

    public String getPrompt(){
        return this.prompt;
    }
    public void setPrompt(String newP){
        this.prompt = newP;
    }
    public int getNumResponses() {
        return this.numResponses;
    }

    public int getQuestionNum() {
        return this.questionNum;
    }

    public void DisplayResponses() {
        if (this.responses.size() > 0) {
            for (Response a : responses) {
                System.out.println(a);
            }
            System.out.println("\n");
        }
    }
}

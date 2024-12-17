import java.io.Serializable;
import java.util.ArrayList;

public class Test extends Survey implements Serializable{

    private ArrayList<Response> correctAnswers;

    public Test(String name) {
        super(name);
        correctAnswers = new ArrayList<Response>();
    }

    public void addAnswers(Response response) {
        this.correctAnswers.add(response);
    }

    public void DisplayWithAnswers() {
        int j = 0;
        for (Question q : this.getQuestions()) {
            q.Display();
            if (q.getClass() != Essay.class) {
                for (int i = 0; i < q.getNumResponses(); i++) {
                    System.out.println(this.getCorrectAnswers().get(j));
                    j++;
                }
            }
            System.out.println("");
        }
    }

    public ArrayList<Response> getCorrectAnswers() {
        return this.correctAnswers;
    }

    public void setCorrectAnswers(ArrayList<Response> newA) {
        this.correctAnswers = newA;
    }

    public void Grade() {
        int numOfEssays = 0;
        int score = 0;
        int j = 0;
        for (Question q : this.getQuestions()) {
            boolean correct = true;
            if (q.getClass() != Essay.class) {
                for (int i = 0; i < q.getNumResponses(); i++) {
                    if (!(q.getResponse().get(i).Equals(this.getCorrectAnswers().get(j)))) {
                        correct = false;
                    }
                    j++;
                }
                if (correct == true) {
                    score++;
                }
            } else {
                numOfEssays++;
            }
        }
        System.out.println(score);
        int finalScore = (score*100)/this.getQuestions().size();
        int pointsAvailable = 100-((numOfEssays*100/this.getQuestions().size()));
        System.out.println("You received a " + finalScore + " on the test. The test was worth 100 points, but only " + pointsAvailable +
                " of those points could be auto graded because there was " + numOfEssays + " essay question(s).");
    }
}
